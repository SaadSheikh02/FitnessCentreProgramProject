import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static Connection connection;
    static Scanner input;

    private static String HOST = "localhost";
    private static String PORT = "5432";
    private static String DB_NAME = "comp3005_project_2";
    private static String USER = "postgres";
    private static String PASSWORD = "50551591";

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
        }
    }

    private static void memberMenuChoices() {
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
                break;
            case 3:
                break;
            case 4:
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
        String birthday = getBirthday();

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

    private static String getBirthday() {
        System.out.println("Enter birthday (YYYY-MM-DD): ");

        if (input.hasNextLine()) {
            input.nextLine();
        }

        String actual_date = input.nextLine();

        SimpleDateFormat expectedFormat = new SimpleDateFormat("yyyy-MM-dd");
        expectedFormat.setLenient(false);

        try {
            Date parsedDate = expectedFormat.parse(actual_date);

            Date currDate = new Date();
            if (currDate.before(parsedDate) || currDate.equals(parsedDate)){
                System.out.println("Date cannot be past the current date");
                return getBirthday();
            }

            return actual_date;
        } catch (ParseException e) {
            System.out.println("Invalid date or date format.");
            return getBirthday();
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
                /*
                * while (resultSet.next()){
                    System.out.print("\n" + resultSet.getString("username") + "\t");
                    System.out.print(resultSet.getInt("credit_card") + "\t");
                    System.out.print(resultSet.getDate("birthday") + "\t");
                    System.out.print(resultSet.getInt("height") + "\t");
                    System.out.print(resultSet.getInt("weight") + "\t");
                    System.out.print(resultSet.getString("diet_plan") + "\t");
                    System.out.print(resultSet.getInt("goal_weight") + "\t");
                    System.out.print(resultSet.getInt("goal_speed") + "\t");
                    System.out.print(resultSet.getInt("goal_lift") + "\t");
                    System.out.print(resultSet.getDate("weight_deadline") + "\t");
                    System.out.print(resultSet.getDate("speed_deadline") + "\t");
                    System.out.print(resultSet.getDate("lift_deadline") + "\t");
                    System.out.print(resultSet.getInt("weight_loss") + "\t");
                    System.out.print(resultSet.getInt("max_speed") + "\t");
                    System.out.print(resultSet.getInt("max_lift") + "\t");
                    System.out.print(resultSet.getInt("bmi") + "\t");
                    System.out.print(resultSet.getInt("systolic_bp") + "\t");
                    System.out.print(resultSet.getInt("diastolic_bp") + "\t");
                    System.out.print(resultSet.getInt("heart_rate") + "\t");
                    System.out.print(resultSet.getInt("cholestrol_level") + "\t");
                    System.out.print(resultSet.getInt("bloodsugar_level") + "\t");
                }
                * */

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
        System.out.println("5) View Health Statistics");
        System.out.println("6) View Fitness Achievements");
        System.out.println("7) Go Back");
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
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
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
                        menuDecider();
                        break;
                    default:
                        personalInformation();
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
        String birthday = getBirthday();
        int height = getHeight();
        int weight = getWeight();

        String sql_profile_statement = "UPDATE profiles SET first_name = ?, last_name = ? WHERE username = ?";

        String sql_member_statement = "UPDATE members SET credit_card = ?, birthday = ?, height = ?, weight = ? WHERE username = ?";

        try{
            PreparedStatement prepStatement_profile = connection.prepareStatement(sql_profile_statement);
            PreparedStatement preparedStatement_member = connection.prepareStatement(sql_member_statement);

            prepStatement_profile.setString(1, firstName);
            prepStatement_profile.setString(2, lastName);
            prepStatement_profile.setString(3, username);

            // adding user profile
            prepStatement_profile.executeUpdate();

            preparedStatement_member.setInt(1, creditCard);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try{
                Date b_date = dateFormat.parse(birthday);
                java.sql.Date sqlDate = new java.sql.Date(b_date.getTime());
                preparedStatement_member.setDate(2, sqlDate);
            } catch (ParseException | SQLException e) {
                throw new RuntimeException(e);
            }

            preparedStatement_member.setInt(3, height);
            preparedStatement_member.setInt(4, weight);
            preparedStatement_member.setString(5, username);

            // adding base member info
            preparedStatement_member.executeUpdate();

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
//                        setFitnessGoals();
                        break;
                    case 'n':
                        menuDecider();
                        break;
                    default:
                        fitnessGoals();
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

    private static int getHeight() {
        System.out.println("Enter height (in cm): ");
        return input.nextInt();
    }

    private static int getWeight() {
        System.out.println("Enter weight (in kg): ");
        return input.nextInt();
    }



}
