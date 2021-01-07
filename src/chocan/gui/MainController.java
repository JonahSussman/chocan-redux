package chocan.gui;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

import chocan.SQLHelper;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class MainController {
	void loadScene(String fxml) {
		Stage stage = new Stage();
		
		try {
			Parent root = 
					FXMLLoader.load(getClass().getResource(fxml));
			
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
			
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(ex.getMessage());
		}
	}
	
	@FXML
	public void launchProviderGUI(Event ev) {
		boolean validated = false;
		
		TextInputDialog dialog = new TextInputDialog("#########");
		dialog.setTitle("Provider Login");
		dialog.setHeaderText("Welcome.\nPlease enter a valid provider id.");
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("That doesn't look right. Please try again.");
		
		while (!validated) {
			Optional<String> result = dialog.showAndWait();
		
			if (!result.isPresent()) return;
			
			try {
				Statement st = SQLHelper.createConnection().createStatement();
				
				ResultSet rs = st.executeQuery(
					"SELECT 1 FROM providers WHERE id = " + result.get());
				
				if (rs.next()) validated = true;
				
			} catch (Exception ex) {
				System.err.println(ex.getMessage());
			}
			
			if (!validated) alert.showAndWait();
		}
		
		loadScene("/fxml/provider_gui.fxml");
	}

	@FXML
	public void launchManagerGUI(Event ev) {
		loadScene("/fxml/manager_gui.fxml");
	}
	
	@FXML
	public void launchOperatorGUI(Event ev) {
		loadScene("/fxml/operator_gui.fxml");
	}
	
	@FXML
	public void mainAccountingProcedure(Event ev) {
		loadScene("/fxml/operator_gui_exp.fxml");
	}
}
