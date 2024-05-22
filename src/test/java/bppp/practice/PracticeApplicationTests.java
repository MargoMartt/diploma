package bppp.practice;

import org.springframework.boot.test.context.SpringBootTest;
import java.sql.Connection;
import java.sql.DriverManager;

@SpringBootTest
class PracticeApplicationTests {
        public static void main(String[] args) {
            String jdbcUrl = "jdbc:mysql://localhost:3306/practice?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            String username = "root";
            String password = "Rita1234";

            try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
                System.out.println("Connection successful!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
