package com.sevilinma.tools.aliyunhelper.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.*;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sevilinma.tools.aliyunhelper.AliyunECSApplication;
import com.sevilinma.tools.aliyunhelper.model.AliyunAPIConfig;
import com.sevilinma.tools.aliyunhelper.model.MyPermission;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable, StageController {
    private IAcsClient client;
    private DefaultProfile profile;
    private Stage mainStage;
    @FXML
    private ComboBox<String> securityGroupComboBox;
    @FXML
    private TableView<MyPermission> permissionTableView;
    @FXML
    private TableColumn directionCol;
    @FXML
    private TableColumn portRangeCol;
    @FXML
    private TableColumn ipProtocolCol;
    @FXML
    private TableColumn policyCol;
    @FXML
    private TableColumn descCol;
    @FXML
    private TableColumn nicTypeCol;

    private Stage aboutViewStage;
    private Stage permissionEditViewStage;
    private PermissionEditController permissionEditController;
    private Stage configViewStage;
    private AliyunAPIConfigViewController aliyunAPIConfigViewController;
    private AliyunAPIConfig apiConfigKey;
    // tableview double click function
    private MyPermission lastRow;
    private Date lastClickTime;
    public final String config_file_name = "aliyun-ecs-config.json";
    public MainController(){
        File jsonFile = new File(config_file_name);
        // load the config file on the current director
        if(!jsonFile.exists()){
            jsonFile = new File(System.getProperty("user.dir")+ File.separator + config_file_name);
        }
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            apiConfigKey = objectMapper.readValue(jsonFile, AliyunAPIConfig.class);
        }catch (Exception e){
            apiConfigKey = new AliyunAPIConfig();
            apiConfigKey.setRegionId("");
            apiConfigKey.setAccessKeyId("");
            apiConfigKey.setAccessKeySecret("");
            e.printStackTrace();
        }

        profile = DefaultProfile.getProfile(
                apiConfigKey.getRegionId(),          // The region ID
                apiConfigKey.getAccessKeyId(),      // The AccessKey ID of the RAM account
                apiConfigKey.getAccessKeySecret()); // The AccessKey Secret of the RAM account
    }

    @Override
    public void setStage(Stage stage) {
        this.mainStage = stage;
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        nicTypeCol.setCellValueFactory(new PropertyValueFactory<MyPermission, String>("nicType"));
        directionCol.setCellValueFactory(new PropertyValueFactory<MyPermission, String>("direction"));
        portRangeCol.setCellValueFactory(new PropertyValueFactory<MyPermission, String>("portRange"));
        ipProtocolCol.setCellValueFactory(new PropertyValueFactory<MyPermission, String>("ipProtocol"));
        policyCol.setCellValueFactory(new PropertyValueFactory<MyPermission, String>("policy"));
        descCol.setCellValueFactory(new PropertyValueFactory<MyPermission, String>("description"));

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AliyunECSApplication.class.getResource("AboutView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            aboutViewStage = new Stage();
            aboutViewStage.setScene(scene);
            aboutViewStage.setTitle("About");
            aboutViewStage.setResizable(false);
            aboutViewStage.initModality(Modality.WINDOW_MODAL);
            StageController stageController = fxmlLoader.getController();
            stageController.setStage(aboutViewStage);
        }catch (IOException e){
            e.printStackTrace();
        }

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(AliyunECSApplication.class.getResource("PermissionEditView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            permissionEditViewStage = new Stage();
            permissionEditViewStage.setScene(scene);
            permissionEditViewStage.initModality(Modality.WINDOW_MODAL);
            permissionEditViewStage.setResizable(false);
            permissionEditViewStage.setTitle("Configure Ruler");
            permissionEditController = fxmlLoader.getController();
            permissionEditController.setStage(permissionEditViewStage);
        }catch (IOException e){
            e.printStackTrace();
        }

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(AliyunECSApplication.class.getResource("AliyunAPIConfigView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            configViewStage = new Stage();
            configViewStage.setScene(scene);
            configViewStage.initModality(Modality.WINDOW_MODAL);
            configViewStage.setResizable(false);
            configViewStage.setTitle("Configure API Key");
            aliyunAPIConfigViewController = fxmlLoader.getController();
            aliyunAPIConfigViewController.setStage(configViewStage);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @FXML
    protected void onConfigButtonClick(){
        if(configViewStage.getOwner() == null){
            configViewStage.initOwner(mainStage);
        }
        aliyunAPIConfigViewController.loadConfig(apiConfigKey);
        configViewStage.showAndWait();
        if(aliyunAPIConfigViewController.getConfig() != null){
            profile = DefaultProfile.getProfile(
                    apiConfigKey.getRegionId(),
                    apiConfigKey.getAccessKeyId(),
                    apiConfigKey.getAccessKeySecret());
        }
    }
    @FXML
    protected void onConnectButtonClick() {
        try {
            client = new DefaultAcsClient(profile);
        }catch (Exception e){
            client = null;
        }

        try{
            DescribeSecurityGroupsRequest r = new DescribeSecurityGroupsRequest();
            DescribeSecurityGroupsResponse rp = client.getAcsResponse(r);
            securityGroupComboBox.getItems().clear();
            for(DescribeSecurityGroupsResponse.SecurityGroup group: rp.getSecurityGroups()){
                //System.out.printf("GroupName:%s Id:%s %n",group.getSecurityGroupName(), group.getSecurityGroupId());
                securityGroupComboBox.getItems().add(group.getSecurityGroupId());
            }
            if(securityGroupComboBox.getItems().size() > 0){
                securityGroupComboBox.getSelectionModel().select(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    protected void onRefreshButtonClick(){
        if(securityGroupComboBox.getItems().size() > 0 && !securityGroupComboBox.getSelectionModel().isEmpty()){
            DescribeSecurityGroupAttributeRequest request = new DescribeSecurityGroupAttributeRequest();
            request.setSecurityGroupId(securityGroupComboBox.getValue());
            try{
                DescribeSecurityGroupAttributeResponse response = client.getAcsResponse(request);
                permissionTableView.getItems().clear();
                for(DescribeSecurityGroupAttributeResponse.Permission permission : response.getPermissions()){
                    //System.out.printf("%s %s %s %s %n", permission.getDirection(),permission.getPortRange(), permission.getIpProtocol(), permission.getPolicy());
                    permissionTableView.getItems().add(new MyPermission(permission));
                }
            }catch (ClientException e){
                e.printStackTrace();
            }
        }
    }
    @FXML
    protected void onCloseButtonClick(){
        securityGroupComboBox.getItems().clear();
        permissionTableView.getItems().clear();
    }
    @FXML
    protected void onAboutButtonClick(){
        if(aboutViewStage.getOwner() == null){
            aboutViewStage.initOwner(mainStage);
        }
        aboutViewStage.showAndWait();
    }
    @FXML
    protected void onAddButtonClick(){
        if(securityGroupComboBox.getItems().size() > 0 && !securityGroupComboBox.getSelectionModel().isEmpty()){
            permissionEditController.createPermission();
            if(permissionEditViewStage.getOwner() == null){
                permissionEditViewStage.initOwner(mainStage);
            }
            permissionEditViewStage.showAndWait();
            if(permissionEditController.getPermission() != null){
                DescribeSecurityGroupAttributeResponse.Permission p = permissionEditController.getPermission().getPermission();
                if(p.getDirection().equals("ingress")){
                    createIngressRule(p);
                }else{
                    createEgressRule(p);
                }
                onRefreshButtonClick();
            }
        }
    }
    @FXML
    protected void onEditButtonClick(){
        MyPermission selectedItem = permissionTableView.getSelectionModel().getSelectedItem();
        if(selectedItem != null){
            permissionEditController.loadPermission(selectedItem);
            if(permissionEditViewStage.getOwner() == null){
                permissionEditViewStage.initOwner(mainStage);
            }
            permissionEditViewStage.showAndWait();
            if(permissionEditController.getPermission() != null){
                DescribeSecurityGroupAttributeResponse.Permission p = selectedItem.getPermission();
                if(p.getDirection().equals("ingress")){
                    removeIngressRule(permissionEditController.getOldPermission());
                    createIngressRule(p);
                }else{
                    removeEgressRule(permissionEditController.getOldPermission());
                    createEgressRule(p);
                }
                onRefreshButtonClick();
            }
        }
    }
    @FXML
    protected void onDeleteButtonClick(){
        MyPermission selectedItem = permissionTableView.getSelectionModel().getSelectedItem();
        if(selectedItem != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Are you sure you want to delete this rule?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                DescribeSecurityGroupAttributeResponse.Permission p = selectedItem.getPermission();
                if(p.getDirection().equals("ingress")){
                    removeIngressRule(p);
                }else{
                    removeEgressRule(p);
                }
                onRefreshButtonClick();
            }
        }
    }

    private void removeIngressRule(DescribeSecurityGroupAttributeResponse.Permission p){
        RevokeSecurityGroupRequest request = new RevokeSecurityGroupRequest();
        request.setIpProtocol(p.getIpProtocol());
        request.setPolicy(p.getPolicy());
        request.setSecurityGroupId(securityGroupComboBox.getValue());
        request.setNicType(p.getNicType());
        request.setSourceCidrIp(p.getSourceCidrIp());
        request.setPortRange(p.getPortRange());
        try{
            client.getAcsResponse(request);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void removeEgressRule(DescribeSecurityGroupAttributeResponse.Permission p){
        RevokeSecurityGroupEgressRequest request = new RevokeSecurityGroupEgressRequest();
        request.setSecurityGroupId(securityGroupComboBox.getValue());
        request.setIpProtocol(p.getIpProtocol());
        request.setPolicy(p.getPolicy());
        request.setNicType(p.getNicType());
        request.setPortRange(p.getPortRange());
        request.setDestCidrIp(p.getDestCidrIp());
        try{
            client.getAcsResponse(request);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void createIngressRule(DescribeSecurityGroupAttributeResponse.Permission p){
        AuthorizeSecurityGroupRequest request = new AuthorizeSecurityGroupRequest();
        request.setSecurityGroupId(securityGroupComboBox.getValue());
        request.setIpProtocol(p.getIpProtocol());
        request.setPolicy(p.getPolicy());
        request.setNicType(p.getNicType());
        request.setSourceCidrIp(p.getSourceCidrIp());
        request.setPortRange(p.getPortRange());
        request.setDescription(p.getDescription());
        try{
            client.getAcsResponse(request);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void createEgressRule(DescribeSecurityGroupAttributeResponse.Permission p){
        AuthorizeSecurityGroupEgressRequest request = new AuthorizeSecurityGroupEgressRequest();
        request.setSecurityGroupId(securityGroupComboBox.getValue());
        request.setIpProtocol(p.getIpProtocol());
        request.setPolicy(p.getPolicy());
        request.setNicType(p.getNicType());
        request.setDestCidrIp(p.getDestCidrIp());
        request.setPortRange(p.getPortRange());
        request.setDescription(p.getDescription());
        try{
            client.getAcsResponse(request);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    protected void onTableViewMouseClicked(){
        MyPermission row = permissionTableView.getSelectionModel().getSelectedItem();
        if (row == null)
            return;
        if(row != lastRow){
            lastRow = row;
            lastClickTime = new Date();
        } else {
            Date now = new Date();
            long diff = now.getTime() - lastClickTime.getTime();
            if (diff < 300){
                onEditButtonClick();
            } else {
                lastClickTime = new Date();
            }
        }
    }
}