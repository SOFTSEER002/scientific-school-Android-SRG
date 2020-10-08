package com.jeannypr.scientificstudy.Classwork.model;

/**
 * Created by Varun John on 4 Dec, 2018
 * Github : https://github.com/varunjohn
 */
public class HWMessage {

    public static int TYPE_TEXT = 1;
    public static int TYPE_AUDIO = 21;

    public String text;
    public int type;
    public int time;

    public HWMessage(String text) {
        this.text = text;
        this.type = TYPE_TEXT;
    }

    public HWMessage(int time) {
        this.time = time;
        this.type = TYPE_AUDIO;
    }

}