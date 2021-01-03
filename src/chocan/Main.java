package chocan;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Random;
import java.util.regex.Pattern;

import org.sqlite.Function;

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
	
	static void multiSQL(String filename) throws SQLException, IOException {
		String[] a = readSQL(filename).split(";");
		Statement stmt = connection.createStatement();

		for (String s : a) {
			if (s.matches("\\S")) continue;
			stmt.execute(s);
			System.out.println(s);
		}
		
	}
	
	public static void main(String args[]) {
    String SALTCHARS = "0123456789abcdef";
    StringBuilder salt = new StringBuilder();
    Random rnd = new Random();
    while (salt.length() < 18) {
        int index = (int) (rnd.nextFloat() * SALTCHARS.length());
        salt.append(SALTCHARS.charAt(index));
    }
		String url = "jdbc:sqlite:" + new File("").getAbsolutePath() + "/" + salt.toString() + ".db";
		
		try {
			connection = DriverManager.getConnection(url);
			Function.create(connection, "REGEXP", new Function() {
        @Override
        protected void xFunc() throws SQLException {
          String expression = value_text(0);
          String value = value_text(1);
          if (value == null)
              value = "";

          Pattern pattern = Pattern.compile(expression);
          result(pattern.matcher(value).find() ? 1 : 0);
        }
	    });
			
			
			multiSQL("/create_tables.sql");
//			
//	    ResultSet rss = connection.getMetaData().getTables(null, null, null, null);
//	    while (rss.next()) {
//	        System.out.println(rss.getString("TABLE_NAME"));
//	    }
			
			Statement stmt = connection.createStatement();
			try {
				execSQL("/insert_member.sql", 
						"321", "Jonah", "Street", "Coty", "GA", "123456"
				);
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			
			// How are CHECKs handled?
//			ResultSet rs = stmt.executeQuery("SELECT * FROM members;");
//			
//			while (rs.next()) {
//				System.out.print("id = " + rs.getString(1));
//			}
			
			connection.close();
		} catch (Exception e) {
			System.err.println(e.getClass() + ": " + e.getMessage());
		}
	}
}
