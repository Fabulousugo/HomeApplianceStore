/*
    Code written and maintained by
    Ugochukwu Precious Onah
    24848777
 */
package org.mjb.dao;

import org.mjb.Connector;
import org.mjb.systemModels.User;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 * The UserDAO class is responsible for interacting with the 'users' table in the database.
 * It provides methods for logging in users and creating new users.
 *
 * This class uses SQL queries to access the database and returns a User object representing user data.
 *
 * <p>Code written and maintained by Ugochukwu Precious Onah, 24848777.</p>
 */
public class UserDAO {
    /**
     * Login as user  user.
     *
     * @param username the username
     * @param password the password
     * @return the user
     */
    public User loginAsUser(String username, String password){
        User user = null;
        try{
            Connection connection = new Connector().connect();
            ResultSet result = connection.createStatement().executeQuery("SELECT * FROM users WHERE username = '"+username+"' AND password = '"+password+"'");
            while(result.next()){
                user = new User(result.getInt("id"),result.getString("username"),result.getString("password"));
            }
            connection.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Create user boolean.
     *
     * @param user the user
     * @return the boolean
     */
    public boolean createUser(User user){
        try{
            Connection connection = new Connector().connect();
            connection.createStatement().execute(String.format("INSERT INTO users (username,password) VALUES ('%s','%s')",
                            user.getUsername(),user.getPassword()
                    )
            );
            connection.close();
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
