<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>受発注管理システム</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header>
    <h1>受発注管理システム</h1>
</header>
<nav>
    <a href="inventory">在庫画面</a>
    <a href="order">受注管理画面</a>
</nav>
<main>
    <h2>メニュー</h2>
    <p>左上のメニューから各画面に遷移してください。</p>
    <ul>
        <li><a href="inventory">在庫画面</a> - 機種ごとの在庫数を確認・CSV出力できます。</li>
        <li><a href="order">受注管理画面</a> - 受注情報の一覧を確認・CSV出力できます。</li>
    </ul>
    <p>※ マスタ（機種・取引先・従業員）の追加・変更・削除は、画面ではなく管理コマンド（コマンドプロンプト）から行います。</p>
</main>
</body>
</html>
