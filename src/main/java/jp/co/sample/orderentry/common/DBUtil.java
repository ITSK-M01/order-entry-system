package jp.co.sample.orderentry.common;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * DB接続を取得するための共通クラス。
 * 接続情報は src/main/resources/db.properties から読み込む。
 *
 * ※ Webアプリ（Servlet）からも、コマンドラインの管理コマンドからも
 * 　 同じクラスを使ってコネクションを取得できるようにしている。
 */
public class DBUtil {

    private static final String PROPERTIES_FILE = "/db.properties";

    private static String url;
    private static String user;
    private static String password;

    static {
        try (InputStream is = DBUtil.class.getResourceAsStream(PROPERTIES_FILE)) {
            if (is == null) {
                throw new RuntimeException(PROPERTIES_FILE + " が見つかりません。src/main/resources 配下に配置してください。");
            }
            Properties props = new Properties();
            props.load(is);
            url = props.getProperty("db.url");
            user = props.getProperty("db.user");
            password = props.getProperty("db.password");

            // Oracle JDBCドライバをロード
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (IOException e) {
            throw new RuntimeException("db.properties の読み込みに失敗しました。", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Oracle JDBCドライバが見つかりません。クラスパスにojdbc8.jar等を追加してください。", e);
        }
    }

    private DBUtil() {
        // インスタンス化不要
    }

    /**
     * DB接続を1件取得する。
     * 呼び出し側で必ずclose()すること（try-with-resources推奨）。
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
