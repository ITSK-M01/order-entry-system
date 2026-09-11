package jp.co.sample.orderentry.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.co.sample.orderentry.common.DBUtil;
import jp.co.sample.orderentry.entity.OrderInfo;

/**
 * 受注管理テーブル（ORDER_MANAGEMENT）に対するDAO。
 * 画面表示用に、取引先マスタ・機種マスタ・従業員マスタと結合して取得する。
 */
public class OrderDao_bk202609 {

    private static final String BASE_SELECT =
        "SELECT O.ORDER_ID, O.CLIENT_ID, C.CLIENT_NAME, O.ORDER_DATE, " +
        "       O.MODEL_ID, M.MODEL_NAME, O.QUANTITY, O.DUE_DATE, O.STATUS, " +
        "       O.EMPLOYEE_ID, E.EMPLOYEE_NAME " +
        "  FROM ORDER_MANAGEMENT O " +
        "  JOIN CLIENT_MASTER C ON O.CLIENT_ID = C.CLIENT_ID " +
        "  JOIN MODEL_MASTER M ON O.MODEL_ID = M.MODEL_ID " +
        "  LEFT JOIN EMPLOYEE_MASTER E ON O.EMPLOYEE_ID = E.EMPLOYEE_ID ";

    /** 受注一覧を発注日の降順（新しい順）で全件取得 */
    public List<OrderInfo> findAll() throws SQLException {
        String sql = BASE_SELECT + " ORDER BY O.ORDER_DATE DESC, O.ORDER_ID";
        List<OrderInfo> list = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(toOrderInfo(rs));
            }
        }
        return list;
    }

    /**
     * ステータス指定で絞り込み検索する。
     * status が null または空文字の場合は全件を返す。
     *
     * ※ この検索条件を「取引先」や「担当者」でも絞れるように拡張するのは
     * 　 変更案件の練習課題として想定している箇所。
     */
    public List<OrderInfo> findByStatus(String status) throws SQLException {
        StringBuilder sql = new StringBuilder(BASE_SELECT);
        boolean hasCondition = status != null && !status.isEmpty();
        if (hasCondition) {
            sql.append(" WHERE O.STATUS = ? ");
        }
        sql.append(" ORDER BY O.ORDER_DATE DESC, O.ORDER_ID");

        List<OrderInfo> list = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            if (hasCondition) {
                ps.setString(1, status);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(toOrderInfo(rs));
                }
            }
        }
        return list;
    }

    private OrderInfo toOrderInfo(ResultSet rs) throws SQLException {
        OrderInfo info = new OrderInfo();
        info.setOrderId(rs.getString("ORDER_ID"));
        info.setClientId(rs.getString("CLIENT_ID"));
        info.setClientName(rs.getString("CLIENT_NAME"));
        info.setOrderDate(rs.getDate("ORDER_DATE"));
        info.setModelId(rs.getString("MODEL_ID"));
        info.setModelName(rs.getString("MODEL_NAME"));
        info.setQuantity(rs.getInt("QUANTITY"));
        info.setDueDate(rs.getDate("DUE_DATE"));
        info.setStatus(rs.getString("STATUS"));
        info.setEmployeeId(rs.getString("EMPLOYEE_ID"));
        info.setEmployeeName(rs.getString("EMPLOYEE_NAME"));
        return info;
    }
}
