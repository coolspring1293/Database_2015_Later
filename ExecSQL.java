package db;

import java.io.*;
import java.util.*;
import java.sql.*;
/**
 * Demo for JDBC connnetion.
 * Created by Liu Wang (@author liuw53) 2015/12/30. All rights reserved.
 * 
 * */

public class ExecSQL {
	// the constant value for jdbc connection
    public static final String DRIVER = "com.mysql.jdbc.Driver";
    public static final String USER = "root";
    public static final String PASS = "coolspring";
    public static final String URL = "jdbc:mysql://localhost:3307/ticket_system";
    
    
    //
    private static ExecSQL per = null;
    private Connection conn = null;
    private Statement stmt = null;

       
    //
    private ExecSQL() {
    	System.out.println("--------STARTED----------");
    	System.out.println(DRIVER + " " + USER + " " + PASS + " " + URL);
    }
    
    //
    public static ExecSQL createInstance() {
    	if (null == per) {
    		per = new ExecSQL();
    		per.initDB();
    	}
    	return per;
    }
    
    public void initDB() {
    	 try {
             Class.forName("com.mysql.jdbc.Driver");
         } catch (Exception e) {
        	 System.out.println("Not found Driver");
             e.printStackTrace();
         }
    }
     
    public void connectDB() {
        System.out.println("Connecting to database...");
        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("ExecSQL:Connect to database successful.");
    }
    
    
    public void closeDB() {
        System.out.println("Close connection to database..");
        try {
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Close connection successful");
    }
    
    // 
    
    
    public static void main (String []args) throws Exception {
    	createInstance();
    	per.connectDB();
    	ResultSet re = per.executeQuery("select * from user;");
    	int id = 0;
    	String name = "No_Name";
    	String pwd = "No_PAssword";
    	int age = 0;
    	String sex = "N";
    	boolean isA = false;
    	while(re.next()) {
    		id = re.getInt("user_id");
    		name = re.getString("user_name");
    		pwd = re.getString("password");
    		age = re.getInt("age");
    		sex = re.getString("sex");
    		isA = re.getBoolean("isAdmin");
    		System.out.print(id); System.out.print("\t");
    		System.out.print(name); System.out.print("\t");
    		System.out.print(pwd); System.out.print("\t");
    		System.out.print(age); System.out.print("\t");
    		System.out.print(sex); System.out.print("\t");
    		System.out.print(isA); System.out.print("\n");
    	}
    	Connection tmp;
    	CallableStatement callStmt = tmp.prepareCall("{proc_register_user(?, ?, ?, ?, ?, ?)}");    	
    	per.closeDB();
    }
    // 查询
    public ResultSet executeQuery(String sql) {
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    // 增添/删除/修改
    public int executeUpdate(String sql) {
        int ret = 0;
        try {
            ret = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

}
