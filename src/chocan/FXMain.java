package chocan;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXMain extends Application {

    @Override
    public void start(Stage stage) {
      Parent root;
			try {
				root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
      
      Scene scene = new Scene(root, 300, 275);
  
      stage.setTitle("FXML Welcome");
      stage.setScene(scene);
      stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}