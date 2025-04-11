package com.example.service;

import com.example.model.EmployeeDetails;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/employeeDATABASE";
    private static final String JDBC_USER = "root"; // Change if needed
    private static final String JDBC_PASS = "manager"; // Change if needed

    public static void addEmployee(String name, double salary, String address) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
            //PreparedStatement stmt = conn.prepareStatement("INSERT INTO EmployeeDetails (Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
//            stmt.setString(1, name);
//            stmt.executeUpdate();
//
//            ResultSet rs = stmt.getGeneratedKeys();
//            int empId = -1;
//            if (rs.next()) {
//                empId = rs.getInt(1);
//            }

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO EmployeeDetails (Name, Salary, Address) VALUES (?, ?, ?)");
            stmt.setString(1, name);
            stmt.setDouble(2, salary);
            stmt.setString(3, address);
            stmt.executeUpdate();

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<EmployeeDetails> getAllEmployees() {
        List<EmployeeDetails> employeeList = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
            PreparedStatement stmt = conn.prepareStatement("Select * from EmployeeDetails");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                EmployeeDetails empDetails = new EmployeeDetails(
                        rs.getInt("Employee_ID"),
                        rs.getString("Name"),
                        rs.getDouble("Salary"),
                        rs.getString("Address")
                );
                
                employeeList.add(empDetails);
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return employeeList;
    }
}
