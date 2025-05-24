package volunteer.plus.backend.api;


import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import volunteer.plus.backend.domain.dto.TokenPairResponse;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.repository.UserRepository;
import volunteer.plus.backend.service.security.TokenFactoryService;
import volunteer.plus.backend.service.security.impl.FaceService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/no-auth/face")
public class FaceController {

    private final FaceService faceService;

    private final UserRepository userRepo;

    @Autowired
    private TokenFactoryService jwtService;

    @GetMapping("/index-face-id")
    public String getPage() {
        return "index-face-id";
    }

    @PostMapping("/enroll")
    @ResponseBody
    public ResponseEntity<?> enroll(
            @RequestParam("username") String username,
            @RequestParam("files") List<MultipartFile> files) {

        try {
            // 1) Extract embeddings
            List<float[]> embs = new ArrayList<>();
            for (MultipartFile f : files) {
                Image img = ImageFactory.getInstance()
                        .fromInputStream(f.getInputStream());
                embs.add(faceService.extractAndAlign(img));
            }

            // 2) Compute template
            float[] centroid = faceService.computeCentroid(embs);
            double threshold = faceService.computeThreshold(embs, centroid);

            // 3) Serialize
            byte[] centBlob = faceService.serializeFloatArray(centroid);
            byte[] embBlob  = faceService.serializeEmbeddingsList(embs);

            // 4) Save to DB
            User u = new User();
            u.setEmail(username);
            u.setCentroid(centBlob);
            u.setThreshold(threshold);
            u.setEmbeddings(embBlob);
            userRepo.save(u);

            return ResponseEntity.ok(Map.of("status","enrolled"));

        } catch (IOException e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error","Failed to read image", "details", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error","Enrollment failed", "details", e.getMessage()));
        }
    }

    @PostMapping("/authenticate")
    @ResponseBody
    public ResponseEntity<?> authenticate(
            @RequestParam("username") String username,
            @RequestParam("file") MultipartFile file) {

        try {
            Image img = ImageFactory.getInstance()
                    .fromInputStream(file.getInputStream());
            float[] emb = faceService.extractAndAlign(img);

            // 2) Load user
            Optional<User> opt = userRepo.findByEmail(username);
            if (opt.isEmpty()) {
                return ResponseEntity.status(404)
                        .body(Map.of("error","User not found"));
            }
            User u = opt.get();

            // 3) Compare
            float[] cent = faceService.deserializeFloatArray(u.getCentroid());
            double dist = faceService.cosineDistance(cent, emb);
            if (dist > u.getThreshold()) {
                return ResponseEntity.status(401)
                        .body(Map.of("error","Face did not match"));
            }

            // 4) Online update template
            List<float[]> history = faceService.deserializeEmbeddingsList(u.getEmbeddings());
            history.add(emb);
            float[] newCent = faceService.computeCentroid(history);
            double newThresh = faceService.computeThreshold(history, newCent);

            u.setCentroid(faceService.serializeFloatArray(newCent));
            u.setThreshold(newThresh);
            u.setEmbeddings(faceService.serializeEmbeddingsList(history));
            userRepo.save(u);

            TokenPairResponse token = jwtService.generateTokenPair(u);
            return ResponseEntity.ok(token);

        } catch (IOException e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error","Failed to read image", "details", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error","Authentication failed", "details", e.getMessage()));
        }
    }

}
