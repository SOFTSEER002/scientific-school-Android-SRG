package com.jeannypr.scientificstudy.Dashboard.model;

public class MenusModel {
    public String getGridLabel() {
        return GridLabel == null ? "" : GridLabel;
    }

    public void setGridLabel(String gridLabel) {
        GridLabel = gridLabel;
    }

    private String GridLabel;

    public int getGridIc() {
        return GridIc;
    }

    public void setGridIc(int gridIc) {
        GridIc = gridIc;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    private int Id;
    private int GridIc;
    public int AdapterPosition;

    public MenusModel(int id, String gridLabel, int gridIc) {
        Id = id;
        GridLabel = gridLabel;
        GridIc = gridIc;
    }
}
