package com.sms.student_management;


import java.sql.Connection;
import java.sql.PreparedStatement;

public class update {
    public static boolean updateStudent(dbgetstudentdet student) {

        String sql = "UPDATE students SET "
                + "student_code = ?, "
                + "name = ?, "
                + "email = ?, "
                + "phone = ?, "
                + "dob = ?, "
                + "admission_date = ?, "
                + "class_section_id = ?, "
                + "status = ? "
                + "WHERE id = ?";

        try (Connection con = Db.get();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, student.student_code);
            ps.setString(2, student.name);
            ps.setString(3, student.email);
            ps.setString(4, student.phone);
            ps.setDate(5, java.sql.Date.valueOf(student.dob));
            ps.setDate(6, java.sql.Date.valueOf(student.admission_date));
            ps.setInt(7, student.class_section_id);
            ps.setString(8, student.status);
            ps.setLong(9, student.id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}


