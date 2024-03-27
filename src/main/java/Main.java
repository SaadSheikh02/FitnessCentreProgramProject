import java.sql.*;
import java.util.Scanner;

public class Main {
    static Connection connection;
    static Scanner input;

    private static String HOST = "localhost";
    private static String PORT = "1433";
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
            } else {
                System.out.println("Failed to connect to the database");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
