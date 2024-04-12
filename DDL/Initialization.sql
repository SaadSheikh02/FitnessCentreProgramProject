CREATE TABLE Profiles (
	username TEXT PRIMARY KEY,
	passwords TEXT NOT NULL,
	first_name TEXT NOT NULL,
	last_name TEXT NOT NULL,
	user_type TEXT NOT NULL
);

CREATE TABLE Members (
    username TEXT PRIMARY KEY,
	credit_card INT NOT NULL,
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
	bmi NUMERIC,
	systolic_bp INT,
	diastolic_bp INT,
	heart_rate INT, 
	bloodsugar_level INT,
	FOREIGN KEY (username) REFERENCES Profiles(username)
);

CREATE TABLE Exercise_Routines (
	username TEXT,
	exercise TEXT,
	FOREIGN KEY (username) REFERENCES Profiles(username)
);

CREATE TABLE Weight_Statistics (
	username TEXT,
	FOREIGN KEY (username) REFERENCES Profiles(username),
	weight NUMERIC
);

CREATE TABLE Speed_Statistics (
	username TEXT,
	FOREIGN KEY (username) REFERENCES Profiles(username),
	speed NUMERIC
);

CREATE TABLE Lift_Statistics (
	username TEXT,
	FOREIGN KEY (username) REFERENCES Profiles(username),
	lift NUMERIC
);

-- Classes Table
CREATE TABLE Classes (
    class_id SERIAL PRIMARY KEY,
	trainer_id TEXT,
	FOREIGN KEY(trainer_id) REFERENCES Profiles(username),
	room_id INT,
	class_type TEXT,
	class_description TEXT,
	class_date DATE,
	time_of_day TEXT
);

ALTER TABLE Classes
ADD CONSTRAINT unique_time_slot UNIQUE (room_id, class_date, time_of_day);

CREATE TABLE Dates_Trainer_Available (
	schedule_id SERIAL PRIMARY KEY,
	trainer_id TEXT,
	FOREIGN KEY(trainer_id) REFERENCES Profiles(username),
	start_trainer_date DATE,
	start_time_of_day TEXT,
	end_trainer_date DATE,
	end_time_of_day TEXT
);

CREATE TABLE Dates_Trainer_Unavailable (
	trainer_id TEXT,
	FOREIGN KEY(trainer_id) REFERENCES Profiles(username),
	trainer_date DATE,
	time_of_day TEXT
);

-- Class Relations
CREATE TABLE Class_Members (
    class_id INT,
	FOREIGN KEY (class_id) REFERENCES Classes(class_id),
	username TEXT,
	FOREIGN KEY(username) REFERENCES Profiles(username)
);

ALTER TABLE Class_Members
ADD CONSTRAINT unique_class_user_constraint UNIQUE (class_id, username);

CREATE TABLE Bookings (
	booking_id SERIAL PRIMARY KEY,
	room_id INT,
	class_date DATE,
	time_of_day TEXT,
	event_info TEXT
);

CREATE TABLE Equipment (
	equipment_id SERIAL PRIMARY KEY,
	room_id INT,
	equipment_status TEXT
);

CREATE TABLE Bills(
	bill_id SERIAL PRIMARY KEY,
	username TEXT,
	FOREIGN KEY(username) REFERENCES Profiles(username),
	price INT,
	date_issued date
);
