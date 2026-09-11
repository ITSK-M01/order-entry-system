package jp.co.sample.orderentry.entity;

import java.util.Date;

/**
 * 受注管理テーブル（ORDER_MANAGEMENT）1レコード分を表すEntity。
 * 画面表示用に、各マスタと結合した名称項目も併せて保持する。
 */
public class OrderInfo {

    private String orderId;       // 受注ID
    private String clientId;      // 発注主体（取引先ID）
    private String clientName;    // 取引先名（結合取得。画面表示用）
    private Date orderDate;       // 発注日
    private String modelId;       // 機種ID
    private String modelName;     // 機種名（結合取得。画面表示用）
    private int quantity;         // 台数
    private Date dueDate;         // 期日
    private String status;        // ステータス
    private String employeeId;    // 担当者（従業員ID）
    private String employeeName;  // 担当者氏名（結合取得。画面表示用）

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
