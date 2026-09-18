<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>在庫画面 - 受発注管理システム</title>
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
    <h2>在庫一覧</h2>

    <c:if test="${not empty errorMessage}">
        <p class="error">${errorMessage}</p>
    </c:if>

    <div class="toolbar">
        <a class="btn" href="${pageContext.request.contextPath}/inventory/csv">CSV出力</a>
    </div>

    <table>
        <thead>
        <tr>
            <th>機種ID</th>
            <th>機種名</th>
            <th>在庫台数</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="inventory" items="${inventoryList}">
			
		    <%-- 在庫台数が１０台未満の場合、行を赤字で表示 --%>
			<tr <c:if test="${inventory.quantity < 10}">class="outofstock"</c:if>>
				<td>${inventory.modelId}</td>
				<td>${inventory.modelName}</td>
				<td>${inventory.quantity}</td>
			</tr>
			
        </c:forEach>
		
        <c:if test="${empty inventoryList}">
            <tr>
                <td colspan="3">在庫データがありません。</td>
            </tr>
        </c:if>
        </tbody>
    </table>
</main>
</body>
</html>
