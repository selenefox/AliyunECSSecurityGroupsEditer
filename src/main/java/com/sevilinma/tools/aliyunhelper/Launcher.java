package com.sevilinma.tools.aliyunhelper;

/**
 * javaFX 启动器
 * 为了支持 fat jar 启动，所以需要通过该启动器进行启动
 * 这是因为 JavaFX 会检查主类是否继承了 javafx.application.Application。如果继承了，它就强制要求从模块路径（ModulePath）启动，而不是类路径（Classpath/Fat Jar）
 */
public class Launcher {
    public static void main(String[] args) {
        // 这里调用你原本继承了 Application 的 JavaFX 主类
        AliyunECSApplication.main(args);
    }
}
