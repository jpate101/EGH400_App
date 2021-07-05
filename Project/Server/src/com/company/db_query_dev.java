package com.company;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Base64;

public class db_query_dev {
    public static void main(String[] args) throws NoSuchAlgorithmException, SQLException, UnsupportedEncodingException {


        int PORT = 12345;
        server_db_conection main_con = null;
        //database connection
        try{
            //create connection
            main_con = new server_db_conection("jdbc:mariadb://localhost:1433","egh400_test","root","jpate101");
        }catch(Exception e){
            System.out.println("error: unable to connect to database");
            //e.printStackTrace();
        }

        SHA sha = new SHA();




/*
        //insert admin into db
        byte[] salt = sha.getSalt();
        String str = Base64.getEncoder().encodeToString(salt);


        main_con.Insert_New_Current_user("Admin",str,sha.encrypt("1234",salt));
        System.out.println(salt);
        System.out.println(sha.encrypt("1234",salt));
        System.out.println("admin success");

 */
        /*
        //insert test user 1  into db
        byte[] salt = sha.getSalt();
        String str = Base64.getEncoder().encodeToString(salt);


        main_con.Insert_New_Current_user("User1",str,sha.encrypt("pass",salt));
        System.out.println(salt);
        System.out.println(sha.encrypt("1234",salt));
        System.out.println("admin success");

         */
        




/*
        //check admin salt and pass

        System.out.println(main_con.get_user_Pass("Admin"));
        byte[] byteArr = Base64.getDecoder().decode(main_con.get_user_salt("Admin"));
        //get PAssword from plaintext

        System.out.println(sha.encrypt("1234",byteArr));

 */
        /*
        //check User1 salt and pass

        System.out.println(main_con.get_user_Pass("User1"));
        byte[] byteArr = Base64.getDecoder().decode(main_con.get_user_salt("User1"));
        //get PAssword from plaintext

        System.out.println(sha.encrypt("pass",byteArr));

         */
        /*
        //testing check for projects
        System.out.println(server_db_conection.check_for_project("Sample_project_1"));
        System.out.println(server_db_conection.check_for_project("Sample_project_2"));

         */

        //server_db_conection.insert_new_project("Sample Project 2","SAmple description","Admin");
        //String[] Projects = new String[] {"Hello", "World","Testing Project 1","Testing Project 2","Testing Project 3"};
        //System.out.println(Projects[0]);
        //System.out.println(Projects[1]);
        //System.out.println(Projects[2]);

        //server_db_conection.get_user_projects("Admin");

        //server_db_conection.get_all_users("Admin");
        System.out.println(server_db_conection.insert_user_into_project("josh","Admin","test3"));
        System.out.println(server_db_conection.insert_user_into_project("Admin","josh","test2"));

    }
}
