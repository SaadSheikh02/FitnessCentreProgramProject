import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Main {
    static Connection connection;
    static Scanner input;

    private static String HOST = "localhost";
    private static String PORT = "5432";
    private static String DB_NAME = "FitnessApplication";
    private static String USER = "postgres";
    private static String PASSWORD = "DarkSniper22";

    private static String username = null;

    public static void main(String[] args) {

        String url = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME; // change url accordingly
        String user = USER;
        String password = PASSWORD; // change password accordingly
        input = new Scanner(System.in);

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            if (connection != null) {
                System.out.println("Connected to the database");

                menuDecider();

            } else {
                System.out.println("Failed to connect to the database");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Menu functions
    private static void menuDecider() {
        try {
            if (username == null) {
                mainMenuChoices();
            } else {
                String sql = "SELECT user_type FROM Profiles WHERE username = ?;";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String user_type = resultSet.getString("user_type");

                    switch (user_type){
                        case "TYPE_MEMBER":
                            memberMenuChoices();
                            break;
                        case "TYPE_TRAINER":
                            trainerMenuChoices();
                            break;
                        case "TYPE_ADMIN":
                            adminMenuChoices();
                            break;
                    }

                } else {
                    System.out.println("User not found!");
                }

                resultSet.close();
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void mainMenuChoices() {
        System.out.println();
        System.out.println("Options: ");
        System.out.println("1) User Registration");
        System.out.println("2) User Login");
        System.out.println("3) Exit the program");
        System.out.println();
        System.out.println("Enter the number of your choice: ");
        int choice = input.nextInt();
        input.nextLine();

        switch (choice){
            case 1:
                userRegistration();
                break;
            case 2:
                String loginResponse = userLogin();
                if(loginResponse != null){
                    username = loginResponse;
                    menuDecider();
                }
                else {
                    System.out.println("username and/or password don't exist");
                    menuDecider();
                }
                break;
            case 3:
                return;
            default:
                System.out.println("Invalid choice. Try again");
                mainMenuChoices();
                return;
        }
    }

    private static void memberMenuChoices() throws SQLException {
        System.out.println();
        System.out.println("Options: ");
        System.out.println("1) Profile Information");
        System.out.println("2) Member Schedule Management");
        System.out.println("3) Logout");
        System.out.println();
        System.out.println("Enter the number of your choice: ");
        int choice = input.nextInt();
        input.nextLine();

        switch (choice) {
            case 1:
                profileInformation();
                break;
            case 2:
                memberScheduleManagement();
                break;
            case 3:
                userLogout();
                break;
            default:
                System.out.println("Invalid choice. Try again");
                memberMenuChoices();
        }
    }

    private static void trainerMenuChoices() {
        System.out.println();
        System.out.println("Options: ");
        System.out.println("1) Trainer Schedule Management");
        System.out.println("2) View Member Profiles");
        System.out.println("3) Logout");
        System.out.println();
        System.out.println("Enter the number of your choice: ");
        int choice = input.nextInt();
        input.nextLine();

        switch (choice) {
            case 1:
                trainerScheduleManagement();
                break;
            case 2:
                break;
            case 3:
                userLogout();
                break;
            default:
                System.out.println("Invalid choice. Try again");
                trainerMenuChoices();
        }
    }

    private static void adminMenuChoices() {
        System.out.println();
        System.out.println("Options: ");
        System.out.println("1) Room Booking Management");
        System.out.println("2) Manage Equipment");
        System.out.println("3) Manage Class Schedule");
        System.out.println("4) View Bills");
        System.out.println("5) Logout");
        System.out.println();
        System.out.println("Enter the number of your choice: ");
        int choice = input.nextInt();
        input.nextLine();

        switch (choice) {
            case 1:
                break;
            case 2:
                manageEquipment();
                break;
            case 3:
                manageClassSchedule();
                break;
            case 4:
                viewBills();
                break;
            case 5:
                userLogout();
                break;
            default:
                System.out.println("Invalid choice. Try again");
                adminMenuChoices();
        }
    }

    private static void userRegistration() {
        String username = getNewUsername();
        String password = getPassword();
        String firstName = getFirstName();
        String lastName = getLastName();
        Integer creditCard = getCreditCard();
        String userType = "TYPE_MEMBER";
        String birthday = getDate("birthday", false);

        Random random = new Random();
        Double bmi = random.nextDouble(18.5, 25.0);
        Integer systolicBP = random.nextInt(90, 121);
        Integer diastolicBP = random.nextInt(60, 81);
        Integer heartRate = random.nextInt(60, 101);
        Integer bloodSugarLevel = random.nextInt(70, 100);

        String sql_profile_statement = "INSERT INTO\n" +
                "Profiles(username, passwords, first_name, last_name, user_type)\n" +
                "VALUES (?, ?, ?, ?, ?)";

        String sql_member_statement = "INSERT INTO\n" +
                "Members(username, credit_card, birthday, bmi, systolic_bp, diastolic_bp, heart_rate, bloodsugar_level)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try{
            PreparedStatement prepStatement_profile = connection.prepareStatement(sql_profile_statement);
            PreparedStatement preparedStatement_member = connection.prepareStatement(sql_member_statement);

            prepStatement_profile.setString(1, username);
            prepStatement_profile.setString(2, password);
            prepStatement_profile.setString(3, firstName);
            prepStatement_profile.setString(4, lastName);
            prepStatement_profile.setString(5, userType);

            // adding user profile
            prepStatement_profile.executeUpdate();

            preparedStatement_member.setString(1, username);
            preparedStatement_member.setInt(2, creditCard);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try{
                Date b_date = dateFormat.parse(birthday);
                java.sql.Date sqlDate = new java.sql.Date(b_date.getTime());
                preparedStatement_member.setDate(3, sqlDate);
            } catch (ParseException | SQLException e) {
                throw new RuntimeException(e);
            }

            preparedStatement_member.setDouble(4, bmi);
            preparedStatement_member.setInt(5, systolicBP);
            preparedStatement_member.setInt(6, diastolicBP);
            preparedStatement_member.setInt(7, heartRate);
            preparedStatement_member.setInt(8, bloodSugarLevel);

            // adding base member info
            preparedStatement_member.executeUpdate();

            System.out.println("Profile Created");
            menuDecider();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private static String userLogin(){
        String username = getUsername();
        String password = getPassword();

        String sql = "SELECT * FROM profiles WHERE username = ? AND passwords = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return resultSet.getString("username");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static void userLogout(){
        username = null;
        menuDecider();
    }

    private static String getUsername() {
        System.out.println("Enter a username: ");
        return input.nextLine();
    }

    private static String getNewUsername() {
        System.out.println("Enter a username: ");
        String potentialUsername = input.nextLine();
        if (usernameExists(potentialUsername)){
            System.out.println("Username is already taken. Please try a different one");
            return getNewUsername();
        }

        return potentialUsername;
    }

    private static String getPassword() {
        System.out.println("Enter password: ");
        return input.nextLine();
    }

    private static String getFirstName() {
        System.out.println("Enter first name: ");
        return input.nextLine();
    }

    private static String getLastName() {
        System.out.println("Enter last name: ");
        return input.nextLine();
    }

    private static String getDate(String context, boolean pastCurrentDate) {
        System.out.println("Enter " + context + " (YYYY-MM-DD): ");
        String actual_date = "";

        while (actual_date.isEmpty()) {
            actual_date = input.nextLine().trim();
        }

        SimpleDateFormat expectedFormat = new SimpleDateFormat("yyyy-MM-dd");
        expectedFormat.setLenient(false);

        try {
            Date parsedDate = expectedFormat.parse(actual_date);

            if (!pastCurrentDate) {
                Date currDate = new Date();
                if (currDate.before(parsedDate) || currDate.equals(parsedDate)) {
                    System.out.println("Date cannot be past the current date");
                    return getDate(context, pastCurrentDate);
                }
            }

            return actual_date;
        } catch (ParseException e) {
            System.out.println("Invalid date or date format.");
            return getDate(context, pastCurrentDate);
        }
    }


    private static int getCreditCard() {
        System.out.println("Enter credit card number: ");
        return input.nextInt();
    }

    // Query functions
    public static boolean usernameExists(String username){
        try{
            String sql = "SELECT * FROM members WHERE username = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                System.out.println();
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    // MEMBER FUNCTIONS ==============================================================================
    private static void profileInformation(){
        System.out.println();
        System.out.println("Options: ");
        System.out.println("1) View Personal Information");
        System.out.println("2) View Fitness Goals");
        System.out.println("3) View Health Metrics");
        System.out.println("4) View Health Action");
        System.out.println("5) View Health Records");
        System.out.println("6) Go Back");
        System.out.println();
        System.out.println("Enter the number of your choice: ");
        int choice = input.nextInt();
        input.nextLine();

        switch (choice) {
            case 1:
                personalInformation();
                break;
            case 2:
                fitnessGoals();
                break;
            case 3:
                healthMetrics();
                break;
            case 4:
                healthActions();
                break;
            case 5:
                healthRecords();
                break;
            case 6:
                menuDecider();
                break;
            default:
                System.out.println("Invalid choice. Try again");
                profileInformation();
        }
    }

    private static void personalInformation() {
        try {
            String sql = "SELECT p.first_name, p.last_name, m.credit_card, m.birthday, m.height, m.weight FROM Profiles p JOIN Members m ON p.username = m.username WHERE p.username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            // Move cursor to the first row
            if (resultSet.next()) {
                String name = resultSet.getString("first_name") + " " + resultSet.getString("last_name");
                System.out.println("Name: " + name);
                System.out.println("Credit Card: " + resultSet.getInt("credit_card"));
                System.out.println("Birthday: " + resultSet.getDate("birthday"));
                System.out.println("Height: " + resultSet.getFloat("height"));
                System.out.println("Weight: " + resultSet.getFloat("weight"));
                System.out.println();

                System.out.println("Would you like to change any of the information? (y/n)");
                char choice = input.next().charAt(0);
                input.nextLine();
                switch (choice){
                    case 'y':
                        setPersonalInformation();
                        break;
                    case 'n':
                        profileInformation();
                        break;
                    default:
                        personalInformation();
                        return;
                }
            } else {
                System.out.println("No data found for the given username.");
                profileInformation();
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void setPersonalInformation(){
        String firstName = getFirstName();
        String lastName = getLastName();
        int creditCard = getCreditCard();
        String birthday = getDate("birthday", false);
        int height = getHeight();
        int weight = getWeight();

        String sql_profile_statement = "UPDATE profiles SET first_name = ?, last_name = ? WHERE username = ?";

        String sql_member_statement = "UPDATE members SET credit_card = ?, birthday = ?, height = ?, weight = ? WHERE username = ?";

        try{
            PreparedStatement prepStatement_profile = connection.prepareStatement(sql_profile_statement);
            PreparedStatement prepStatement_member = connection.prepareStatement(sql_member_statement);

            prepStatement_profile.setString(1, firstName);
            prepStatement_profile.setString(2, lastName);
            prepStatement_profile.setString(3, username);

            // adding user profile
            prepStatement_profile.executeUpdate();

            prepStatement_member.setInt(1, creditCard);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try{
                Date b_date = dateFormat.parse(birthday);
                java.sql.Date sqlDate = new java.sql.Date(b_date.getTime());
                prepStatement_member.setDate(2, sqlDate);
            } catch (ParseException | SQLException e) {
                throw new RuntimeException(e);
            }

            prepStatement_member.setInt(3, height);
            prepStatement_member.setInt(4, weight);
            prepStatement_member.setString(5, username);

            // adding base member info
            prepStatement_member.executeUpdate();

            System.out.println("Profile Created");
            menuDecider();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void fitnessGoals() {
        try {
            String sql = "SELECT diet_plan, goal_weight, goal_speed, goal_lift, weight_deadline, speed_deadline, lift_deadline FROM members WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            // Move cursor to the first row
            if (resultSet.next()) {
                System.out.println("Diet Plan: " + resultSet.getString("diet_plan"));
                System.out.println("Goal Weight: " + resultSet.getInt("goal_weight"));
                System.out.println("Goal Speed: " + resultSet.getInt("goal_speed"));
                System.out.println("Goal Lift: " + resultSet.getInt("goal_lift"));
                System.out.println("Weight Deadline: " + resultSet.getDate("weight_deadline"));
                System.out.println("Speed Deadline: " + resultSet.getDate("speed_deadline"));
                System.out.println("Lift Deadline: " + resultSet.getDate("lift_deadline"));
                System.out.println();

                System.out.println("Would you like to change any of the information? (y/n)");
                char choice = input.next().charAt(0);
                input.nextLine();
                switch (choice){
                    case 'y':
                        setFitnessGoals();
                        break;
                    case 'n':
                        profileInformation();
                        break;
                    default:
                        fitnessGoals();
                        return;
                }
            } else {
                System.out.println("No data found for the given username.");
                profileInformation();
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void setFitnessGoals() {
        String dietPlan = getDietPlan();
        int goalWeight = getGoalWeight();
        int goalSpeed = getGoalSpeed();
        int goalLift = getGoalLift();
        String weightDeadline = getDate("weight deadline", true);
        String speedDeadline = getDate("speed deadline", true);
        String liftDeadline = getDate("lift deadline", true);

        Random random = new Random();

        String sql_statement = "UPDATE members SET diet_plan = ?, goal_weight = ?, goal_speed = ?, goal_lift = ?, weight_deadline = ?, speed_deadline = ?, lift_deadline = ? WHERE username = ?";

        try{
            PreparedStatement prepStatement = connection.prepareStatement(sql_statement);

            prepStatement.setString(1, dietPlan);
            prepStatement.setInt(2, goalWeight);
            prepStatement.setInt(3, goalSpeed);
            prepStatement.setInt(4, goalLift);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try{
                Date w_date = dateFormat.parse(weightDeadline);
                Date s_date = dateFormat.parse(speedDeadline);
                Date l_date = dateFormat.parse(liftDeadline);
                java.sql.Date sqlWDate = new java.sql.Date(w_date.getTime());
                java.sql.Date sqlSDate = new java.sql.Date(s_date.getTime());
                java.sql.Date sqlLDate = new java.sql.Date(l_date.getTime());
                prepStatement.setDate(5, sqlWDate);
                prepStatement.setDate(6, sqlSDate);
                prepStatement.setDate(7, sqlLDate);

            } catch (ParseException | SQLException e) {
                throw new RuntimeException(e);
            }

            prepStatement.setString(8, username);


            // adding user profile
            prepStatement.executeUpdate();

            System.out.println("Fitness Goals Set");
            fitnessGoals();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void healthMetrics() {
        try {
            String sql = "SELECT bmi, systolic_bp, diastolic_bp, heart_rate, bloodsugar_level FROM members WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            // Move cursor to the first row
            if (resultSet.next()) {
                System.out.println("BMI: " + resultSet.getInt("bmi"));
                System.out.println("Systolic Blood Pressure: " + resultSet.getInt("systolic_bp"));
                System.out.println("Diastolic Blood Pressure: " + resultSet.getInt("diastolic_bp"));
                System.out.println("Heart Rate: " + resultSet.getInt("heart_rate"));
                System.out.println("Blood Sugar Level: " + resultSet.getInt("bloodsugar_level"));
                System.out.println();

                profileInformation();
            } else {
                System.out.println("No data found for the given username.");
                profileInformation();
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void healthActions(){
        try {
            String sql_exercise = "SELECT e.exercise FROM Exercise_Routines e JOIN Members m ON e.username = m.username WHERE e.username = ?";
            String sql_diet_plan = "SELECT diet_plan FROM Members WHERE username = ?";
            PreparedStatement statement_exercise = connection.prepareStatement(sql_exercise);
            PreparedStatement statement_diet_plan = connection.prepareStatement(sql_diet_plan);
            statement_exercise.setString(1, username);
            statement_diet_plan.setString(1, username);
            ResultSet resultSet_exercise = statement_exercise.executeQuery();
            ResultSet resultSet_diet_plan = statement_diet_plan.executeQuery();

            if(resultSet_diet_plan.next()){
                System.out.println("Diet Plan: " + resultSet_diet_plan.getString("diet_plan"));
            }

            boolean dataFound = false;
            String dietPlan = null;

            // Move cursor to the first row
            while (resultSet_exercise.next()) {
                if(!dataFound){
                    dataFound = true;
                }

                System.out.println("Exercise: " + resultSet_exercise.getString("exercise"));
            }

            if(dataFound){
                System.out.println("Would you like to change any of the information? (y/n)");
                char choice = input.next().charAt(0);
                input.nextLine();
                switch (choice){
                    case 'y':
                        setHealthActions();
                        break;
                    case 'n':
                        profileInformation();
                        break;
                    default:
                        healthActions();
                        return;
                }
            }
            else {
                System.out.println("No data found for the given username.");
                profileInformation();
            }

            resultSet_exercise.close();
            statement_exercise.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void setHealthActions(){
        System.out.println();
        System.out.println("Options: ");
        System.out.println("1) Update Routine");
        System.out.println("2) Change Diet Plan");
        System.out.println("3) Go Back");
        System.out.println();
        System.out.println("Enter the number of your choice: ");
        int choice = input.nextInt();
        input.nextLine();

        switch (choice) {
            case 1:
                updateRoutine();
                break;
            case 2:
                changeDietPlan();
                break;
            case 3:
                menuDecider();
                break;
            default:
                System.out.println("Invalid choice. Try again");
                setHealthActions();

        }
    }

    private static void updateRoutine(){
        clearExistingRoutine();

        boolean addAnother = true;

        while(addAnother){
            addNewExercise();

            System.out.println("Would you like to add another exercise? (y/n)");
            char choice = input.next().charAt(0);
            input.nextLine();
            switch (choice){
                case 'y':
                    break;
                case 'n':
                    addAnother = false;
                    break;
                default:
                    System.out.println("Invalid input. Please enter y or n after entering an exercise");
            }
        }

        healthActions();
    }

    private static void clearExistingRoutine(){
        String sql_clear_statment = "DELETE FROM Exercise_Routines WHERE username = ?";
        try {
            // Clearing Existing Exercise Routine
            PreparedStatement prepClearStatement = connection.prepareStatement(sql_clear_statment);
            prepClearStatement.setString(1, username);
            prepClearStatement.executeUpdate();
            prepClearStatement.close();

            System.out.println("Existing Routine Cleared");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void addNewExercise(){
        String exercise = getExercise();

        String sql_additon_statement = "INSERT INTO Exercise_Routines(username, exercise) VALUES(?, ?)";

        try{
            PreparedStatement prepAdditionStatement = connection.prepareStatement(sql_additon_statement);
            prepAdditionStatement.setString(1, username);
            prepAdditionStatement.setString(2, exercise);
            prepAdditionStatement.executeUpdate();
            prepAdditionStatement.close();

            System.out.println("Exercise Added");
            System.out.println();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void changeDietPlan(){
        String dietPlan = getDietPlan();

        String sql_statement = "UPDATE Members SET diet_plan = ? WHERE username = ?";

        try{
            PreparedStatement prepStatement = connection.prepareStatement(sql_statement);
            prepStatement.setString(1, dietPlan);
            prepStatement.setString(2, username);
            prepStatement.executeUpdate();
            prepStatement.close();

            System.out.println("Diet Plan Updated");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        healthActions();
    }
    private static void healthRecords() {
        System.out.println();
        System.out.println("Options: ");
        System.out.println("1) View Full Records");
        System.out.println("2) Add Entry");
        System.out.println("3) View Health Statistics");
        System.out.println("4) Go Back");
        System.out.println();
        System.out.println("Enter the number of your choice: ");
        int choice = input.nextInt();
        input.nextLine();

        switch (choice) {
            case 1:
                viewHealthRecords();
                break;
            case 2:
                addHealthEntry();
                break;
            case 3:
                healthStatistics();
            case 4:
                profileInformation();
                break;
            default:
                System.out.println("Invalid choice. Try again");
                healthRecords();
        }
    }

    private static void viewHealthRecords(){
        try {
            String weightQuery = "SELECT weight FROM Weight_Statistics WHERE username = ?";
            PreparedStatement weightStatement = connection.prepareStatement(weightQuery);
            weightStatement.setString(1, username);
            ResultSet weightResultSet = weightStatement.executeQuery();

            System.out.println("Weight Statistics:");
            while (weightResultSet.next()) {
                System.out.println("Weight: " + weightResultSet.getInt("weight") + " kg");
            }
            System.out.println();

            String speedQuery = "SELECT speed FROM Speed_Statistics WHERE username = ?";
            PreparedStatement speedStatement = connection.prepareStatement(speedQuery);
            speedStatement.setString(1, username);
            ResultSet speedResultSet = speedStatement.executeQuery();

            System.out.println("Speed Statistics:");
            while (speedResultSet.next()) {
                System.out.println("Speed: " + speedResultSet.getInt("speed") + " m/s");
            }
            System.out.println();

            String liftQuery = "SELECT lift FROM Lift_Statistics WHERE username = ?";
            PreparedStatement liftStatement = connection.prepareStatement(liftQuery);
            liftStatement.setString(1, username);
            ResultSet liftResultSet = liftStatement.executeQuery();

            System.out.println("Lift Statistics:");
            while (liftResultSet.next()) {
                System.out.println("Lift: " + liftResultSet.getInt("lift")  + " kg");
            }
            System.out.println();

            weightResultSet.close();
            weightStatement.close();
            speedResultSet.close();
            speedStatement.close();
            liftResultSet.close();
            liftStatement.close();

            healthRecords();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addHealthEntry() {
        System.out.println();
        System.out.println("Options: ");
        System.out.println("1) Add to weight statistics");
        System.out.println("2) Add to speed statistics");
        System.out.println("3) Add to lift statistics");
        System.out.println("4) Go Back");
        System.out.println();
        System.out.println("Enter the number of your choice: ");
        int choice = input.nextInt();
        input.nextLine();

        switch (choice) {
            case 1:
                addWeightEntry();
                break;
            case 2:
                addSpeedEntry();
                break;
            case 3:
                addLiftEntry();
                break;
            case 4:
                healthRecords();
                break;
            default:
                System.out.println("Invalid choice. Try again");
                addHealthEntry();
        }
    }

    private static void addWeightEntry() {
        try {
            System.out.println("Enter weight: ");
            int weight = input.nextInt();
            input.nextLine();

            String sql = "INSERT INTO Weight_Statistics (username, weight) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setInt(2, weight);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {System.out.println("Weight entry added successfully.");}
            else {System.out.println("Failed to add weight entry.");}

            statement.close();
            addHealthEntry();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addSpeedEntry() {
        try {
            System.out.println("Enter speed: ");
            int speed = input.nextInt();
            input.nextLine();

            String sql = "INSERT INTO Speed_Statistics (username, speed) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setInt(2, speed);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {System.out.println("Speed entry added successfully.");}
            else {System.out.println("Failed to add speed entry.");}

            statement.close();
            addHealthEntry();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addLiftEntry() {
        try {
            System.out.println("Enter lift: ");
            int lift = input.nextInt();
            input.nextLine();

            String sql = "INSERT INTO Lift_Statistics (username, lift) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setInt(2, lift);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {System.out.println("Lift entry added successfully.");}
            else {System.out.println("Failed to add lift entry.");}

            statement.close();
            addHealthEntry();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void healthStatistics() {
        try {
            String sql_weight_statement = "SELECT\n" +
                    "    MAX(weight) AS max_weight,\n" +
                    "    MIN(weight) AS min_weight,\n" +
                    "    AVG(weight) AS avg_weight\n" +
                    "FROM Weight_Statistics\n" +
                    "WHERE username = ?;";
            String sql_speed_statement = "SELECT\n" +
                    "    MAX(speed) AS max_speed,\n" +
                    "    MIN(speed) AS min_speed,\n" +
                    "    AVG(speed) AS avg_speed\n" +
                    "FROM Speed_Statistics\n" +
                    "WHERE username = ?;";
            String sql_lift_statement = "SELECT\n" +
                    "    MAX(lift) AS max_lift,\n" +
                    "    MIN(lift) AS min_lift,\n" +
                    "    AVG(lift) AS avg_lift\n" +
                    "FROM Lift_Statistics\n" +
                    "WHERE username = ?;";

            PreparedStatement weightStatement = connection.prepareStatement(sql_weight_statement);
            weightStatement.setString(1, username);
            ResultSet weightResultSet = weightStatement.executeQuery();


            if (weightResultSet.next()) {
                System.out.println("Weight Statistics:");
                System.out.println("Maximum Weight: " + weightResultSet.getFloat("max_weight"));
                System.out.println("Minimum Weight: " + weightResultSet.getFloat("min_weight"));
                System.out.println("Average Weight: " + weightResultSet.getFloat("avg_weight"));
            } else {
                System.out.println("No weight statistics found for the given username.");
            }

            PreparedStatement speedStatement = connection.prepareStatement(sql_speed_statement);
            speedStatement.setString(1, username);
            ResultSet speedResultSet = speedStatement.executeQuery();

            if (speedResultSet.next()) {
                System.out.println("Speed Statistics:");
                System.out.println("Maximum Speed: " + speedResultSet.getInt("max_speed"));
                System.out.println("Minimum Speed: " + speedResultSet.getInt("min_speed"));
                System.out.println("Average Speed: " + speedResultSet.getInt("avg_speed"));
            } else {
                System.out.println("No speed statistics found for the given username.");
            }

            PreparedStatement liftStatement = connection.prepareStatement(sql_lift_statement);
            liftStatement.setString(1, username);
            ResultSet liftResultSet = liftStatement.executeQuery();

            if (liftResultSet.next()) {
                System.out.println("Lift Statistics:");
                System.out.println("Maximum Lift: " + liftResultSet.getInt("max_lift"));
                System.out.println("Minimum Lift: " + liftResultSet.getInt("min_lift"));
                System.out.println("Average Lift: " + liftResultSet.getInt("avg_lift"));
            } else {
                System.out.println("No lift statistics found for the given username.");
            }

            weightResultSet.close();
            weightStatement.close();
            speedResultSet.close();
            speedStatement.close();
            liftResultSet.close();
            liftStatement.close();

            healthRecords();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void memberScheduleManagement() throws SQLException {
        System.out.println();
        System.out.println("Options: ");
        System.out.println("1) Book a personal session");
        System.out.println("2) Book a group session");
        System.out.println("3) Go Back");
        System.out.println();
        System.out.println("Enter the number of your choice: ");
        int choice = input.nextInt();
        input.nextLine();

        switch (choice) {
            case 1:
                personalTraining();
                break;
            case 2:
                groupTraining();
                break;
            case 3:
                menuDecider();
                break;
            default:
                System.out.println("Invalid choice. Try again");
                memberScheduleManagement();
        }
    }

    private static void personalTraining() {
        String date = getDate("session date", true);
        System.out.println("What time of day?");
        System.out.println("1. Morning");
        System.out.println("2. Afternoon");
        System.out.println("3. Evening");
        System.out.println("Enter the number of your choice: ");
        int choice = input.nextInt();
        input.nextLine();

        String timeOfDay = "";
        switch (choice) {
            case 1:
                timeOfDay = "MORNING";
                break;
            case 2:
                timeOfDay = "AFTERNOON";
                break;
            case 3:
                timeOfDay = "EVENING";
                break;
            default:
                System.out.println("Invalid choice. Try again");
                personalTraining();
                return;
        }

        try {
            String availableTrainersQuery = "SELECT p.username, p.first_name, p.last_name, d.start_trainer_date, d.start_time_of_day, d.end_trainer_date, d.end_time_of_day " +
                    "FROM Dates_Trainer_Available d " +
                    "JOIN Profiles p ON d.trainer_id = p.username " +
                    "WHERE ? BETWEEN d.start_trainer_date AND d.end_trainer_date " +
                    "AND NOT EXISTS (" +
                    "    SELECT 1 " +
                    "    FROM Dates_Trainer_Unavailable u " +
                    "    WHERE u.trainer_id = d.trainer_id " +
                    "    AND u.trainer_date = ?" +
                    "    AND u.time_of_day = ?" +
                    ")";
            PreparedStatement availableTrainersStatement = connection.prepareStatement(availableTrainersQuery);
            availableTrainersStatement.setDate(1, java.sql.Date.valueOf(date));
            availableTrainersStatement.setDate(2, java.sql.Date.valueOf(date));
            availableTrainersStatement.setString(3, timeOfDay);
            ResultSet availableTrainersResult = availableTrainersStatement.executeQuery();

            List<String> availableTrainers = new ArrayList<>();
            List<String> availableIDs = new ArrayList<>();
            while (availableTrainersResult.next()) {
                String firstName = availableTrainersResult.getString("first_name");
                String lastName = availableTrainersResult.getString("last_name");
                String ID = availableTrainersResult.getString("username");
                availableTrainers.add(firstName + " " + lastName);
                availableIDs.add(ID);
            }

            System.out.println("Available Trainers:");
            for (int i = 0; i < availableTrainers.size(); i++) {
                System.out.println((i + 1) + ". " + availableTrainers.get(i));
            }

            if (availableTrainers.isEmpty()){
                System.out.println("There are no available trainers during that time.");
                memberScheduleManagement();
                return;
            }

            System.out.println("Enter the number corresponding to the trainer you want to select: ");
            int trainerChoice = input.nextInt();
            input.nextLine();

            if (trainerChoice < 1 || trainerChoice > availableTrainers.size()) {
                System.out.println("Invalid choice. Try again.");
                personalTraining();
                return;
            }
            String selectedTrainer = availableIDs.get(trainerChoice - 1);

            String insertClassQuery = "INSERT INTO Classes (trainer_id, class_type, class_date, time_of_day) VALUES (?, ?, ?, ?)";
            PreparedStatement insertClassStatement = connection.prepareStatement(insertClassQuery, Statement.RETURN_GENERATED_KEYS);
            insertClassStatement.setString(1, selectedTrainer);
            insertClassStatement.setString(2, "INDIVIDUAL_TYPE");
            insertClassStatement.setDate(3, java.sql.Date.valueOf(date));
            insertClassStatement.setString(4, timeOfDay);
            insertClassStatement.executeUpdate();

            String insertUnavailableQuery = "INSERT INTO Dates_Trainer_Unavailable (trainer_id, trainer_date, time_of_day) VALUES (?, ?, ?)";
            PreparedStatement insertUnavailableStatement = connection.prepareStatement(insertUnavailableQuery);
            insertUnavailableStatement.setString(1, selectedTrainer);
            insertUnavailableStatement.setDate(2, java.sql.Date.valueOf(date)); // Convert string to java.sql.Date
            insertUnavailableStatement.setString(3, timeOfDay);
            insertUnavailableStatement.executeUpdate();


            int sessionPrice = 50;

            String insertBillQuery = "INSERT INTO Bills (username, price, date_issued) VALUES (?, ?, CURRENT_DATE)";
            PreparedStatement insertBillStatement = connection.prepareStatement(insertBillQuery);
            insertBillStatement.setString(1, username);
            insertBillStatement.setInt(2, sessionPrice);
            insertBillStatement.executeUpdate();

            System.out.println("Do you want to schedule another personal session? (y/n)");
            char choice2 = input.next().charAt(0);
            input.nextLine();
            switch (choice2){
                case 'y':
                    personalTraining();
                    break;
                default:
                    memberScheduleManagement();
                    return;
            }

            availableTrainersResult.close();
            availableTrainersStatement.close();
            insertUnavailableStatement.close();
            insertBillStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void groupTraining() throws SQLException {
        String sql_classes = "SELECT Classes.*\n" +
                "FROM Classes\n" +
                "LEFT JOIN Class_Members ON Classes.class_id = Class_Members.class_id AND Class_Members.username = ?\n" +
                "WHERE Classes.class_type = 'GROUP_TYPE'\n" +
                "AND Class_Members.class_id IS NULL;\n";
        PreparedStatement preparedStatementClasses = connection.prepareStatement(sql_classes);
        preparedStatementClasses.setString(1, username);
        ResultSet resultSetClasses = preparedStatementClasses.executeQuery();

        boolean classesAvailable = false;

        while(resultSetClasses.next()){
            classesAvailable = true;
            System.out.println("Class ID: " + resultSetClasses.getInt("class_id"));
            System.out.println("Trainer ID: " + resultSetClasses.getString("trainer_id"));
            System.out.println("Room ID: " + resultSetClasses.getString("room_id"));
            System.out.println("Class Type: " + resultSetClasses.getString("class_type"));
            System.out.println("Class Description: " + resultSetClasses.getString("class_description"));
            System.out.println("Class Date: " + resultSetClasses.getDate("class_date"));
            System.out.println("Time of Day: " + resultSetClasses.getString("time_of_day"));
        }

        resultSetClasses.close();
        preparedStatementClasses.close();

        if(classesAvailable){
            System.out.println("Enter Class ID of the class you wish to participate in: (or '0' to cancel)");
            int classID = input.nextInt();

            if (classID == 0){
                memberScheduleManagement();
                return;
            }

            // Bill Generation
            String sql_bill_insertion = "INSERT INTO Bills (username, price, date_issued)\n" +
                    "VALUES (?, ?, ?);";

            Date currDate = new Date();
            java.sql.Date sqlCurrDate = new java.sql.Date(currDate.getTime());
            PreparedStatement preparedStatementBills = connection.prepareStatement(sql_bill_insertion);
            preparedStatementBills.setString(1, username);
            preparedStatementBills.setInt(2, 50);
            preparedStatementBills.setDate(3, sqlCurrDate);

            preparedStatementBills.close();

            // Member enrolment
            try{
                String sql_class_member = "INSERT INTO Class_Members (class_id, username)\n" +
                        "VALUES (?, ?);";
                PreparedStatement preparedStatementClassMembers = connection.prepareStatement(sql_class_member);
                preparedStatementClassMembers.setInt(1, classID);
                preparedStatementClassMembers.setString(2, username);

                preparedStatementClassMembers.executeUpdate();

                preparedStatementClassMembers.close();
            }
            catch (SQLException e){
                if(e.getSQLState().startsWith("23")){
                    System.out.println("You are already enrolled in this class");
                }
                else{
                    e.printStackTrace();
                }
            }

            System.out.println("Would you like to enrol in another class? (y/n)");
            char choice = input.next().charAt(0);
            input.nextLine();
            switch (choice){
                case 'y':
                    groupTraining();
                    break;
                case 'n':
                    memberScheduleManagement();
                    break;
                default:
                    groupTraining();
            }
        }
        else{
            System.out.println("No more classes available for you to enrol in");
            memberScheduleManagement();
        }
    }

    private static void trainerScheduleManagement() {
        System.out.println();
        System.out.println("Options: ");
        System.out.println("1) Add available time");
        System.out.println("2) View available time");
        System.out.println("3) change available time");
        System.out.println("4) Go Back");
        System.out.println();
        System.out.println("Enter the number of your choice: ");
        int choice = input.nextInt();
        input.nextLine();

        switch (choice) {
            case 1:
                addSchedule();
                break;
            case 2:
                viewSchedule();
                break;
            case 3:
                changeSchedule();
            case 4:
                menuDecider();
                break;
            default:
                System.out.println("Invalid choice. Try again");
                trainerScheduleManagement();
        }
    }

    private static void addSchedule() {
        String startingDate = getDate("starting date", true);
        System.out.println("What time of day?");
        System.out.println("1. Morning");
        System.out.println("2. Afternoon");
        System.out.println("3. Evening");
        System.out.println("Enter the number of your choice: ");
        int choice = input.nextInt();
        input.nextLine();

        String startingTimeOfDay = "";
        switch (choice) {
            case 1:
                startingTimeOfDay = "MORNING";
                break;
            case 2:
                startingTimeOfDay = "AFTERNOON";
                break;
            case 3:
                startingTimeOfDay = "EVENING";
                break;
            default:
                System.out.println("Invalid choice. Try again");
                addSchedule();
                return;
        }

        String endingDate = getDate("ending date", true);
        System.out.println("What time of day?");
        System.out.println("1. Morning");
        System.out.println("2. Afternoon");
        System.out.println("3. Evening");
        System.out.println("Enter the number of your choice: ");
        choice = input.nextInt();
        input.nextLine();

        String endingTimeOfDay = "";
        switch (choice) {
            case 1:
                endingTimeOfDay = "MORNING";
                break;
            case 2:
                endingTimeOfDay = "AFTERNOON";
                break;
            case 3:
                endingTimeOfDay = "EVENING";
                break;
            default:
                System.out.println("Invalid choice. Try again");
                addSchedule();
                return;
        }

        try {
            String checkConflictsQuery = "SELECT COUNT(*) AS count FROM Dates_Trainer_Available WHERE trainer_id = ? AND ((?::DATE >= start_trainer_date AND ?::DATE < end_trainer_date) OR (?::DATE > start_trainer_date AND ?::DATE <= end_trainer_date) OR (?::DATE <= start_trainer_date AND ?::DATE >= end_trainer_date))";
            PreparedStatement checkConflictsStatement = connection.prepareStatement(checkConflictsQuery);
            checkConflictsStatement.setString(1, username); // Assuming username is the trainer_id
            checkConflictsStatement.setString(2, startingDate);
            checkConflictsStatement.setString(3, startingDate);
            checkConflictsStatement.setString(4, endingDate);
            checkConflictsStatement.setString(5, endingDate);
            checkConflictsStatement.setString(6, startingDate);
            checkConflictsStatement.setString(7, endingDate);
            ResultSet conflictsResult = checkConflictsStatement.executeQuery();
            conflictsResult.next();
            int conflictCount = conflictsResult.getInt("count");
            conflictsResult.close();
            checkConflictsStatement.close();

            if (conflictCount > 0) {
                System.out.println("Schedule conflicts with existing schedules. Please choose a different time.");
                addSchedule();
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to check for schedule conflicts.");
            return;
        }

        try {
            String insertScheduleQuery = "INSERT INTO Dates_Trainer_Available (start_trainer_date, start_time_of_day, end_trainer_date, end_time_of_day, trainer_id) VALUES (?::DATE, ?, ?::DATE, ?, ?)";
            PreparedStatement insertScheduleStatement = connection.prepareStatement(insertScheduleQuery);
            insertScheduleStatement.setString(1, startingDate);
            insertScheduleStatement.setString(2, startingTimeOfDay);
            insertScheduleStatement.setString(3, endingDate);
            insertScheduleStatement.setString(4, endingTimeOfDay);
            insertScheduleStatement.setString(5, username);
            insertScheduleStatement.executeUpdate();
            System.out.println("Schedule added successfully.");
            insertScheduleStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to add schedule. Please try again.");
        }

        System.out.println("Would you like to add another range of avaiability? (y/n)");
        char another = input.next().charAt(0);
        input.nextLine();
        switch (another){
            case 'y':
                addSchedule();
                break;
            default:
                trainerScheduleManagement();
        }
    }

    private static void viewSchedule() {
        try {
            String viewScheduleQuery =
                "SELECT schedule_id, start_trainer_date, end_trainer_date, start_time_of_day, end_time_of_day " +
                "FROM Dates_Trainer_Available " +
                "WHERE trainer_id = ?";

            PreparedStatement viewScheduleStatement = connection.prepareStatement(viewScheduleQuery);
            viewScheduleStatement.setString(1, username);

            ResultSet scheduleResult = viewScheduleStatement.executeQuery();

            if (!scheduleResult.isBeforeFirst()) {
                System.out.println("No schedules found for the trainer.");
                trainerScheduleManagement();
                return;
            }

            System.out.println("Trainer's Schedules:");
            System.out.println("----------------------------------------------------");
            System.out.printf("| %-10s | %-15s | %-15s | %-15s | %-15s |\n", "Schedule ID", "Start Date", "Start Time", "End Date", "End Time");
            System.out.println("----------------------------------------------------");
            while (scheduleResult.next()) {
                int scheduleId = scheduleResult.getInt("schedule_id");
                String startDate = scheduleResult.getString("start_trainer_date");
                String startTime = scheduleResult.getString("start_time_of_day");
                String endDate = scheduleResult.getString("end_trainer_date");
                String endTime = scheduleResult.getString("end_time_of_day");

                System.out.printf("| %-10d | %-15s | %-15s | %-15s | %-15s |\n", scheduleId, startDate, startTime, endDate, endTime);
            }
            System.out.println("----------------------------------------------------");

            scheduleResult.close();
            viewScheduleStatement.close();

            trainerScheduleManagement();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void changeSchedule(){
        if(!schedulesAvailableForTrainer()){
            menuDecider();
            return;
        }

        int scheduleID = getScheduleID();

        System.out.println();
        System.out.println("Options: ");
        System.out.println("1) Change Start Date");
        System.out.println("2) Change End Date");
        System.out.println("3) Change Start Time");
        System.out.println("4) Change End Time");
        System.out.println("5) Go Back");
        System.out.println();
        System.out.println("Enter the number of your choice: ");
        int choice = input.nextInt();
        input.nextLine();

        switch (choice) {
            case 1:
                changeStartDate(scheduleID);
                break;
            case 2:
                changeEndDate(scheduleID);
                break;
            case 3:
//                changeStartTime();
            case 4:
//                changeEndTime();
                break;
            case 5:
                menuDecider();
                break;
            default:
                System.out.println("Invalid choice. Try again");
                changeSchedule();
        }
    }

    private static boolean schedulesAvailableForTrainer(){
        try {
            String viewScheduleQuery =
                    "SELECT schedule_id, start_trainer_date, end_trainer_date, start_time_of_day, end_time_of_day " +
                            "FROM Dates_Trainer_Available " +
                            "WHERE trainer_id = ?";

            PreparedStatement viewScheduleStatement = connection.prepareStatement(viewScheduleQuery);
            viewScheduleStatement.setString(1, username);

            ResultSet scheduleResult = viewScheduleStatement.executeQuery();

            if (!scheduleResult.isBeforeFirst()) {
                System.out.println("No schedules found for the trainer.");
                return false;
            }

            return  true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void changeStartDate(int scheduleID){
        if(!schedulesAvailableForTrainer()){
            changeSchedule();
            return;
        }

        String newStartDateString = getDate("new start date of schedule", true);

        try{
            String sql_statement = "UPDATE Dates_Trainer_Available SET start_trainer_date = ? WHERE trainer_id = ? AND schedule_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql_statement);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date newStartDate = dateFormat.parse(newStartDateString);
            java.sql.Date newStartDateSQL = new java.sql.Date(newStartDate.getTime());

            preparedStatement.setDate(1, newStartDateSQL);
            preparedStatement.setString(2, username);
            preparedStatement.setInt(3, scheduleID);

            preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (ParseException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void changeEndDate(int scheduleID){
        if(!schedulesAvailableForTrainer()){
            changeSchedule();
            return;
        }

        String newStartDateString = getDate("new end date of schedule", true);

        try{
            String sql_statement = "UPDATE Dates_Trainer_Available SET end_trainer_date = ? WHERE trainer_id = ? AND schedule_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql_statement);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date newStartDate = dateFormat.parse(newStartDateString);
            java.sql.Date newStartDateSQL = new java.sql.Date(newStartDate.getTime());

            preparedStatement.setDate(1, newStartDateSQL);
            preparedStatement.setString(2, username);
            preparedStatement.setInt(3, scheduleID);

            preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (ParseException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static int getHeight() {
        System.out.println("Enter height (in cm): ");
        return input.nextInt();
    }

    private static int getWeight() {
        System.out.println("Enter weight (in kg): ");
        return input.nextInt();
    }

    private static String getDietPlan() {
        System.out.println("Enter diet plan description: ");
        return input.nextLine();
    }

    private static int getGoalWeight() {
        System.out.println("Enter goal weight (in kg): ");
        return input.nextInt();
    }

    private static int getGoalSpeed(){
        System.out.println("Enter goal speed (in m/s): ");
        return input.nextInt();
    }

    private static int getGoalLift(){
        System.out.println("Enter goal lift (in kg): ");
        return input.nextInt();
    }

    private static String getExercise(){
        System.out.println("Enter exercise: ");
        return input.nextLine();
    }

    private static int getScheduleID(){
        System.out.println("Enter Schedule ID for the schedule you'd like to change: ");
        return input.nextInt();
    }

    private static void manageEquipment() {
        System.out.println();
        System.out.println("Options: ");
        System.out.println("1) View equipment");
        System.out.println("2) Update equipment");
        System.out.println("3) Go Back");
        System.out.println();
        System.out.println("Enter the number of your choice: ");
        int choice = input.nextInt();
        input.nextLine();

        switch (choice) {
            case 1:
                viewEquipments();
                break;
            case 2:
                updateEquipmentStatus();
                break;
            case 3:
                menuDecider();
                break;
            default:
                System.out.println("Invalid choice. Try again");
                manageEquipment();
        }
    }

    private static void viewEquipments() {
        try {
            String viewEquipmentsQuery = "SELECT * FROM Equipment;";

            PreparedStatement viewEquipmentsStatement = connection.prepareStatement(viewEquipmentsQuery);
            ResultSet equipmentsResult = viewEquipmentsStatement.executeQuery();

            if (!equipmentsResult.isBeforeFirst()) {
                System.out.println("No equipments found.");
                manageEquipment();
                return;
            }

            System.out.println("Equipments:");
            System.out.println("----------------------------------------");
            System.out.printf("| %-12s | %-5s | %-20s |\n", "Equipment ID", "Room", "Status");
            System.out.println("----------------------------------------");
            while (equipmentsResult.next()) {
                int equipmentID = equipmentsResult.getInt("equipment_id");
                String roomID = equipmentsResult.getString("room_id");
                String status = equipmentsResult.getString("equipment_status");

                System.out.printf("| %-12d | %-5s | %-20s |\n", equipmentID, roomID, status);
            }
            System.out.println("----------------------------------------");

            equipmentsResult.close();
            viewEquipmentsStatement.close();

            manageEquipment();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateEquipmentStatus() {
        try {
            System.out.println("Enter the Equipment ID of the equipment you want to update:");
            int equipmentId = input.nextInt();
            input.nextLine();

            String checkEquipmentQuery = "SELECT * FROM Equipment WHERE equipment_id = ?";
            PreparedStatement checkEquipmentStatement = connection.prepareStatement(checkEquipmentQuery);
            checkEquipmentStatement.setInt(1, equipmentId);
            ResultSet equipmentResult = checkEquipmentStatement.executeQuery();

            if (!equipmentResult.isBeforeFirst()) {
                System.out.println("Equipment not found.");
                updateEquipmentStatus();
            }

            System.out.println();
            System.out.println("Options: ");
            System.out.println("1) Good");
            System.out.println("2) Requires Maintenance");
            System.out.println("3) Broken");
            System.out.println("4) Removed");
            System.out.println("5) Go Back");
            System.out.println();
            System.out.println("Enter new status: ");
            String newStatus = "";
            int choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    newStatus = "GOOD";
                    break;
                case 2:
                    newStatus = "REQUIRES MAINTENANCE";
                    break;
                case 3:
                    newStatus = "BROKEN";
                    break;
                case 4:
                    newStatus = "REMOVED";
                    break;
                default:
                    System.out.println("Invalid choice. Try again");
                    updateEquipmentStatus();
            }

            if (newStatus == "REMOVED"){
                String deleteEquipmentQuery = "DELETE FROM Equipment WHERE equipment_id = ?";
                PreparedStatement deleteEquipmentStatement = connection.prepareStatement(deleteEquipmentQuery);
                deleteEquipmentStatement.setInt(1, equipmentId);
                deleteEquipmentStatement.executeUpdate();
                deleteEquipmentStatement.close();
            }
            else {
                String updateEquipmentQuery = "UPDATE Equipment SET equipment_status = ? WHERE equipment_id = ?";
                PreparedStatement updateEquipmentStatement = connection.prepareStatement(updateEquipmentQuery);
                updateEquipmentStatement.setString(1, newStatus);
                updateEquipmentStatement.setInt(2, equipmentId);
                int rowsAffected = updateEquipmentStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Equipment status updated successfully.");
                } else {
                    System.out.println("Failed to update equipment status.");
                }

                equipmentResult.close();
                checkEquipmentStatement.close();
                updateEquipmentStatement.close();
            }

            manageEquipment();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void manageClassSchedule(){
        System.out.println();
        System.out.println("Options: ");
        System.out.println("1) view class schedule");
        System.out.println("2) book a new class");
        System.out.println("3) cancel a class");
        System.out.println("4) Go Back");
        System.out.println();
        System.out.println("Enter the number of your choice: ");
        int choice = input.nextInt();
        input.nextLine();

        switch (choice) {
            case 1:
                viewClassSchedule();
                break;
            case 2:
                bookClass();
                break;
            case 3:
                cancelClass();
                break;
            case 4:
                menuDecider();
                break;
            default:
                System.out.println("Invalid choice. Try again");
                manageClassSchedule();
        }
    }

    private static void viewClassSchedule(){
        try {
            String viewClassScheduleQuery =
                    "SELECT class_id, room_id, class_description, class_date, time_of_day " +
                    "FROM Classes " +
                    "WHERE class_type = 'GROUP_TYPE';";

            PreparedStatement viewClassScheduleStatement = connection.prepareStatement(viewClassScheduleQuery);
            ResultSet classScheduleResult = viewClassScheduleStatement.executeQuery();

            System.out.println("Class Schedule:");
            System.out.println("----------------------------------------------------------------------------");
            System.out.printf("| %-10s | %-10s | %-20s | %-15s | %-10s |\n",
                    "Class ID", "Room ID", "Class Description", "Date", "Time of Day");
            System.out.println("----------------------------------------------------------------------------");

            while (classScheduleResult.next()) {
                int classId = classScheduleResult.getInt("class_id");
                int roomId = classScheduleResult.getInt("room_id");
                String classDescription = classScheduleResult.getString("class_description");
                String classDate = classScheduleResult.getString("class_date");
                String timeOfDay = classScheduleResult.getString("time_of_day");

                System.out.printf("| %-10d | %-10d | %-20s | %-15s | %-10s |\n",
                        classId, roomId, classDescription, classDate, timeOfDay);
            }

            System.out.println("----------------------------------------------------------------------------");

            classScheduleResult.close();
            viewClassScheduleStatement.close();

            manageClassSchedule();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void bookClass() {
        try {
            System.out.println("Enter the Room ID:");
            int roomId = input.nextInt();
            input.nextLine();

            System.out.println("Enter the Class Description:");
            String classDescription = input.nextLine();

            String date = getDate("class date", true);

            System.out.println("What time of day?");
            System.out.println("1. Morning");
            System.out.println("2. Afternoon");
            System.out.println("3. Evening");
            System.out.println("Enter the number of your choice: ");
            int timechoice = input.nextInt();
            input.nextLine();

            String timeOfDay = "";
            switch (timechoice) {
                case 1:
                    timeOfDay = "MORNING";
                    break;
                case 2:
                    timeOfDay = "AFTERNOON";
                    break;
                case 3:
                    timeOfDay = "EVENING";
                    break;
                default:
                    System.out.println("Invalid choice. Try again");
                    bookClass();
                    return;
            }

            String checkRoomAvailabilityQuery = "SELECT * FROM Classes WHERE room_id = ? AND class_date = ? AND time_of_day = ?";
            PreparedStatement checkRoomAvailabilityStatement = connection.prepareStatement(checkRoomAvailabilityQuery);
            checkRoomAvailabilityStatement.setInt(1, roomId);
            checkRoomAvailabilityStatement.setDate(2, java.sql.Date.valueOf(date));
            checkRoomAvailabilityStatement.setString(3, timeOfDay);
            ResultSet roomAvailabilityResult = checkRoomAvailabilityStatement.executeQuery();

            if (roomAvailabilityResult.next()) {
                System.out.println("Error: Room not available for that time of day on that date.");
                bookClass();
                return;
            }

            String availableTrainersQuery = "SELECT p.username, p.first_name, p.last_name, d.start_trainer_date, d.start_time_of_day, d.end_trainer_date, d.end_time_of_day " +
                    "FROM Dates_Trainer_Available d " +
                    "JOIN Profiles p ON d.trainer_id = p.username " +
                    "WHERE ? BETWEEN d.start_trainer_date AND d.end_trainer_date " +
                    "AND NOT EXISTS (" +
                    "    SELECT 1 " +
                    "    FROM Dates_Trainer_Unavailable u " +
                    "    WHERE u.trainer_id = d.trainer_id " +
                    "    AND u.trainer_date = ?" +
                    "    AND u.time_of_day = ?" +
                    ")";
            PreparedStatement availableTrainersStatement = connection.prepareStatement(availableTrainersQuery);
            availableTrainersStatement.setDate(1, java.sql.Date.valueOf(date));
            availableTrainersStatement.setDate(2, java.sql.Date.valueOf(date));
            availableTrainersStatement.setString(3, timeOfDay);
            ResultSet availableTrainersResult = availableTrainersStatement.executeQuery();

            List<String> availableTrainers = new ArrayList<>();
            List<String> availableIDs = new ArrayList<>();
            while (availableTrainersResult.next()) {
                String firstName = availableTrainersResult.getString("first_name");
                String lastName = availableTrainersResult.getString("last_name");
                String ID = availableTrainersResult.getString("username");
                availableTrainers.add(firstName + " " + lastName);
                availableIDs.add(ID);
            }

            System.out.println("Available Trainers:");
            for (int i = 0; i < availableTrainers.size(); i++) {
                System.out.println((i + 1) + ". " + availableTrainers.get(i));
            }

            if (availableTrainers.isEmpty()){
                System.out.println("There are no available trainers during that time.");
                manageClassSchedule();
                return;
            }

            System.out.println("Enter the number corresponding to the trainer you want to select:");
            int trainerChoice = input.nextInt();
            input.nextLine();

            if (trainerChoice < 1 || trainerChoice > availableTrainers.size()) {
                System.out.println("Invalid choice. Try again.");
                bookClass();
                return;
            }

            String selectedTrainer = availableIDs.get(trainerChoice - 1);

            String insertClassQuery = "INSERT INTO Classes (trainer_id, room_id, class_type, class_description, class_date, time_of_day) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertClassStatement = connection.prepareStatement(insertClassQuery, Statement.RETURN_GENERATED_KEYS);
            insertClassStatement.setString(1, selectedTrainer);
            insertClassStatement.setInt(2, roomId);
            insertClassStatement.setString(3, "GROUP_TYPE");
            insertClassStatement.setString(4, classDescription);
            insertClassStatement.setDate(5, java.sql.Date.valueOf(date));
            insertClassStatement.setString(6, timeOfDay);
            insertClassStatement.executeUpdate();

            String insertUnavailableQuery = "INSERT INTO Dates_Trainer_Unavailable (trainer_id, trainer_date, time_of_day) VALUES (?, ?, ?)";
            PreparedStatement insertUnavailableStatement = connection.prepareStatement(insertUnavailableQuery);
            insertUnavailableStatement.setString(1, selectedTrainer);
            insertUnavailableStatement.setDate(2, java.sql.Date.valueOf(date));
            insertUnavailableStatement.setString(3, timeOfDay);
            insertUnavailableStatement.executeUpdate();


            System.out.println("Do you want to schedule another class? (y/n)");
            char choice = input.next().charAt(0);
            input.nextLine();

            if (choice == 'y' || choice == 'Y') {
                bookClass();
            } else {
                manageClassSchedule();
            }

            roomAvailabilityResult.close();
            checkRoomAvailabilityStatement.close();
            availableTrainersResult.close();
            availableTrainersStatement.close();
            insertUnavailableStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void cancelClass(){
        try {
            System.out.println("Enter the class_id of the class you want to cancel:");
            int classId = input.nextInt();
            input.nextLine();

            String checkClassQuery = "SELECT * FROM Classes WHERE class_id = ?";
            PreparedStatement checkClassStatement = connection.prepareStatement(checkClassQuery);
            checkClassStatement.setInt(1, classId);
            ResultSet classResult = checkClassStatement.executeQuery();

            if (!classResult.next()) {
                System.out.println("Class with class_id " + classId + " not found.");
                manageClassSchedule();
            }

            String deleteClassQuery = "DELETE FROM Classes WHERE class_id = ?";
            PreparedStatement deleteClassStatement = connection.prepareStatement(deleteClassQuery);
            deleteClassStatement.setInt(1, classId);
            int rowsAffected = deleteClassStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Class with class_id " + classId + " has been canceled.");
            } else {
                System.out.println("Failed to cancel class with class_id " + classId);
            }

            System.out.println("Do you want to delete another class? (y/n)");
            char choice = input.next().charAt(0);
            input.nextLine();

            if (choice == 'y' || choice == 'Y') {
                cancelClass();
            } else {
                manageClassSchedule();
            }

            classResult.close();
            checkClassStatement.close();
            deleteClassStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewBills() {
        try {
            String viewBillsQuery = "SELECT * FROM Bills";
            PreparedStatement viewBillsStatement = connection.prepareStatement(viewBillsQuery);
            ResultSet billsResult = viewBillsStatement.executeQuery();

            System.out.println("Bills:");
            System.out.println("------------------------------------------------------");
            System.out.printf("| %-8s | %-15s | %-6s | %-12s |\n",
                    "Bill ID", "Username", "Price", "Date Issued");
            System.out.println("------------------------------------------------------");

            while (billsResult.next()) {
                int billId = billsResult.getInt("bill_id");
                String username = billsResult.getString("username");
                int price = billsResult.getInt("price");
                Date dateIssued = billsResult.getDate("date_issued");

                System.out.printf("| %-8d | %-15s | $%-5d | %-12s |\n",
                        billId, username, price, dateIssued);
            }

            System.out.println("------------------------------------------------------");

            billsResult.close();
            viewBillsStatement.close();

            menuDecider();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}