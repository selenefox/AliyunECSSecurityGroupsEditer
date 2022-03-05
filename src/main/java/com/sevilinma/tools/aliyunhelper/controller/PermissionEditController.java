package com.sevilinma.tools.aliyunhelper.controller;

import com.aliyuncs.ecs.model.v20140526.DescribeSecurityGroupAttributeResponse;
import com.sevilinma.tools.aliyunhelper.model.MyPermission;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.beanutils.BeanUtils;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class PermissionEditController implements Initializable,StageController {
    @FXML
    public TextField portEndText;
    @FXML
    public TextField ipText;
    @FXML
    public TextArea descriptionText;
    @FXML
    public TextField portStartText;
    @FXML
    public ComboBox<String> directionComboBox;
    @FXML
    public ComboBox<String> protocolComboBox;
    @FXML
    public ComboBox<String> policyComboBox;
    @FXML
    public ComboBox<String> nicTypeComboBox;

    private Stage stage;
    private MyPermission permission;
    private DescribeSecurityGroupAttributeResponse.Permission oldPermission;

    public void setStage(Stage stage){
        this.stage = stage;
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                onCancelButtonClick();
            }
        });
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void loadPermission(MyPermission permission){
        this.permission = permission;
        refreshUIData();
        directionComboBox.setDisable(true);
        oldPermission = new DescribeSecurityGroupAttributeResponse.Permission();
        try{
            BeanUtils.copyProperties(oldPermission, permission.getPermission());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void createPermission(){
        directionComboBox.setDisable(false);
        portEndText.setText("");
        ipText.setText("");
        descriptionText.setText("");
        portStartText.setText("");
    }

    public MyPermission getPermission() {
        return permission;
    }

    public DescribeSecurityGroupAttributeResponse.Permission getOldPermission(){
        return oldPermission;
    }

    private void refreshUIData(){
        DescribeSecurityGroupAttributeResponse.Permission p = permission.getPermission();
        String[] portR = p.getPortRange().split("/");
        portStartText.setText(portR[0]);
        portEndText.setText(portR[1]);
        if(p.getDirection().equals("ingress")){
            ipText.setText(p.getSourceCidrIp());
        }else{
            ipText.setText(p.getDestCidrIp());
        }

        descriptionText.setText(p.getDescription());
        directionComboBox.setValue(p.getDirection());
        protocolComboBox.setValue(p.getIpProtocol());
        policyComboBox.setValue(p.getPolicy());
        nicTypeComboBox.setValue(p.getNicType());
    }
    @FXML
    protected void onOkButtonClick(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to commit this rule?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            if(this.permission == null){
                this.permission = new MyPermission(new DescribeSecurityGroupAttributeResponse.Permission());
            }
            DescribeSecurityGroupAttributeResponse.Permission p = this.permission.getPermission();
            if(directionComboBox.getValue().equals("ingress")){
                p.setSourceCidrIp(ipText.getText());
            }else{
                p.setDestCidrIp(ipText.getText());
            }
            p.setDescription(descriptionText.getText());
            p.setDirection(directionComboBox.getValue());
            p.setIpProtocol(protocolComboBox.getValue());
            p.setPolicy(policyComboBox.getValue());
            p.setNicType(nicTypeComboBox.getValue());
            p.setPortRange(String.format("%s/%s", portStartText.getText(), portEndText.getText()));
            stage.close();
        }
    }
    @FXML
    protected void onCancelButtonClick(){
        this.permission = null;
        stage.close();
    }
}
