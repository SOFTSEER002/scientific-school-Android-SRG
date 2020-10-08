package com.jeannypr.scientificstudy.Dashboard.navigator;

public interface DashboardTodayTabNavigator {

    /**
     * Override to show full desc.
     *
     * @param desc
     * @param title
     */
    void showFullDesc(String desc, String title);

    /**
     * Override to get class list.
     */
//    ArrayList<DropDownModel> getClasses();

    void setClassesForMessenger(int adapterPosition);

    /**
     * Override to create bar graph.
     * @param barDataSet
     * @param formatter
     */
//    void createBarGraph(BarDataSet barDataSet, IndexAxisValueFormatter formatter);
}
