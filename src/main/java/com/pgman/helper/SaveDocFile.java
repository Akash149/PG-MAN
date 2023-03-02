package com.pgman.helper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

public class SaveDocFile {

    public static String savefile(String role, String type, String email, String id, MultipartFile file) {

        try {
            if (role.equals("GUEST")) {
                String saveto = "/static/guest";
                // C:\Users\sakas\OneDrive\Desktop\Work\pgman\src\main\resources\static\guest
                String filename = email + "_" + type + "_" + id;

                File savefile = new ClassPathResource(saveto).getFile();
                System.out.println(savefile.getParentFile());
                Path path = Paths.get(savefile.getAbsolutePath() + File.separator + filename);

                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                return filename;
            }

            if (role.equals("OWNER")) {
                String saveto = "/static/OWNER/";
                String filename = email + "_" + type + "_" + id;

                File savefile = new ClassPathResource(saveto).getFile();
                System.out.println(savefile.getParentFile());
                Path path = Paths.get(savefile.getAbsolutePath() + File.separator + filename);

                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("-----------  file  saved  -----------");
                return filename;
            }

            else {
                throw new Exception("User role is not given");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return null;
    }
}
