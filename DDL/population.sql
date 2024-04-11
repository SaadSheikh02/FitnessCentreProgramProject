INSERT INTO 
Profiles (username, passwords, first_name, last_name, user_type) 
VALUES ('abdool', 'password123', 'Abdullah', 'Aswad', 'TYPE_ADMIN');

INSERT INTO 
Profiles (username, passwords, first_name, last_name, user_type) 
VALUES ('squigz', 'compression', 'Saad', 'Sheikh', 'TYPE_ADMIN');

INSERT INTO 
Profiles (username, passwords, first_name, last_name, user_type) 
VALUES ('sonic', 'speed', 'Sonic', 'Hedgehog', 'TYPE_TRAINER');

INSERT INTO
Dates_Trainer_Available (trainer_id, start_trainer_date, start_time_of_day, end_trainer_date, end_time_of_day)
VALUES ('sonic', '2024-05-01', 'MORNING', '2024-05-31', 'EVENING');

INSERT INTO
Classes (trainer_id, room_id, class_type, class_description, class_date, time_of_day)
VALUES ('sonic', 1, 'GROUP_TYPE', 'Kinesthetics', '2024-05-15', 'AFTERNOON');

INSERT INTO
Dates_Trainer_Unavailable (trainer_id, trainer_date, time_of_day) VALUES 
('sonic', '2024-05-15', 'AFTERNOON');

INSERT INTO Bookings (room_id, class_date, time_of_day, event_info) 
VALUES (1, '2024-05-15', 'AFTERNOON', 'Kinesthetics');

INSERT INTO Equipment (room_id, equipment_status) VALUES
(1, 'GOOD'),
(1, 'REQUIRES MAINTENANCE'),
(1, 'BROKEN');

INSERT INTO 
Profiles (username, passwords, first_name, last_name, user_type) 
VALUES ('shadow', 'edge', 'Shadow', 'Hedgehog', 'TYPE_TRAINER');

INSERT INTO 
Profiles (username, passwords, first_name, last_name, user_type) 
VALUES ('knuckles', 'Punch', 'Knuckles', 'Echidna', 'TYPE_TRAINER');

INSERT INTO
Profiles(username, passwords, first_name, last_name, user_type)
VALUES ('jadfakhoury', 'football', 'Jad', 'Fakhoury', 'TYPE_MEMBER');

INSERT INTO
Members(username, credit_card, birthday, height, weight, diet_plan, goal_weight, goal_speed, goal_lift, weight_deadline, speed_deadline, lift_deadline, bmi, systolic_bp, diastolic_bp, heart_rate, bloodsugar_level)
VALUES ('jadfakhoury', '123456789', '2003-01-29', 196, 87, 'keto', 80, 9, 225, '2024-06-16', '2024-06-17', '2024-06-18', 25, 109, 68, 70, 83);

INSERT INTO Exercise_Routines(username, exercise)
VALUES ('jadfakhoury', 'burpees'),
('jadfakhoury', 'pushups'),
('jadfakhoury', 'mountain climbers'),
('jadfakhoury', 'situps');

INSERT INTO Weight_Statistics (username, weight) VALUES
('jadfakhoury', 70),
('jadfakhoury', 72),
('jadfakhoury', 68),
('jadfakhoury', 75),
('jadfakhoury', 65);

INSERT INTO Speed_Statistics (username, speed) VALUES
('jadfakhoury', 10),
('jadfakhoury', 11),
('jadfakhoury', 9),
('jadfakhoury', 12),
('jadfakhoury', 8);

INSERT INTO Lift_Statistics (username, lift) VALUES
('jadfakhoury', 100),
('jadfakhoury', 105),
('jadfakhoury', 95),
('jadfakhoury', 110),
('jadfakhoury', 90);
