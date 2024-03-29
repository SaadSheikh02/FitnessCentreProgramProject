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

CREATE TABLE Rooms (
	room_id SERIAL PRIMARY KEY,
	booking_status TEXT
);

CREATE TABLE Exercise_Routines (
	member_id TEXT,
	exercise TEXT,
	FOREIGN KEY (member_id) REFERENCES Profiles(username)
);

-- Classes Table
CREATE TABLE Classes (
    class_id SERIAL PRIMARY KEY,
	trainer_id TEXT,
	FOREIGN KEY(trainer_id) REFERENCES Profiles(username),
	room_id INT,
	FOREIGN KEY(room_id) REFERENCES Rooms(room_id),
	class_type INT
);

CREATE TABLE Dates_Trainer_unavailable (
	trainer_id TEXT,
	FOREIGN KEY(trainer_id) REFERENCES Profiles(username),
	trainer_date DATE,
	time_of_day TEXT
);

-- Class Relations
CREATE TABLE Class_Members (
    class_id INT,
	FOREIGN KEY (class_id) REFERENCES Classes(class_id),
	member_id TEXT,
	FOREIGN KEY(member_id) REFERENCES Profiles(username),
	class_date DATE
);

CREATE TABLE Bookings (
	room_id INT,
	FOREIGN KEY(room_id) REFERENCES Rooms(room_id),
	class_date DATE,
	time_of_day TEXT
);

CREATE TABLE Equipment (
	equipment_id SERIAL PRIMARY KEY,
	room_id INT,
	FOREIGN KEY(room_id) REFERENCES Rooms(room_id),
	equipment_status TEXT
);

CREATE TABLE Bills(
	bill_id SERIAL PRIMARY KEY,
	member_id TEXT,
	FOREIGN KEY(member_id) REFERENCES Profiles(username),
	price INT,
	date_issued date
);
