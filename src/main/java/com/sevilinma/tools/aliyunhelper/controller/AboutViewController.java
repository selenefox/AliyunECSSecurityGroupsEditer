package com.sevilinma.tools.aliyunhelper.controller;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class AboutViewController implements StageController {
    private Stage stage;

    public void setStage(Stage stage){
        this.stage = stage;
    }
    @FXML
    protected void onCloseButtonClick(){
        stage.close();
    }
}
