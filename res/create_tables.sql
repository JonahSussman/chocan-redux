DROP TABLE IF EXISTS members;
DROP TABLE IF EXISTS providers;
DROP TABLE IF EXISTS services;
DROP TABLE IF EXISTS bills;

DROP TABLE IF EXISTS states;
CREATE TABLE states ( state TEXT PRIMARY KEY );
INSERT INTO states VALUES
("AK"), ("AL"), ("AR"), ("AZ"), ("CA"), ("CO"), ("CT"), ("DE"), ("FL"), ("GA"), 
("HI"), ("IA"), ("ID"), ("IL"), ("IN"), ("KS"), ("KY"), ("LA"), ("MA"), ("MD"), 
("ME"), ("MI"), ("MN"), ("MO"), ("MS"), ("MT"), ("NC"), ("ND"), ("NE"), ("NH"), 
("NJ"), ("NM"), ("NV"), ("NY"), ("OH"), ("OK"), ("OR"), ("PA"), ("RI"), ("SC"), 
("SD"), ("TN"), ("TX"), ("UT"), ("VA"), ("VT"), ("WA"), ("WI"), ("WV"), ("WY"), 
("AS"), ("DC"), ("GU"), ("MP"), ("PR"), ("UM"), ("VI");

CREATE TABLE IF NOT EXISTS members (
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

INSERT INTO members(id, name, street, city, state, zip)
VALUES
	("123456789", "Jonah", "Apple St.", "Dodge City", "GA", "13456");