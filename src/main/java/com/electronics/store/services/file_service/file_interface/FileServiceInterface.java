package com.electronics.store.services.file_service.file_interface;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FileServiceInterface {

     String uploadFile(MultipartFile file,String uploadPath) throws IOException;

     InputStream getResource(String path,String name) throws FileNotFoundException;
}
