package com.jeannypr.scientificstudy.Base.Model;

public class UserGuidanceDetail {
    private boolean add_btn_inv_items = false;
    private boolean item_row_inv_items = false;
    private boolean start_journey_drive_home = false;

    public boolean isTrack_location_btn() {
        return track_location_btn;
    }

    public void setTrack_location_btn(boolean track_location_btn) {
        this.track_location_btn = track_location_btn;
    }

    private boolean track_location_btn = false;

    public boolean isStart_journey_drive_home() {
        return start_journey_drive_home;
    }

    public void setStart_journey_drive_home(boolean start_journey_drive_home) {
        this.start_journey_drive_home = start_journey_drive_home;
    }

    public boolean isAdd_btn_inv_items() {
        return add_btn_inv_items;
    }

    public void setAdd_btn_inv_items(boolean add_btn_inv_items) {
        this.add_btn_inv_items = add_btn_inv_items;
    }

    public boolean isItem_row_inv_items() {
        return item_row_inv_items;
    }

    public void setItem_row_inv_items(boolean item_row_inv_items) {
        this.item_row_inv_items = item_row_inv_items;
    }


}
