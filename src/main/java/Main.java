import java.sql.*;
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

                String username = null;

                while(true){
                    int menuChoice = mainMenuChoices();

                    if(menuChoice == 1){
                        userRegistration();

                    }
                    else if(menuChoice == 2){
                        String loginResponse = userLogin();
                        if(loginResponse != null){
                            username = loginResponse;
                        }
                    }
                    else if(menuChoice == 3){
                    }
                    else if(menuChoice == 4){
                    }
                    else if(menuChoice == 5){
                    }

                }
            } else {
                System.out.println("Failed to connect to the database");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Main Menu functions
    private static int mainMenuChoices() {
        System.out.println();
        System.out.println("Options: ");
        System.out.println("1) User Registration");
        System.out.println("2) User Login");
        System.out.println("3) User Logout");
        System.out.println("4) Profile Information");
        System.out.println("5) Exit the program");
        System.out.println();
        System.out.println("Enter the number of your choice: ");
        int choice = input.nextInt();
        input.nextLine();
        return choice;
    }

    private static void userRegistration() {
        String username = getNewUsername();
        String password = getPassword();
        String firstName = getFirstName();
        String lastName = getLastName();
        String userType = "TYPE_MEMBER";

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
                "Members(username, bmi, systolic_bp, diastolic_bp, heart_rate, bloodsugar_level)\n" +
                "VALUES (?, ?, ?, ?, ?, ?)";

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
            preparedStatement_member.setDouble(2, bmi);
            preparedStatement_member.setInt(3, systolicBP);
            preparedStatement_member.setInt(4, diastolicBP);
            preparedStatement_member.setInt(5, heartRate);
            preparedStatement_member.setInt(6, bloodSugarLevel);

            // adding base member info
            preparedStatement_member.executeUpdate();
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

            statement.executeUpdate();

            ResultSet resultSet = statement.executeQuery();
            return resultSet.getString("username");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
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

    // Query functions
    public static boolean usernameExists(String username){
        try{
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT * FROM members WHERE username = ?;");
            ResultSet resultSet = statement.getResultSet();
            System.out.println("student_id\tfirst_name\tlast_name\temail\tenrolment_date");
            if(resultSet.isLast()){
                System.out.println();
                while (resultSet.next()){
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
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

}
