package net.fernandosalas.ems.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * One-time migration runner for migrate_user_fk.sql. Run:
 * mvn -q compile exec:java -Dexec.mainClass=net.fernandosalas.ems.util.MigrateUserFk
 */
public class MigrateUserFk {

    private static final String URL =
            "jdbc:mysql://localhost:3306/studentmanagementsystem"
                    + "?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Path sqlPath = Path.of("src/main/resources/db/migrate_user_fk.sql");
        if (!Files.exists(sqlPath)) {
            sqlPath = Path.of("ems-backend/src/main/resources/db/migrate_user_fk.sql");
        }
        List<String> statements = parseStatements(Files.readString(sqlPath));

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false);
            try (Statement stmt = conn.createStatement()) {
                for (String sql : statements) {
                    if (sql.isBlank()) {
                        continue;
                    }
                    try {
                        if (sql.trim().toUpperCase().startsWith("SELECT")
                                || sql.trim().toUpperCase().startsWith("DESCRIBE")) {
                            continue;
                        }
                        System.out.println("Executing: " + sql.trim().replaceAll("\\s+", " "));
                        stmt.execute(sql);
                    } catch (SQLException ex) {
                        if (isIgnorableDropError(ex)) {
                            System.out.println("Skipped (column may already be dropped): "
                                    + ex.getMessage());
                        } else if (isIgnorableBackfillError(ex)) {
                            System.out.println("Skipped backfill (legacy columns may be gone): "
                                    + ex.getMessage());
                        } else {
                            throw ex;
                        }
                    }
                }
                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }

            describeUsers(conn);
        }
    }

    private static boolean isIgnorableDropError(SQLException ex) {
        String msg = ex.getMessage().toLowerCase();
        return msg.contains("check that column/key exists")
                || msg.contains("can't drop")
                || msg.contains("unknown column");
    }

    private static boolean isIgnorableBackfillError(SQLException ex) {
        String msg = ex.getMessage().toLowerCase();
        return msg.contains("unknown column 'u.student_id'")
                || msg.contains("unknown column 'u.department_id'");
    }

    private static void describeUsers(Connection conn) throws SQLException {
        System.out.println("\nusers table columns:");
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("DESCRIBE users")) {
            while (rs.next()) {
                System.out.println("  - " + rs.getString("Field") + " (" + rs.getString("Type") + ")");
            }
        }
    }

    private static List<String> parseStatements(String content) {
        List<String> statements = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        for (String line : content.split("\n")) {
            String trimmed = line.trim();
            if (trimmed.startsWith("--") || trimmed.isEmpty()) {
                continue;
            }
            current.append(line).append('\n');
            if (trimmed.endsWith(";")) {
                statements.add(current.toString().trim());
                current.setLength(0);
            }
        }
        if (!current.isEmpty()) {
            statements.add(current.toString().trim());
        }
        return statements;
    }
}
