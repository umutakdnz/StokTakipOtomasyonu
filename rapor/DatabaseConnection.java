package rapor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/stok_takip";
    private static final String USER = "root"; // MySQL kullanıcı adın
    private static final String PASSWORD = "1234"; // MySQL şifren

    public static Connection connect() {
        Connection connection = null;
        try {
            // JDBC sürücüsünü yükle
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Veritabanına bağlantı başarılı!");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver bulunamadı! " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Veritabanı bağlantısı başarısız! " + e.getMessage());
        }
        return connection;
    }
}
