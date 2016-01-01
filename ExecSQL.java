package com.company;

/**
 * Created by liuw53 on 12/31/15.
 * Demo for JDBC connnetion.
 * Created by Liu Wang (@author liuw53) 2015/12/30. All rights reserved.
 *
 * */
import java.io.*;
import java.util.*;
import java.sql.*;


public class ExecSQL {
    // the constant value for jdbc connection
    public static final String DRIVER = "com.mysql.jdbc.Driver";
    public static final String USER = "root";
    public static final String PASS = "coolspring";
    public static final String URL = "jdbc:mysql://localhost:3307/ticket_system";


    //
    private static ExecSQL per = null;
    private static Connection conn = null;
    private static Statement stmt = null;


    /**
     * 构造函数：输出一些常量*/
    private ExecSQL() {
        System.out.println("--------STARTED----------");
        System.out.println(DRIVER + " " + USER + " " + PASS + " " + URL);
    }

    /**
     * 创建实例
     * */
    public static ExecSQL createInstance() {
        if (null == per) {
            per = new ExecSQL();
            per.initDB();
        }
        return per;
    }
    /**
     * 初始化操作，好像只有加载驱动
     * */
    public void initDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            System.out.println("Not found Driver");
            e.printStackTrace();
        }
    }
    /**
     * 连接数据库
     * */
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

    /**
     * 关闭数据库连接
     * */
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

    /**
     * main方法，用于测试，在运行的时候需要注释掉！！ATTENTION!
     * */
    public static void main (String []args) throws Exception {
        createInstance();
        per.connectDB();
        //insertNewUser(111, "DFA", "sdggfdvasz", 12, "F");
        //deleteUser("liusy7", "1asdfa234");
        //changePassword("liusy7", "dsaf", "1234");
        ResultSet re = per.getTripTimetablebyNo("G381", 0.35);
        int re1 = per.CreateStationView(1);
        int re3 = per.CreateStationView(3);
        ResultSet re2 = per.QuerybyStation(1, 3);

        per.closeDB();
    }
    /**
     * insert a new user with id, name, pwd and others
     * return value means whether the insert operation is completed.
     * para.s are easy to understand
     * 在SQL的proc_register_user检查用户名重复，主键冲突？
     * */
    public static Boolean insertNewUser(int id, String name, String pwd, int age, String sex) throws SQLException {
        System.out.println("--------------BRFORE register" + name + "---------------");
        selectUserTable();
        CallableStatement callStmt = conn.prepareCall("call proc_register_user(?, ?, ?, ?, ?, ?)");
        callStmt.setInt("register_user_id", id);
        callStmt.setString("register_user_name", name);
        callStmt.setString("register_user_pwd", pwd);
        callStmt.setInt("register_user_age", 10);
        callStmt.setString("register_user_sex", sex);
        callStmt.registerOutParameter("register_user_output", Types.BOOLEAN);
        callStmt.executeUpdate();

        Boolean isC = callStmt.getBoolean("register_user_output");
        System.out.printf("The change: " + isC + " Register " + name + " ");
        if (isC) {
            System.out.println("Suceessfully!");
        }
        else {
            System.out.println("Failed!");
        }
        selectUserTable();
        System.out.println("--------------AFTER register" + name + "---------------");
        return isC;
    }
    // 删除用户，需要用户名和密码
    public static Boolean deleteUser(String name, String pwd) throws SQLException {
        System.out.println("--------------BRFORE delete " + name + "---------------");
        selectUserTable();
        CallableStatement callStmt = conn.prepareCall("call proc_delete_user(?, ?, ?)");
        callStmt.setString("delete_user_name", name);
        callStmt.setString("delete_user_pwd", pwd);
        callStmt.registerOutParameter("is_delete", Types.BOOLEAN);
        callStmt.executeUpdate();

        Boolean isC = callStmt.getBoolean("is_delete");
        System.out.printf("The change: " + isC + " DELETE " + name + " ");
        if (isC) {
            System.out.println("Suceessfully!");
        }
        else {
            System.out.println("Failed!");
        }
        selectUserTable();
        System.out.println("--------------AFTER delete " + name + "---------------");
        return isC;
    }

    // 修改用户密码， 需要用户名，旧的密码，新的密码，在外部（Android端）检查密码是否重复
    // 注意这里面有一点点小bug，就是它的返回值，下次修复
    public static Boolean changePassword(String name, String old_pwd, String new_pwd) throws SQLException {
        System.out.println("--------------BRFORE Change PASSWORD " + name + "---------------");
        selectUserTable();
        CallableStatement callStmt = conn.prepareCall("call proc_change_pwd(?, ?, ?, ?)");
        callStmt.setString("chp_user_name", name);
        callStmt.setString("old_pwd", old_pwd);
        callStmt.setString("new_pwd", new_pwd);
        callStmt.registerOutParameter("is_changed", Types.BOOLEAN);
        callStmt.executeUpdate();

        Boolean isC = callStmt.getBoolean("is_changed");
        System.out.printf("The change: " + isC + " Change PASSWORD " + name + " ");
        if (isC) {
            System.out.println("Suceessfully!");
        }
        else {
            System.out.println("Failed!");
        }
        selectUserTable();
        System.out.println("--------------AFTER Change PASSWORD " + name + "---------------");
        return isC;
    }
    // 把所有user表中的内容输出到Server端的控制台，用于调试，注意所有的方法都需要抛出个异常
    public static void selectUserTable() throws SQLException {
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

    public ResultSet getTripTimetablebyNo(String trip_no, double price) throws SQLException {
        String tip_sql = "select trip_no, station_name, arrive_time, leave_time, timediff(leave_time, arrive_time) as stop_time, distance, distance*" + price + " as price\n" +
                "    from timetable, station \n" +
                "    where trip_no = '" + trip_no  + "' and timetable.station_no = station.station_no\n" +
                "    order by arrive_time  asc;";
        ResultSet re = null;
        try {
            re = stmt.executeQuery(tip_sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /////////////////////////////////////////////////////////////////////
        String no, station_name;
        Time arrive_time, leave_time, stop_time;
        int distance;
        double price_tmp;
        while(re.next()) {
            no = re.getString("trip_no");
            station_name = re.getString("station_name");
            arrive_time = re.getTime("arrive_time");
            leave_time = re.getTime("leave_time");
            stop_time = re.getTime("stop_time");
            distance = re.getInt("distance");
            price_tmp = re.getDouble("price");
            System.out.print(no); System.out.print("\t");
            System.out.print(station_name); System.out.print("\t");
            System.out.print(arrive_time); System.out.print("\t");
            System.out.print(leave_time); System.out.print("\t");
            System.out.print(distance); System.out.print("\t");
            System.out.print(price_tmp); System.out.print("\t");
            System.out.print(stop_time); System.out.print("\n");
        }
        /////////////////////////////////////////////////////////////////////////

        return re;
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


    //create a view for a single station
    public int CreateStationView(int station) throws SQLException {
        int ret = 0;
        String drop_if_exist = "drop view if exists view_double_station_timetable_"+ station + ";";
        String csv_sql = "create view view_double_station_timetable_"+ station +"\n" +
                "    (trip_no, station_no, arrive_time, leave_time, distance) as \n" +
                "    select trip_no, station_no, arrive_time, leave_time, distance from timetable \n" +
                "    where station_no = "+ station;
        try {
            ret = stmt.executeUpdate(drop_if_exist);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ret = stmt.executeUpdate(csv_sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    //query timetbale betwwen stations
    public ResultSet QuerybyStation(int station1, int station2) throws SQLException {
        ResultSet re = null;
        String csv_sql = "select \n" +
                "return_station_name(t1.station_no) as st, \n" +
                "t1.leave_time as st_time, \n" +
                "return_station_name(t2.station_no) as ed, \n" +
                "t2.arrive_time as ed_time, \n" +
                "timediff(t2.arrive_time, t1.leave_time) as diff_time , \n" +
                "(t2.distance - t1.distance) * 0.35 as price\n" +
                "    from \n" +
                "    view_double_station_timetable_"+ station1 +" t1, \n" +
                "    view_double_station_timetable_"+ station2 +" t2\n" +
                "    where t1.trip_no = t2.trip_no;";
        try {
            re = stmt.executeQuery(csv_sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ////////////////////////////////////////////////////////////////
        String s1, s2;
        Time t1, t2, t_c;
        Double pr;
        while (re.next()){
            s1 = re.getString(1);
            t1 = re.getTime(2);
            s2 = re.getString(3);
            t2 = re.getTime(4);
            t_c = re.getTime(5);
            pr = re.getDouble(6);
            System.out.print(s1 + "\t");
            System.out.print(t1 + "\t");
            System.out.print(s2 + "\t");
            System.out.print(t2 + "\t");
            System.out.print(t_c + "\t");
            System.out.print(pr + "\n");
        }
        ///////////////////////////////////////////////////////////////
        return re;
    }



}

