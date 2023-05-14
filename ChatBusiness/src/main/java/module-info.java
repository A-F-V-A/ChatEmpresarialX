module ChatBusiness {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.business to javafx.fxml;
    exports com.business;

    opens com.business.controller to javafx.fxml;
    exports com.business.controller;

    opens com.business.model to javafx.fxml;
    exports com.business.model;

    opens com.business.view to javafx.fxml;
    exports com.business.view;
}