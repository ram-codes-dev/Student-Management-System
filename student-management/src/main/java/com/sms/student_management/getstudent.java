package com.sms.student_management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class getstudent {


    public static List<dbgetstudentdet> getAllStudents() {

        List<dbgetstudentdet> students = new ArrayList<>();

        String sql = "SELECT * FROM students ORDER BY id DESC";

        try (Connection con = Db.get();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                dbgetstudentdet student = new dbgetstudentdet();

                student.id = rs.getLong("id");
                student.student_code = rs.getString("student_code");
                student.name = rs.getString("name");
                student.email = rs.getString("email");
                student.phone = rs.getString("phone");
                student.dob = rs.getDate("dob") != null
                        ? rs.getDate("dob").toString() : "";

                student.admission_date = rs.getDate("admission_date") != null
                        ? rs.getDate("admission_date").toString() : "";
                student.class_section_id = rs.getInt("class_section_id");
                student.status = rs.getString("status");

                students.add(student);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return students;
    }
}