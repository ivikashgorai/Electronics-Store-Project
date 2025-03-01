package com.electronics.store.services.file_service.file_implementations;

import com.electronics.store.exceptions.BadApiRequestException;
import com.electronics.store.services.file_service.file_interface.FileServiceInterface;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Primary
public class FileServiceImplementation implements FileServiceInterface {

    @Override
    public String uploadFile(MultipartFile file, String uploadPath) throws IOException {
        String originalFilename = file.getOriginalFilename(); // file name when user upload through frontend
        String filename = UUID.randomUUID().toString(); // randomly creating file name
        assert originalFilename != null;
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.')); // take extension of file like .png , .jpg
        String fileNameWithExtension = filename+extension;
        String fullPathWithFileName = uploadPath+File.separator+fileNameWithExtension; //upload image in this path
        if(extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")){
            //save file
            File folder = new File(uploadPath);
            if(!folder.exists()){
                //create the folder
                folder.mkdirs();
            }
            //upload file
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            return fileNameWithExtension;

        }
        else{
            //throw custom exception
            throw new BadApiRequestException("File can be png, jpg or jpeg");
        }
    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        String fullPath = path+File.separator+name;
        InputStream inputStream = new FileInputStream(fullPath);
        return inputStream;
    }
}
