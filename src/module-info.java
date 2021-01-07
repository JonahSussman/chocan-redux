module chocan {
	requires java.base;
	requires java.sql;
	requires sqlite.jdbc;
	requires javafx.graphics;
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.base;
	
	exports chocan;
	opens chocan to javafx.graphics;
	opens chocan.gui to javafx.fxml;
	opens chocan.information to javafx.fxml, javafx.base;
}