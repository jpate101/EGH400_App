package com.company;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * class creates connection to db
 * all db reads and modification functions are called from this class
 */
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

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        System.out.println(formatter.format(date));

        String start_D = formatter.format(date);
        start_D = "STR_TO_DATE(\'"+start_D+"\',\'%d/%m/%Y\')";


        Statement stmt = myConn.createStatement();
        String query = "INSERT INTO projects (Project_Name, Created_By, Description,Project_Creation_date)\n" +
                "VALUES (\""+P_name+"\", \""+User+"\",\""+Des+"\","+start_D+");";
        ResultSet rs = stmt.executeQuery(query);
        query = "INSERT INTO people_per_project (Project, User, Role)\n" +
                "VALUES (\""+P_name+"\", \""+User+"\",1);";
        rs = stmt.executeQuery(query);
        query = "INSERT INTO people_per_project (Project, User, Role)\n" +
                "VALUES (\""+P_name+"\", \""+"!No User Selected"+"\",2);";
        rs = stmt.executeQuery(query);    }
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

    public static String[] get_all_users_in_project(String project) throws SQLException {
        System.out.println(project);

        Statement stmt = myConn.createStatement();
        String query = "SELECT Project,User,Role FROM people_per_project WHERE Project=\""+project+"\"";
        ResultSet rs = stmt.executeQuery(query);


        int i = 0;
        ArrayList list = new ArrayList();

        while (rs.next()) {

            String test = rs.getString("User");
            //System.out.println(test);
            list.add(test);
        }
        String[] array = (String[]) list.toArray(new String[list.size()]);
        //System.out.println(array.length);
        return array;
        //String[] test = {"Hello", "World","test","testing"};
        //return test;
    }

    public static boolean insert_task_into_project(String Task_name,String Project,String Assign_User,String Created_By,String start_D,String end_D,String Des) {
        start_D = start_D.substring(7);
        end_D = end_D.substring(5);

        if (start_D.charAt(1) == '/') start_D = "0" + start_D;
        if (start_D.charAt(4) == '/') start_D = start_D.substring(0,3) + "0" + start_D.substring(3);

        start_D = "STR_TO_DATE(\'"+start_D+"\',\'%d/%m/%Y\')";
        end_D = "STR_TO_DATE(\'"+end_D+"\',\'%d/%m/%Y\')";




        try{
            Statement stmt = myConn.createStatement();

            // eg TO_DATE('17/12/2015', 'DD/MM/YYYY')
            String query = "INSERT INTO tasks (Task_name, Project, Assigned_User,Created_By,Status_int,start_date,end_date,Description) VALUES (\""+Task_name+"\",\""+Project+"\",\""+Assign_User+"\",\""+Created_By+"\",\""+String.valueOf(1)+"\","+start_D+","+end_D+",\""+Des+"\");";
            ResultSet rs = stmt.executeQuery(query);
            return true;
        }catch (SQLException e ){
            System.out.println(e);
            return false;
        }
        /*
        status
        1 = in Progress
        2 = Resolved
        3 = Canceled
         */
    }
    public static String[] get_project_tasks(String Project) throws SQLException {
        //project creation date
        Statement stmt2 = myConn.createStatement();
        String query2 = "SELECT Project_Name,Project_Creation_date FROM projects WHERE Project_Name=\""+Project+"\"";
        ResultSet rs2 = stmt2.executeQuery(query2);

        int i = 0;
        ArrayList list = new ArrayList();

        while (rs2.next()) {

            String test = rs2.getString("Project_Creation_date");
            System.out.println(test);
            list.add(test);
        }
        //String[] array = (String[]) list.toArray(new String[list.size()]);
        //System.out.println(array);


        //get project tasks
        Statement stmt = myConn.createStatement();
        String query = "SELECT Task_Name,Status_int,start_date,end_date FROM tasks WHERE Project=\""+Project+"\"ORDER BY end_date ASC";
        ResultSet rs = stmt.executeQuery(query);

        i = 0;
        //list = new ArrayList();

        while (rs.next()) {

            String test = rs.getString("Task_Name");
            String test2 = rs.getString("Status_int");
            String test3 = rs.getString("start_date");
            String test4 = rs.getString("end_date");
            String test5 = "___________";
            System.out.println(test);
            System.out.println(test2);
            System.out.println(test3);
            System.out.println(test4);
            System.out.println(test5);
            list.add(test);
            list.add(test2);
            list.add(test3);
            list.add(test4);
            //list.add(test5);
        }
        String[] array = (String[]) list.toArray(new String[list.size()]);
        System.out.println(Arrays.toString(array));
        System.out.println(array.length);

        return array;
    }

    public static String[] GET_TASK_INFO(String Project,String Task) throws SQLException {
        Statement stmt = myConn.createStatement();
        String query = "SELECT Task_name,Project,Assigned_User,Created_By,Status_int,start_date,end_date,Description FROM tasks WHERE Project=\""+Project+"\" AND Task_name=\""+Task+"\"";
        ResultSet rs = stmt.executeQuery(query);

        rs.next();
        //byte[] test = rs.getBytes("salt");
        String Des = rs.getString("Description");
        String Status = rs.getString("Status_int");
        String Assigned_user = rs.getString("Assigned_User");
        String s_date = rs.getString("start_date");
        String e_date = rs.getString("end_date");

        System.out.println(Des+Status+Assigned_user+s_date+e_date+Task);

        String[] data = {Des, Status, Assigned_user, s_date, e_date, Task};


        return data;
    }
    public static boolean UPDATE_TASK_STATUS(String Project,String Task,String Status) {
        Statement stmt = null;
        try {
            stmt = myConn.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String query = "UPDATE tasks\n" +
                "SET Status_int = "+Status+"\n" +
                "WHERE Task_name = \""+Task+"\" AND Project = \""+Project+"\";";
        try {
            ResultSet rs = stmt.executeQuery(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;

    }

    public static boolean insert_minor_task_into_project(String root_task,String Task_name,String Project,String Assign_User,String Created_By,String start_D,String end_D,String Des) {
        start_D = start_D.substring(7);
        end_D = end_D.substring(5);

        if (start_D.charAt(1) == '/') start_D = "0" + start_D;
        if (start_D.charAt(4) == '/') start_D = start_D.substring(0,3) + "0" + start_D.substring(3);

        start_D = "STR_TO_DATE(\'"+start_D+"\',\'%d/%m/%Y\')";
        end_D = "STR_TO_DATE(\'"+end_D+"\',\'%d/%m/%Y\')";




        try{
            Statement stmt = myConn.createStatement();

            // eg TO_DATE('17/12/2015', 'DD/MM/YYYY')
            String query = "INSERT INTO minortasks (root_task,Task_name, Project, Assigned_User,Created_By,Status_int,start_date,end_date,Description) VALUES (\""+root_task+"\",\""+Task_name+"\",\""+Project+"\",\""+Assign_User+"\",\""+Created_By+"\",\""+String.valueOf(1)+"\","+start_D+","+end_D+",\""+Des+"\");";
            ResultSet rs = stmt.executeQuery(query);
            return true;
        }catch (SQLException e ){
            System.out.println(e);
            return false;
        }
    }

    public static String[] get_project_tasks_minor(String Project, String root_task) throws SQLException {

        int i = 0;
        ArrayList list = new ArrayList();



        //get project tasks
        Statement stmt = myConn.createStatement();
        String query = "SELECT Task_Name,Status_int,start_date,end_date FROM minortasks WHERE Project=\""+Project+"\" AND root_task=\""+root_task+"\"ORDER BY end_date ASC";
        ResultSet rs = stmt.executeQuery(query);
        //list = new ArrayList();

        while (rs.next()) {

            String test = rs.getString("Task_Name");
            String test2 = rs.getString("Status_int");
            String test3 = rs.getString("start_date");
            String test4 = rs.getString("end_date");
            String test5 = "___________";
            System.out.println(test);
            System.out.println(test2);
            System.out.println(test3);
            System.out.println(test4);
            System.out.println(test5);
            list.add(test);
            list.add(test2);
            list.add(test3);
            list.add(test4);
            //list.add(test5);
        }
        String[] array = (String[]) list.toArray(new String[list.size()]);
        System.out.println(Arrays.toString(array));
        System.out.println(array.length);

        return array;
    }


}
