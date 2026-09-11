package jp.co.sample.orderentry.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.sample.orderentry.common.CsvUtil;
import jp.co.sample.orderentry.dao.InventoryDao;
import jp.co.sample.orderentry.entity.Inventory;

/**
 * 在庫一覧をCSVファイルとしてダウンロードさせるServlet。
 * URL: /inventory/csv
 *
 * 文字コードはExcelでそのまま開けるよう Windows-31J（MS932）を使用する。
 */
@WebServlet("/inventory/csv")
public class InventoryCsvServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String CHARSET = "Windows-31J";

    private final InventoryDao inventoryDao = new InventoryDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/csv; charset=" + CHARSET);
        response.setCharacterEncoding(CHARSET);
        response.setHeader("Content-Disposition", "attachment; filename=\"inventory.csv\"");

        try (PrintWriter writer = response.getWriter()) {
            // ヘッダー行
            writer.write(CsvUtil.toLine("機種ID", "機種名", "在庫台数"));

            List<Inventory> inventoryList;
            try {
                inventoryList = inventoryDao.findAll();
            } catch (SQLException e) {
                throw new ServletException("在庫情報の取得に失敗しました。", e);
            }

            for (Inventory inventory : inventoryList) {
                writer.write(CsvUtil.toLine(
                        inventory.getModelId(),
                        inventory.getModelName(),
                        inventory.getQuantity()));
            }
        }
    }
}
