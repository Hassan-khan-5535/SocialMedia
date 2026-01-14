module com.example.socialmedia {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;             // <--- Grants permission to use SQL classes
    requires mysql.connector.j;    // <--- Grants permission to use the MySQL driver

    opens com.example.socialmedia to javafx.fxml;
    exports com.example.socialmedia;
}