package chocan;

import java.io.File;
import java.sql.*;

public class Main {
	public static void connect() {
		Connection conn = null;
		File dbFile = new File("");
		String url = "jdbc:sqlite:" + dbFile.getAbsolutePath() + "/Demo.db";
		
		System.out.println(url);
		
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		}
	}
	
	public static void main(String args[]) {
		connect();
	}
}
