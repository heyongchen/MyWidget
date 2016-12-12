package com.widget.howard.bobwidget.model;

import java.util.List;

/**
 * 有多级选项的筛选项
 * Created by MuHan on 16/7/28.
 */
public class ExpandableFilterData extends FilterData {
    public ExpandableFilterData(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public ExpandableFilterData(String key, String value, List<ExpandableFilterData> expandableChildren) {
        this.key = key;
        this.value = value;
        this.expandableChildren = expandableChildren;
    }

    /**
     * 子栏目
     */
    public List<ExpandableFilterData> expandableChildren;
    public ExpandableFilterData selectedExpandFilterData;
}
