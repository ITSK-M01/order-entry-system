package jp.co.sample.orderentry.entity;

/**
 * 従業員マスタ（EMPLOYEE_MASTER）1レコード分を表すEntity。
 */
public class Employee {

    private String employeeId;   // 従業員ID
    private String employeeName; // 氏名
    private Integer hireYear;    // 入社年度
    private String department;  // 部署

    public Employee() {
    }

    public Employee(String employeeId, String employeeName, Integer hireYear, String department) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.hireYear = hireYear;
        this.department = department;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getHireYear() {
        return hireYear;
    }

    public void setHireYear(Integer hireYear) {
        this.hireYear = hireYear;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
