package chocan.information;

import javafx.beans.property.SimpleStringProperty;

public class Person {
	private final SimpleStringProperty id     = new SimpleStringProperty("");
	private final SimpleStringProperty name   = new SimpleStringProperty("");
	private final SimpleStringProperty street = new SimpleStringProperty("");
	private final SimpleStringProperty city   = new SimpleStringProperty("");
	private final SimpleStringProperty state  = new SimpleStringProperty("");
	private final SimpleStringProperty zip    = new SimpleStringProperty("");

	public Person() {
		this("", "", "", "", "", "");
	}
	
	public Person(
		String id, String name, String street, 
		String city, String state, String zip
	) {
		setId(id);
		setName(name);
		setStreet(street);
		setCity(city);
		setState(state);
		setZip(zip);
	}

	public String getId() {
		return id.get();
	}

	public void setId(String input) {
		id.set(input);
	}

	public String getName() {
		return name.get();
	}

	public void setName(String input) {
		name.set(input);
	}

	public String getStreet() {
		return street.get();
	}

	public void setStreet(String input) {
		street.set(input);
	}

	public String getCity() {
		return city.get();
	}

	public void setCity(String input) {
		city.set(input);
	}

	public String getState() {
		return state.get();
	}

	public void setState(String input) {
		state.set(input);
	}

	public String getZip() {
		return zip.get();
	}

	public void setZip(String input) {
		zip.set(input);
	}
}
