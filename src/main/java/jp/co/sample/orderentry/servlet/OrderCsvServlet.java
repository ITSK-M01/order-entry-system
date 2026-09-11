package jp.co.sample.orderentry.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.sample.orderentry.common.CsvUtil;
import jp.co.sample.orderentry.dao.OrderDao;
import jp.co.sample.orderentry.entity.OrderInfo;

/**
 * 受注一覧をCSVファイルとしてダウンロードさせるServlet。
 * URL: /order/csv
 * パラメータ: status、clientId（任意。画面の絞り込み条件を引き継ぐ）
 */
@WebServlet("/order/csv")
public class OrderCsvServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String CHARSET = "Windows-31J";

    private final OrderDao orderDao = new OrderDao();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String status = request.getParameter("status");
        String clientId = request.getParameter("clientId");

        response.setContentType("text/csv; charset=" + CHARSET);
        response.setCharacterEncoding(CHARSET);
        response.setHeader("Content-Disposition", "attachment; filename=\"order.csv\"");

        try (PrintWriter writer = response.getWriter()) {
            writer.write(CsvUtil.toLine(
                    "受注ID",
                    "取引先名",
                    "発注日",
                    "機種名",
                    "台数",
                    "期日",
                    "ステータス",
                    "担当者名"));

            // 受注情報を取得
            List<OrderInfo> orderList;
            
            try {
                orderList = orderDao.findByCondition(status, clientId);
            } catch (SQLException e) {
                throw new ServletException("受注情報の取得に失敗しました。", e);
            }

            for (OrderInfo order : orderList) {
                writer.write(CsvUtil.toLine(
                        order.getOrderId(),
                        order.getClientName(),
                        formatDate(order.getOrderDate()),
                        order.getModelName(),
                        order.getQuantity(),
                        formatDate(order.getDueDate()),
                        order.getStatus(),
                        order.getEmployeeName()));
            }
        }
    }

    private String formatDate(java.util.Date date) {
        return date == null ? "" : dateFormat.format(date);
    }
}
