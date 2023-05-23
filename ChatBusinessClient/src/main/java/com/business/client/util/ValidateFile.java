package com.business.client.util;

import com.business.client.model.FileType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * Clase que proporciona métodos estáticos para validar y procesar archivos.
 */
public class ValidateFile {
    private static final long MAX_FILE_SIZE = 600 * 1024; // 600KB
    private static final String[] ALLOWED_EXTENSIONS = { "pdf", "jpg", "jpeg" };

    /**
     * Valida un archivo según las siguientes condiciones: existencia, tamaño máximo y extensión permitida.
     *
     * @param file El archivo a validar.
     * @throws Exception Si el archivo no existe, excede el tamaño máximo o tiene una extensión no permitida.
     */
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

    /**
     * Obtiene el tipo de archivo a partir del nombre de archivo, basado en su extensión.
     *
     * @param fileName El nombre de archivo.
     * @return El tipo de archivo.
     * @throws Exception Si la extensión del archivo no está permitida.
     */
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

    /**
     * Obtiene la extensión de un nombre de archivo.
     *
     * @param fileName El nombre de archivo.
     * @return La extensión del archivo.
     */
    public static String getFileExtension(String fileName) {
        String extension = "";
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            extension = fileName.substring(dotIndex + 1).toLowerCase();
        }
        return extension;
    }



    /**
     * Convierte un archivo de imagen o archivo a su representación en base64.
     *
     * @param imageFile El archivo de imagen.
     * @return La representación en base64 del archivo de imagen.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    public static String fileToBase64(File imageFile) throws IOException {
        byte[] fileContent = Files.readAllBytes(Paths.get(imageFile.getAbsolutePath()));
        return Base64.getEncoder().encodeToString(fileContent);
    }

    /**
     * Convierte una cadena base64 en un arreglo de bytes.
     *
     * @param base64String La cadena base64.
     * @return El arreglo de bytes decodificado.
     */
    public static byte[] base64ToFile(String base64String) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64String);
        return decodedBytes;
    }
}
