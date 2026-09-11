package jp.co.sample.orderentry.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.co.sample.orderentry.common.DBUtil;
import jp.co.sample.orderentry.entity.Employee;

/**
 * 従業員マスタ（EMPLOYEE_MASTER）に対するDAO。
 */
public class EmployeeDao {

    public List<Employee> findAll() throws SQLException {
        String sql = "SELECT EMPLOYEE_ID, EMPLOYEE_NAME, HIRE_YEAR, DEPARTMENT FROM EMPLOYEE_MASTER ORDER BY EMPLOYEE_ID";
        List<Employee> list = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(toEmployee(rs));
            }
        }
        return list;
    }

    public Employee findById(String employeeId) throws SQLException {
        String sql = "SELECT EMPLOYEE_ID, EMPLOYEE_NAME, HIRE_YEAR, DEPARTMENT FROM EMPLOYEE_MASTER WHERE EMPLOYEE_ID = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return toEmployee(rs);
                }
                return null;
            }
        }
    }

    public void insert(Employee employee) throws SQLException {
        String sql = "INSERT INTO EMPLOYEE_MASTER (EMPLOYEE_ID, EMPLOYEE_NAME, HIRE_YEAR, DEPARTMENT) VALUES (?, ?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, employee.getEmployeeId());
            ps.setString(2, employee.getEmployeeName());
            ps.setInt(3, employee.getHireYear());
            ps.setString(4, employee.getDepartment());
            ps.executeUpdate();
        }
    }

    public void update(Employee employee) throws SQLException {
        String sql = "UPDATE EMPLOYEE_MASTER SET EMPLOYEE_NAME = ?, HIRE_YEAR = ?, DEPARTMENT = ?, UPDATED_AT = SYSDATE WHERE EMPLOYEE_ID = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, employee.getEmployeeName());
            ps.setInt(2, employee.getHireYear());
            ps.setString(3, employee.getDepartment());
            ps.setString(4, employee.getEmployeeId());
            ps.executeUpdate();
        }
    }

    public void delete(String employeeId) throws SQLException {
        String sql = "DELETE FROM EMPLOYEE_MASTER WHERE EMPLOYEE_ID = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, employeeId);
            ps.executeUpdate();
        }
    }

    private Employee toEmployee(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        employee.setEmployeeId(rs.getString("EMPLOYEE_ID"));
        employee.setEmployeeName(rs.getString("EMPLOYEE_NAME"));
        int hireYear = rs.getInt("HIRE_YEAR");
        employee.setHireYear(rs.wasNull() ? null : hireYear);
        employee.setDepartment(rs.getString("DEPARTMENT"));
        return employee;
    }
}
