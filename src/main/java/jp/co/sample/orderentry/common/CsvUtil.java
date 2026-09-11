package jp.co.sample.orderentry.common;

/**
 * CSV出力用の共通処理。
 * 値にカンマ・改行・ダブルクォートが含まれる場合はダブルクォートで囲む。
 */
public class CsvUtil {

    private CsvUtil() {
    }

    /**
     * CSVの1項目として安全な文字列に変換する。
     * nullは空文字として扱う。
     */
    public static String escape(Object value) {
        if (value == null) {
            return "";
        }
        String str = value.toString();
        if (str.contains(",") || str.contains("\"") || str.contains("\n") || str.contains("\r")) {
            str = str.replace("\"", "\"\"");
            return "\"" + str + "\"";
        }
        return str;
    }

    /**
     * 複数項目をカンマ区切りの1行に結合する。
     */
    public static String toLine(Object... values) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(escape(values[i]));
        }
        sb.append("\r\n");
        return sb.toString();
    }
}
