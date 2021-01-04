PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS states ( state TEXT PRIMARY KEY );
INSERT OR IGNORE INTO states VALUES
("AK"), ("AL"), ("AR"), ("AZ"), ("CA"), ("CO"), ("CT"), ("DE"), ("FL"), ("GA"), 
("HI"), ("IA"), ("ID"), ("IL"), ("IN"), ("KS"), ("KY"), ("LA"), ("MA"), ("MD"), 
("ME"), ("MI"), ("MN"), ("MO"), ("MS"), ("MT"), ("NC"), ("ND"), ("NE"), ("NH"), 
("NJ"), ("NM"), ("NV"), ("NY"), ("OH"), ("OK"), ("OR"), ("PA"), ("RI"), ("SC"), 
("SD"), ("TN"), ("TX"), ("UT"), ("VA"), ("VT"), ("WA"), ("WI"), ("WV"), ("WY"), 
("AS"), ("DC"), ("GU"), ("MP"), ("PR"), ("UM"), ("VI");

CREATE TABLE IF NOT EXISTS members (
	id TEXT PRIMARY KEY
		CHECK ( id REGEXP "^[0-9]{9}$" ),
	name TEXT
		CHECK ( name REGEXP "^[. a-zA-Z]{1,25}$" ),
	street TEXT
		CHECK ( street REGEXP "^.{1,25}$" ),
	city TEXT
		CHECK ( city REGEXP "^.{1,25}$" ),
	state TEXT,
	zip TEXT,
		CHECK ( zip REGEXP "^[0-9]{5}$" ),
	FOREIGN KEY (state)
		REFERENCES states (state)
);

CREATE TABLE IF NOT EXISTS providers (
	id TEXT PRIMARY KEY
		CHECK (id REGEXP "^[0-9]{9}$"),
	name TEXT
		CHECK (name REGEXP "^[. a-zA-Z]{1,25}$"),
	street TEXT
		CHECK (street REGEXP "^.{1,25}$"),
	city TEXT
		CHECK ( city REGEXP "^.{1,25}$"),
	state TEXT,
	zip TEXT,
		CHECK ( zip REGEXP "^[0-9]{5}$"),
	FOREIGN KEY (state)
		REFERENCES states (state)
);

CREATE TABLE IF NOT EXISTS services (
	id TEXT PRIMARY KEY
		CHECK (id REGEXP "^[0-9]{6}$"),
	name TEXT
		CHECK (name REGEXP "^.{1,20}$"),
	fee INTEGER
		CHECK ( fee >= 0 )
);

CREATE TABLE IF NOT EXISTS bills (
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	date_of_service TEXT 
		CHECK ( date(date_of_service) IS NOT NULL ),
	current_time TEXT
		CHECK ( dateTime(current_time) IS NOT NULL ),
	provider_id TEXT,
	member_id TEXT,
	service_id TEXT,
	comments TEXT,
		CHECK ( comments REGEXP "^.{0,100}$"),
	FOREIGN KEY ( provider_id )
		REFERENCES providers (id)
		ON UPDATE CASCADE
		ON DELETE CASCADE,
	FOREIGN KEY ( member_id )
		REFERENCES members (id)
		ON UPDATE CASCADE
		ON DELETE CASCADE,
	FOREIGN KEY ( service_id )
		REFERENCES services (id)
		ON UPDATE CASCADE
		ON DELETE CASCADE
);

INSERT OR IGNORE INTO members(id, name, street, city, state, zip)
VALUES
	("123456789", "Jonah", "Apple St.", "Dodge City", "GA", "13456");
