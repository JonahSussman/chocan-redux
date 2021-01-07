package chocan.gui;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import chocan.SQLHelper;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.util.Pair;

public class OperatorController {
	String getTable(Event ev) {
		String id = ((Control)ev.getSource()).getId();
		if (id.contains("Member")) {
			return "members";
		} else {
			return "providers";
		}
	}
	
	Dialog<String[]> getForm(String table, String inputId, String inputName, String inputStreet, String inputCity, String inputState, String inputZip) {
		Dialog<String[]> dialog = new Dialog<>();
		dialog.setHeaderText("Add to " + table);

		ButtonType submitButtonType = new ButtonType("Submit", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(
				submitButtonType, ButtonType.CANCEL);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		TextField id = new TextField();
		id.setPromptText("ID");
		TextField name = new TextField();
		name.setPromptText("Name");
		TextField street = new TextField();
		street.setPromptText("Street");
		TextField city = new TextField();
		city.setPromptText("City");
		TextField state = new TextField();
		state.setPromptText("State");
		TextField zip = new TextField();
		zip.setPromptText("Zip");

		grid.add(new Label("ID:"), 0, 0);
		grid.add(id, 1, 0);
		grid.add(new Label("Name:"), 0, 1);
		grid.add(name, 1, 1);
		grid.add(new Label("Street:"), 0, 2);
		grid.add(street, 1, 2);
		grid.add(new Label("City:"), 0, 3);
		grid.add(city, 1, 3);
		grid.add(new Label("State:"), 0, 4);
		grid.add(state, 1, 4);
		grid.add(new Label("Zip:"), 0, 5);
		grid.add(zip, 1, 5);

		dialog.getDialogPane().setContent(grid);

		dialog.setResultConverter(dialogButton -> {
	    if (dialogButton == submitButtonType) {
    		String[] arr = {
  				id.getText(), name.getText(), street.getText(), 
  				city.getText(), state.getText(), zip.getText() 
    		};
        return arr;
	    }
	    return null;
		});
		
		return dialog;
	}
	
	@FXML
	public void add(Event ev) {
		String table = getTable(ev);
		
		Dialog<String[]> dialog = getForm(table, table, table, table, table, table, table);

		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("That doesn't look right. Please try again.");

		boolean validated = false;
		
		while(!validated) {
			Optional<String[]> result = dialog.showAndWait();
			
			if (!result.isPresent()) return;
			
			try {
				Connection conn = SQLHelper.createConnection();
				PreparedStatement st = conn.prepareStatement(
						"INSERT INTO " + table + " VALUES(?, ?, ?, ?, ?, ?);");
				
				String[] r = result.get();
				st.setString(1, r[0]);
				st.setString(2, r[1]);
				st.setString(3, r[2]);
				st.setString(4, r[3]);
				st.setString(5, r[4]);
				st.setString(6, r[5]);
				
				// ResultSet rs = 
				st.execute();
				validated = true;
				
			} catch (Exception ex) {
				System.err.println(ex.getMessage());
			}
			
			if (!validated) alert.showAndWait();
		}
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
