package com.business.client.controller;

import com.business.client.model.Chat;
import com.business.client.model.ChatFile;
import com.business.client.model.Message;
import com.business.client.view.ChatView;
import javafx.util.Duration;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.business.client.view.ChatView.UserConnect;


public class ChatController {

    /* Campo para almacenar la referencia al Stage principal */
    private Stage primaryStage;

    /* Lógica de negocio */
    private Chat chat;

    /* Interfaz lógica */
    @FXML
    private VBox vbox;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TextArea I_newMessage;
    @FXML
    private VBox messageContainer;
    @FXML
    private Button B_sendMessage;
    @FXML
    private Button B_sendFIle;
    @FXML
    private Button B_connect;
    @FXML
    private Button B_close;

    /**
     * Establece la referencia al Stage principal.
     *
     * @param primaryStage El Stage principal
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Inicializa el controlador.
     * Configura el estado inicial de los elementos de la interfaz de usuario.
     */
    public void initialize() {
        B_sendFIle.setDisable(true);
        B_sendFIle.setMouseTransparent(true);
        B_sendFIle.setCursor(Cursor.WAIT);
        I_newMessage.setDisable(true);
        I_newMessage.setMouseTransparent(true);
        I_newMessage.setCursor(Cursor.WAIT);
        B_sendMessage.setDisable(true);
        B_sendMessage.setMouseTransparent(true);
        B_sendMessage.setCursor(Cursor.WAIT);
        B_close.setDisable(true);
    }


    /**
     * Maneja el evento de enviar un mensaje.
     *
     * @param e El evento del botón
     */
    @FXML
    protected void sendMessage(ActionEvent e){
        String text = I_newMessage.getText().replaceAll("\\n", "");
        if(!text.isEmpty()){
            Message newMessage = new Message(chat.getNickname(),text);
            messageContainer.getChildren().add(ChatView.Message(newMessage,true));
            System.out.println(newMessage.toString());
            I_newMessage.setText("");
            chat.getConnection().sendMessage(newMessage.toString());

            // Scroll to bottom
            scrollPane.applyCss();
            scrollPane.layout();
            scrollPane.setVvalue(1.0);
        }

    }


    @FXML
    protected void sendMessageEnter(KeyEvent e){
        if (e.getCode() == KeyCode.ENTER) {
            String text = I_newMessage.getText().replaceAll("\\n", "");
            if(!text.isEmpty()){
                Message newMessage = new Message(chat.getNickname(),text);
                messageContainer.getChildren().add(ChatView.Message(newMessage,true));
                System.out.println(newMessage.toString());
                I_newMessage.setText("");
                chat.getConnection().sendMessage(newMessage.toString());

                // Scroll to bottom
                scrollPane.applyCss();
                scrollPane.layout();
                scrollPane.setVvalue(1.0);
            }
        }

    }


    /**
     * Maneja el evento de enviar un archivo.
     *
     * @param e El evento del botón
     */
    @FXML
    protected void sendFile(ActionEvent e) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("File PDF", "*.pdf"),
                    new FileChooser.ExtensionFilter("File JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("File JPEG", "*.jpeg")
            );

            /* Show the file selection dialog */
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        ChatFile newFile = new ChatFile(chat.getNickname(), selectedFile);

        messageContainer.getChildren().add(ChatView.MessageFile(newFile,true));

