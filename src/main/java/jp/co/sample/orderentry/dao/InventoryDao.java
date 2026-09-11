package jp.co.sample.orderentry.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.co.sample.orderentry.common.DBUtil;
import jp.co.sample.orderentry.entity.Inventory;

/**
 * 在庫テーブル（INVENTORY）に対するDAO。
 * 画面表示用に、機種マスタ（MODEL_MASTER）と結合して取得する。
 */
public class InventoryDao {

    /** 在庫一覧を機種ID順に取得（機種名も併せて取得） */
    public List<Inventory> findAll() throws SQLException {
        String sql =
            "SELECT I.MODEL_ID, M.MODEL_NAME, I.QUANTITY " +
            "  FROM INVENTORY I " +
            "  JOIN MODEL_MASTER M ON I.MODEL_ID = M.MODEL_ID " +
            " ORDER BY I.MODEL_ID";
        List<Inventory> list = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Inventory inventory = new Inventory();
                inventory.setModelId(rs.getString("MODEL_ID"));
                inventory.setModelName(rs.getString("MODEL_NAME"));
                inventory.setQuantity(rs.getInt("QUANTITY"));
                list.add(inventory);
            }
        }
        return list;
    }

    /** 在庫数を更新する（在庫の増減処理などで利用する想定） */
    public void updateQuantity(String modelId, int quantity) throws SQLException {
        String sql = "UPDATE INVENTORY SET QUANTITY = ?, UPDATED_AT = SYSDATE WHERE MODEL_ID = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setString(2, modelId);
            ps.executeUpdate();
        }
    }
}
