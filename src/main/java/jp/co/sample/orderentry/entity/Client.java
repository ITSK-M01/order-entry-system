package jp.co.sample.orderentry.entity;

/**
 * 取引先マスタ（CLIENT_MASTER）1レコード分を表すEntity。
 */
public class Client {

    private String clientId;      // 取引先ID
    private String clientName;    // 取引先名
    private String clientContact; // 連絡先

    public Client() {
    }

    public Client(String clientId, String clientName, String clientContact) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.clientContact = clientContact;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientContact() {
        return clientContact;
    }

    public void setClientContact(String clientContact) {
        this.clientContact = clientContact;
    }
}
