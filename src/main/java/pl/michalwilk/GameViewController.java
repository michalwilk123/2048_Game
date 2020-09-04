package pl.michalwilk;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import pl.michalwilk.business.GameGrid;
import pl.michalwilk.business.GameSavedData;

import java.io.IOException;

public class GameViewController {
    public enum Moves{
        UP, DOWN, RIGHT, LEFT
    }

    @FXML
    private Label score_label;

    @FXML
    private AnchorPane grid_container;

    @FXML
    private Label highscore_label;
    @FXML
    private Button restart_button;
    @FXML
    private Button undo_button;

    private GameSavedData gameSavedData;
    private GameGrid game_grid;
    public static String label_text;
    private long high_score;
    private long current_score;

    @FXML
    public void initialize(){
        if (game_grid == null){
            game_grid = new GameGrid();
            // adding first tile
            game_grid.add_tile();
        }
        else{
            game_grid.create_gamegrid();
            game_grid.rebuild_grid();
        }
        grid_container.getChildren().add(game_grid);
        highscore_label.setText("HIGHSCORE: " + gameSavedData.get_highest_score());
        high_score = Integer.parseInt(gameSavedData.get_highest_score());
        current_score = 0;
        System.out.println("initialized");
    }

    @FXML
    public void shutdown(){
        // this function will run when exit button would be pressed
        gameSavedData.setGameGrid(null);

        if (game_grid.isGameInterrupted()){
            gameSavedData.setGameGrid(this.game_grid);
        }
        gameSavedData.save_data();
    }

    @FXML
    public void key_pressed(KeyEvent keyEvent){
        switch(keyEvent.getCode()){
            case UP :   game_grid.move(Moves.UP);       update_score(); break;
            case DOWN:  game_grid.move(Moves.DOWN);     update_score(); break;
            case RIGHT: game_grid.move(Moves.RIGHT);    update_score(); break;
            case LEFT:  game_grid.move(Moves.LEFT);     update_score(); break;
            case NUMPAD0:;  case U : undo_button_clicked(); break;
        };
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
    public void undo_button_clicked(){
        game_grid.rollback();
    }

    private void update_score() {
        long current_round_score = game_grid.get_current_round_score();

        current_score += current_round_score;
        if (current_score > high_score){
            high_score = current_score;
            highscore_label.setText("HIGHSCORE: " + Integer.toString((int) high_score));
        }
        score_label.setText("SCORE: " + Integer.toString((int) current_score));
        label_text = score_label.getText();
        game_grid.set_round_score(0);
    }

    public GameViewController(){
        gameSavedData = new GameSavedData();
        if(App.is_game_continued()){
            gameSavedData = GameSavedData.load_from_memory();
            game_grid = gameSavedData.getGameGrid();
            //gameSavedData.setGameGrid(game_grid);
        }
    }
}
