package com.sms.student_management;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class delete {

    public static boolean deletestu(long id) {

        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection con = Db.get();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}	