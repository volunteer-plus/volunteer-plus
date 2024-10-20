package volunteer.plus.backend.util;

import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class HashUtil {

    public static final String SHA_256 = "SHA-256";

    private HashUtil() {
    }

    @SneakyThrows
    public static String hashValue(String algorithm, String value) {
        final var digest = MessageDigest.getInstance(SHA_256);
        byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }
}
