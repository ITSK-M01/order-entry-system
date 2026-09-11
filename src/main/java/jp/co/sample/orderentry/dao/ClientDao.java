package jp.co.sample.orderentry.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.co.sample.orderentry.common.DBUtil;
import jp.co.sample.orderentry.entity.Client;

/**
 * 取引先マスタ（CLIENT_MASTER）に対するDAO。
 */
public class ClientDao {

    public List<Client> findAll() throws SQLException {
        String sql = "SELECT CLIENT_ID, CLIENT_NAME, CLIENT_CONTACT FROM CLIENT_MASTER ORDER BY CLIENT_ID";
        List<Client> list = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(toClient(rs));
            }
        }
        return list;
    }

    public Client findById(String clientId) throws SQLException {
        String sql = "SELECT CLIENT_ID, CLIENT_NAME, CLIENT_CONTACT FROM CLIENT_MASTER WHERE CLIENT_ID = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, clientId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return toClient(rs);
                }
                return null;
            }
        }
    }

    public void insert(Client client) throws SQLException {
        String sql = "INSERT INTO CLIENT_MASTER (CLIENT_ID, CLIENT_NAME, CLIENT_CONTACT) VALUES (?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, client.getClientId());
            ps.setString(2, client.getClientName());
            ps.setString(3, client.getClientContact());
            ps.executeUpdate();
        }
    }

    public void update(Client client) throws SQLException {
        String sql = "UPDATE CLIENT_MASTER SET CLIENT_NAME = ?, CLIENT_CONTACT = ?, UPDATED_AT = SYSDATE WHERE CLIENT_ID = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, client.getClientName());
            ps.setString(2, client.getClientContact());
            ps.setString(3, client.getClientId());
            ps.executeUpdate();
        }
    }

    public void delete(String clientId) throws SQLException {
        String sql = "DELETE FROM CLIENT_MASTER WHERE CLIENT_ID = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, clientId);
            ps.executeUpdate();
        }
    }

    private Client toClient(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setClientId(rs.getString("CLIENT_ID"));
        client.setClientName(rs.getString("CLIENT_NAME"));
        client.setClientContact(rs.getString("CLIENT_CONTACT"));
        return client;
    }
}
