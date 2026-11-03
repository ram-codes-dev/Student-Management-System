package com.sms.student_management;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) throws Exception {
        try (Connection c = Db.get()) {
            System.out.println("Database is connected!");
        }
    }
}