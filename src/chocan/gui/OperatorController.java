package chocan.gui;

import java.io.IOException;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;

public class OperatorController {
	@FXML
  private Label addTitle = null;
	
	String getTable(Event ev) {
		String id = ((Control)ev.getSource()).getId();
		if (id.contains("Member")) {
			return "members";
		} else {
			return "providers";
		}
	}
	
	@FXML
	public void add(Event ev) {
		String table = getTable(ev);
		Stage stage = new Stage();
		
		
		
		try {
			Parent root = 
					FXMLLoader.load(getClass().getResource("/fxml/operator/add.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		addTitle.setText("Add to " + table);
		
		stage.show();
	}
	
	@FXML
	public void update(Event ev) {
		String table = getTable(ev);
	}
	
	@FXML
	public void delete(Event ev) {
		String table = getTable(ev);
	}
}
