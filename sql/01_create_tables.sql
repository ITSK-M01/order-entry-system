--------------------------------------------------------------------------
-- 受発注管理システム テーブル作成スクリプト（Oracle Database用）
-- ※ Oracle 21c以降のXEを使用する場合は、CDBではなくプラガブルDB「XEPDB1」に
-- 　 接続したうえで、アプリ用ユーザー（例：order_app）で本スクリプトを実行してください。
-- 　 詳細は 00_環境構築手順.md を参照してください。
--------------------------------------------------------------------------

-- 既存テーブル削除（再実行用。初回実行時はエラーが出ますが無視してください）
DROP TABLE ORDER_MANAGEMENT PURGE;
DROP TABLE INVENTORY PURGE;
DROP TABLE MODEL_MASTER PURGE;
DROP TABLE CLIENT_MASTER PURGE;
DROP TABLE EMPLOYEE_MASTER PURGE;

--------------------------------------------------------------------------
-- ①機種マスタ
--------------------------------------------------------------------------
CREATE TABLE MODEL_MASTER (
    MODEL_ID     VARCHAR2(10)  NOT NULL,  -- 機種ID
    MODEL_NAME   VARCHAR2(100) NOT NULL,  -- 機種名
    MODEL_INFO   VARCHAR2(500),           -- 付属情報（仕様・備考等）
    CREATED_AT   DATE          DEFAULT SYSDATE NOT NULL,
    UPDATED_AT   DATE          DEFAULT SYSDATE NOT NULL,
    CONSTRAINT PK_MODEL_MASTER PRIMARY KEY (MODEL_ID)
);

--------------------------------------------------------------------------
-- ②取引先マスタ
--------------------------------------------------------------------------
CREATE TABLE CLIENT_MASTER (
    CLIENT_ID       VARCHAR2(10)  NOT NULL,  -- 取引先ID
    CLIENT_NAME     VARCHAR2(100) NOT NULL,  -- 取引先名
    CLIENT_CONTACT  VARCHAR2(200),           -- 連絡先（電話番号・メール等）
    CREATED_AT      DATE          DEFAULT SYSDATE NOT NULL,
    UPDATED_AT      DATE          DEFAULT SYSDATE NOT NULL,
    CONSTRAINT PK_CLIENT_MASTER PRIMARY KEY (CLIENT_ID)
);

--------------------------------------------------------------------------
-- ③従業員マスタ
--------------------------------------------------------------------------
CREATE TABLE EMPLOYEE_MASTER (
    EMPLOYEE_ID    VARCHAR2(10)  NOT NULL,  -- 従業員ID
    EMPLOYEE_NAME  VARCHAR2(100) NOT NULL,  -- 氏名
    HIRE_YEAR      NUMBER(4),               -- 入社年度
    DEPARTMENT     VARCHAR2(100),           -- 部署
    CREATED_AT     DATE          DEFAULT SYSDATE NOT NULL,
    UPDATED_AT     DATE          DEFAULT SYSDATE NOT NULL,
    CONSTRAINT PK_EMPLOYEE_MASTER PRIMARY KEY (EMPLOYEE_ID)
);

--------------------------------------------------------------------------
-- ④在庫テーブル
--------------------------------------------------------------------------
CREATE TABLE INVENTORY (
    MODEL_ID     VARCHAR2(10)  NOT NULL,  -- 機種ID
    QUANTITY     NUMBER(10)    DEFAULT 0 NOT NULL,  -- 在庫台数
    UPDATED_AT   DATE          DEFAULT SYSDATE NOT NULL,
    CONSTRAINT PK_INVENTORY PRIMARY KEY (MODEL_ID),
    CONSTRAINT FK_INVENTORY_MODEL FOREIGN KEY (MODEL_ID)
        REFERENCES MODEL_MASTER (MODEL_ID)
);

--------------------------------------------------------------------------
-- ⑤受注管理テーブル
--------------------------------------------------------------------------
CREATE TABLE ORDER_MANAGEMENT (
    ORDER_ID      VARCHAR2(10)  NOT NULL,  -- 受注ID
    CLIENT_ID     VARCHAR2(10)  NOT NULL,  -- 発注主体（取引先ID）
    ORDER_DATE    DATE          NOT NULL,  -- 発注日
    MODEL_ID      VARCHAR2(10)  NOT NULL,  -- 機種ID
    QUANTITY      NUMBER(10)    NOT NULL,  -- 台数
    DUE_DATE      DATE,                    -- 期日（納期）
    STATUS        VARCHAR2(20)  DEFAULT '未処理' NOT NULL,  -- ステータス（未処理／手配中／出荷済／完了／キャンセル）
    EMPLOYEE_ID   VARCHAR2(10),            -- 担当者（従業員ID）
    CREATED_AT    DATE          DEFAULT SYSDATE NOT NULL,
    UPDATED_AT    DATE          DEFAULT SYSDATE NOT NULL,
    CONSTRAINT PK_ORDER_MANAGEMENT PRIMARY KEY (ORDER_ID),
    CONSTRAINT FK_ORDER_CLIENT   FOREIGN KEY (CLIENT_ID)   REFERENCES CLIENT_MASTER (CLIENT_ID),
    CONSTRAINT FK_ORDER_MODEL    FOREIGN KEY (MODEL_ID)    REFERENCES MODEL_MASTER (MODEL_ID),
    CONSTRAINT FK_ORDER_EMPLOYEE FOREIGN KEY (EMPLOYEE_ID) REFERENCES EMPLOYEE_MASTER (EMPLOYEE_ID)
);

COMMIT;
