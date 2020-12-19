package chocan;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.sql.*;

public class Main {
	public static void main(String args[]) {
		Connection connection = null;
		File dbFile = new File("");
		String url = "jdbc:sqlite:" + dbFile.getAbsolutePath() + "/Demo.db";
		
		try {
			connection = DriverManager.getConnection(url);
			
			try(BufferedReader br = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/test.txt")))) {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    String everything = sb.toString();
		    System.out.println(everything);
			} catch(Exception e) {
				System.err.println(e.getMessage());
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if (connection != null) connection.close();
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		}
	}
}
