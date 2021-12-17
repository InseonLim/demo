package com.example.demo.service;



import com.example.demo.payload.FileUploadResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DemoController {

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);
    private final FileUploadDownloadService service;

    @GetMapping("/")
    public String controllerMain() {
        return "Hello Test";
    }

    @GetMapping("/uploadFiles")
    public Iterable<Image> getUploadFileList() {
        return service.getFileList();
    }

    @GetMapping("/uploadFile/{id}")
    public Optional<Image> uploadFile(@PathVariable int id) {
        return service.getImage(id);
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        String name = service.storeFile(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(name)
                .toUriString();

        return fileDownloadUri;
    }




    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = service.loadFileAsResource(fileName);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}