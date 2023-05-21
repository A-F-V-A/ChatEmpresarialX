module com.business.client.chatbusinessclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens com.business.client to javafx.fxml;
    exports com.business.client;

    opens com.business.client.controller to javafx.fxml;
    exports com.business.client.controller;

    opens com.business.client.model to javafx.fxml;
    exports com.business.client.model;

    opens com.business.client.view to javafx.fxml;
    exports com.business.client.view;
}