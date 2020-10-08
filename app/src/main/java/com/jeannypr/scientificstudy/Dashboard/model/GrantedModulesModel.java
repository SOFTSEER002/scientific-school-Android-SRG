package com.jeannypr.scientificstudy.Dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GrantedModulesModel {
    @SerializedName("id")
    @Expose
    private int moduleId;

    public int getModuleId() {
        return moduleId == -1 ? 0 : moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }
}
