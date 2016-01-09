package com.db;

/**
 * Created by liuw53 on 1/7/16.
 */
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;

import static org.fusesource.jansi.Ansi.ansi;

public class DBManager {

    // 数据库连接常量
    public static final String DRIVER = "com.mysql.jdbc.Driver";
    public static final String USER = "root";
    public static final String PASS = "coolspring";
    public static final String URL = "jdbc:mysql://localhost:3307/ticket_system";

    // 静态成员，支持单态模式
    private static DBManager per = null;
    private static Connection conn = null;
    private static Statement stmt = null;
    private static Statement stmt1 = null;
    private static PreparedStatement pstmt = null;
    private static CallableStatement callStmt = null;

    private static String NullTime = "--:--:--";

    // 单态模式-懒汉模式
    private DBManager() {


    }
    public static DBManager createInstance() {
        if (per == null) {
            per = new DBManager();
            per.initDB();
        }
        return per;
    }

    // 加载驱动
    public void initDB() {
        try {
            Class.forName(DRIVER);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("Error loading Mysql Driver!");
        }
    }

    // 连接数据库，获取句柄+对象
    public void connectDB() {
        //System.out.println("Connecting to database...");
        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
            stmt = conn.createStatement();
            stmt1 = conn.createStatement();
            pstmt = conn.prepareStatement("");
            callStmt = conn.prepareCall("");
            System.out.println("SqlManager: Connect to database successful.");
        } catch (SQLException e) {
            System.out.println("SqlManager: Connect to database wrong.");
            e.printStackTrace();
        }
    }

    // 关闭数据库 关闭对象，释放句柄
    public void closeDB() {
        System.out.println("Close connection to database..");
        try {
            callStmt.close();
            pstmt.close();
            stmt.close();
            stmt1.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Close connection successful");
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

    //检查站是否已经存在
    public Boolean checkStation(String station) throws SQLException {
        // 获取Sql查询语句
        String logSql = "select * from station where station_name ='" + station + "'";
        stmt = conn.createStatement();

        try {
            ResultSet rs = stmt.executeQuery(logSql);
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //查找用户名
    public String findUserNick(String tmp_user_name) throws SQLException {
        // 获取Sql查询语句
        String nick = "No Nick Name";
        String logSql = "select nick_name from user where user_name ='" + tmp_user_name + "'";
        stmt = conn.createStatement();
        try {
            ResultSet rs = stmt.executeQuery(logSql);
            if (rs.next()) {
                nick = rs.getString("nick_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nick;
    }

    //检查车次是否已经存在
    public Boolean checkTripNo(String station) throws SQLException {
        // 获取Sql查询语句
        String logSql = "select * from timetable where trip_no ='" + station + "'";
        stmt = conn.createStatement();

        try {
            ResultSet rs = stmt.executeQuery(logSql);
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //获得站编号的最大值
    public int get_Max_Station_No() {
        String logSql = "call get_max_no(?)";
        // 获取DB对象

        int max = 0;
        try {
            callStmt = conn.prepareCall(logSql);
            callStmt.registerOutParameter("max_no", Types.INTEGER);
            callStmt.executeUpdate();
            max = callStmt.getInt("max_no");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return max;
    }

    //获得票编号的最大值
    public int get_Max_Ticket_No() {
        String logSql = "call get_max_ticket_no(?)";
        // 获取DB对象
        int max = 0;
        try {
            callStmt = conn.prepareCall(logSql);
            callStmt.registerOutParameter("max_no", Types.INTEGER);
            callStmt.executeUpdate();
            max = callStmt.getInt("max_no");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return max;
    }

    //获得站的编号
    public int getStationNo(String station) {
        String logSql = "SELECT station_no from station where station_name = '" + station + "'";
        int max = 0;
        try {
            ResultSet rs = stmt.executeQuery(logSql);
            if (rs.next()) {
                max = rs.getInt("station_no");
                return max;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return max;
    }

    //获得站名
    public String getStationName(int station) {
        String logSql = "SELECT station_name from station where station_no = '" + station + "'";
        String max = "";
        try {
            ResultSet rs = stmt1.executeQuery(logSql);
            if (rs.next()) {
                max = rs.getString("station_name");
                return max;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return max;
    }

    //添加车票
    public Boolean addTicket(String trip_no, String ticket_date, String departure_station, String terminal_station, int total_count) {

        //转换Java String到mysql date
        java.util.Date d = new Date(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            d=sdf.parse(ticket_date);

        } catch(Exception e) {
            e.printStackTrace();
        }

        //获得新插入的ticket_no等
        int max = get_Max_Ticket_No() + 1;
        int departure_station_no = getStationNo(departure_station);
        int terminal_station_no = getStationNo(terminal_station);

        // 操作DB对象：向ticket表中插入一条数据
        try {
            PreparedStatement pstmt = conn.prepareStatement("insert into ticket values (?,?,?,?,?,?,?)");
            pstmt.setInt(1, max);
            pstmt.setString(2, trip_no);
            pstmt.setDate(3, new java.sql.Date(d.getTime()));
            pstmt.setInt(4, departure_station_no);
            pstmt.setInt(5, terminal_station_no);
            pstmt.setInt(6, total_count);
            pstmt.setInt(7, total_count);
            int count = pstmt.executeUpdate();
            if (count > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //显示所有车票
    public void show_all_ticket() {
        String logSql = "SELECT * from ticket";
        try {
            ResultSet rs = stmt.executeQuery(logSql);
            while (rs.next()) {
                int ticket_no = rs.getInt("ticket_no");
                String trip_no = rs.getString("trip_no");
                java.sql.Date ticket_date = rs.getDate("ticket_date");
                int departure_station_no = rs.getInt("departure_station_no");
                int terminal_station_no = rs.getInt("terminal_station_no");
                int total_count = rs.getInt("total_count");
                int remaining_count = rs.getInt("remaining_count");

                //一个stmt执行完execute后回关闭rs。解决方法：开多个stmt
                String departure_station = getStationName(departure_station_no);
                String terminal_station = getStationName(terminal_station_no);

                System.out.println(ticket_no + "    " + trip_no + "    " + ticket_date + "    " + departure_station + "    " + terminal_station + "    " + total_count + "    " + remaining_count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void show_all_station() {
        String logSql = "SELECT * from station";
        try {
            ResultSet rs = stmt.executeQuery(logSql);
            while (rs.next()) {
                int station_no = rs.getInt("station_no");
                String station_name = rs.getString("station_name");
                String station_rank = rs.getString("station_rank");


                System.out.println(station_no + "    " + station_name + "    " + station_rank);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void show_all_user() {
        String logSql = "SELECT * from user";
        try {
            ResultSet rs = stmt.executeQuery(logSql);
            while (rs.next()) {
                String user_name = rs.getString("user_name");
                String nick_name = rs.getString("nick_name");
                int age = rs.getInt("age");
                String sex = rs.getString("sex");
                String sex_word = "";
                if ((char)sex.getBytes()[0] == 'M') sex_word = "男";
                else sex_word = "女";
                Boolean isAdmin = rs.getBoolean("isAdmin");
                String is_word = "";
                if (isAdmin) is_word = "是";
                else is_word = "否";
                System.out.println(user_name + "    " + nick_name + "    " + age +"    " + sex_word + "    " + is_word);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void show_all_timetable() {
        String logSql = "SELECT * from timetable";
        try {
            ResultSet rs = stmt.executeQuery(logSql);
            while (rs.next()) {
                String trip_no = rs.getString("trip_no");
                java.util.Date arrive_time = rs.getTime("arrive_time");
                java.util.Date leave_time = rs.getTime("leave_time");
                int station_no = rs.getInt("station_no");
                int distance = rs.getInt("distance");
                String station = getStationName(station_no);
                if (arrive_time == null)
                    System.out.println(trip_no + "    " + station + "    " + "始发站" +"    " + leave_time + "    " + distance);
                else if (leave_time == null)
                    System.out.println(trip_no + "    " + station + "    " + arrive_time +"    " + "终点站" + "    " + distance);
                else
                    System.out.println(trip_no + "    " + station + "    " + arrive_time +"    " + leave_time + "    " + distance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //获得总票数
    public int getTotalTicket(int station) {
        String logSql = "SELECT total_count from ticket where ticket_no = '" + station + "'";
        int max = 0;
        try {
            ResultSet rs = stmt1.executeQuery(logSql);
            if (rs.next()) {
                max = rs.getInt("total_count");
                return max;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return max;
    }

    //更新余票数
    public Boolean update_remain_ticket(int ticket_no, int remaining_count) {
        String logSql = "update ticket set remaining_count = " + remaining_count +" where ticket_no = " + ticket_no;
        // 操作DB对象：向ticket表中插入一条数据
        try {

            int count = stmt.executeUpdate(logSql);
            if (count > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //新增站
    public Boolean addStation(String station_name, int station_rank) {
        int max = get_Max_Station_No() + 1;
        String rank = "";
        if (station_rank == 1) rank = "COM";
        else if (station_rank == 2) rank = "TER";
        else rank = "EXC";
        try {
            PreparedStatement pstmt = conn.prepareStatement("insert into station values (?,?,?)");
            pstmt.setInt(1, max);
            pstmt.setString(2, station_name);
            pstmt.setString(3, rank);
            int count = pstmt.executeUpdate();
            if (count > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean insertNewUser(String name, String nick, String pwd, int age, String sex) throws SQLException {
        //System.out.println("--------------BRFORE register" + name + "---------------");
        //selectUserTable();
        CallableStatement callStmt = conn.prepareCall("call proc_register_user(?, ?, ?, ?, ?, ?)");
        callStmt.setString("register_user_name", name);
        callStmt.setString("register_nick_name", nick);
        callStmt.setString("register_user_pwd", pwd);
        callStmt.setInt("register_user_age", 10);
        callStmt.setString("register_user_sex", sex);
        callStmt.registerOutParameter("register_user_output", Types.BOOLEAN);
        callStmt.executeUpdate();

        Boolean isC = callStmt.getBoolean("register_user_output");
        //System.out.printf("The change: " + isC + " Register " + name + " ");
        if (isC) {
            System.out.println("Suceessfully!");
        }
        else {
            System.out.println("Failed!");
        }
        //selectUserTable();6
        //System.out.println("--------------AFTER register" + name + "---------------");
        return isC;
    }

    public static void selectUserTable() throws SQLException {
        ResultSet re = per.executeQuery("select * from user;");
        String nick = "No nickname";
        String name = "No_Name";
        String pwd = "No_PAssword";
        int age = 0;
        String sex = "N";
        boolean isA = false;
        while(re.next()) {
            nick = re.getString("nick_name");
            name = re.getString("user_name");
            pwd = re.getString("password");
            age = re.getInt("age");
            sex = re.getString("sex");
            isA = re.getBoolean("isAdmin");
            System.out.print(name); System.out.print("\t");
            System.out.print(nick); System.out.print("\t");
            System.out.print(pwd); System.out.print("\t");
            System.out.print(age); System.out.print("\t");
            System.out.print(sex); System.out.print("\t");
            System.out.print(isA); System.out.print("\n");
        }
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
        String no, station_name, ed = "终点站", st = "终点站", null_time = "--:--";
        Time arrive_time, leave_time, stop_time;
        int distance;
        double price_tmp;
        System.out.println( ansi().eraseScreen().render("\n@|red 车次       车站\t   到达时间   出发时间   停站时间    里程/km   票价/元     |@"));
        while(re.next()) {
            no = re.getString("trip_no");
            station_name = re.getString("station_name");
            arrive_time = re.getTime("arrive_time");
            leave_time = re.getTime("leave_time");
            stop_time = re.getTime("stop_time");
            distance = re.getInt("distance");
            price_tmp = re.getDouble("price");

            System.out.printf("%-6s", no);
            System.out.printf("%6s\t", station_name);
            if(arrive_time == null) System.out.printf("%10s",NullTime); else System.out.printf("%10s",arrive_time);
            if(leave_time == null) System.out.printf("%10s",NullTime); else System.out.printf("%10s",leave_time);
            if(stop_time == null) System.out.printf("%10s",NullTime); else System.out.printf("%10s",stop_time);
            System.out.printf("%10d",distance);
            System.out.println(ansi().eraseScreen().render("@|green    " + price_tmp + "|@"));

        }
        /////////////////////////////////////////////////////////////////////////

        return re;
    }


    public Boolean getStationTimetable(String station_name) throws SQLException {
        int station_no = getStationNo(station_name);
        String tip_sql = "select station_no, trip_no, arrive_time, leave_time, timediff(leave_time, arrive_time) as stop_time from timetable \n" +
                "where station_no = " + station_no;
        ResultSet re = null;
        try {
            re = stmt.executeQuery(tip_sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /////////////////////////////////////////////////////////////////////
        String no, null_time = "--:--";
        Time arrive_time, leave_time, stop_time;
        System.out.println( ansi().eraseScreen().render("\n@|red 车站\t   车次      到达时间   出发时间   停站时间 |@"));
        while(re.next()) {
            no = re.getString("trip_no");
            arrive_time = re.getTime("arrive_time");
            leave_time = re.getTime("leave_time");
            stop_time = re.getTime("stop_time");
            System.out.printf("%-6s\t", station_name);
            System.out.printf("%-6s", no);
            if(arrive_time == null) System.out.printf("%10s",NullTime); else System.out.printf("%10s",arrive_time);
            if(leave_time == null) System.out.printf("%10s",NullTime); else System.out.printf("%10s",leave_time);
            if(stop_time == null) System.out.printf("%10s\n",NullTime); else System.out.printf("%10s\n",stop_time);


        }
        /////////////////////////////////////////////////////////////////////////

        return true;
    }

    public Boolean getStationAndStation(String st, String ed, double price)  throws SQLException {
        int st_no = getStationNo(st), ed_no = getStationNo(ed);
        String tip_sql = "select \n" +
                "t1.trip_no as trip_number," +
                "return_station_name(t1.station_no) as st, \n" +
                "t1.leave_time as st_time, \n" +
                "return_station_name(t2.station_no) as ed, \n" +
                "t2.arrive_time as ed_time, \n" +
                "timediff(t2.arrive_time, t1.leave_time) as diff_time , \n" +
                "(t2.distance - t1.distance) * " + price + " as price\n" +
                "    from \n" +
                "    (select trip_no, station_no, arrive_time, leave_time, distance from timetable \n" +
                "    where station_no = " + st_no + ") t1, \n" +
                "    (select trip_no, station_no, arrive_time, leave_time, distance from timetable \n" +
                "    where station_no = " + ed_no + ") t2\n" +
                "    where t1.trip_no = t2.trip_no and (t2.distance - t1.distance) > 0;";
        ResultSet re = null;
        try {
            re = stmt.executeQuery(tip_sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /////////////////////////////////////////////////////////////////////
        String no;
        Time arrive_time, leave_time, diff_time;
        double price_tmp;
        if (re == null) return false;
        boolean flg = false;
        System.out.println( ansi().eraseScreen().render("\n@|red 车次     起点站\t  出发时间    终点站\t  到达时间    历时       票价|@"));
        while(re.next()) {
            no = re.getString("trip_number");
            arrive_time = re.getTime("st_time");
            leave_time = re.getTime("ed_time");
            diff_time = re.getTime("diff_time");
            price_tmp = re.getDouble("price");

            System.out.printf("%-6s", no);
            System.out.printf("%6s\t", st);
            if(arrive_time == null) System.out.printf("%10s",NullTime); else System.out.printf("%10s",arrive_time);
            System.out.printf("%6s\t", ed);
            if(leave_time == null) System.out.printf("%10s",NullTime); else System.out.printf("%10s",leave_time);
            if(diff_time == null) System.out.printf("%10s",NullTime); else System.out.printf("%10s",diff_time);
            System.out.println(ansi().eraseScreen().render("@|green    " + price_tmp + "|@"));
            flg = true;
        }
        /////////////////////////////////////////////////////////////////////////

        return flg;
    }


    public int getRemainingAmount(String no, String st, String ed, String d)  throws SQLException {
        int st_no = getStationNo(st), ed_no = getStationNo(ed);
        String tip_sql = "select remaining_count from ticket \n" +
                "    where ticket.trip_no = '" + no + "'\n" +
                "    and ticket.ticket_date = '" + d + "'\n" +
                "    and ticket.departure_station_no = " + st_no + " \n" +
                "    and ticket.terminal_station_no = '" + ed_no + "'";
        ResultSet re = null;
        try {
            re = stmt.executeQuery(tip_sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /////////////////////////////////////////////////////////////////////;
        while(re.next()) {
            return re.getInt(1);
        }
        /////////////////////////////////////////////////////////////////////////
        return 0;
    }


    public Boolean Purchasing(String t_user_name, String no, String d, String st, String ed, int tm)  throws SQLException {
        int st_no = getStationNo(st), ed_no = getStationNo(ed);
        CallableStatement callStmt = conn.prepareCall("call proc_prushing(?, ?, ?, ?, ?, ?, ?)");
        callStmt.setString("name", t_user_name);
        callStmt.setString("no", no);
        callStmt.setString("tdate", d);
        callStmt.setInt("departure", st_no);
        callStmt.setInt("terminal", ed_no);
        callStmt.setInt("tamount", tm);
        callStmt.registerOutParameter("isP", Types.BOOLEAN);
        callStmt.executeUpdate();

        Boolean isP = callStmt.getBoolean("isP");

        if (isP) {
            System.out.println( ansi().eraseScreen().render("\n您购买了乘车日期为@|red " + d + "|@, @|blue "
                    + no + "|@次@|red " + st + "|@到@|green " + ed + "|@的车票, 共@|red " + tm + "|@张.目前状态:购买成功!"));

        }
        else {
            System.out.println("购买失败,可能是没有足够的票!");
        }

        return isP;
    }

    public Boolean getUserInfo(String name)  throws SQLException {
        String tip_sql = "select nick_name, age, sex from user where user_name = '" + name + "'";
        ResultSet re = null;
        try {
            re = stmt.executeQuery(tip_sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /////////////////////////////////////////////////////////////////////;
        while(re.next()) {
            System.out.println("用户名: " + name);
            System.out.println("昵称:" + re.getString(1));
            System.out.println("性别:" + re.getString(3));
            System.out.println("年龄:" + re.getString(2));
        }
        /////////////////////////////////////////////////////////////////////////
        return true;

    }

    public Boolean getPurchaingInfo(String name)  throws SQLException {
        String tip_sql = "select return_pursh_amt('" + name + "') as rs";
        ResultSet re = null;
        try {
            re = stmt.executeQuery(tip_sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int ag = 0;
        /////////////////////////////////////////////////////////////////////;
        while(re.next()) {
            ag = re.getInt("rs");
        }
        if (ag == 0) {
            System.out.println("您竟然一张票也没买!");
        }
        else {

            tip_sql = "select \n" +
                    "    ticket.ticket_date dt,\n" +
                    "    ticket.trip_no no,\n" +
                    "    return_station_name(ticket.departure_station_no) as sd,\n" +
                    "    return_station_name(ticket.terminal_station_no) as ed,\n" +
                    "    purchasing.amount amt\n" +
                    "    from ticket, purchasing\n" +
                    "    where ticket.ticket_no = purchasing.ticket_no and purchasing.user_name = '"+ name +"'";

            re = null;
            try {
                re = stmt.executeQuery(tip_sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("您已经购买了:");
            while (re.next()) {
                System.out.println(ansi().eraseScreen().render(" #出发时间@|red " + re.getDate(1) + "|@, @|blue " +
                        re.getString(2)+ "|@次, 从@|yellow "+  re.getString(3)+ "|@到@|green " +
                        re.getString(4)+ "|@, 共计@|red " +re.getInt(5) + "|@张"));
            }


        }

        /////////////////////////////////////////////////////////////////////////
        return true;

    }


}
