package com.business.client.view;

import com.business.client.controller.ChatController;

import com.business.client.model.User;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChatView extends Application{

    private ChatController chatController;
    private User user;
    private ListView<String> chatListView;
    private TextArea messageTextArea;

    public ChatView(ChatController chatController, User user) {
        this.chatController = chatController;
        this.user = user;
    }

    public ChatView() {
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chat Empresarial");

        BorderPane root = new BorderPane();

        chatListView = new ListView<>();
        messageTextArea = new TextArea();
        messageTextArea.setPromptText("Escribe un mensaje...");
        Button sendButton = new Button("Enviar");

        VBox chatBox = new VBox();
        chatBox.getChildren().addAll(chatListView);
        chatBox.setSpacing(10);

        HBox messageBox = new HBox();
        messageBox.getChildren().addAll(messageTextArea, sendButton);
        messageBox.setSpacing(10);
        messageBox.setPadding(new Insets(10));

        root.setCenter(chatBox);
        root.setBottom(messageBox);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);

        sendButton.setOnAction(event -> {
            String message = messageTextArea.getText();
            if (!message.isEmpty()) {
                chatController.sendMessage(user, message);
                messageTextArea.clear();
            }
        });

        primaryStage.show();
    }

    public void displayMessage(String sender, String message) {
        String formattedMessage = "[" + sender + "]: " + message;
        chatListView.getItems().add(formattedMessage);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
