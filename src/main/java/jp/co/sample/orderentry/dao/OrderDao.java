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
public class OrderDao {

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
     * 検索条件に一致する受注情報を取得する。
     * 
     * @param status 受注ステータス
     * @param clientId 受注ステータス 
     * @return 受注情報一覧
     * @throws SQLException データベースアクセスエラー
     * status または clientId が null または空文字の場合は全件取得する。
     * ※ この検索条件を「取引先」や「担当者」でも絞れるように拡張するのは
     * 　 変更案件の練習課題として想定している箇所。
     */
    
    public List<OrderInfo> findByCondition(
    		String status, 
    		String clientId)
    				throws SQLException {
    	
    	// 検索条件に応じてSQLを動的に構築する
        StringBuilder sql = 
        		new StringBuilder(BASE_SELECT);
        
        // 検索条件の入力有無を判定
        boolean hasStatus = status != null && !status.isEmpty();
        boolean hasClientId = clientId != null && !clientId.isEmpty();
        
        // 動的に検索条件を追加するための固定条件
        sql.append(" WHERE 1 = 1 ");
        
        // ステータスが指定されている場合のみ条件を追加
        if (hasStatus) {
        	sql.append(" AND O.STATUS = ? ");
        }
        
        // 取引先IDが指定されている場合のみ条件を追加
        if (hasClientId) {
        	sql.append(" AND O.CLIENT_ID = ? ");
        	
        }
        
        // 受注日降順、受注ID昇順で並び替え
        sql.append(" ORDER BY O.ORDER_DATE DESC, O.ORDER_ID");

        List<OrderInfo> list = new ArrayList<>();
        
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
        	
        	int index = 1;
        	
        	// ステータス条件を設定
            if (hasStatus) {
                ps.setString(index++, status);
            }
            
            // 取引先ID条件を設定
            if (hasClientId) {
            	ps.setString(index++, clientId);
            }
            
            // SQLを実行し、検索結果をOrderInfoへ変換
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(toOrderInfo(rs));
                }
            }
        }
        
        // 検索結果を返却
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
