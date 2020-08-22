package pl.michalwilk.business;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import pl.michalwilk.App;
import pl.michalwilk.GameViewController;
import pl.michalwilk.PrimaryController;

import java.util.*;

// TODO: POKI CO TEN GP MOZNA WYJEBAC TYLKO ZE POZNIEJ TRZEBA PRZY TYM POROBIC
public class GameGrid extends GridPane{
    public static final byte GRID_WIDTH = 4, GRID_HEIGHT = 4;
    private static final int TILE_SIZE = 120;
    private ArrayList<GameTile> tile_components_list ;
    private int number_of_tile_components;
    private Integer[][] tile_array_values;
    private Random generator;
    private GridPane main_grid;
    private EventHandler<KeyEvent> keyboard_event_handler;
    private GameTile[][] tile_array;
    private int current_round_score;

    public void add_tile(int row, int column, int value){
        GameTile new_game_tile = new GameTile(row, column, value);
        tile_components_list.add(new_game_tile);
        tile_array_values[row][column] = value;
        tile_array[row][column] = new_game_tile;

        this.add(new_game_tile, column, row);
    }

    private static int emptyval(){  return 0; }

    public void add_tile(){
        // generate tile on random empty location
        ArrayList<Coordinates> coords = new ArrayList<>();

        // looking for empty spaces in grid, then adding them to the temporary list
        for(int row=0;row<4;row++){
            for(int column=0 ; column<4 ; column++){
                if (tile_array_values[row][column] == emptyval()){
                    coords.add(new Coordinates(row, column));
                }
            }
        }

        if (coords.size() == 0){    game_over(); return;    }
        else    {
            int random_val = generator.nextInt(coords.size());
            add_tile(coords.get(random_val).row,
                    coords.get(random_val).column, (random_val % 2 ) * 2);
        }

    }

    private void game_over() {
        System.out.println("you lost :(");
        PrimaryController.set_root("game_over");
    }

    public void move(GameViewController.Moves move) {
        switch (move){
            case UP: System.out.println("gora"); break;
            case DOWN: break;
            case RIGHT: break;
            case LEFT: break;
        };
    }

    private class Coordinates{
        public int row, column;
        public Coordinates(int row, int column){
            this.row = row; this.column = column;
        }
    }

    public int get_current_round_score(){
        return current_round_score;
    }

    public GameGrid() {
        tile_array_values = new Integer[GRID_HEIGHT][GRID_WIDTH];

        for( int i=0 ; i<GRID_HEIGHT ; i++){
            for(int j=0 ; j<GRID_WIDTH ; j++){
                tile_array_values[i][j] = 0;
            }
        }

        tile_components_list = new ArrayList<GameTile>();
        generator = new Random();
        create_gamegrid();

        requestFocus();
    }

    private void create_gamegrid(){
        set_same_cell_size();
        tile_array = new GameTile[GRID_HEIGHT][GRID_HEIGHT];

        empty_grid();

        setStyle("-fx-background-color : #A2AEBB;");
        gridLinesVisibleProperty().set(true);
    }

    private void set_same_cell_size() {
        for (int i=0 ; i<GRID_WIDTH ; i++){
            getColumnConstraints().add(new ColumnConstraints(TILE_SIZE));
            getRowConstraints().add(new RowConstraints(TILE_SIZE));
        }
    }

    private void empty_grid() {
        for (int i=0 ; i<GRID_HEIGHT ; i++){
            for (int j=0 ; j<GRID_WIDTH ; j++){
                if (tile_array[i][j] != null){
                    this.getChildren().remove(i, j);
                }
            }
        }
    }
}
