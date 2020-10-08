package com.jeannypr.scientificstudy.Base.Model;

public class DropDownModel {

    private int _id;
    private String _name;
    private Object _object;

    public DropDownModel() {
        this._id = 0;
        this._name = "";
    }

    public DropDownModel(int _id, String _name) {
        this._id = _id;
        this._name = _name;
    }

    public void setId(int id) {
        this._id = id;
    }

    public int getId() {
        return this._id;
    }

    public void setText(String name) {
        this._name = name;
    }

    public String getText() {
        return this._name;
    }

    public Object getObject() {
        return this._object;
    }

    public void setObject(Object _object) {
        this._object = _object;
    }
}