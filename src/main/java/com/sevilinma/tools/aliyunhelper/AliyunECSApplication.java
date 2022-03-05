package com.sevilinma.tools.aliyunhelper;

import com.sevilinma.tools.aliyunhelper.controller.StageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AliyunECSApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AliyunECSApplication.class.getResource("MainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Aliyun ECS Config Tools");
        stage.setScene(scene);
        stage.show();
        StageController stageController = fxmlLoader.getController();
        stageController.setStage(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}