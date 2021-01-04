package chocan;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.Scanner;

import org.sqlite.Function;

public class ConsoleDatabaseEditor {
	static Scanner in = new Scanner(System.in);
	
	static final HashMap<String, String[]> schemas = new HashMap<>();
	static final HashMap<String, String> addSQLs = new HashMap<>();
	static final HashMap<String, String> readSQLs = new HashMap<>();
	static final HashMap<String, String> updateSQLs = new HashMap<>();
	static final HashMap<String, String> deleteSQLs = new HashMap<>();
	
	static {
		schemas.put("members",
				new String[]{ "id", "name", "street", "city", "state", "zip" });
		schemas.put("providers",
				new String[]{ "id", "name", "street", "city", "state", "zip" });
		schemas.put("services",
				new String[]{ "id", "name", "fee"});
		schemas.put("bills",
				new String[]{ "date_of_service", "current_time", "provider_id", "member_id", "service_id", "comments" });
		
		addSQLs.put("members", "INSERT INTO members(id, name, street, city, state, zip) VALUES(?, ?, ?, ?, ?, ?);");
		addSQLs.put("providers", "INSERT INTO providers(id, name, street, city, state, zip) VALUES(?, ?, ?, ?, ?, ?);");
		addSQLs.put("services", "INSERT INTO services(id, name, fee) VALUES(?, ?, ?);");
		addSQLs.put("bills", "INSERT INTO bills(date_of_service, current_time, provider_id, member_id, service_id, comments) VALUES(?, datetime('now', 'localtime'), ?, ?, ?, ?);");
		
		updateSQLs.put("members", "UPDATE members SET id=?, name=?, street=?, city=?, state=?, zip=? WHERE id=?;");
		updateSQLs.put("providers", "UPDATE providers SET id=?, name=?, street=?, city=?, state=?, zip=? WHERE id=?;");
		updateSQLs.put("services", "UPDATE services SET id=?, name=?, fee=? WHERE id=?;");
		updateSQLs.put("bills", "UPDATE members SET date_of_service=?, provider_id=?, member_id=?, service_id=?, comments=? WHERE id = ?;");
	}
	
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
	
	static void multiSQL(Connection conn, String filename) throws SQLException, IOException {
		String[] a = readSQL(filename).split("(;(\\r)?\\n)|(--\\n)");
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
	
	public static void main(String[] args) {
		System.out.println(".db file? (Leave blank for random.)");
		String filename = in.nextLine();
		
		if (filename.length() == 0) {
	    String SALTCHARS = "0123456789abcdef";
	    StringBuilder salt = new StringBuilder();
	    Random rnd = new Random();
	    while (salt.length() < 18) {
	        int index = (int) (rnd.nextFloat() * SALTCHARS.length());
	        salt.append(SALTCHARS.charAt(index));
	    }
	    filename = salt.toString();
		}
		
		String url = "jdbc:sqlite:" + new File("").getAbsolutePath() + "/_" + filename + ".db";
		
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
			
			Function.create(conn, "REGEXP", new Function() {
	      @Override
	      protected void xFunc() throws SQLException {
	        String expression = value_text(0);
	        String value = value_text(1);
	        if (value == null) value = "";
	
	        Pattern pattern = Pattern.compile(expression);
	        result(pattern.matcher(value).find() ? 1 : 0);
	      }
	    });
			
			multiSQL(conn, "/create_tables.sql");
			
			String input = "";
			while (!input.equals("q")) {
				if (input.equals("1")) edit(conn, "members");
				if (input.equals("2")) edit(conn, "providers");
				if (input.equals("3")) edit(conn, "services");
				if (input.equals("4")) edit(conn, "bills");
				
				System.out.println("Which table? (Type 'q' to quit.)");
				System.out.println("  1) members");
				System.out.println("  2) providers");
				System.out.println("  3) services");
				System.out.println("  4) bills");
				
				input = in.nextLine();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	private static void print(String table, String[] schema, ResultSet rs) throws SQLException {
		while(rs.next()) {
			int to = table.equals("bills") ? schema.length + 1 : schema.length;
			for (int i = 0; i < to; i++) {
				System.out.print(rs.getString(i + 1) + " ");
			} System.out.print("\n");
		}
	}
	
	private static void add(Connection conn, String[] schema, String addSQL) throws SQLException {
		PreparedStatement st = conn.prepareStatement(addSQL);		
		
		int sub = 0;
		for (int i = 0; i < schema.length; i++) {
			if (schema[i].equals("current_time")) {
				sub += 1;
				continue;
			}
			
			System.out.print(schema[i] + "? ");
			st.setString(i + 1 - sub, in.nextLine());
		}
		
		st.execute();
		st.close();
	}
	
	private static void update(Connection conn, String[] schema, String updateSQL, String id) throws SQLException {
		PreparedStatement st = conn.prepareStatement(updateSQL);		
		
		int sub = 0;
		for (int i = 0; i < schema.length; i++) {
			if (schema[i].equals("current_time")) {
				sub += 1;
				continue;
			}
			
			System.out.print(schema[i] + "? ");
			st.setString(i + 1 - sub, in.nextLine());
		}
		
		st.setString(schema.length + 1 - sub, id);
		st.execute();
		st.close();
	}
	
	private static void edit(Connection conn, String table) {
		String input = "";
		String[] schema = schemas.get(table);
		String addSQL = addSQLs.get(table);
		String updateSQL = updateSQLs.get(table);
		
		while(!input.equals("q")) {
			try {
				if (input.equals("a")) {
					add(conn, schema, addSQL);
				}
				
				if (input.equals("r")) {
					System.out.println("id? ");
					ResultSet rs = 
							conn.createStatement().executeQuery("SELECT * FROM " + table + " WHERE id = " + in.nextLine() + ";");
					
					print(table, schema, rs);
				}
				
				if (input.equals("u")) {
					System.out.println("id? ");
					String id = in.nextLine();

					update(conn, schema, updateSQL, id);
				}
				
				if (input.equals("d")) {
					System.out.println("id? ");
					conn.createStatement().execute("DELETE FROM " + table + " WHERE id = " + in.nextLine() + ";");
				}
				
				if (input.equals("v")) {
					for (String column : schema) {
						System.out.print(column + " ");
					} System.out.print("\n");
					
					ResultSet rs = 
							conn.createStatement().executeQuery("SELECT * FROM " + table + ";");
	
					print(table, schema, rs);
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			
			System.out.println("Editing " + table + ".");
			System.out.println("What do you want to do? (Type 'q' to return back.)");
			System.out.println("  [a]dd");
			System.out.println("  [r]ead");
			System.out.println("  [u]pdate");
			System.out.println("  [d]elete");
			System.out.println("  [v]iew database");
			input = in.nextLine();
		}
	}
}
