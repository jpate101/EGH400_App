package com.company;

import java.sql.Connection;
import java.sql.DriverManager;

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
}
