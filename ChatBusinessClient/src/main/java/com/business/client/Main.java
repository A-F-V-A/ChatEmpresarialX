package com.business.client;

import com.business.client.controller.ChatController;
import com.business.client.view.ChatView;
import javafx.application.Application;

public class Main /*extends Application*/ {

    /*

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main/chat.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Chat");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

    */

    public static void main(String[] args) {
        ChatController chatController = new ChatController();
        User user = new User("Juan", "David");

        // Crear la instancia de ChatView y configurar el usuario
        ChatView chatView = new ChatView(chatController, user);

        // Configurar el ChatController como el controlador de la vista

        // Lanzar la aplicación JavaFX
        Application.launch(ChatView.class, args);
    }
}