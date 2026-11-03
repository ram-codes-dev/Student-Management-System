
package com.sms.student_management;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.mindrot.jbcrypt.BCrypt;

public class registeruser {

    public static boolean createUser(String email, String password) {

        String sql = "INSERT INTO users "
                   + "(email, password_hash, role, status) "
                   + "VALUES (?, ?, 'USER', 'ACTIVE')";

        try (Connection con = Db.get();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String hashedPassword =
                    BCrypt.hashpw(password, BCrypt.gensalt(10));

            ps.setString(1, email);
            ps.setString(2, hashedPassword);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
