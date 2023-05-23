package com.business.server.model;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * La clase ChatFile representa un archivo adjunto enviado a través del chat.
 * Contiene información sobre el remitente, la fecha, el tipo de archivo y el contenido del archivo.
 */
public class ChatFile {

    private String nickname;
    private  Date date;
    private FileType fileType;
    private String readFileContents;

    /**
     * Construye un nuevo objeto ChatFile con la información del remitente, la fecha, el contenido y el tipo de archivo.
     *
     * @param nickname         el apodo del remitente del archivo
     * @param date             la fecha de envío del archivo
     * @param file             el contenido del archivo adjunto
     * @param type             el tipo de archivo adjunto
     */
    public ChatFile(String nickname, Date date, String file, FileType type) {
        this.nickname = nickname;
        this.date = date;
        this.fileType = type;
        this.readFileContents = file;
    }

    /**
     * Establece el apodo del remitente del archivo.
     *
     * @param nickname el apodo del remitente
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Obtiene el apodo del remitente del archivo.
     *
     * @return el apodo del remitente
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Obtiene la fecha de envío del archivo en formato de texto.
     *
     * @return la fecha de envío del archivo como una cadena en formato "dd/MM/yyyy HH:mm"
     */
    public String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return formatter.format(date);
    }

    /**
     * Obtiene el contenido del archivo adjunto.
     *
     * @return el contenido del archivo
     */
    public String getReadFileContents() {
        return readFileContents;
    }

    /**
     * Obtiene el tipo de archivo adjunto.
     *
     * @return el tipo de archivo
     */
    public FileType getFileType() {
        return fileType;
    }

    /**
     * Genera una representación en cadena del objeto ChatFile en el formato adecuado para enviarlo a través del chat.
     *
     * @return una cadena que representa el objeto ChatFile en el formato "code|nickname|date|extension|contents"
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
