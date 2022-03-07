module com.sevilinma.tools.aliyunhelper {
    requires javafx.controls;
    requires javafx.fxml;
    requires aliyun.java.sdk.core;
    requires aliyun.java.sdk.ecs;
    requires aliyun.java.sdk.alidns;
    requires com.fasterxml.jackson.databind;
    requires commons.beanutils;

    opens com.sevilinma.tools.aliyunhelper.controller to javafx.fxml;
    exports com.sevilinma.tools.aliyunhelper;
}