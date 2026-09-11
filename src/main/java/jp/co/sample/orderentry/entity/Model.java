package jp.co.sample.orderentry.entity;

/**
 * 機種マスタ（MODEL_MASTER）1レコード分を表すEntity。
 */
public class Model {

    private String modelId;    // 機種ID
    private String modelName;  // 機種名
    private String modelInfo;  // 付属情報

    public Model() {
    }

    public Model(String modelId, String modelName, String modelInfo) {
        this.modelId = modelId;
        this.modelName = modelName;
        this.modelInfo = modelInfo;
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

    public String getModelInfo() {
        return modelInfo;
    }

    public void setModelInfo(String modelInfo) {
        this.modelInfo = modelInfo;
    }
}
