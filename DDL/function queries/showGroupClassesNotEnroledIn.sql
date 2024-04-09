SELECT Classes.*
FROM Classes
LEFT JOIN Class_Members ON Classes.class_id = Class_Members.class_id AND Class_Members.username = 'jadfakhoury'
WHERE Classes.class_type = 'GROUP_TYPE'
AND Class_Members.class_id IS NULL;
