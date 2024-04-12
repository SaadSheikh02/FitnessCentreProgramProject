SELECT d.trainer_id, p.first_name, p.last_name, d.start_trainer_date, d.start_time_of_day, d.end_trainer_date, d.end_time_of_day
FROM Dates_Trainer_Available d
JOIN Profiles p ON d.trainer_id = p.username
WHERE '2024-05-20' BETWEEN d.start_trainer_date AND d.end_trainer_date
AND NOT EXISTS (
    SELECT 1
    FROM Dates_Trainer_Unavailable u
    WHERE u.trainer_id = d.trainer_id
    AND u.trainer_date = '2024-05-20'
	AND u.time_of_day = 'MORNING'
);
