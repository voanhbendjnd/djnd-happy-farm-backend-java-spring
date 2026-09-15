package djnd.happy.farm.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class FileService {
    @Value("${djnd.upload-file.base-uri}")
    private String absolutePathURLServer;
    public static final String SAVE_IMAGE_PLANT = "plant-images";
    public String getNameImageURL(MultipartFile file) throws URISyntaxException, IOException {
        var uploadPath = absolutePathURLServer + SAVE_IMAGE_PLANT;
        var directoryPath = Paths.get(uploadPath);
        Files.createDirectories(directoryPath);
        var originalNameFile = file.getOriginalFilename();
        if (originalNameFile == null) {
            throw new IOException("File name invalid!");
        }
        var fileName = "djnd-" + System.currentTimeMillis() + ".webp";
        var filePath = directoryPath.resolve(fileName);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
        return SAVE_IMAGE_PLANT + "/" + fileName;
    }
    public String getFileNameTemp(MultipartFile file) throws URISyntaxException, IOException {
        var uploadPath = absolutePathURLServer + SAVE_IMAGE_PLANT;
        var directoryPath = Paths.get(uploadPath);
        Files.createDirectories(directoryPath);
        var originalName = file.getOriginalFilename();
        if(originalName == null) {
            throw new IOException("File name invalid!");
        }
        var fileName = "djnd-" + System.currentTimeMillis() + ".webp";
        var filePath = directoryPath.resolve(fileName);
        try(InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
        return fileName;
    }

    public String moveFileToSave(String fileName, String to) throws URISyntaxException,IOException {
        var tempPath  = Paths.get(absolutePathURLServer + fileName);
        var saveAt = Paths.get(absolutePathURLServer + to);
        Files.createDirectories(saveAt);
        var finalPath = saveAt.resolve(fileName);
        if(Files.exists(finalPath)) {
            Files.move(tempPath, finalPath, StandardCopyOption.REPLACE_EXISTING);
            return Paths.get(to).resolve(finalPath).toString().replace("\\", "/");
        }
        else{
            return null;
        }

    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void clearOldTempAfterDay() {
        var directoryPath = Paths.get(absolutePathURLServer + SAVE_IMAGE_PLANT);
        if (!Files.exists(directoryPath)) {
            return;
        }
        long twentyFourHoursAgo = Instant.now().minusSeconds(24 * 60 * 60).toEpochMilli();
        try {
            Files.list(directoryPath).forEach(file -> {
                try {
                    BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);
                    if (attrs.creationTime().toMillis() <= twentyFourHoursAgo) {
                        Files.delete(file);
                        System.out.println("Deleted old temp file: " + file.getFileName());
                    }
                } catch (IOException e) {
                    System.err.println("Error processing file in " + SAVE_IMAGE_PLANT + file.getFileName());
                }
            });
        } catch (IOException e) {
            System.err.println("Error listing " + SAVE_IMAGE_PLANT + " directory.");
        }
    }
}
