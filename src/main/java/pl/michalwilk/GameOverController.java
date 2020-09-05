package pl.michalwilk;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class GameOverController {
    private String label_string;

    @FXML
    private Label end_score_label;

    @FXML
    public void exit_program(){
        Platform.exit();
    }

    @FXML
    public void restart_game(){
        try {
            App.start_main_game();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize(){
        label_string = GameViewController.label_text;
        if (label_string == null){
            throw new NullPointerException("VALUE OF SCORE TEXT CANNOT BE EMPTY!!!");
        }
        end_score_label.setText(label_string);
    }
}
