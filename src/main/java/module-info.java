module com.sevilinma.tools.aliyunhelper {
    requires javafx.controls;
    requires javafx.fxml;
    requires aliyun.java.sdk.core;
    requires aliyun.java.sdk.ecs;
    requires aliyun.java.sdk.alidns;
    requires com.fasterxml.jackson.databind;
    requires commons.beanutils;

    opens com.sevilinma.tools.aliyunhelper.controller to javafx.fxml;
    opens com.sevilinma.tools.aliyunhelper.model to com.fasterxml.jackson.databind, javafx.base;

    exports com.sevilinma.tools.aliyunhelper;
}