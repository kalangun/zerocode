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
    private static final java.util.regex.Pattern HOST_PATTERN =
            java.util.regex.Pattern.compile("^[a-zA-Z0-9.-]+$");

    public void getUserData(String userId) {
        try {
            // 2. SQL Injection (Concatenating raw input into SQL query)
            String query = "SELECT * FROM users WHERE id = '" + userId + "'";
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", DB_USER, DB_PASSWORD);
                 Statement statement = conn.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery(query)) {
                    while (resultSet.next()) {
                        System.out.println("User: " + resultSet.getString("username"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void executeUserCommand(String userInput) {
        try {
            // 3. Command Injection fix: validate input and avoid shell parsing
            // by passing arguments as an array instead of a concatenated string.
            if (userInput == null || !HOST_PATTERN.matcher(userInput).matches()) {
                throw new IllegalArgumentException("Invalid host/IP: " + userInput);
            }
            Process process = Runtime.getRuntime().exec(new String[]{"ping", "-c", "1", userInput});
            
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