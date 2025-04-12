package volunteer.plus.backend.service.general.impl;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import volunteer.plus.backend.config.storage.AWSProperties;
import volunteer.plus.backend.domain.dto.RegistrationData;
import volunteer.plus.backend.domain.dto.UserInfo;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.repository.UserRepository;
import volunteer.plus.backend.service.general.S3Service;
import volunteer.plus.backend.service.general.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AWSProperties awsProperties;
    private final S3Service s3Service;

    public UserServiceImpl(final UserRepository userRepository,
                           final AWSProperties awsProperties,
                           final S3Service s3Service) {
        this.userRepository = userRepository;
        this.awsProperties = awsProperties;
        this.s3Service = s3Service;
    }

    @Override
    public UserInfo getCurrentUser() throws AuthenticationException {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            User user =  (User) authentication.getPrincipal();
            return new UserInfo(user);
        } else {
            throw new AuthenticationException("You aren't authorized to perform this operation!");
        }
    }

    @Override
    @Transactional
    public UserInfo generalInfoUpdate(final User user,
                                      final RegistrationData registrationData) {

        user.setFirstName(registrationData.getFirstName());
        user.setLastName(registrationData.getLastName());
        user.setMiddleName(registrationData.getMiddleName());
        user.setPhoneNumber(registrationData.getPhoneNumber());
        user.setDateOfBirth(registrationData.getDateOfBirth());

        return new UserInfo(userRepository.saveAndFlush(user));
    }

    @Override
    @Transactional
    public UserInfo uploadLogo(final User user,
                               final MultipartFile multipartFile) {

        // remove previous logo if exists
        if (user.getLogoS3Link() != null) {
            s3Service.deleteFile(awsProperties.getReportBucketName(), user.getLogoS3Link());
        }

        final String s3Key = s3Service.uploadFile(awsProperties.getReportBucketName(), multipartFile);

        user.setLogoS3Link(s3Key);
        user.setLogoFilename(multipartFile.getOriginalFilename());

        return new UserInfo(userRepository.saveAndFlush(user));
    }

    @Override
    @Transactional
    public ResponseEntity<byte[]> downloadLogo(final User user) {
        if (user.getLogoS3Link() == null) {
            throw new ApiException("User with id=" + user.getId() + " does not have any logo");
        }

        final byte[] bytes = s3Service.downloadFile(awsProperties.getReportBucketName(), user.getLogoS3Link());

        return ResponseEntity.ok()
                .header("content-disposition", "attachment; filename=" + user.getLogoFilename())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(bytes);
    }

    public User loadUserByUsername(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email " + email + " was not found"));
    }

    @Override
    public boolean checkEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void createUser(User user) {
        final String email = user.getEmail();

        if (checkEmail(email)) {
            throw new ApiException("User with email " + email + " was not found");
        }

        userRepository.saveAndFlush(user);
    }

    @Override
    public User getUserByResetToken(String resetToken) {
        return userRepository.findUserByResetToken(resetToken)
                .orElse(null);
    }

}
