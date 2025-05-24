package volunteer.plus.backend.service.general.impl;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import volunteer.plus.backend.config.storage.AWSProperties;
import volunteer.plus.backend.domain.dto.RegistrationData;
import volunteer.plus.backend.domain.dto.UserInfo;
import volunteer.plus.backend.domain.entity.PasswordResetToken;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.repository.PasswordResetTokenRepository;
import volunteer.plus.backend.repository.UserRepository;
import volunteer.plus.backend.service.general.S3Service;
import volunteer.plus.backend.service.general.UserService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final AWSProperties awsProperties;
    private final S3Service s3Service;
    private static final long DEFAULT_TOKEN_EXPIRATION_TIME = 432000L;

    public UserServiceImpl(final UserRepository userRepository, PasswordResetTokenRepository passwordResetTokenRepository,
                           final AWSProperties awsProperties,
                           final S3Service s3Service) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.awsProperties = awsProperties;
        this.s3Service = s3Service;
    }

    @Override
    public UserInfo getCurrentUser() throws AuthenticationException {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
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
    @Transactional
    public User getUserByResetToken(String resetToken) {
        var resetPasswordToken = getResetToken(resetToken);
        var user = resetPasswordToken.getUser();

        if(user == null) {
            throw new ApiException("User with token " + resetToken + " was not found");
        }

        return user;
    }

    @Override
    public PasswordResetToken getResetToken(String token) {
        return passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new ApiException("Token with token " + token + " was not found"));
    }

    @Override
    public void setPasswordResetToken(User user, String token) {
        var passwordResetToken = new PasswordResetToken();
        passwordResetToken.setUser(user);
        passwordResetToken.setToken(token);

        Instant expiryInstant = Instant.ofEpochMilli(
                System.currentTimeMillis() + DEFAULT_TOKEN_EXPIRATION_TIME
        );
        LocalDateTime expiryDate = LocalDateTime.ofInstant(
                expiryInstant, ZoneId.systemDefault()
        );
        passwordResetToken.setExpiryDate(expiryDate);

        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

}
