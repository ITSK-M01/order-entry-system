<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>受注管理画面 - 受発注管理システム</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header>
    <h1>受発注管理システム</h1>
</header>
<nav>
    <a href="${pageContext.request.contextPath}/inventory">在庫画面</a>
    <a href="${pageContext.request.contextPath}/order">受注管理画面</a>
</nav>
<main>
    <h2>受注一覧</h2>

    <c:if test="${not empty errorMessage}">
        <p class="error">${errorMessage}</p>
    </c:if>

    <form method="get" action="${pageContext.request.contextPath}/order" class="toolbar">
        ステータス絞り込み：
        <select name="status" onchange="this.form.submit()">
            <option value="" ${empty status ? 'selected' : ''}>すべて</option>
            <option value="未処理" ${status == '未処理' ? 'selected' : ''}>未処理</option>
            <option value="手配中" ${status == '手配中' ? 'selected' : ''}>手配中</option>
            <option value="出荷済" ${status == '出荷済' ? 'selected' : ''}>出荷済</option>
            <option value="完了" ${status == '完了' ? 'selected' : ''}>完了</option>
            <option value="キャンセル" ${status == 'キャンセル' ? 'selected' : ''}>キャンセル</option>
        </select>
		
		取引先：
		<select name="clientId" onchange="this.form.submit()">
		
			<option value="">すべて</option>
		
			<c:forEach var="client" items="${clientList}">
				<option value="${client.clientId}"
				${clientId eq client.clientId ? 'selected' : ''}>
				${client.clientName}
				</option>
			</c:forEach>
		
		</select>
		
        <noscript><input type="submit" value="絞り込み"></noscript>
        <a class="btn" href="${pageContext.request.contextPath}/order/csv?status=${status}&clientId=${clientId}">CSV出力</a>
    </form>

    <table>
        <thead>
        <tr>
            <th>受注ID</th>
            <th>取引先</th>
            <th>発注日</th>
            <th>機種</th>
            <th>台数</th>
            <th>期日</th>
            <th>ステータス</th>
            <th>担当者</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="order" items="${orderList}">
            <tr>
                <td>${order.orderId}</td>
                <td>${order.clientName}</td>
                <td><fmt:formatDate value="${order.orderDate}" pattern="yyyy/MM/dd"/></td>
                <td>${order.modelName}</td>
                <td>${order.quantity}</td>
                <td><fmt:formatDate value="${order.dueDate}" pattern="yyyy/MM/dd"/></td>
                <td>${order.status}</td>
                <td>${order.employeeName}</td>
            </tr>
        </c:forEach>
        <c:if test="${empty orderList}">
            <tr>
                <td colspan="8">該当する受注データがありません。</td>
            </tr>
        </c:if>
        </tbody>
    </table>
</main>
</body>
</html>
