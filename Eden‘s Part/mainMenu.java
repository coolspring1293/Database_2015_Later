package com.main;

import java.sql.*;
import java.util.*;

import com.db.DBManager;
import com.verify.Verifing;

public class mainMenu {
	static DBManager per = DBManager.createInstance();
	static String usrname;
	static String password;
	static String nickname;
	static int age;
	static String sex;
	static String trip_no;
	static String station;
	static String departure_station;
	static String terminal_station;
	static String date;
	static String arrive_time;
	static String leave_time;
	static int distance;
	static int rank;
	static int amount;
	static int total_count;
	static int remaining_count;
	static int ticket_no;

    public static void main (String []args) throws Exception {
        while (true) {
        	try {
            	per.connectDB();
        		Scanner sc = new Scanner(System.in);
            	System.out.println("*************22306购票系统**************");
                System.out.println("1、登录           2、注册          3、退出");
                System.out.print("请输入选项:");
                int choice = sc.nextInt();
                switch (choice) {
                	case 1: {
                		System.out.print("请输入用户名:");
                		usrname = sc.next();
                		System.out.print("请输入密码:");
                		password = sc.next();
                		Verifing verify = new Verifing();
                		int isExit = verify.login(usrname, password);
                		nickname = "Eden";
                		if (isExit > 0) {
                			if (isExit == 1) {
                				System.out.println("登录成功，欢迎" + nickname + "~\n");
                            	Boolean flag = true;
                            	while (flag) {
                    				System.out.println("*******************22306功能界面*******************");
                    	            System.out.println("1、车次查询           2、车站查询          3、时刻表查询");
                    	            System.out.println("4、购买车票           5、个人信息          6、登出");
                    	            System.out.print("请输入选项:");
                    	            int choice_1 = sc.nextInt();
                    	            switch (choice_1) {
                    	            	case 1: {
                    	            		System.out.print("请输入车次:");
                                    		trip_no = sc.next();
                                    		
                                    		//to-do:查询车次
                                    		System.out.println("车次    日期     起点站    终点站    余票");
                                    	
                    	            		break;
                    	            	}
                    	            	
                    	            	case 2: {
                    	            		System.out.print("请输入车站名:");
                                    		station = sc.next();
                                    		//to-do:查询车站
                                    		
                    	            		break;
                    	            	}
                    	            	
                    	            	case 3: {
                    	            		
                    	            		break;
                    	            	}
                    	            	
                    	            	case 4: {
                    	            		System.out.print("请输入起点站名:");
                    	            		departure_station = sc.next();
                    	            		System.out.print("请输入终点站名:");
                    	            		terminal_station = sc.next();
                    	            		//to-do:判断在这区间是否有车次
                    	            		Boolean have = true;
                    	            		if (have) {
                    	            			remaining_count = 100;
                    	            			trip_no = "G381";
                    	            			date = "2016-01-05";
                    	            			System.out.println("此区间内有车次" + trip_no + "，发车时间为" + date +"，余票" + remaining_count +"张。");
                    	            			System.out.print("请输入购票数量: ");
                    	            			amount = sc.nextInt();
                    	            			//to-do:添加购票信息(purchasing)
                    	            			
                    	            			System.out.println("购买成功！\n");
                    	            		}
                    	            		else {
                    	            			System.out.println("此区间内尚无车次！\n");
                    	            		}
                    	            		break;
                    	            	}
                    	            	
                    	            	case 5: {
                    	            		System.out.println("*******" + nickname + "的个人信息******");
                    	            		System.out.println("用户名: " + usrname);
                    	            		System.out.println("年龄: " + age);
                    	            		System.out.println("性别: " + sex);
                    	            		System.out.println("购票信息: ");
                    	            		//to-do:查询购票信息
                    	            		
                    	            		System.out.println("所购车次: " + trip_no + "     发车时间：" + date + "     车票数量：" + amount);
                    	            		break;
                    	            	}
                    	            	
                    	            	case 6: {
                    	            		System.out.print('\n');
                    	            		flag = false;
                    	            		break;
                    	            	}
                    	            	
                    	            	
                    	            	default: {
                    	            		System.out.println("错误的选择！请输入１～6中的数字做出选择。\n");
                    	            	}
                    	            }	
                    			}
                			}
                			else {
                				Boolean is = true;
                				System.out.println("登录成功，欢迎管理员" + nickname + "~\n");
                            	Boolean flag = true;
                            	int time = 0;
                				while (flag) {
                    				System.out.println("********************22306管理界面**************************");
                    	            System.out.println("1、添加车票信息           2、添加车站          3、添加车次");
                    	            System.out.println("4、显示所有车次的时刻表     5、显示所有车站       6、显示所有用户信息");
                    	            System.out.println("7、显示所有车票信息        8、修改余票数量       9、登出");
                    	            //System.out.println("10、            ");
                    	            System.out.print("请输入选项:");
                    	            int choice_1 = sc.nextInt();
                    	            switch (choice_1) {
                    	            	case 1: {
                    	            		System.out.print("请输入需要新增车票的车次(格式如“G381”): ");
                                    		trip_no = sc.next();
                                    		is = per.checkTripNo(trip_no);
                                    		if (is == false) {
                                    			System.out.println("您输入的车次不在时刻表中，请新增该车次的时刻表。\n");
                                    			break;
                                    		}
                                    		System.out.print("请输入发车时间(格式如“2016-01-06”): ");
                                    		date = sc.next();
                                    		System.out.print("请输入始发站(格式如“北京”): ");
                                    		departure_station = sc.next();
                                    		is = per.checkStation(departure_station);
                                    		if (is == false) {
                                    			System.out.println("您输入的车站不在车站表中，请新增该车站。\n");
                                    			break;
                                    		}
                                    		System.out.print("请输入终点站(格式如“北京”): ");
                                    		terminal_station = sc.next();
                                    		is = per.checkStation(terminal_station);
                                    		if (is == false) {
                                    			System.out.println("您输入的车站不在车站表中，请修改或者新增该车站。\n");
                                    			break;
                                    		}
                                    		System.out.print("请输入总票数: ");
                                    		total_count = sc.nextInt();
                                    		is = per.addTicket(trip_no, date, departure_station, terminal_station, total_count);
                                    		if (is) {
                                    			System.out.println("新增车票信息成功！");
                                    		}
                                    		else {
                                    			System.out.println("新增车票信息失败，请重试!");
                                    		}
                    	            		break;
                    	            	}
                    	            	
                    	            	case 2: {
                    	            		System.out.print("请输入新增站的名称(格式如“北京”): ");
                                    		station = sc.next();
                                    		is = per.checkStation(station);
                                    		if (is == true) {
                                    			System.out.println("该车站已存在于车站表中！\n");
                                    			break;
                                    		}
                                    		System.out.print("请输入新增站的等级(输入'1'代表'COM'，'2'->'TER'，'3'->'EXC'): ");
                                    		rank = sc.nextInt();
                                    		if (rank == 1 || rank == 2 || rank == 3) {
                                    			is = per.addStation(station, rank);
                                    			if (is) {
                                        			System.out.println("新增车站成功！");
                                        		}
                                        		else {
                                        			System.out.println("新增车站失败，请重试!");
                                        		}
                                    		}
                                    		else 
                                    			System.out.println("错误的输入！请输入１～3中的数字。");
                                    		
                    	            		break;
                    	            	}
                    	            	
                    	            	case 3: {
                    	            		System.out.print("请输入需要新增车票的车次(格式如“G381”): ");
                                    		trip_no = sc.next();
                                    		is = per.checkTripNo(trip_no);
                                    		if (is == true) {
                                    			System.out.println("您输入的车次已存在与时刻表中！\n");
                                    			break;
                                    		}
                                    		
                                    		System.out.print("请输入该车次中的站数: ");
                                    		int count = sc.nextInt();
                                    		if (count < 2) {
                                    			System.out.println("站数至少为2！\n");
                                    			break;
                                    		}
                                    		
                                    		time = 0;
                                    		System.out.print("请输入始发站(格式如“北京”): ");
                                    		station = sc.next();
                                    		is = per.checkStation(station);
                                    		if (is == false) {
                                    			System.out.println("您输入的车站不在车站表中，请新增该车站。\n");
                                    			break;
                                    		}
                                    		System.out.print("请输入发车时间(格式如“10：00：00”): ");
                                    		leave_time = sc.next();
                                    		arrive_time = "00:00:00";
                                    		distance = 0;
                                    		is = per.addTimetable(trip_no, station, arrive_time, leave_time, distance, time);
                                			if (is) {
                                    			System.out.println("新增车站成功！");
                                    		}
                                    		else {
                                    			System.out.println("新增车站失败，请重试!");
                                        		break;
                                    		}
                                			
                                			time = 2;
                                			for (int i = 0; i < count - 2; i++) {
                                				System.out.print("请输入第" + i+2 + "个车站(格式如“北京”): ");
                                        		station = sc.next();
                                        		is = per.checkStation(station);
                                        		if (is == false) {
                                        			System.out.println("您输入的车站不在车站表中，请新增该车站。\n");
                                        			break;
                                        		}
                                        		System.out.print("请输入到站时间(格式如“10：00：00”): ");
                                        		arrive_time = sc.next();
                                        		System.out.print("请输入离站时间(格式如“10：00：00”): ");
                                        		leave_time = sc.next();
                                        		System.out.print("请输入距始发站的距离(公里)(格式如“100”): ");
                                        		distance = sc.nextInt();
                                        		is = per.addTimetable(trip_no, station, arrive_time, leave_time, distance, time);
                                    			if (is) {
                                        			System.out.println("新增车站成功！");
                                        		}
                                        		else {
                                        			System.out.println("新增车站失败，请重试!");
                                            		break;
                                        		}
                                			}
                                			
                                			time = 1;
                                			System.out.print("请输入终点站(格式如“北京”): ");
                                    		station = sc.next();
                                    		is = per.checkStation(station);
                                    		if (is == false) {
                                    			System.out.println("您输入的车站不在车站表中，请新增该车站。\n");
                                    			break;
                                    		}
                                    		System.out.print("请输入到站时间(格式如“10：00：00”): ");
                                    		arrive_time = sc.next();
                                    		leave_time = "00:00:00";
                                    		System.out.print("请输入距始发站的距离(公里)(格式如“100”): ");
                                    		distance = sc.nextInt();
                                    		is = per.addTimetable(trip_no, station, arrive_time, null, distance, time);
                                			if (is) {
                                    			System.out.println("新增车站成功！");
                                    		}
                                    		else {
                                    			System.out.println("新增车站失败，请重试!");
                                        		break;
                                    		}
                                			
                                			System.out.println("新增时刻表成功！\n");
                                			break;
                    	            	}
                    	            	
                    	            	case 4: {
                    	            		System.out.println("车次" + "    " + "站名" + "    " + "到站时间" + "    " + "离开时间" + "    " + "距离(距始发站)");
                    	            		per.show_all_timetable();
                    	            		break;
                    	            	}
                    	            	
                    	            	case 5: {
                    	            		System.out.println("车站编号" + "    " + "名称" + "    " + "等级");
                    	            		per.show_all_station();
                    	            		break;
                    	            	}
                    	            	
                    	            	case 6: {
                    	            		System.out.println("用户名" + "    " + "昵称" + "    " + "年龄" + "    " + "性别" + "    " + "管理员");
                    	            		per.show_all_user();
                    	            		break;
                    	            	}
                    	            	
                    	            	case 7: {
                    	            		System.out.println("车票编号" + "    " + "车次" + "    " + "发车时间" + "    " + "始发站" + "    " + "终点站" + "    " + "总票数" + "    " + "余票数");
                    	            		per.show_all_ticket();
                    	            		break;
                    	            	}
                    	            	
                    	            	case 8: {
                    	            		System.out.print("请输入需要修改余票数的车票编号：");
                    	            		ticket_no = sc.nextInt();
                    	            		System.out.print("请输入余票数量：");
                    	            		remaining_count = sc.nextInt();
                    	            		int max = per.getTotalTicket(ticket_no);
                    	            		if (remaining_count > max) {
                    	            			System.out.println("余票数量大于总票数！");
                    	            			break;
                    	            		}
                    	            		is = per.update_remain_ticket(ticket_no, remaining_count);
                    	            		if (is) {
                                    			System.out.println("更新余票数成功！");
                                    		}
                                    		else {
                                    			System.out.println("更新余票数失败，请重试!");
                                    		}
                    	            		break;
                    	            	}
                    	            	
                    	            	case 9: {
                    	            		System.out.print('\n');
                    	            		flag = false;
                    	            		break;
                    	            	}
                    	            	
                    	            	default: {
                    	            		System.out.println("错误的选择！请输入１～10中的数字做出选择。\n");
                    	            	}
                    	            }	
                    			}
                			}
                			
                		}
                		else {
                			System.out.println("用户名或密码错误！请重新登录.\n");
                			break;
                		}
                		break;
                	}
                	
                	case 2: {
                		System.out.print("请设置用户名：");
                		usrname = sc.next();
                		System.out.print("请设置密码：");
                		password = sc.next();
                		System.out.print("请设置昵称：");
                		nickname = sc.next();
                		System.out.print("请输入您的年龄：");
                		age = sc.nextInt();
                		System.out.print("请输入您的性别(男或女)：");
                		sex = sc.next();
                		//to-do:注册函数
                		
                		Boolean succeed = true;
                		if (succeed) {
                			System.out.println("注册成功，请重新登录!\n");
                		}
                		else {
                			System.out.println("存在相同用户名，请重新注册!");
                		}
                		break;
                	}
                	
                	case 3: {
                		sc.close();
                		per.closeDB();
                		System.exit(0);
                	}
                	
                	default:
                		System.out.println("错误的选择！请输入１～3中的数字做出选择。\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            /*ResultSet re = per.getTripTimetablebyNo("G381", 0.35);
            int re1 = per.CreateStationView(1);
            int re3 = per.CreateStationView(3);
            ResultSet re2 = per.QuerybyStation(1, 3);

            per.closeDB();*/
        }
    }
}