        chat.getConnection().sendMessage(newFile.toString());
        scrollPane.applyCss();
        scrollPane.layout();
        scrollPane.setVvalue(1.0);

    } catch (Exception err) {
            showAlert(Alert.AlertType.ERROR, err.getMessage(), 6);
        System.err.println(err);
    }
}

    /**
     * Maneja el evento de iniciar una conexión.
     *
     * @param e El evento del botón
     */
    @FXML
    protected void startConnection(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/business/client/modal-connection.fxml"));
            Parent root = loader.load();
            ModalConnectionController modal = loader.getController();

            Scene sceneModal = new Scene(root);
            Stage stageModal = new Stage();
            stageModal.initModality(Modality.APPLICATION_MODAL);
            stageModal.setScene(sceneModal);
            stageModal.showAndWait();

            Chat newChat = modal.getNewChat();
            if(newChat != null){
                chat = newChat;
                if(chat.getConnection().connect(newChat.connectSession())){
                    showAlert(Alert.AlertType.INFORMATION, "Operación exitosa.", 1);
                    setVisual(false);
                    B_connect.setDisable(true);
                    chat.getConnection().setDrawMessage(this);
                    chat.getConnection().Hear();
                }else{
                    showAlert(Alert.AlertType.ERROR, "Error de connection", 5);
                }

            }
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, ex.getMessage(), 5);
            throw new RuntimeException(ex);
        }
    }

    /**
     * Maneja el evento de cerrar la conexión.
     *
     * @param e El evento del botón
     */
    @FXML
    protected void  closeConnection(ActionEvent e){
        System.out.println("Connection Finality");
        chat.getConnection().disconnect(chat.signOff());
        B_connect.setDisable(false);
        setVisual(true);
        messageContainer.getChildren().clear();
        vbox.getChildren().clear();
    }


    private void setVisual(boolean status){

        B_sendFIle.setDisable(status);
        B_sendFIle.setMouseTransparent(status);

        I_newMessage.setDisable(status);
        I_newMessage.setMouseTransparent(status);

        B_sendMessage.setDisable(status);
        B_sendMessage.setMouseTransparent(status);

        B_close.setDisable(status);

        if(!status){
            B_sendFIle.setCursor(Cursor.HAND);
            I_newMessage.setCursor(Cursor.HAND);
            B_sendMessage.setCursor(Cursor.HAND);
            B_close.setCursor(Cursor.HAND);
        }

    }

    /**
     * Establece la instancia de la clase Chat.
     *
     * @param chat La instancia de Chat
     */
    public void setChat(Chat chat) {
        this.chat = chat;
    }

    /**
     * Dibuja un mensaje en la interfaz de usuario.
     *
     * @param message El mensaje a dibujar
     */
    public void  DrawMessage(Message message){
        Platform.runLater(() -> {
            messageContainer.getChildren().add(ChatView.Message(message,false));
            // Scroll to bottom
            scrollPane.applyCss();
            scrollPane.layout();
            scrollPane.setVvalue(1.0);
        });
    }

    /**
     * Dibuja un archivo en la interfaz de usuario.
     *
     * @param message El archivo a dibujar
     */
    public void  DrawMessageFile(ChatFile message){
        Platform.runLater(() -> {
            try {
                messageContainer.getChildren().add(ChatView.MessageFile(message,false));
                // Scroll to bottom
                scrollPane.applyCss();
                scrollPane.layout();
                scrollPane.setVvalue(1.0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Actualiza la lista de usuarios conectados en la interfaz de usuario.
     *
     * @param user La lista de usuarios conectados
     */
    public void  loggedInUser(List<String> user){
        Platform.runLater(() -> {
            vbox.getChildren().clear();
            for (String data:user) {
                HBox hbox = UserConnect(data);
                HBox.setHgrow(hbox, Priority.ALWAYS); // Hacer que el HBox ocupe todo el ancho disponible
                vbox.getChildren().add(hbox);
            }
        });
    }

    public void closeConnectionForce(){
        Platform.runLater(() -> {
            System.out.println("Connection Finality");
            chat.getConnection().disconnect(chat.signOff());
            B_connect.setDisable(false);
            setVisual(true);
            messageContainer.getChildren().clear();
            vbox.getChildren().clear();
        });
    }

    /**
     * Muestra una alerta con el tipo, mensaje y duración especificados.
     *
     * @param alertType          El tipo de alerta (ERROR, INFORMATION, WARNING, etc.).
     * @param message            El mensaje que se mostrará en la alerta.
     * @param durationInSeconds La duración en segundos antes de cerrar automáticamente la alerta. Un valor de 0 o negativo significa que la alerta no se cerrará automáticamente.
     */
    public static void showAlert(Alert.AlertType alertType, String message, int durationInSeconds) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.show();

        // Cerrar automáticamente la alerta después de la duración especificada
        if (durationInSeconds > 0) {
            Duration duration = Duration.seconds(durationInSeconds);
            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(duration);
            pause.setOnFinished(event -> alert.close());
            pause.play();
        }
    }
}



