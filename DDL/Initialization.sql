CREATE TABLE Profiles (
	username TEXT PRIMARY KEY,
	passwords TEXT NOT NULL
);

-- Classes Table
CREATE TABLE Classes (
    class_id SERIAL PRIMARY KEY,
	username TEXT,
	FOREIGN KEY(username) REFERENCES Profiles(username),
	class_date DATE,
	class_type INT
);

-- Class Relations
CREATE TABLE Class_Members (
    class_id INT,
	FOREIGN KEY (class_id) REFERENCES Classes(class_id),
	username TEXT,
	FOREIGN KEY(username) REFERENCES Profiles(username),
	class_date DATE
);

CREATE TABLE Members (
    username TEXT PRIMARY KEY,
	first_name TEXT NOT NULL,
	last_name TEXT NOT NULL,
	birthday DATE NOT NULL,
	height NUMERIC, 
	weight NUMERIC, 
	diet_plan TEXT, 
	goal_weight NUMERIC, 
	goal_speed NUMERIC, 
	goal_Lift NUMERIC, 
	weight_deadline DATE, 
	speed_deadline DATE, 
	lift_deadline DATE,
	weight_loss NUMERIC, 
	max_speed NUMERIC, 
	max_lift NUMERIC, 
	bmi NUMERIC,
	systolic_bp INT,
	diastolic_bp INT,
	heart_rate INT, 
	cholestrol_level INT, 
	bloodsugar_level INT,
	FOREIGN KEY (username) REFERENCES Profiles(username)
);

CREATE TABLE Payment_Options (
	payment_id SERIAL PRIMARY KEY,
	username TEXT,
	payment NUMERIC,
	FOREIGN KEY (username) REFERENCES Profiles(username)
);

CREATE TABLE Exercise_Routines (
	exercise_id SERIAL PRIMARY KEY,
	username TEXT,
	exercise TEXT,
	FOREIGN KEY (username) REFERENCES Profiles(username)
);
