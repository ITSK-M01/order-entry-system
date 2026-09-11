package jp.co.sample.orderentry.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.sample.orderentry.dao.ClientDao;
import jp.co.sample.orderentry.dao.OrderDao;
import jp.co.sample.orderentry.entity.Client;
import jp.co.sample.orderentry.entity.OrderInfo;

/**
 * 受注管理画面（一覧表示・絞り込み）を表示するServlet。
 * URL: /order
 * パラメータ: status（任意。未指定 or 空文字なら全件表示）
 * パラメータ: clientId（任意。未指定 or 空文字なら全件表示）
 */
@WebServlet("/order")
public class OrderServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final OrderDao orderDao = new OrderDao();
    
    private final ClientDao clientDao = new ClientDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	// 検索条件を取得
        String status = request.getParameter("status");
        String clientId = request.getParameter("clientId");

        List<OrderInfo> orderList;
		try {
			// 検索条件に一致する受注情報を取得
			orderList = orderDao.findByCondition(status, clientId);
			
			//取引先一覧を取得
			List<Client> clientList = clientDao.findAll();
			
			// JSPに受注情報と取引先一覧を渡す
			request.setAttribute("orderList", orderList);
			request.setAttribute("clientList", clientList);
			
		} catch (SQLException e) {
			
			// DBアクセスエラー時はエラーメッセージを設定
			request.setAttribute( "errorMessage", "受注情報の取得に失敗しました。" + e.getMessage());
		}
		
		// 画面に検索条件を保持するためリクエストスコープ(データを格納)に設定
        request.setAttribute("status", status);
        request.setAttribute("clientId", clientId);
        
     // 受注一覧画面へ遷移
        request.getRequestDispatcher("/WEB-INF/jsp/order.jsp").forward(request, response);
    }
}
