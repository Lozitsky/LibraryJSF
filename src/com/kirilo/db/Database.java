package com.kirilo.db;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    private static Connection connection;

    public static Connection getConnection() {
/*        try {
            if (connection != null) {
                return connection;
            }
            Context context = new InitialContext();
            DataSource ds = (DataSource) context.lookup("jdbc/Library");
            if (connection == null) {
                connection = ds.getConnection();
            }
        } catch (NamingException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, "Error connection!", e);
        } catch (SQLException throwables) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, "SQL Exception!", throwables);
        }*/
        return connection;
    }
}
