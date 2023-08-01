package com.lcwt.electronic.store.servicesI.serviceImpl;

import com.lcwt.electronic.store.exceptions.BadApiRequest;
import com.lcwt.electronic.store.helper.AppConstants;
import com.lcwt.electronic.store.servicesI.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;


@Service
public class FileServiceImpl implements FileService {

    private static Logger log= LoggerFactory.getLogger(FileServiceImpl.class);
    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {
        String originalFilename = file.getOriginalFilename();
        log.info("Filename :{}",originalFilename);
        String filename = UUID.randomUUID().toString();
        String extension = originalFilename.substring(originalFilename.lastIndexOf(AppConstants.EXTENSION_SUBSTRING));
        String fileNameWithExtension=filename+extension;
        String fullPathWithFileName = path + fileNameWithExtension;
        if(extension.equalsIgnoreCase(AppConstants.FILE_EXTENSION_PNG) || extension.equalsIgnoreCase(AppConstants.FILE_EXTENSION_JPG) || extension.equalsIgnoreCase(AppConstants.FILE_EXTENSION_JPEG)){
            File folder=new File(path);
            if(!folder.exists()){
                //create the folder
                folder.mkdirs();
            }
            //upload
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            return fileNameWithExtension;
        }else{
            throw new BadApiRequest(AppConstants.BAD_API_REQUEST +extension+AppConstants.BAD_API_REQUEST1);
        }
    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        String fullPath=path+File.separator+name;
        InputStream inputStream=new FileInputStream(fullPath);
        return inputStream;
    }
}
