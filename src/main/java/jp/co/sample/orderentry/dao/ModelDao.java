package jp.co.sample.orderentry.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.co.sample.orderentry.common.DBUtil;
import jp.co.sample.orderentry.entity.Model;

/**
 * 機種マスタ（MODEL_MASTER）に対するDAO。
 * 管理コマンドから呼び出され、一覧・追加・更新・削除を行う。
 */
public class ModelDao {

    /** 全件取得（機種ID昇順） */
    public List<Model> findAll() throws SQLException {
        String sql = "SELECT MODEL_ID, MODEL_NAME, MODEL_INFO FROM MODEL_MASTER ORDER BY MODEL_ID";
        List<Model> list = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(toModel(rs));
            }
        }
        return list;
    }

    /** 主キー指定で1件取得。存在しない場合はnull */
    public Model findById(String modelId) throws SQLException {
        String sql = "SELECT MODEL_ID, MODEL_NAME, MODEL_INFO FROM MODEL_MASTER WHERE MODEL_ID = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, modelId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return toModel(rs);
                }
                return null;
            }
        }
    }

    /** 新規追加 */
    public void insert(Model model) throws SQLException {
        String sql = "INSERT INTO MODEL_MASTER (MODEL_ID, MODEL_NAME, MODEL_INFO) VALUES (?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, model.getModelId());
            ps.setString(2, model.getModelName());
            ps.setString(3, model.getModelInfo());
            ps.executeUpdate();
        }
    }

    /** 更新（UPDATED_ATも合わせて更新） */
    public void update(Model model) throws SQLException {
        String sql = "UPDATE MODEL_MASTER SET MODEL_NAME = ?, MODEL_INFO = ?, UPDATED_AT = SYSDATE WHERE MODEL_ID = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, model.getModelName());
            ps.setString(2, model.getModelInfo());
            ps.setString(3, model.getModelId());
            ps.executeUpdate();
        }
    }

    /** 削除 */
    public void delete(String modelId) throws SQLException {
        String sql = "DELETE FROM MODEL_MASTER WHERE MODEL_ID = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, modelId);
            ps.executeUpdate();
        }
    }

    private Model toModel(ResultSet rs) throws SQLException {
        Model model = new Model();
        model.setModelId(rs.getString("MODEL_ID"));
        model.setModelName(rs.getString("MODEL_NAME"));
        model.setModelInfo(rs.getString("MODEL_INFO"));
        return model;
    }
}
