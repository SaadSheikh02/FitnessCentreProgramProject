SELECT
    MAX(weight) AS max_weight,
    MIN(weight) AS min_weight,
    AVG(weight) AS avg_weight,
    MAX(speed) AS max_speed,
    MIN(speed) AS min_speed,
    AVG(speed) AS avg_speed,
    MAX(lift) AS max_lift,
    MIN(lift) AS min_lift,
    AVG(lift) AS avg_lift
FROM Health_Statistics
WHERE username = 'jadfakhoury';
