module chocan {
	requires java.base;
	requires java.sql;
	requires sqlite.jdbc;
	requires javafx.graphics;
	requires javafx.controls;
	requires javafx.fxml;
	
	exports chocan;
	opens chocan to javafx.graphics;
}