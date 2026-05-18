module org.example.pa_pbo {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.pa_pbo to javafx.fxml;
    exports org.example.pa_pbo;
}