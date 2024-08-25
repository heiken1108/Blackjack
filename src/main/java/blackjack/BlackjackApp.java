package blackjack;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class BlackjackApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Blackjack");
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("Blackjack.fxml")));
        scene.getStylesheets().add(getClass().getResource("layout.css").toExternalForm());
        Image icon = new Image(getClass().getResourceAsStream("pngegg.png"));
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(icon);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    
}
