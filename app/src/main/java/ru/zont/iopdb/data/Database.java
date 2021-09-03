package ru.zont.iopdb.data;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database implements Closeable {
    private static final String url = "jdbc:mariadb://185.189.255.57:3306/iopdb?user=iopdb-client&password=228322";

    private static Database instance;

    static {
        try {
            DriverManager.registerDriver(new org.mariadb.jdbc.Driver());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static Database inst() {
        if (instance == null)
            instance = new Database();
        return instance;
    }

    private final Connection connection;

    private Database() {
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public Statement getStatement() throws SQLException {
        return connection.createStatement();
    }

    @Override
    public void close() throws IOException {
        try {
            connection.close();
        } catch (SQLException ignored) {
        } finally {
            instance = null;
        }
    }
}
