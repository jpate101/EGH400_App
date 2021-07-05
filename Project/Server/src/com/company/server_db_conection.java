package com.company;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.ArrayList;

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
    public static Boolean check_for_project(String P_name) throws SQLException {
        Statement stmt = myConn.createStatement();
        String query = "SELECT Project_Name\n" +
                "FROM projects\n" +
                "WHERE EXISTS\n" +
                "(SELECT Project_Name FROM projects WHERE Project_Name=\""+P_name+"\");";
        ResultSet rs = stmt.executeQuery(query);
        try{
            rs.next();
            String test = rs.getString("Project_Name");
            //System.out.println(test);
        }catch (SQLException e){
            return true;
        }
        return false;
    }
    public static void insert_new_project(String P_name,String Des,String User) throws SQLException {
        Statement stmt = myConn.createStatement();
        String query = "INSERT INTO projects (Project_Name, Created_By, Description)\n" +
                "VALUES (\""+P_name+"\", \""+User+"\",\""+Des+"\");";
        ResultSet rs = stmt.executeQuery(query);
        query = "INSERT INTO people_per_project (Project, User, Role)\n" +
                "VALUES (\""+P_name+"\", \""+User+"\",1);";
        rs = stmt.executeQuery(query);
    }
    public static String[] get_user_projects(String User) throws SQLException {
        Statement stmt = myConn.createStatement();
        String query = "SELECT Project FROM people_per_project WHERE User=\""+User+"\"";
        ResultSet rs = stmt.executeQuery(query);

        
        int i = 0;
        ArrayList list = new ArrayList();

        while (rs.next()) {
            
            String test = rs.getString("Project");
            //System.out.println(test);
            list.add(test);
        }
        String[] array = (String[]) list.toArray(new String[list.size()]);
        //System.out.println(array.length);
        return array;
    }
    public static String[] get_all_users(String User) throws SQLException {
        Statement stmt = myConn.createStatement();
        String query = "SELECT ID FROM users";
        ResultSet rs = stmt.executeQuery(query);

        int i = 0;
        ArrayList list = new ArrayList();

        while (rs.next()) {

            String test = rs.getString("ID");
            //System.out.println(test);
            list.add(test);
        }
        String[] array = (String[]) list.toArray(new String[list.size()]);
        //System.out.println(array[0]);
        return array;
    }

    public static String insert_user_into_project(String inserting_User,String new_User,String Project) {
        //check if user has permission to add users to project
        ResultSet rs;
        ResultSet rs2;
        Statement stmt;
        try{
            stmt = myConn.createStatement();
            String query = "SELECT Role FROM people_per_project WHERE User=\""+inserting_User+"\" AND  Project=\""+Project+"\"";
            rs = stmt.executeQuery(query);
            //inset user
            rs.next();
        }catch (SQLException e){
            return "error check permission";
        }
        try{
            int Roledb = rs.getInt("Role");
            System.out.println(Roledb);
            if(Roledb == 1){
                stmt = myConn.createStatement();
                String query2 = "INSERT INTO people_per_project (Project, User, Role)" +
                        "VALUES (\""+Project+"\", \""+new_User+"\",2);";
                rs2 = stmt.executeQuery(query2);
                return "T";
            }else{
                return "need permission";
            }
        }catch (SQLException e){
            return "error inserting new user";
        }

    }
}
