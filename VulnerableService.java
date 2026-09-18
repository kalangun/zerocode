import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class VulnerableService {

    // 1. Hardcoded Credentials / Secrets
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "SuperSecretPassword123!"; 

    public void getUserData(String userId) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", DB_USER, DB_PASSWORD);
            Statement statement = conn.createStatement();
            
            // 2. SQL Injection (Concatenating raw input into SQL query)
            String query = "SELECT * FROM users WHERE id = '" + userId + "'";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                System.out.println("User: " + resultSet.getString("username"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void executeUserCommand(String userInput) {
        try {
            // 3. Command Injection (Passing raw user input directly to system shell)
            String command = "ping -c 1 " + userInput;
            Process process = Runtime.getRuntime().exec(command);
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}