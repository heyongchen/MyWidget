package com.widget.howard.bobwidget.model;

import java.util.List;

/**
 * Created by howard on 2016/7/27.
 */
public class FilterDataWithHeader {

    public String headerName;
    public List<FilterData> filterDataList;
    public FilterData selectedFilterData;

    public FilterDataWithHeader() {

    }

    public FilterDataWithHeader(String headerName, List<FilterData> filterDataList, FilterData selectedFilterData) {
        this.headerName = headerName;
        this.filterDataList = filterDataList;
        this.selectedFilterData = selectedFilterData;
    }
}
