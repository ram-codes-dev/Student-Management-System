
package com.sms.student_management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.mindrot.jbcrypt.BCrypt;

public class loginpage {

    public static logininfo login(newlogin request) {

        logininfo result = new logininfo();

        if (request == null ||
            request.email == null ||
            request.password == null ||
            request.email.isBlank() ||
            request.password.isEmpty()) {

            result.success = false;
            result.message = "Email and password are required";
            return result;
        }

        String sql = "SELECT password_hash FROM users "
                   + "WHERE email = ? AND status = 'ACTIVE'";

        try (Connection con = Db.get();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, request.email.trim());

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    String storedHash = rs.getString("password_hash");

                    if (BCrypt.checkpw(request.password, storedHash)) {

                        result.success = true;
                        result.message = "Login successful";

                    } else {

                        result.success = false;
                        result.message = "Invalid email or password";

                    }

                } else {

                    result.success = false;
                    result.message = "Invalid email or password";

                }
            }

        } catch (Exception e) {

            result.success = false;
            result.message = "Login service temporarily unavailable";
            e.printStackTrace();

        }

        return result;
    }
}


