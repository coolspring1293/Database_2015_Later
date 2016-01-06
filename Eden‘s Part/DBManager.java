package com.db;

import java.sql.*;
import java.text.SimpleDateFormat;

public class DBManager {

    // 数据库连接常量
    public static final String DRIVER = "com.mysql.jdbc.Driver";
    public static final String USER = "root";
    public static final String PASS = "1112233";
    public static final String URL = "jdbc:mysql://localhost:3307/ticket_system";

    // 静态成员，支持单态模式
    private static DBManager per = null;
    private static Connection conn = null;
    private static Statement stmt = null;
    private static Statement stmt1 = null;
    private static PreparedStatement pstmt = null;
    private static CallableStatement callStmt = null;

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
            //System.out.println("SqlManager:Connect to database successful.");
        } catch (SQLException e) {
        	System.out.println("SqlManager:Connect to database wrong.");
            e.printStackTrace();
        }
    }

    // 关闭数据库 关闭对象，释放句柄
    public void closeDB() {
        //System.out.println("Close connection to database..");
        try {
        	callStmt.close();
        	pstmt.close();
            stmt.close();
            stmt1.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //System.out.println("Close connection successful");
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
  	
}