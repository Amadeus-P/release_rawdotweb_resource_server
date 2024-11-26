package com.main.web.siwa.utility;


import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUpload {

    public String saveImage(MultipartFile file, String directory){
        Path location = Paths.get(System.getProperty("user.dir"), directory);
        System.out.println("==============location:" + location.toString());

        if(file != null && !file.isEmpty()) {
            // 고유 파일명 생성
            String originalFilename = file.getOriginalFilename();
            String fileName = originalFilename;
            int count = 1;
            System.out.println("originalFilename" + originalFilename);

            // 파일명 중복 확인 및 버전 번호 증가
            Path targetLocation = location.resolve(fileName);
            System.out.println("targetLocation " + targetLocation);
            while (Files.exists(targetLocation)) {
                String nameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
                String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
                fileName = nameWithoutExtension + "(" + count++ + ")" + extension;
//                targetLocation = Paths.get(location + File.separator + fileName); // 경로와 파일명 사이 구분자가 들어가지않음
                targetLocation = location.resolve(fileName);
            }

            File targetFile = new File(String.valueOf(targetLocation));

//            Files.createDirectories(location);
            // 디렉터리가 존재하지 않으면 생성
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }

            // 파일 저장
            try {
                file.transferTo(targetFile);
                System.out.println("File Saved: " + targetLocation.toString());
//                return targetLocation.toString(); // 저장된 파일 경로 반환
                System.out.println("directory + \"/\" + fileName " + directory + "/" + fileName);
                return directory + "/" + fileName;
            } catch (IOException e) {
                throw new RuntimeException("파일 저장 중 오류가 발생했습니다.", e);
            }
        }
        throw new IllegalArgumentException("파일이 비어 있습니다.");
    }

    private void deleteImage(String imagePath) {
        if (imagePath != null) {
            Path path = Paths.get(imagePath);
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                throw new RuntimeException("이미지 삭제 중 오류가 발생했습니다: " + imagePath, e);
            }
        }
    }
}


