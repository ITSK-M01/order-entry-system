package jp.co.sample.orderentry.entity;

/**
 * 在庫テーブル（INVENTORY）1レコード分を表すEntity。
 * 画面表示用に、機種マスタと結合した機種名も併せて保持する。
 */
public class Inventory {

    private String modelId;     // 機種ID
    private String modelName;   // 機種名（MODEL_MASTERから結合取得。画面表示用）
    private int quantity;       // 在庫台数

    public Inventory() {
    }

    public Inventory(String modelId, String modelName, int quantity) {
        this.modelId = modelId;
        this.modelName = modelName;
        this.quantity = quantity;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
