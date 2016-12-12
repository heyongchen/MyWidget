package com.widget.howard.mywidget.model;

import com.widget.howard.bobwidget.model.ExpandableFilterData;
import com.widget.howard.bobwidget.model.ExpandableFilterDataWithHeader;
import com.widget.howard.bobwidget.model.FilterData;
import com.widget.howard.bobwidget.model.FilterDataWithHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by howard on 2016/12/12.
 */

public class ExpandList {
    private static List<FilterDataWithHeader> filterDataWithHeaderList = new ArrayList<>();

    public static List<FilterDataWithHeader> getList() {
        ExpandList.filterDataWithHeaderList.clear();
        setDefaultValue();
        return ExpandList.filterDataWithHeaderList;
    }

    private static void setDefaultValue() {
        List<FilterData> filterDatas = new ArrayList<>();
        filterDatas.add(new FilterData("10", "未知"));
        filterDatas.add(new FilterData("11", "金牛"));
        filterDatas.add(new FilterData("12", "摩羯"));
        filterDatas.add(new FilterData("13", "水瓶"));
        filterDatas.add(new FilterData("14", "狮子"));

        List<ExpandableFilterData> expandableFilterDatas = new ArrayList<>();
        List<ExpandableFilterData> city1 = new ArrayList<>();
        city1.add(new ExpandableFilterData("210", "全部"));
        city1.add(new ExpandableFilterData("211", "杭州"));
        city1.add(new ExpandableFilterData("214", "台州"));
        city1.add(new ExpandableFilterData("215", "小和山"));

        List<ExpandableFilterData> city2 = new ArrayList<>();
        city2.add(new ExpandableFilterData("220", "全部"));
        city2.add(new ExpandableFilterData("221", "大陆"));

        expandableFilterDatas.add(new ExpandableFilterData("20", "全部", null));
        expandableFilterDatas.add(new ExpandableFilterData("21", "浙江", city1));
        expandableFilterDatas.add(new ExpandableFilterData("22", "中国", city2));
        expandableFilterDatas.add(new ExpandableFilterData("22", "中国", city2));
        expandableFilterDatas.add(new ExpandableFilterData("22", "中国", city2));
        expandableFilterDatas.add(new ExpandableFilterData("22", "中国", city2));
        expandableFilterDatas.add(new ExpandableFilterData("22", "中国", city2));

        ExpandList.filterDataWithHeaderList.add(new FilterDataWithHeader("星座", filterDatas, null));
        ExpandList.filterDataWithHeaderList.add(new ExpandableFilterDataWithHeader("地区", expandableFilterDatas, null));
        ExpandList.filterDataWithHeaderList.add(new ExpandableFilterDataWithHeader("地区", expandableFilterDatas, null));
    }
}
