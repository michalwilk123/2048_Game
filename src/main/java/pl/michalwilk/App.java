package pl.michalwilk;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.michalwilk.business.GameMode;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;
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

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("2048 Game");
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
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