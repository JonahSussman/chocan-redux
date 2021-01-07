package chocan;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.regex.Pattern;

import org.sqlite.Function;

public class SQLHelper {
	private static String filename = "_tests";
	
	static String read(String filename) throws IOException {
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
	
	static boolean exec(Connection conn, String filename, String ...args) throws SQLException, IOException {
		String str;
		str = read(filename);
		
		PreparedStatement st = conn.prepareStatement(str);
		
		for (int i = 0; i < args.length; i++) {
			st.setString(i + 1, args[i]);
		}
		
		return st.execute();
	}
	
	static void multi(Connection conn, String filename) throws SQLException, IOException {
		String[] a = read(filename).split("(;(\\r)?\\n)|(--\\n)");
		Statement st = null;
		
		try {
			st = conn.createStatement();
			
			for (String s : a) {
				if (s.matches("\\S")) continue;
				if (s.trim().length() > 0) st.execute(s);
			}
		} finally {
			if (st != null) st.close();
		}
	}
	
	public static void start() throws SQLException, IOException {
		Connection conn = createConnection();
		multi(conn, "/create_tables.sql");
		conn.close();
	}
	
	public static Connection createConnection() {
		if (filename == null) {
	    String SALTCHARS = "0123456789abcdef";
	    StringBuilder salt = new StringBuilder();
	    Random rnd = new Random();
	    while (salt.length() < 18) {
	        int index = (int) (rnd.nextFloat() * SALTCHARS.length());
	        salt.append(SALTCHARS.charAt(index));
	    }
	    filename = "_" + salt.toString();
		}
		
		String url = 
			"jdbc:sqlite:" + new File("").getAbsolutePath() + "/" + filename + ".db";
		
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(url);
			Function.create(conn, "REGEXP", new Function() {
	      @Override
	      protected void xFunc() throws SQLException {
	        String exp = value_text(0);
	        String val = value_text(1);
	        if (val == null) val = "";
	
	        Pattern pattern = Pattern.compile(exp);
	        result(pattern.matcher(val).find() ? 1 : 0);
	      }
	    });
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return conn;
	}
}
