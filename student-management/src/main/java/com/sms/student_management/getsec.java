
package com.sms.student_management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class getsec{

    public static List<dbgetsec> getAllSections() {

        List<dbgetsec> sections = new ArrayList<>();

        String sql = "SELECT id, class_name, section_name "
                   + "FROM class_sections "
                   + "WHERE status = 'ACTIVE' "
                   + "ORDER BY class_name, section_name";

        try (Connection con = Db.get();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                dbgetsec section = new dbgetsec();

                section.id = rs.getInt("id");
                section.class_name = rs.getString("class_name");
                section.section_name = rs.getString("section_name");

                sections.add(section);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sections;
    }
}
