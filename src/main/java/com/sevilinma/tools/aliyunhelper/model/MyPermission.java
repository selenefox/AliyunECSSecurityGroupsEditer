package com.sevilinma.tools.aliyunhelper.model;

import com.aliyuncs.ecs.model.v20140526.DescribeSecurityGroupAttributeResponse;
import javafx.beans.property.SimpleStringProperty;

public class MyPermission {
    private final SimpleStringProperty direction;
    private final SimpleStringProperty portRange;
    private final SimpleStringProperty ipProtocol;
    private final SimpleStringProperty policy;
    private final SimpleStringProperty nicType;
    private final SimpleStringProperty description;

    private final DescribeSecurityGroupAttributeResponse.Permission permission;

    public MyPermission(DescribeSecurityGroupAttributeResponse.Permission permission){
        this.direction = new SimpleStringProperty(permission.getDirection());
        this.portRange = new SimpleStringProperty(permission.getPortRange());
        this.ipProtocol = new SimpleStringProperty(permission.getIpProtocol());
        this.policy = new SimpleStringProperty(permission.getPolicy());
        this.nicType = new SimpleStringProperty(permission.getNicType());
        this.description = new SimpleStringProperty(permission.getDescription());
        this.permission = permission;
    }

    public String getDirection() {
        return direction.get();
    }

    public void setDirection(String direction) {
        this.direction.set(direction);
    }

    public String getPortRange() {
        return portRange.get();
    }

    public void setPortRange(String portRange) {
        this.portRange.set(portRange);
    }

    public String getIpProtocol() {
        return ipProtocol.get();
    }

    public void setIpProtocol(String ipProtocol) {
        this.ipProtocol.set(ipProtocol);
    }

    public String getPolicy() {
        return policy.get();
    }

    public void setPolicy(String policy) {
        this.policy.set(policy);
    }

    public DescribeSecurityGroupAttributeResponse.Permission getPermission(){
        return permission;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getNicType() {
        return nicType.get();
    }

    public void setNicType(String nicType) {
        this.nicType.set(nicType);
    }
}
