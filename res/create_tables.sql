CREATE TABLE IF NOT EXISTS digits (
	number INTEGER PRIMARY KEY 
		CHECK(number >= 0 AND number <= 999999999)
);