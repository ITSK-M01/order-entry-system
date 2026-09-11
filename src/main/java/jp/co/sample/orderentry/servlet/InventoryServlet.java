package jp.co.sample.orderentry.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.sample.orderentry.dao.InventoryDao;
import jp.co.sample.orderentry.entity.Inventory;

/**
 * 在庫画面（一覧表示）を表示するServlet。
 * URL: /inventory
 */
@WebServlet("/inventory")
public class InventoryServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final InventoryDao inventoryDao = new InventoryDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Inventory> inventoryList = inventoryDao.findAll();
            request.setAttribute("inventoryList", inventoryList);
        } catch (SQLException e) {
            // 本来はロギング処理を行う。サンプルのため画面にエラーメッセージのみ表示する。
            request.setAttribute("errorMessage", "在庫情報の取得に失敗しました。" + e.getMessage());
        }
        request.getRequestDispatcher("/WEB-INF/jsp/inventory.jsp").forward(request, response);
    }
}
