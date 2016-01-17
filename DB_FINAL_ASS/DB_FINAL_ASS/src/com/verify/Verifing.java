package com.verify;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.db.DBManager;

public class Verifing {

    public int login(DBManager sql, String username, String password) {

        // 获取Sql查询语句
        String logSql = "select * from user where user_name ='" + username
                + "' and password ='" + password + "'";

        // 获取DB对象
        //DBManager sql = DBManager.createInstance();
        //sql.connectDB();

        // 操作DB对象
        try {
            ResultSet rs = sql.executeQuery(logSql);
            if (rs.next()) {
                Boolean is = rs.getBoolean("isAdmin");
                if (is) {
                    return 2;
                }
                return 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Boolean register(DBManager sql, String username, String password) {

        // 获取Sql查询语句
        String regSql = "insert into student values('"+ username+ "','"+ password+ "') ";

        // 获取DB对象
        //DBManager sql = DBManager.createInstance();
        //sql.connectDB();

        int ret = sql.executeUpdate(regSql);
        if (ret != 0) {
            //sql.closeDB();
            return true;
        }
        //sql.closeDB();

        return false;
    }
}