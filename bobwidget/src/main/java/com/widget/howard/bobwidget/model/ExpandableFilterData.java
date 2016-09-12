package com.widget.howard.bobwidget.model;

import java.util.List;

/**
 * 有多级选项的筛选项
 * Created by MuHan on 16/7/28.
 */
public class ExpandableFilterData extends FilterData {
    /**
     * 子栏目
     */
    public List<ExpandableFilterData> expandableChildren;
    public ExpandableFilterData selectedExpandFilterData;
}
