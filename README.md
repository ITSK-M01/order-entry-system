# 受発注管理システム（サンプル）

中途採用者のスキル確認・変更案件practice用に作成したサンプルシステムです。
実際のSES現場でよくある構成（Java EE + Servlet/JSP + JDBC + Oracle + Tomcat + Eclipse）を
なるべくシンプルに再現しています。Spring等のフレームワークは使わず、素のServlet/JSP/JDBCで
実装しているため、詳細設計書とソースを1対1で追いやすくなっています。

## 1. 構成

| 項目 | 内容 |
|---|---|
| OS | Windows |
| 言語 | Java（Java EE / Servlet, JSP） |
| DB | Oracle Database |
| AP実行環境 | Apache Tomcat（8.5系 or 9系を想定） |
| 開発エディタ | Eclipse (Pleiades All in One Java) |
| ビルドツール | Maven |

## 2. フォルダ構成

```
order-system/
├── pom.xml                      … Maven設定（依存ライブラリ、War化設定）
├── sql/
│   ├── 01_create_tables.sql     … テーブル作成SQL
│   └── 02_sample_data.sql       … サンプルデータ投入SQL
├── bin/
│   └── run_master_command.bat   … マスタ管理コマンド起動バッチ
├── lib/                          … ojdbc8.jar 等、Mavenで取得できない場合の手動配置用（初期は空）
└── src/main/
    ├── java/jp/co/sample/orderentry/
    │   ├── common/    … DB接続・CSV出力の共通クラス
    │   ├── entity/    … テーブル1行に対応するEntityクラス
    │   ├── dao/       … DB操作（CRUD）を行うDAOクラス
    │   ├── servlet/   … 画面表示・CSV出力用のServlet
    │   └── command/   … マスタ管理コマンド（コンソールアプリ）
    ├── resources/
    │   └── db.properties        … DB接続情報
    └── webapp/
        ├── index.jsp             … トップページ
        ├── css/style.css
        └── WEB-INF/
            ├── web.xml
            └── jsp/
                ├── inventory.jsp … 在庫画面
                └── order.jsp     … 受注管理画面
```

## 3. テーブル構成

- **MODEL_MASTER**（機種マスタ）… 機種ID, 機種名, 付属情報
- **CLIENT_MASTER**（取引先マスタ）… 取引先ID, 取引先名, 連絡先
- **EMPLOYEE_MASTER**（従業員マスタ）… 従業員ID, 氏名, 入社年度, 部署
- **INVENTORY**（在庫）… 機種ID, 在庫台数
- **ORDER_MANAGEMENT**（受注管理）… 受注ID, 取引先ID, 発注日, 機種ID, 台数, 期日, ステータス, 担当者（従業員ID）

ER関係: INVENTORY / ORDER_MANAGEMENT は MODEL_MASTER を参照し、ORDER_MANAGEMENT はさらに
CLIENT_MASTER・EMPLOYEE_MASTER も参照します（詳細は `sql/01_create_tables.sql` を参照）。

## 4. セットアップ手順

> **Eclipse・Oracleがまだ何も入っていない場合は、先に `00_環境構築手順.md` の手順で
> インストールを済ませてください。** ここでは、それらが既にセットアップ済みである
> 前提での手順を説明します。

### (1) Oracle Databaseの準備
1. Oracle Database（Oracle XE等）にログインできるユーザーを用意する。
2. `sql/01_create_tables.sql` を実行してテーブルを作成する。
3. `sql/02_sample_data.sql` を実行してサンプルデータを投入する。

### (2) 接続情報の設定
`src/main/resources/db.properties` を、自分の環境のOracle接続情報に書き換える。

```properties
db.url=jdbc:oracle:thin:@localhost:1521/XEPDB1
db.user=order_app
db.password=order_app_pass
```

### (3) Eclipseへのインポート
1. Eclipse (Pleiades All in One) を起動する。
2. 「ファイル」→「インポート」→「Maven」→「既存のMavenプロジェクト」でこのフォルダを選択する。
3. Mavenが `pom.xml` の依存関係（JSTL、Oracle JDBCドライバ等）を自動取得する。
   - 社内プロキシ等でOracle JDBCドライバ（ojdbc8）がダウンロードできない場合は、
     Oracle公式サイトから ojdbc8.jar を手動取得し、`lib` フォルダ及び
     プロジェクトの「ビルド・パス」に追加してください。
4. プロジェクトを右クリック →「プロパティ」→「プロジェクト・ファセット」で
   「動的Webモジュール」にチェックが入っていることを確認する（War化のため）。

### (4) Tomcatへのデプロイ
1. Eclipseの「サーバー」ビューでTomcat（8.5 or 9）を追加する。
2. 本プロジェクトをTomcatに追加し、起動する。
3. ブラウザで `http://localhost:8080/order-entry-system/` にアクセスする。

### (5) マスタ管理コマンドの実行
画面上ではマスタの追加・変更・削除はできません。以下のいずれかで実行してください。

- Eclipse上で `MasterManageCommand.java` を右クリック →「実行」→「Javaアプリケーション」
- または、ビルド後に `bin\run_master_command.bat` をダブルクリック
  （事前に `lib\ojdbc8.jar` を配置してください）

コマンド実行後、以下のようなメニューが表示されます。

```
=========================================
 受発注管理システム マスタ管理コマンド
=========================================
 1. 機種マスタ管理
 2. 取引先マスタ管理
 3. 従業員マスタ管理
 0. 終了
番号を入力してください >
```

## 5. 画面

| 画面 | URL | 概要 |
|---|---|---|
| トップ | `/` | 各画面への導線 |
| 在庫画面 | `/inventory` | 在庫一覧表示、CSV出力（`/inventory/csv`） |
| 受注管理画面 | `/order` | 受注一覧表示（ステータス絞り込み可）、CSV出力（`/order/csv`） |

## 6. 変更案件（練習課題）の例

このシステムをベースに、以下のような小さな変更案件を練習として出すことを想定しています
（あくまで一例です。実際の前職の業務内容に合わせて調整してください）。

1. **受注管理画面に「取引先」での絞り込みを追加する**
   `OrderDao#findByStatus` を参考に、取引先IDでも絞り込めるように改修する。
2. **在庫画面に「在庫不足（10台未満）」の行を赤字で表示する**
   JSPの表示条件分岐（`<c:choose>` 等）を使う練習。
3. **受注管理テーブルに「備考」カラムを追加し、画面・CSVに反映する**
   DB変更→Entity→DAO→Servlet→JSPまで一気通貫で改修する練習（詳細設計〜製造〜単体テストの練習に最適）。
4. **受注一覧画面にページング（20件ごと表示）を追加する**
5. **在庫が0の機種は受注登録できないようにするバリデーションを追加する**
   （受注登録画面は本サンプルには含まれていないため、あわせて画面追加も必要になる想定）

## 7. 補足

- 本サンプルは学習・スキル確認用のため、入力チェックや例外処理は最小限にしています。
- 受注の新規登録・更新画面はあえて含めていません（③の練習課題などで、受注登録機能を
  新規に作ってもらうと詳細設計〜結合テストまで一通り経験できます）。
- 文字コードはCSV出力も含めてExcel（日本語Windows）を意識し、Windows-31J（MS932）を使用しています。
