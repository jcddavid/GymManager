module com.progetto.gymmanager {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires java.logging;

    opens com.progetto.gymmanager to javafx.fxml;
    exports com.progetto.gymmanager;

    opens com.progetto.gymmanager.controller to javafx.fxml;
    exports com.progetto.gymmanager.controller;

    opens com.progetto.gymmanager.view.fx to javafx.fxml;
    exports com.progetto.gymmanager.view.fx;

    opens com.progetto.gymmanager.bean to javafx.base;
    exports com.progetto.gymmanager.bean;
}