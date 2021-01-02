package chocan;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class Main {
	public static Connection connection = null;
	
	static String readSQL(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream(filename)));
	  StringBuilder sb = new StringBuilder();
	  String line = br.readLine();
	
	  while (line != null) {
	      sb.append(line);
	      sb.append(System.lineSeparator());
	      line = br.readLine();
	  }
	  
	  return sb.toString();
	}
	
	static boolean execSQL(String filename, String ...args) throws SQLException, IOException {
		String str;
			
		str = readSQL(filename);
		
		PreparedStatement stmt = connection.prepareStatement(str);
		
		for (int i = 0; i < args.length; i++) {
			stmt.setString(i + 1, args[i]);
		}
		
		return stmt.execute();
	}
	
	public static void main(String args[]) {
		String url = "jdbc:sqlite:" + new File("").getAbsolutePath() + "/Garbage.db";
		
		try {
			connection = DriverManager.getConnection(url);
			
			Statement stmt = connection.createStatement();
			stmt.execute(readSQL("/create_tables.sql"));
			
			try {
				execSQL("/insert_digit.sql", "123456789");
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			
			try {
				execSQL("/insert_digit.sql", "-1");
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			
			// How are CHECKs handled?
			ResultSet rs = stmt.executeQuery("SELECT * FROM digits;");
			
			while (rs.next()) {
				System.out.print("id = " + rs.getInt("id"));
			}
			
		} catch (Exception e) {
			System.err.println(e.getClass() + ": " + e.getMessage());
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		}
	}
}
