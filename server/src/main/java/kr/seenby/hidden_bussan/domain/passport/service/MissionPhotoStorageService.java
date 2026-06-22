package kr.seenby.hidden_bussan.domain.passport.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import kr.seenby.hidden_bussan.domain.passport.dto.MissionPhotoUploadResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MissionPhotoStorageService {

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );
    private static final Map<String, String> EXTENSION_BY_CONTENT_TYPE = Map.of(
            "image/jpeg", ".jpg",
            "image/png", ".png",
            "image/webp", ".webp"
    );
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".jpg", ".jpeg", ".png", ".webp");

    private final Path missionPhotoDir;
    private final String publicBaseUrl;

    public MissionPhotoStorageService(
            @Value("${app.upload.dir:uploads}") String uploadDir,
            @Value("${app.upload.public-base-url:http://localhost:8080/uploads}") String publicBaseUrl
    ) {
        this.missionPhotoDir = Path.of(uploadDir)
                .toAbsolutePath()
                .normalize()
                .resolve("mission-completions");
        this.publicBaseUrl = trimTrailingSlash(publicBaseUrl);
    }

    public MissionPhotoUploadResponse store(MultipartFile file) {
        validate(file);

        String fileName = UUID.randomUUID() + extensionFor(file);
        Path target = missionPhotoDir.resolve(fileName).normalize();

        try {
            Files.createDirectories(missionPhotoDir);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to store mission photo", exception);
        }

        return new MissionPhotoUploadResponse(publicBaseUrl + "/mission-completions/" + fileName);
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Photo file is required");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Photo file must be 10MB or smaller");
        }

        String contentType = normalize(file.getContentType());
        if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only JPG, PNG, and WebP photos are supported");
        }

        String extension = originalExtension(file.getOriginalFilename());
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only JPG, PNG, and WebP photos are supported");
        }
    }

    private String extensionFor(MultipartFile file) {
        String contentType = normalize(file.getContentType());
        if ("image/jpeg".equals(contentType)) {
            String originalExtension = originalExtension(file.getOriginalFilename());
            if (".jpeg".equals(originalExtension)) {
                return ".jpeg";
            }
        }

        return EXTENSION_BY_CONTENT_TYPE.get(contentType);
    }

    private String originalExtension(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return "";
        }

        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == fileName.length() - 1) {
            return "";
        }

        return fileName.substring(dotIndex).toLowerCase(Locale.ROOT);
    }

    private String normalize(String contentType) {
        if (contentType == null) {
            return "";
        }

        return contentType.toLowerCase(Locale.ROOT);
    }

    private String trimTrailingSlash(String value) {
        if (value.endsWith("/")) {
            return value.substring(0, value.length() - 1);
        }

        return value;
    }
}
