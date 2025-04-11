package com.example.controller;

import com.example.model.EmployeeDetails;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

@WebServlet("/EmployeeServlet")
public class EmployeeServlet extends HttpServlet {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/employeeDATABASE";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "manager";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action"); // 👈 Get action parameter (insert/update)

        String name = request.getParameter("name");
        String salaryParam = request.getParameter("salary");
        String address = request.getParameter("address");

        if (name == null || salaryParam == null || address == null) {
            response.getWriter().write("Error: Missing parameters.");
            return;
        }

        try {
            double salary = Double.parseDouble(salaryParam);
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);

            if ("update".equalsIgnoreCase(action)) {
                String employeeId = request.getParameter("id");

                if (employeeId == null || employeeId.trim().isEmpty()) {
                    response.getWriter().write("Error: Employee ID is required for update.");
                    return;
                }

                String sql = "UPDATE EmployeeDetails SET Name = ?, Salary = ?, Address = ? WHERE Employee_ID = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, name);
                stmt.setDouble(2, salary);
                stmt.setString(3, address);
                stmt.setInt(4, Integer.parseInt(employeeId));

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    response.getWriter().write("Employee details updated successfully.");
                } else {
                    response.getWriter().write("No employee found with ID: " + employeeId);
                }
                stmt.close();
            } else {
                // Default action: insert
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO EmployeeDetails (Name, Salary, Address) VALUES (?, ?, ?)");
                stmt.setString(1, name);
                stmt.setDouble(2, salary);
                stmt.setString(3, address);
                stmt.executeUpdate();
                stmt.close();

                response.getWriter().write("Employee Added Successfully");
            }

            conn.close();
        } catch (NumberFormatException e) {
            response.getWriter().write("Error: Invalid number format.");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Error: " + e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<EmployeeDetails> employeeList = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM EmployeeDetails");
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
            response.setContentType("application/json");
            response.getWriter().write(new Gson().toJson(employeeList));
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Error: " + e.getMessage());
        }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            response.getWriter().write("Error: Employee ID is required.");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);

            PreparedStatement stmt = conn.prepareStatement("DELETE FROM EmployeeDetails WHERE Employee_ID = ?");
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            conn.close();

            if (rowsAffected > 0) {
                response.getWriter().write("Employee Deleted Successfully");
            } else {
                response.getWriter().write("Error: Employee ID Not Found");
            }

        } catch (NumberFormatException e) {
            response.getWriter().write("Error: Invalid Employee ID format.");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Error: " + e.getMessage());
        }
    }
}
