package com.main;

import java.sql.*;
import java.util.*;
import java.util.concurrent.SynchronousQueue;

import com.db.DBManager;
import com.verify.Verifing;
import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;
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
    static int rank;
    static int amount;
    static int total_count;
    static int remaining_count;
    static int ticket_no;
    static double price = 0.35;

    public static void main (String []args) throws Exception {
        System.out.print( ansi().eraseScreen().render("@|red 22306购票系统|@ @|green V1.0|@ @|yellow Created by Liu Wang, Liu Zihan|@") );
        per.connectDB();
        while (true) {
            try {
                Scanner sc = new Scanner(System.in);
                System.out.println( ansi().eraseScreen().render("@|red *************|@ @|green 22306购票系统|@ @|red **************|@"));
                System.out.println( ansi().eraseScreen().render("@|blue 1、登录           2、注册          3、退出|@"));
                System.out.print("请输入选项:");
                int choice = sc.nextInt();
                switch (choice) {
                    case 1: {
                        System.out.print("请输入用户名:");
                        usrname = sc.next();
                        System.out.print("请输入密码:");
                        password = sc.next();
                        //usrname = "13349076";
                        //password = "1234";
                        Verifing verify = new Verifing();
                        int isExit = verify.login(per, usrname, password);
                        nickname = "Not Found Nickname";
                        if (isExit > 0) {

                            nickname = per.findUserNick(usrname);
                            if (isExit == 1) {

                                System.out.println( ansi().eraseScreen().render("@|yellow 登录成功，欢迎|@ @|green " + nickname + "|@ ~\n"));
                                Boolean flag = true;
                                while (flag) {
                                    System.out.println( ansi().eraseScreen().render("@|red \n*************|@ @|green 22306功能界面|@ @|red **************|@"));
                                    System.out.println( ansi().eraseScreen().render("@|blue 1、车次查询           2、车站查询          3、站站查询|@"));
                                    System.out.println( ansi().eraseScreen().render("@|blue 4、购买车票           5、个人信息         |@ @|red 6、登出|@"));
                                    System.out.print("请输入选项:");
                                    int choice_1 = sc.nextInt();
                                    switch (choice_1) {
                                        case 1: {
                                            System.out.print("请输入车次:");
                                            trip_no = sc.next();
                                            //trip_no = "G381";
                                            per.getTripTimetablebyNo(trip_no, price);
                                            break;
                                        }

                                        case 2: {
                                            System.out.print("请输入车站名:");
                                            station = sc.next();
                                            //to-do:查询车站
                                            //station = "北京";
                                            per.getStationTimetable(station);
                                            break;
                                        }

                                        case 3: {
                                            System.out.print("请输入起点站名:");
                                            departure_station = sc.next();
                                            System.out.print("请输入终点站名:");
                                            terminal_station = sc.next();

                                            //departure_station = "北京";
                                            //terminal_station = "沈阳";

                                            if (!per.getStationAndStation(departure_station, terminal_station, price)) {
                                                System.out.println("并没发现什么车票!");
                                            }

                                            break;
                                        }

                                        case 4: {
                                            System.out.print("请输入起点站名:");
                                            departure_station = sc.next();
                                            System.out.print("请输入终点站名:");
                                            terminal_station = sc.next();

                                            //departure_station = "北京";
                                            //terminal_station = "沈阳";

                                            if (!per.getStationAndStation(departure_station, terminal_station, price)) {
                                                System.out.println("No 并没发现什么车票 fu!");
                                                break;
                                            }
                                            //to-do:判断在这区间是否有车次
                                            Boolean have = true;
                                            if (have) {
                                                System.out.println(ansi().eraseScreen().render("@|red 查询余票数量|@"));
                                                System.out.print("请输入车次:");
                                                trip_no = sc.next();

                                                System.out.print("请输入乘车日期:");
                                                date = sc.next();

                                                remaining_count = 0;
                                                //trip_no = "D1";
                                                //date = "2016-01-01";

                                                int r_tmp = per.getRemainingAmount(trip_no, departure_station, terminal_station, date);

                                                System.out.println(ansi().eraseScreen().render(" 此区间内有车次@|red " + trip_no +
                                                        "|@，发车时间为@|green " + date + "|@，余票@|yellow " + r_tmp  +"|@张。"));

                                                if (r_tmp == 0) {
                                                    System.out.println(ansi().eraseScreen().render("没有票\uD83D\uDE02"));
                                                    break;
                                                }
                                                System.out.print("请输入购票数量: ");
                                                amount = sc.nextInt();
                                                //to-do:添加购票信息(purchasing
                                                //amount = 10;
                                                per.Purchasing(usrname, trip_no, date, departure_station, terminal_station, amount);
                                                //System.out.println("购买成功！\n");
                                            }
                                            else {
                                                System.out.println("此区间内尚无车次！\n");
                                            }
                                            break;
                                        }

                                        case 5: {
                                            System.out.println("*******" + nickname + "的个人信息******");
                                            per.getUserInfo(usrname);
                                            //to-do:查询购票信息
                                            per.getPurchaingInfo(usrname);
                                            //System.out.println("所购车次: " + trip_no + "     发车时间：" + date + "     车票数量：" + amount);
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
                                System.out.println( ansi().eraseScreen().render("@|yellow 登录成功，欢迎管理员 |@ @|green " + nickname + "|@ ~\n"));
                                //System.out.println("登录成功，欢迎管理员" + nickname + "~\n");
                                Boolean flag = true;
                                while (flag) {
                                    System.out.println("********************22306管理界面**************************");
                                    System.out.println("1、添加车票信息           2、添加车站          3、添加车次");
                                    System.out.println("4、显示所有车次的时刻表    5、显示所有车站       6、显示所有用户信息");
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
                                                System.out.println("您输入的车站不在时刻表中，请新增该车次的时刻表。\n");
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



                        Boolean succeed = per.insertNewUser(usrname, nickname, password, age, sex);
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

        }
    }
}
