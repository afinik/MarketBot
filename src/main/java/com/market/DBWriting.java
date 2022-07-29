package com.market;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.market.Constants.DB_NAME;
import static java.sql.DriverManager.getConnection;

public class DBWriting {
    private static String USER = Constants.USER;
    private static String PASS = Constants.PASS;

    public static void main(String[] args) {
        String[] strings = sendQuery("select messagetype, ':'  from messages where telegramId = 1114036;").split(":");
        for (int i = 0; i < strings.length; i++) {
            System.out.println(strings[i]);
        }
    }

    protected static void createDBenviroment() {
        sendQuery("create table if not exists messages(id SERIAL PRIMARY KEY, telegramid int8, " +
                "date timestamptz NOT NULL DEFAULT now(),  messagetype text)");
        sendQuery("create table if not exists items(id SERIAL PRIMARY KEY, telegramid int8, " +
                "date timestamptz NOT NULL DEFAULT now(),  item text)");
    }

    protected static String sendQuery(String query) {
        String s = "";
        String url = "jdbc:postgresql://" + Constants.HOST + ":" + Constants.PORT +
                "/" + DB_NAME;
        try (Connection conn = getConnection(url, USER, PASS);
             PreparedStatement pst = conn.prepareStatement(query)) {
            boolean isResult = pst.execute();
            do {
                try (ResultSet rs = pst.getResultSet()) {
                    if (rs != null)
                        while (rs.next()) {
                            for (int i = 1; i <= pst.getResultSet().getMetaData().getColumnCount(); i++) {
                                s = s + rs.getString(i);
                            }
                        }
                    isResult = pst.getMoreResults();
                }
            } while (isResult);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return s;
    }
}
