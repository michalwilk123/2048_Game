package pl.michalwilk;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import pl.michalwilk.business.GameGrid;
import pl.michalwilk.business.GameSavedData;

public class GameViewController {
    // TODO: TUTAJ TRZEBA ZMIENIC TO NA GAMEGRID I W FXMLU WYJEBAC GP NA ANCHORPANE
    public enum Moves{
        UP, DOWN, RIGHT, LEFT
    }

    @FXML
    private Label score_label;

    @FXML
    private AnchorPane grid_container;

    @FXML
    private Label highscore_label;
    // TODO: DODAC HANDLERY DO OBU LABELOW !!! TAK SAMO DO TYCH PRZYCISKOW
    @FXML
    private Button restart_button;
    @FXML
    private Button undo_button;

    private GameSavedData gameSavedData;
    private GameGrid game_grid;

    @FXML
    public void initialize(){
        game_grid = new GameGrid();
        grid_container.getChildren().add(game_grid);

        highscore_label.setText("HIGHSCORE: " + gameSavedData.get_highest_score());

        // adding first tile
        game_grid.add_tile();
    }


    @FXML
    public void key_pressed(KeyEvent keyEvent){
        switch(keyEvent.getCode()){
            case UP :   game_grid.move(Moves.UP);       update_score(); break;
            case DOWN:  game_grid.move(Moves.DOWN);     update_score(); break;
            case RIGHT: game_grid.move(Moves.RIGHT);    update_score(); break;
            case LEFT:  game_grid.move(Moves.LEFT);     update_score(); break;
        };
    }

    private static int retrieve_integer(Label l){
        int cursor = 0;
        for(char c : l.getText().toCharArray()){
            if(Character.isDigit(c))    break;
            else    cursor++;
        }
        if (cursor == l.getText().toCharArray().length){
            System.out.println("empty label!!");
            return -1;
        }

        int value = Integer.parseInt(l.getText().substring(cursor));
        return value;
    }

    private void update_score() {
        int s_value = retrieve_integer(score_label);
        int hs_value = retrieve_integer(highscore_label);

        int current_round_score = game_grid.get_current_round_score();

        s_value += current_round_score;
        if (s_value > hs_value){
            highscore_label.setText("HIGHSCORE: " + Integer.toString(s_value));
        }
        score_label.setText("SCORE: " + Integer.toString(s_value));
    }


    public GameViewController(){
        gameSavedData = new GameSavedData();
        if(App.is_game_continued()){
            gameSavedData.load_from_memory();
            this.continue_game();
            System.out.println("game is continued");
        }
    }

    private void continue_game() {
    }
}
