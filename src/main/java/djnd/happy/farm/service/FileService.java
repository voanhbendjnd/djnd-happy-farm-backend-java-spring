package djnd.happy.farm.service;

import djnd.happy.farm.service.errors.BadRequestExceptionGlobal;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.coyote.BadRequestException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class FileService {
    @Value("${djnd.upload-file.base-uri}")
    private String absolutePathURLServer;
    public static final String SAVE_IMAGE_PLANT = "plant-images";
    public static final String SAVE_TO_TEMP= "temp-images";

    public String saveAndGetFileNameImageURL(MultipartFile file) throws URISyntaxException, IOException {
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
    public List<String> saveAndGetFilesURL(List<MultipartFile> files) throws URISyntaxException, IOException {
        if(files == null && files.isEmpty()){
            throw new BadRequestExceptionGlobal("File not found", "fileManagement", "filenotfound");
        }
        List<String> errorMessages = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file == null) {
                errorMessages.add("File is null!");
                continue;
            }
            if (file.isEmpty()) {
                errorMessages.add(
                        "File (" + file.getOriginalFilename() + ") is empty!"
                );
                continue;
            }
            if (file.getOriginalFilename() == null
                    || file.getOriginalFilename().isBlank()) {

                errorMessages.add(
                        "File has invalid original file name!"
                );
            }
        }
        if(!errorMessages.isEmpty()){
            throw new BadRequestExceptionGlobal(
                    String.join("/n", errorMessages),
                    "fileManagement",
                    "fileoriginalfilenameinvalid"
            );
        }
        var uploadPath = absolutePathURLServer + SAVE_TO_TEMP;
        var directoryPath = Paths.get(uploadPath);
        Files.createDirectories(directoryPath);
        List<String> filesUrl = new ArrayList<>();
        for(MultipartFile file : files){
            String fileName = "djnd-" + System.currentTimeMillis() + "-" + UUID.randomUUID() +  ".webp";
            var filePath = directoryPath.resolve(fileName);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
            filesUrl.add(SAVE_TO_TEMP + "/" + fileName);


        }
        return filesUrl;


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

    public String moveFileToSave(String fileName, String to) throws URISyntaxException, IOException {
        var tempPath = Paths.get(absolutePathURLServer + FileService.SAVE_TO_TEMP + "/" + fileName);
        var saveAt = Paths.get(absolutePathURLServer + to);
        Files.createDirectories(saveAt);
        var finalPath = saveAt.resolve(fileName);
        if (Files.exists(tempPath)) {
            Files.move(tempPath, finalPath, StandardCopyOption.REPLACE_EXISTING);
            return to + "/" + fileName;
        }
        return null;
    }


    public List<String> moveFilesToSave(List<String> fileNames, String to)throws URISyntaxException,IOException{
        var saveAt = Paths.get(absolutePathURLServer + to);
        Files.createDirectories(saveAt);
        List<String> lastUrls = new ArrayList<>();
        for(String fileName : fileNames){
            var tempPath  = Paths.get(absolutePathURLServer + fileName);
            var finalPath = saveAt.resolve(fileName);
            if(Files.exists(finalPath)) {
                Files.move(tempPath, finalPath, StandardCopyOption.REPLACE_EXISTING);
                lastUrls.add(Paths.get(to).resolve(finalPath).toString().replace("\\", "/"));
            }

        }
        if(!lastUrls.isEmpty()){
            return lastUrls;
        }
        return null;

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
