module financeflow.com {
    requires javafx.controls;
    requires javafx.fxml;

    opens financeflow.com to javafx.fxml;
    exports financeflow.com;
}
