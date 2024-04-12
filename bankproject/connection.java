package bankproject;

import java.sql.Connection;
import java.sql.DriverManager;

public class connection {
    static Connection con; // Global Connection Object
    public static Connection getConnection()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/bank";             
            String user = "root";	
            String pass = "";
            con = DriverManager.getConnection(url, user, pass);
        }
        catch (Exception e) {
            System.out.println("Connection Failed!");
        }

        return con;
    }
}