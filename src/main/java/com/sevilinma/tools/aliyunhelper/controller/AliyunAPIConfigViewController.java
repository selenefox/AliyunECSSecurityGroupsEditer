package com.sevilinma.tools.aliyunhelper.controller;

import com.sevilinma.tools.aliyunhelper.model.AliyunAPIConfig;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class AliyunAPIConfigViewController implements Initializable,StageController{
    @FXML
    private TextField regionIdText;
    @FXML
    private TextField accessKeyIdText;
    @FXML
    private TextField accessKeySecretText;

    private Stage stage;
    private AliyunAPIConfig config;

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                onCancelButtonClick();
            }
        });
    }

    public void loadConfig(AliyunAPIConfig config){
        this.config = config;
        regionIdText.setText(config.getRegionId());
        accessKeyIdText.setText(config.getAccessKeyId());
        accessKeySecretText.setText(config.getAccessKeySecret());
    }

    private void saveConfig(){
        if(config == null){
            config = new AliyunAPIConfig();
        }
        config.setRegionId(regionIdText.getText());
        config.setAccessKeyId(accessKeyIdText.getText());
        config.setAccessKeySecret(accessKeySecretText.getText());
    }

    public AliyunAPIConfig getConfig(){
        return config;
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    protected void onSaveButtonClick(){
        saveConfig();
        stage.close();
    }
    @FXML
    protected void onCancelButtonClick(){
        config = null;
        stage.close();
    }
}
