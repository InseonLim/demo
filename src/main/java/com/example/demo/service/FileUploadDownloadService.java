package com.example.demo.service;

import com.example.demo.exception.FileDownloadException;
import com.example.demo.exception.FileUploadException;
import com.example.demo.property.DemoProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import javax.transaction.Transactional;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import com.example.demo.service.FileDAO;

// 파일이 저장될 디렉토리 설정, 디렉토리 생성
// Service가 실행될 때 생성자에서 기존에 생성한 설정 클래스인 DemoPropetise 클래스로 기본 디렉토리 설정 후 생성

@Service
public class FileUploadDownloadService {
    private final Path fileLocation;
    private final FileDAO fileDAO;
    @Autowired
    public FileUploadDownloadService(DemoProperties prop, FileDAO fileDAO){
        this.fileLocation = Paths.get(prop.getUploadDir()).toAbsolutePath().normalize();
        this.fileDAO = fileDAO;
        try{
            Files.createDirectories(this.fileLocation);
        }catch (Exception e){
            throw new FileUploadException("파일을 업로드할 디렉토리를 생성하지 못했습니다.",e);
        }
    }

    @Transactional
    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // 파일명에 부적합 문자가 있는지 확인한다.
            if(fileName.contains(".."))
                throw new FileUploadException("파일명에 부적합 문자가 포함되어 있습니다. " + fileName);

            Path targetLocation = this.fileLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            Image uploadFile = new Image(fileName, file.getSize(), file.getContentType(), targetLocation.toString());
            fileDAO.save(uploadFile);

            return fileName;
        }catch(Exception e) {
            throw new FileUploadException("["+fileName+"] 파일 업로드에 실패하였습니다. 다시 시도하십시오.",e);
        }
    }

    @Transactional
    public Resource loadFileAsResource(String fileName){
        try{
            Path filePath = this.fileLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()){
                return resource;
            }else{
                throw new FileDownloadException(fileName + " 파일을 찾을 수 없습니다.");
            }
        }catch(MalformedURLException e){
            throw new FileDownloadException(fileName+" 파일을 찾을 수 없습니다.", e);
        }
    }
    public Iterable<Image> getFileList(){
        Iterable<Image> iterable = fileDAO.findAll();

        if(null == iterable){
            throw new FileDownloadException("업로드 된 파일이 존재하지 않습니다.");
        }
        return iterable;
    }
    public Optional<Image> getImage(int id) {
        Optional<Image> image = fileDAO.findById(id);

        if(null == image){
            throw new FileDownloadException("해당 아이디 ["+id+"[로 업로드된 파일이 존재하지 않습니다.");
        }
        return image;
    }
}
