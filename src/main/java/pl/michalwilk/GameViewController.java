package pl.michalwilk;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import pl.michalwilk.business.GameSavedData;

public class GameViewController {

    private GameSavedData gameSavedData;

    public GameViewController(){
        if(App.is_game_continued()){
            gameSavedData = new GameSavedData();
            gameSavedData.load_from_device();
            this.continue_game();
            System.out.println("game is continued");
        }
    }

    private void continue_game() {
    }

    @FXML
    private GridPane tile_grid;

    @FXML
    private Label score_label;

    @FXML
    private Label highscore_label;

    @FXML
    private Button restart_button;

    @FXML
    private Button undo_button;

    @FXML
    public void initialize(){

    }
}
