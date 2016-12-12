package com.widget.howard.bobwidget.model;

import java.util.List;

/**
 * 有多级选项的筛选类型
 * Created by MuHan on 16/7/28.
 */
public class ExpandableFilterDataWithHeader extends FilterDataWithHeader {
    public ExpandableFilterDataWithHeader(String headerName, List<ExpandableFilterData> filterDataList, FilterData selectedFilterData) {
        this.headerName = headerName;
        this.filterDataList = filterDataList;
        this.selectedFilterData = selectedFilterData;
    }

    public List<ExpandableFilterData> filterDataList;
}
