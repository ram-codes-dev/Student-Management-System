package com.sms.student_management;
import java.sql.Connection;
import java.sql.PreparedStatement;
public class addnewstudent {



    public static boolean addStudent(
          dbgetstudentdet student) {

        String sql = "INSERT INTO students "
            + "(student_code, name, email, phone, dob, "
                + "admission_date, class_section_id) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Db.get();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, student.student_code);
      ps.setString(2, student.name);
        ps.setString(3, student.email);
            ps.setString(4, student.phone);
            ps.setDate(5, java.sql.Date.valueOf(student.dob));
            ps.setDate(6, java.sql.Date.valueOf(student.admission_date));
            ps.setInt(7, student.class_section_id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;	
        }
    }
}
