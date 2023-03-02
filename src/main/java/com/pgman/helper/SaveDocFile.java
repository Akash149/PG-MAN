package com.pgman.helper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

public class SaveDocFile {

    private static Logger logger = LoggerFactory.getLogger(SaveDocFile.class);

    /**
     * @param role
     * @param type
     * @param name
     * @param id
     * @param file
     * @return
     */
    public static String savefile(String role, String types, String name, String id, MultipartFile file) {

        try {
            if (role.equals("GUEST")) {
                final String saveto = new ClassPathResource( "static/image/guest/").getFile().getAbsolutePath();
                logger.info("Resource path: {}",saveto);
                String[] type = file.getContentType().split("/");
                String fname = name.replace(" ", "-") + id + "_" + types + "."+type[1];
                logger.info("New file nam: {}",fname);
                Path destination = Paths.get("C:/Users/sakas/OneDrive/Desktop/Work/pgman/src/main/resources/static/image/guest/"+fname);
                logger.info("File Type: {}",file.getContentType());
                Path path = Paths.get(saveto + File.separator + fname);
                logger.info("Path is {}",path.toFile().getAbsolutePath().toString());
    
                // It will save in target folder
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                // It will save in RealPath
                Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
                
                logger.info("File saved successfully");
                return fname;
            }

            if (role.equals("OWNER")) {
                final String saveto = new ClassPathResource( "static/image/owner/").getFile().getAbsolutePath();
                logger.info("Resource path: {}",saveto);
                String[] type = file.getContentType().split("/");
                String fname = name.replace(" ", "-") + id + "_" + types + "."+type[1];
                logger.info("New file nam: {}",fname);
                Path destination = Paths.get("C:/Users/sakas/OneDrive/Desktop/Work/pgman/src/main/resources/static/image/guest/"+fname);
                logger.info("File Type: {}",file.getContentType());
                Path path = Paths.get(saveto + File.separator + fname);
                logger.info("Path is {}",path.toFile().getAbsolutePath().toString());
 
                // It will save in target folder
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                // It will save in RealPath
                Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
                logger.info("File saved successfully");
                return fname;
            }

            else {
                throw new Exception("User role is not given");
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        return null;
    }
}
