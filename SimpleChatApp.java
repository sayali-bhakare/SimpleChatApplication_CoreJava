package Project;
import java.sql.*;
import java.util.Scanner;

public class SimpleChatApp 
{
    private static final String URL = "jdbc:postgresql://localhost:5432/ChatDB";
    private static final String USER = "postgres";
    private static final String PASSWORD = "system";

    public static void main(String[] args) 
    {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) 
        {
            System.out.println("Connected to the Server successfully.");

            Scanner scanner = new Scanner(System.in);
            while (true) 
            {
            	System.out.println("**********SIMPLE CHAT APP*********");
                System.out.println("1. Send Message");
                System.out.println("2. View Messages");
                System.out.println("3. Exit");
                scanner.nextLine();
                System.out.print("Enter Your Choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); 

                switch (choice) 
                {
                    case 1:
                        System.out.print("Enter your name: ");
                        String sender = scanner.nextLine();
                        System.out.print("Enter message you want to sent: ");
                        String message = scanner.nextLine();
                        sendMessage(conn, sender, message);
                        break;
                        
                    case 2:
                        viewMessages(conn);
                        break;
                        
                    case 3:
                        System.out.println("Exiting...");
                        return;
                        
                    default:
                        System.out.println("Invalid Choice...");
                }
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    private static void sendMessage(Connection conn, String sender, String message) throws SQLException 
    {
        String sql = "INSERT INTO messages (sender, message) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            pstmt.setString(1, sender);
            pstmt.setString(2, message);
            pstmt.executeUpdate();
            System.out.println("Message Sent Successfully.");
        }
    }
    private static void viewMessages(Connection conn) throws SQLException 
    {
        String sql = "SELECT * FROM messages ORDER BY timestamp DESC";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) 
        {
            while (rs.next()) 
            {
                System.out.println("[" + rs.getTimestamp("timestamp") + "] " +
                        rs.getString("sender") + ": " +
                        rs.getString("message"));
            }
        }
    }
}