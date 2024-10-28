package volunteer.plus.backend.service.general;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    byte[] downloadFile(String s3BucketName, String s3ObjectKey);
    String uploadFile(String s3BucketName, MultipartFile multipartFile);
    void deleteFile(String s3BucketName, String s3ObjectKey);
}
