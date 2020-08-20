package pl.michalwilk;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import pl.michalwilk.business.GameMode;

public class PrimaryController {

    private void switch_to_main_game(){
        try {
            App.setRoot("secondary");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void new_game_clicked(){
        App.setGameMode(GameMode.NEW_GAME);
        switch_to_main_game();
    }

    @FXML
    private void continue_game_clicked(){
        App.setGameMode(GameMode.CONTINUE_GAME);
        switch_to_main_game();
    }
}
