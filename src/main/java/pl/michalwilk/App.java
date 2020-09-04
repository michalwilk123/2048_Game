package pl.michalwilk;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.michalwilk.business.GameMode;

import java.io.IOException;

/**
 * @author Michał Wilk <michal.wilk0@yahoo.com>
 */

public class App extends Application {

    public static final String MAIN_GAME_PATH = "secondary";
    private static Scene scene;
    private static Stage primary_stage;
    private static boolean game_continued;

    public static void setGameMode(GameMode game_mode) {
        if(game_mode.equals(GameMode.NEW_GAME)){
            game_continued = false;
        } else if (game_mode.equals(GameMode.CONTINUE_GAME)){
            game_continued = true;
        } else{
            System.out.println("something went very wrong!!!");
        }
    }

    public static boolean is_game_continued(){
        return game_continued;
    }

    public static void start_main_game() throws IOException{
        FXMLLoader loader = new FXMLLoader(App.class.getResource("secondary.fxml"));
        Parent root = null;

        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        GameViewController controller = loader.getController();

        // we set method for the window to exit to
        primary_stage.setOnHidden(e -> controller.shutdown());

        // we set the view of the application
        scene.setRoot(root);
    }

    @Override
    public void start(Stage stage) throws IOException {
        primary_stage = stage;
        FXMLLoader loader = new FXMLLoader(App.class.getResource("primary.fxml"));

        Parent root = null;
        root = loader.load();

        scene = new Scene(root);
        primary_stage.setScene(scene);

        stage.setResizable(false);
        stage.setTitle("2048 Game");
        stage.show();
    }

    public void pre_game_preparations() {
        FXMLLoader loader = new FXMLLoader(App.class.
                getResource("secondary.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GameViewController controller = loader.getController();

        primary_stage.setOnHidden(e -> controller.shutdown());
        loader = new FXMLLoader(App.class.getResource("secondary.fxml"));

        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        scene = new Scene(root);
        primary_stage.setScene(scene);
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}