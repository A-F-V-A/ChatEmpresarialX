package com.business.client;

import com.business.client.controller.ChatController;
import com.business.client.model.Chat;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;

public class Main extends Application {

    private Chat chat;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("chat-business.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        scene.getStylesheets().add(getClass().getResource("src/style/style.css").toExternalForm());

        /* Get controller after loading FXML file */
        ChatController chatController = fxmlLoader.getController();

        /* Get the controller after loading the FXML file */
        chatController.setPrimaryStage(stage);


        chatController.setChat(chat);

        stage.setTitle("Chat");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        if(chat != null){
            System.out.println("La aplicación está finalizando.");
            chat.getConnection().disconnect(chat.signOff());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}