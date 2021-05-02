package com.company;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.*;

public class server_db_conection {

    /**
     * stores database connection
     */
    public static Connection myConn;
    //Connection myConn;

    /**
     * create server connection to db
     * @param dbURL the URL for the Database
     * @param dbName the database name to check
     * @param dbUser the username of the admin
     * @param dbPass the password of the admin
     */
    public server_db_conection(String dbURL, String dbName,String dbUser,String dbPass){
        try {
            myConn = DriverManager.getConnection(dbURL + "/" + dbName, dbUser, dbPass);
            System.out.println(dbURL + "/" + dbName+"  "+dbUser + dbPass);
            System.out.println("connection successful");
        }catch(Exception db){
            db.printStackTrace();
        }

    }

    public static Boolean Insert_New_Current_user(String user, String salt, String pass) {
        //Connection con = DriverManager.getConnection("jdbc:mariadb://localhost:1433/mariadb", "root", "password");
        try{
            Statement stmt = myConn.createStatement();

            String query = "INSERT INTO users (ID, salt, Password) VALUES (\""+user+"\",\""+salt+"\", \""+pass+"\");";
            ResultSet rs = stmt.executeQuery(query);
            return true;
        }catch (SQLException e ){
            return false;
        }


    }

    public static String get_user_salt(String user) throws UnsupportedEncodingException {
        try{
            Statement stmt = myConn.createStatement();
            String query = "select salt from users where ID=\""+user+"\"";
            ResultSet rs = stmt.executeQuery(query);
            //System.out.println(rs);
            rs.next();
            //byte[] test = rs.getBytes("salt");
            String test = rs.getString("salt");
            //System.out.println(test);
            return test;

        }catch(SQLException e ){
            return "SQL_ERROR";
        }


    }
    public static String get_user_Pass(String user) throws SQLException, UnsupportedEncodingException {

        Statement stmt = myConn.createStatement();
        String query = "select Password from users where ID=\""+user+"\"";
        ResultSet rs = stmt.executeQuery(query);
        //System.out.println(rs);
        rs.next();
        String test = rs.getString("Password");
        return test;

    }
}
