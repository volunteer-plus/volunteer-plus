package volunteer.plus.backend.service.security.impl;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.types.DataType;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.Batchifier;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;


@Service
public class FaceService {

    private ZooModel<Image, DetectedObjects> detectorModel;
    private ZooModel<Image, float[]> extractorModel;
    private static final int EMBEDDING_DIM = 512;


    @PostConstruct
    public void init() throws ModelException, IOException {
        // Detector
        Criteria<Image, DetectedObjects> detCriteria = Criteria.builder()
                .setTypes(Image.class, DetectedObjects.class)
                .optArtifactId("ssd")
                .optEngine("PyTorch")
                .optProgress(new ProgressBar())
                .build();
        detectorModel = ModelZoo.loadModel(detCriteria);

        // Feature extractor with manual translator (no explicit batch dim)
        Translator<Image, float[]> faceFeatTranslator = new Translator<>() {
            @Override
            public NDList processInput(TranslatorContext ctx, Image img) {
                // Convert HWC -> CHW, normalize [0,1] and add batch dimension
                NDArray array = img.toNDArray(ctx.getNDManager(), Image.Flag.COLOR)
                        .transpose(2, 0, 1)
                        .toType(DataType.FLOAT32, false)
                        .div(255f)
                        .expandDims(0);    // add batch dimension here
                return new NDList(array);
            }

            @Override
            public float[] processOutput(TranslatorContext ctx, NDList list) {
                return list.singletonOrThrow().toFloatArray();
            }

            @Override
            public Batchifier getBatchifier() {
                // No implicit batching; Predictor.predict wraps single input
                return null;
            }
        };

        Criteria<Image, float[]> extCriteria = Criteria.<Image, float[]>builder()
                .setTypes(Image.class, float[].class)
                .optEngine("PyTorch")
                .optTranslator(faceFeatTranslator)
                .optProgress(new ProgressBar())
                .optModelUrls("https://resources.djl.ai/test-models/pytorch/face_feature.zip")
                .optModelName("face_feature")
                .build();
        extractorModel = ModelZoo.loadModel(extCriteria);
    }


    public float[] extractAndAlign(Image img) throws TranslateException {
        try (Predictor<Image, DetectedObjects> detP = detectorModel.newPredictor();
             Predictor<Image, float[]> extP = extractorModel.newPredictor()) {

            DetectedObjects det = detP.predict(img);
            if (det.getNumberOfObjects() != 1) {
                throw new IllegalArgumentException("Expected exactly one face in the image");
            }
            DetectedObjects.DetectedObject obj = det.best();
            var rect = obj.getBoundingBox().getBounds();

            int imgW = img.getWidth(), imgH = img.getHeight();
            int x = Math.max(0, (int)(rect.getX() * imgW));
            int y = Math.max(0, (int)(rect.getY() * imgH));
            int w = (int)(rect.getWidth() * imgW);
            int h = (int)(rect.getHeight() * imgH);
            if (x + w > imgW) w = imgW - x;
            if (y + h > imgH) h = imgH - y;

            // 1) Crop face region
            Image faceCrop = img.getSubImage(x, y, w, h);
            // 2) Resize to model's expected input size (112x112)
            BufferedImage buf = (BufferedImage) faceCrop.getWrappedImage();
            BufferedImage scaled = new BufferedImage(112, 112, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = scaled.createGraphics();
            g.drawImage(buf, 0, 0, 112, 112, null);
            g.dispose();
            Image face = ImageFactory.getInstance().fromImage(scaled);

            // 3) Extract embedding
            float[] raw = extP.predict(face);
            return l2Normalize(raw);
        }
    }

    public float[] computeCentroid(List<float[]> embeddings) {
        float[] centroid = new float[embeddings.getFirst().length];
        for (float[] emb : embeddings) {
            for (int i = 0; i < centroid.length; i++) {
                centroid[i] += emb[i];
            }
        }
        for (int i = 0; i < centroid.length; i++) {
            centroid[i] /= embeddings.size();
        }
        return centroid;
    }

    public double computeThreshold(List<float[]> embeddings, float[] centroid) {
        List<Double> distances = embeddings.stream()
                .map(e -> cosineDistance(centroid, e))
                .toList();
        double mean = distances.stream().mapToDouble(d -> d).average().orElse(0.0);
        double std = Math.sqrt(distances.stream()
                .mapToDouble(d -> (d - mean) * (d - mean))
                .sum() / distances.size());
        return mean + 2 * std;
    }

    private float[] l2Normalize(float[] vector) {
        double sum = 0;
        for (float v : vector) {
            sum += v * v;
        }
        double norm = Math.sqrt(sum);
        float[] normalized = new float[vector.length];
        for (int i = 0; i < vector.length; i++) {
            normalized[i] = (float) (vector[i] / (norm + 1e-10));
        }
        return normalized;
    }

    public double cosineDistance(float[] a, float[] b) {
        double dot = 0, na = 0, nb = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            na += a[i] * a[i];
            nb += b[i] * b[i];
        }
        return 1 - (dot / (Math.sqrt(na) * Math.sqrt(nb) + 1e-10));
    }

    public byte[] serializeFloatArray(float[] arr) {
        ByteBuffer bb = ByteBuffer.allocate(arr.length * Float.BYTES).order(ByteOrder.LITTLE_ENDIAN);
        for (float v : arr) {
            bb.putFloat(v);
        }
        return bb.array();
    }

    public float[] deserializeFloatArray(byte[] bytes) {
        FloatBuffer fb = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer();
        float[] arr = new float[fb.remaining()];
        fb.get(arr);
        return arr;
    }

    public byte[] serializeEmbeddingsList(List<float[]> list) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(list.toArray(new float[list.size()][]));
            return baos.toByteArray();
        }
    }

    public List<float[]> deserializeEmbeddingsList(byte[] blob) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(blob);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            float[][] array = (float[][]) ois.readObject();
            List<float[]> list = new ArrayList<>();
            for (float[] v : array) {
                list.add(v);
            }
            return list;
        }
    }

}
