package com.jeannypr.scientificstudy.Dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardModulesModel {
   /* @SerializedName("id")
    @Expose
    private int moduleId;*/

    @SerializedName("moduleName")
    @Expose
    private String moduleName;

    public int getModuleId() {
        return moduleId == -1 ? 0 : moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    @SerializedName("moduleId")
    @Expose
    private int moduleId;
    public int AdapterPosition;

    public String getModuleImg() {
        return moduleImg == null ? "" : moduleImg;
    }

    public void setModuleImg(String moduleImg) {
        this.moduleImg = moduleImg;
    }

    private String moduleImg;

  /*  public int getModuleId() {
        return moduleId == -1 ? 0 : moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }*/

    public String getModuleName() {
        return moduleName != null ? moduleName : "";
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
