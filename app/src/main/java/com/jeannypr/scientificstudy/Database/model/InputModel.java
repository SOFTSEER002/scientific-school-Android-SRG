package com.jeannypr.scientificstudy.Database.model;

public class InputModel {
    public String Id;
    public String WidgetName;
    public String UserId;
    public String SchoolCode;
    public String SchoolId;
    public String UserRole;

    public String getId() {
        return Id;
    }

    public String getWidgetName() {
        return WidgetName;
    }

    public String getUserId() {
        return UserId;
    }

    public String getSchoolCode() {
        return SchoolCode;
    }

    public String getSchoolId() {
        return SchoolId;
    }

    public String getUserRole() {
        return UserRole;
    }


    public InputModel(String widgetName, String userId, String schoolCode, String schoolId, String userRole) {
        WidgetName = widgetName;
        UserId = userId;
        SchoolCode = schoolCode;
        SchoolId = schoolId;
        UserRole = userRole;
    }
}
