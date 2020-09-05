package pl.michalwilk;

import javafx.fxml.FXML;
import pl.michalwilk.business.GameMode;

import java.io.IOException;

public class PrimaryController {

    private void switch_to_main_game(){
        try {
            App.start_main_game();
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
