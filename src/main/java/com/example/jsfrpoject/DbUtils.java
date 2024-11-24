package com.example.jsfrpoject;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DbUtils {

    private static String url;
    private static String username;
    private static String password;

    static {
        // Загружаем параметры из properties файла
        Properties properties = new Properties();
        try (InputStream input = DbUtils.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                System.out.println("Не удалось найти файл db.properties");
            }
            properties.load(input);

            // Инициализируем параметры подключения
            url = properties.getProperty("db.url");
            username = properties.getProperty("db.username");
            password = properties.getProperty("db.password");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public static User getUserByLogin(String login) {
        String sql = "SELECT * FROM users WHERE login = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String password = rs.getString("password");
                String role = rs.getString("role");
                return new User(login, password, role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
