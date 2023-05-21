package com.business.client.util;

import com.business.client.model.FileType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class ValidateFile {
    private static final long MAX_FILE_SIZE = 600 * 1024; // 600KB
    private static final String[] ALLOWED_EXTENSIONS = { "pdf", "jpg", "jpeg" };

    public static void validateFile(File file) throws Exception {
        if (file == null || !file.exists())
            throw new Exception("The file does not exist.");

        if (file.length() > MAX_FILE_SIZE)
            throw new Exception("The file exceeds the maximum allowed size.");

        String extension = getFileExtension(file.getName());

        boolean validExtension = false;

        for (String ext : ALLOWED_EXTENSIONS) {
            if (ext.equalsIgnoreCase(extension)) {
                validExtension = true;
                break;
            }
        }

        if (!validExtension)
            throw new Exception("The file extension is not allowed.");
    }

    public static FileType getFileTypeFromFileName(String fileName) throws Exception {
        String extension = getFileExtension(fileName);

        if (extension.equalsIgnoreCase("pdf")) {
            return FileType.PDF;
        } else if (extension.equalsIgnoreCase("jpg")) {
            return FileType.JPG;
        } else if (extension.equalsIgnoreCase("jpeg")) {
            return FileType.JPEG;
        }

        throw new Exception("The file extension is not allowed.");
    }

    public static String getFileExtension(String fileName) {
        String extension = "";
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            extension = fileName.substring(dotIndex + 1).toLowerCase();
        }
        return extension;
    }


    /* Image processing */

    public static String fileToBase64(File imageFile) throws IOException {
        byte[] fileContent = Files.readAllBytes(Paths.get(imageFile.getAbsolutePath()));
        return Base64.getEncoder().encodeToString(fileContent);
    }

    public static byte[] base64ToFile(String base64String/*, String outputFilePath*/) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(base64String);
        //Files.write(Paths.get(outputFilePath), decodedBytes);
        return decodedBytes;
    }
}
