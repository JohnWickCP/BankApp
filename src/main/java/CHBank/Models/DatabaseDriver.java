package CHBank.Models;

import java.sql.*;

public class DatabaseDriver {
    private Connection connection;

    public DatabaseDriver() {
        try{
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:bank.db");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    /* Client Section */

    public ResultSet getClientData(String pAddress, String pPassword) {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Clients WHERE PayeeAddress = '"+pAddress+"' AND Password ='"+pPassword+"';");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }



    /* Admin Section */

    public ResultSet getAdminData(String username, String pPassword) {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Admins WHERE Username = '"+username+"' AND Password ='"+pPassword+"';");
        } catch (Exception e){
            e.printStackTrace();
        }
        return resultSet;
    }


    /* Utility Methods */

}
