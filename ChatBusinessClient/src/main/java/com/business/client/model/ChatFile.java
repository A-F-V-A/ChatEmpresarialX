package com.business.client.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatFile {

    private static final long MAX_FILE_SIZE = 600 * 1024; // 600KB
    private static final String[] ALLOWED_EXTENSIONS = { "pdf", "jpg" };
    private String nickname;
    private String filePath;
    private final Date date;
    private String readFileContents;
    private FileType fileType;


    public ChatFile(String nickname, Date date, String filePath) {
        this.nickname = nickname;
        this.date = date;

        validateFile(filePath);
        this.filePath = filePath;
        this.fileType = getFileTypeFromExtension(filePath);

        readFileContents();
    }

    public ChatFile (String nickname, String filePath){
        this(nickname,new Date(),filePath);
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return formatter.format(date);
    }

    private void validateFile(String filePath) {
        File file = new File(filePath);

        long fileSize = file.length();
        String fileExtension = getFileExtension(file.getName());

        // Check if the file exists
        if (!file.exists()) {
            throw new IllegalArgumentException("File does not exist.");
        }

        // Check the size of the file
        if (fileSize > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds the allowed limit.");
        }

        // Check the file extension
        boolean validExtension = false;
        for (String allowedExtension : ALLOWED_EXTENSIONS) {
            if (fileExtension.equalsIgnoreCase(allowedExtension)) {
                validExtension = true;
                break;
            }
        }

        if (!validExtension) {
            throw new IllegalArgumentException("Invalid file extension.");
        }
    }
    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }
    private FileType getFileTypeFromExtension(String filePath) {
        String fileExtension = getFileExtension(filePath);
        for (FileType fileType : FileType.values()) {
            if (fileType.getExtension().equalsIgnoreCase(fileExtension)) {
                return fileType;
            }
        }
        return null; // Or handle the case when the extension is not found
    }
    private void readFileContents() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder contentBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line);
                contentBuilder.append("\n");
            }
            readFileContents = contentBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb
          .append(CommunicationCode.SEND_FILES.getCode())
          .append(nickname).append("|")
          .append(getDate()).append("|")
          .append(fileType.getExtension()).append("|")
          .append(readFileContents);
        return sb.toString();
    }
}