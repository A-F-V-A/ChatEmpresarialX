package com.business.client.model;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.business.client.util.ValidateFile.*;

/**
 * Clase que representa un archivo adjunto en un chat.
 */
public class ChatFile {

    private final String nickname;
    private final Date date;
    private final FileType  fileType;
    private final String readFileContents;

    /**
     * Constructor de la clase ChatFile que recibe el apodo del usuario, la fecha, el contenido del archivo leído y el tipo de archivo.
     *
     * @param nickname            Apodo del usuario que envía el archivo.
     * @param date                Fecha de envío del archivo.
     * @param readFileContents    Contenido del archivo leído.
     * @param fileType            Tipo de archivo.
     */

    public ChatFile(String nickname, Date date,String readFileContents,String fileType){
        this.nickname = nickname;
        this.date = date;
        this.readFileContents = readFileContents;
        try {
            this.fileType = getFileTypeFromFileName("name." + fileType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Constructor de la clase ChatFile que recibe el apodo del usuario, la fecha y el archivo adjunto.
     *
     * @param nickname            Apodo del usuario que envía el archivo.
     * @param date                Fecha de envío del archivo.
     * @param file                Archivo adjunto.
     * @throws Exception          Excepción lanzada en caso de error en la validación del archivo.
     */
    public ChatFile(String nickname, Date date, File file) throws Exception {
        this.nickname = nickname;
        this.date = date;
        validateFile(file);
        this.fileType = getFileTypeFromFileName(file.getName());
        readFileContents = fileToBase64(file);
    }

    /**
     * Constructor de la clase ChatFile que recibe el apodo del usuario y el archivo adjunto.
     * La fecha se establece como la fecha actual.
     *
     * @param nickname            Apodo del usuario que envía el archivo.
     * @param file                Archivo adjunto.
     * @throws Exception          Excepción lanzada en caso de error en la validación del archivo.
     */
    public ChatFile (String nickname, File file) throws Exception {
        this(nickname,new Date(),file);
    }


    /**
     * Obtiene el apodo del usuario.
     *
     * @return El apodo del usuario que envía el archivo.
     */
    public String getNickname() {
        return nickname;
    }


    /**
     * Obtiene la fecha de envío del archivo en formato "dd/MM/yyyy HH:mm".
     *
     * @return La fecha de envío del archivo.
     */
    public String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return formatter.format(date);
    }

    /**
     * Obtiene el contenido del archivo leído.
     *
     * @return El contenido del archivo leído en formato Base64.
     */
    public String getReadFileContents() {
        return readFileContents;
    }

    /**
     * Obtiene el tipo de archivo.
     *
     * @return El tipo de archivo.
     */

    public FileType getFileType() {
        return fileType;
    }

    /**
     * Genera una representación en cadena de la instancia de ChatFile.
     *
     * @return La representación en cadena de la instancia de ChatFile.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb
          .append(CommunicationCode.SEND_FILES.getCode()).append("|")
          .append(nickname).append("|")
          .append(getDate()).append("|")
          .append(fileType.getExtension()).append("|")
          .append(readFileContents);
        return sb.toString();
    }
}