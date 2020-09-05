module pl.michalwilk {
    requires javafx.controls;
    requires javafx.fxml;

    opens pl.michalwilk to javafx.fxml;
    exports pl.michalwilk;
}