package com.widget.howard.bobwidget.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.widget.howard.bobwidget.R;
import com.widget.howard.bobwidget.adapter.ExplandFilterAdapter;
import com.widget.howard.bobwidget.adapter.FilterAdapter;
import com.widget.howard.bobwidget.adapter.SecondFilterAdapter;
import com.widget.howard.bobwidget.model.ExpandableFilterData;
import com.widget.howard.bobwidget.model.ExpandableFilterDataWithHeader;
import com.widget.howard.bobwidget.model.FilterData;
import com.widget.howard.bobwidget.model.FilterDataWithHeader;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by howard on 2016/7/27.
 */
public class FilterView extends LinearLayout {

    private int window_height;//屏幕高度
    private int max_menu_height;//筛选菜单最大高度
    private MenuClickListener mMenuClickListener;
    private Context mContext;
    private List<FilterDataWithHeader> mfilterDataWithHeaderList = new ArrayList<>();
    //已选项
    private List<FilterData> filterDatas = new ArrayList<>();
    //选项菜单ID
    private int mMenuListId = 0;
    //选项标题
    private List<String> mTabTexts = new ArrayList<>();
    //选项菜单
    private List<View> menuList = new ArrayList<>();
    //顶部菜单布局
    private LinearLayout tabMenuView;
    //底部容器，包含popupMenuViews，maskView
    private FrameLayout containerView;
    //弹出菜单父布局
    private FrameLayout popupMenuViews;
    //遮罩半透明View，点击可关闭DropDownMenu
    private View maskView;
    //tabMenuView里面选中的tab位置，-1表示未选中
    private int current_tab_position = -1;
    //分割线颜色
    private int dividerColor = 0xffcccccc;
    //tab选中颜色
    private int textSelectedColor = 0xff890c85;
    //tab未选中颜色
    private int textUnselectedColor = 0xff111111;
    //遮罩颜色
    private int maskColor = 0x88888888;
    //tab字体大小
    private int menuTextSize = 14;
    //tab选中图标
    private int menuSelectedIcon = R.drawable.ic_filter_triangle_down;
    //tab未选中图标
    private int menuUnselectedIcon = R.drawable.ic_filter_triangle_up;
    //tab背景色
    private int menuBackgroundColor = 0xffffffff;
    //tab下划线颜色
    private int underlineColor = 0xffcccccc;

    public FilterView(Context context) {
        super(context, null);
    }

    public FilterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FilterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        setOrientation(VERTICAL);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        window_height = wm.getDefaultDisplay().getHeight();
        max_menu_height = (int) (window_height * 0.6);

        //为DropDownMenu添加自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FilterView);
        underlineColor = typedArray.getColor(R.styleable.FilterView_underlineColor, underlineColor);
        dividerColor = typedArray.getColor(R.styleable.FilterView_dividerColor, dividerColor);
        textSelectedColor = typedArray.getColor(R.styleable.FilterView_textSelectedColor, textSelectedColor);
        textUnselectedColor = typedArray.getColor(R.styleable.FilterView_textUnselectedColor, textUnselectedColor);
        menuBackgroundColor = typedArray.getColor(R.styleable.FilterView_menuBackgroundColor, menuBackgroundColor);
        maskColor = typedArray.getColor(R.styleable.FilterView_maskColor, maskColor);
        menuTextSize = typedArray.getDimensionPixelSize(R.styleable.FilterView_menuTextSize, menuTextSize);
        menuSelectedIcon = typedArray.getResourceId(R.styleable.FilterView_menuSelectedIcon, menuSelectedIcon);
        menuUnselectedIcon = typedArray.getResourceId(R.styleable.FilterView_menuUnselectedIcon, menuUnselectedIcon);
        typedArray.recycle();

        //初始化tabMenuView并添加到tabMenuView
        tabMenuView = new LinearLayout(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tabMenuView.setOrientation(HORIZONTAL);
        tabMenuView.setBackgroundColor(menuBackgroundColor);
        tabMenuView.setLayoutParams(params);
        addView(tabMenuView);

        //为tabMenuView添加下划线
        View underLine = new View(getContext());
        underLine.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpTpPx(1.0f)));
        underLine.setBackgroundColor(underlineColor);
        addView(underLine);

        //初始化containerView并将其添加到DropDownMenu
        containerView = new FrameLayout(context);
        containerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        addView(containerView);

    }

    public void setDefaultMenu(List<FilterDataWithHeader> filterDataWithHeaderList, View contentView) {
        mfilterDataWithHeaderList = filterDataWithHeaderList;
        for (int i = 0; i < filterDataWithHeaderList.size(); i++) {
            mTabTexts.add(filterDataWithHeaderList.get(i).headerName);
            if (filterDataWithHeaderList.get(i) instanceof ExpandableFilterDataWithHeader) {
                //有多级选项的筛选类型
                addExplandTabList(((ExpandableFilterDataWithHeader) filterDataWithHeaderList.get(i)).filterDataList, ((ExpandableFilterDataWithHeader) filterDataWithHeaderList.get(i)).selectedFilterData);
            } else {
                addTabList(filterDataWithHeaderList.get(i).filterDataList, filterDataWithHeaderList.get(i).selectedFilterData);
            }
            FilterData nullFilterData = new FilterData();
            nullFilterData.value = "";
            //FilterView默认值
            nullFilterData.key = "0";
            filterDatas.add(i, nullFilterData);
        }
        setDropDownMenu(mTabTexts, menuList, contentView);
    }

    //添加二级分类选项列表
    public void addExplandTabList(final List<ExpandableFilterData> list, FilterData selected) {

        LinearLayout ll_group = new LinearLayout(mContext);
        ll_group.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        ll_group.setBackgroundColor(getResources().getColor(R.color.gray_filter_item));

        final ListView listView_r = new ListView(mContext);
        listView_r.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 2));
        listView_r.setDividerHeight(0);
        listView_r.setId(mMenuListId + 100);

        final ListView listView_l = new ListView(mContext);
        listView_l.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 3));
        listView_l.setDividerHeight(0);
        listView_l.setId(mMenuListId);
        final ExplandFilterAdapter explandFilterAdapter = new ExplandFilterAdapter(mContext, list);
        listView_l.setAdapter(explandFilterAdapter);
        listView_l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                explandFilterAdapter.setCheckItem(position);
                setTabText(list.get(position).value);
                filterDatas.set(listView_l.getId(), list.get(position));

                if (mMenuClickListener != null)
                    mMenuClickListener.onItemClick(getKey());

                final int pos0 = position;
                if (list.get(position).expandableChildren != null) {
                    final SecondFilterAdapter filterAdapter = new SecondFilterAdapter(mContext, list.get(position).expandableChildren);
                    listView_r.setAdapter(filterAdapter);
                    listView_r.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            filterAdapter.setCheckItem(position);
                            setTabText(position == 0 ? list.get(pos0).value : list.get(pos0).expandableChildren.get(position).value);
                            if (position == 0) {
                                filterDatas.set(listView_l.getId(), list.get(pos0));
                            } else {
                                filterDatas.set(listView_l.getId(), list.get(pos0).expandableChildren.get(position));
                            }
                            closeMenu();

                            if (mMenuClickListener != null)
                                mMenuClickListener.onItemClick(getKey());
                        }
                    });
                } else {
                    listView_r.setAdapter(null);
                    closeMenu();
                }
            }
        });

        //默认选项 二级
        for (int j = 0; j < list.size(); j++) {
            if (selected != null) {
                if (list.get(j).key.equals(selected.key)) {
                    explandFilterAdapter.setCheckItem(j);

                    if (list.get(j).selectedExpandFilterData != null) {
                        for (int i = 0; i < list.get(j).expandableChildren.size(); i++) {
                            if (list.get(j).selectedExpandFilterData.key.equals(list.get(j).expandableChildren.get(i).key)) {
                                final SecondFilterAdapter filterAdapter = new SecondFilterAdapter(mContext, list.get(j).expandableChildren);
                                listView_r.setAdapter(filterAdapter);
                                filterAdapter.setCheckItem(i);

                                final int parent_pos = j;
                                listView_r.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        filterAdapter.setCheckItem(position);
                                        setTabText(position == 0 ? list.get(parent_pos).value : list.get(parent_pos).expandableChildren.get(position).value);
                                        if (position == 0) {
                                            filterDatas.set(listView_l.getId(), list.get(parent_pos));
                                        } else {
                                            filterDatas.set(listView_l.getId(), list.get(parent_pos).expandableChildren.get(position));
                                        }
                                        closeMenu();

                                        if (mMenuClickListener != null)
                                            mMenuClickListener.onItemClick(getKey());
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }

        ll_group.addView(listView_l);
        ll_group.addView(listView_r);

        mMenuListId += 1;
        menuList.add(ll_group);
    }

    //添加分类选项列表
    public void addTabList(final List<FilterData> list, FilterData selected) {
        final ListView listView = new ListView(mContext);
        listView.setDividerHeight(0);
        listView.setId(mMenuListId);
        final FilterAdapter filterAdapter = new FilterAdapter(mContext, list);
        listView.setAdapter(filterAdapter);
//        filterAdapter.setCheckItem(-1);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                filterAdapter.setCheckItem(position);
                setTabText(list.get(position).value);
                filterDatas.set(listView.getId(), list.get(position));
                closeMenu();
                if (mMenuClickListener != null)
                    mMenuClickListener.onItemClick(getKey());
            }
        });
        //默认选项 一级
        for (int j = 0; j < list.size(); j++) {
            if (selected != null) {
                if (list.get(j).key.equals(selected.key)) {
                    filterAdapter.setCheckItem(j);
                }
            }
        }
        mMenuListId += 1;
        menuList.add(listView);
    }

    public interface MenuClickListener {
        void onItemClick(List<FilterData> key);
    }

    public void setMenuClickListener(MenuClickListener menuClickListener) {
        this.mMenuClickListener = menuClickListener;
    }

    //设置分类标题及内容区域
    public void setMenu(@NonNull List<String> tabTexts, @NonNull View contentView) {
        setDropDownMenu(tabTexts, menuList, contentView);
        mTabTexts = tabTexts;
    }

    public List<FilterData> getKey() {
        return filterDatas;
    }

    /**
     * 初始化DropDownMenu
     *
     * @param tabTexts
     * @param popupViews
     * @param contentView
     */
    private void setDropDownMenu(@NonNull List<String> tabTexts, @NonNull List<View> popupViews, @NonNull View contentView) {
        if (tabTexts.size() != popupViews.size()) {
            throw new IllegalArgumentException("params not match, tabTexts.size() should be equal popupViews.size()");
        }

        for (int i = 0; i < tabTexts.size(); i++) {
            addTab(tabTexts, i);
        }
        containerView.addView(contentView);

        maskView = new View(getContext());
        maskView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        maskView.setBackgroundColor(maskColor);
        maskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
            }
        });
        containerView.addView(maskView);
        maskView.setVisibility(GONE);

        popupMenuViews = new FrameLayout(getContext());
        popupMenuViews.setVisibility(GONE);
        popupMenuViews.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, max_menu_height));
        containerView.addView(popupMenuViews);

        for (int i = 0; i < popupViews.size(); i++) {
            popupViews.get(i).setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            popupMenuViews.addView(popupViews.get(i), i);
        }

        for (int i = 0; i < mfilterDataWithHeaderList.size() * 2; i = i + 2) {
            FilterData nullFilterData = new FilterData();
            nullFilterData.value = "";
            nullFilterData.key = "0";

            int real_pos = (i == 0 ? 0 : (i / 2));

            //默认选项 标题
            if (mfilterDataWithHeaderList.get(real_pos) instanceof ExpandableFilterDataWithHeader) {
                if (((ExpandableFilterDataWithHeader) mfilterDataWithHeaderList.get(real_pos)).selectedFilterData != null) {
                    if (((ExpandableFilterDataWithHeader) mfilterDataWithHeaderList.get(real_pos)).filterDataList != null) {
                        ((TextView) ((LinearLayout) tabMenuView.getChildAt(i)).getChildAt(0)).setText(((ExpandableFilterDataWithHeader) mfilterDataWithHeaderList.get(real_pos)).selectedFilterData.value);
                        nullFilterData.value = ((ExpandableFilterDataWithHeader) mfilterDataWithHeaderList.get(real_pos)).selectedFilterData.value;
                        nullFilterData.key = ((ExpandableFilterDataWithHeader) mfilterDataWithHeaderList.get(real_pos)).selectedFilterData.key;

                        for (int k = 0; k < ((ExpandableFilterDataWithHeader) mfilterDataWithHeaderList.get(real_pos)).filterDataList.size(); k++) {
                            if (((ExpandableFilterDataWithHeader) mfilterDataWithHeaderList.get(real_pos)).filterDataList.get(k).selectedExpandFilterData != null) {
                                ((TextView) ((LinearLayout) tabMenuView.getChildAt(i)).getChildAt(0)).setText(((ExpandableFilterDataWithHeader) mfilterDataWithHeaderList.get(real_pos)).filterDataList.get(k).selectedExpandFilterData.value);
                                nullFilterData.value = ((ExpandableFilterDataWithHeader) mfilterDataWithHeaderList.get(real_pos)).filterDataList.get(k).selectedExpandFilterData.value;
                                nullFilterData.key = ((ExpandableFilterDataWithHeader) mfilterDataWithHeaderList.get(real_pos)).filterDataList.get(k).selectedExpandFilterData.key;
                            }
                        }
                    }
                }
            } else {
                if (mfilterDataWithHeaderList.get(real_pos).selectedFilterData != null) {
                    if (mfilterDataWithHeaderList.get(real_pos).filterDataList != null) {
                        ((TextView) ((LinearLayout) tabMenuView.getChildAt(i)).getChildAt(0)).setText(mfilterDataWithHeaderList.get(real_pos).selectedFilterData.value);
                        nullFilterData.value = mfilterDataWithHeaderList.get(real_pos).selectedFilterData.value;
                        nullFilterData.key = mfilterDataWithHeaderList.get(real_pos).selectedFilterData.key;
                    }
                }
            }
            filterDatas.set(real_pos, nullFilterData);
        }
    }

    private void addTab(@NonNull List<String> tabTexts, int i) {

        TextView tab = new TextView(getContext());
        tab.setSingleLine();
        tab.setEllipsize(TextUtils.TruncateAt.END);
        tab.setGravity(Gravity.CENTER);
        tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, menuTextSize);
        tab.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tab.setTextColor(textUnselectedColor);
        tab.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, menuUnselectedIcon), null);
        tab.setCompoundDrawablePadding(dpTpPx(8));
        tab.setText(tabTexts.get(i));
        tab.setPadding(dpTpPx(5), dpTpPx(12), dpTpPx(5), dpTpPx(12));
        tab.setEllipsize(TextUtils.TruncateAt.END);

        final LinearLayout ll_tab = new LinearLayout(getContext());
        ll_tab.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        ll_tab.setGravity(Gravity.CENTER);
        ll_tab.addView(tab);
        //添加点击事件
        ll_tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMenu(ll_tab);
            }
        });
        tabMenuView.addView(ll_tab);
        //添加分割线
        if (i < tabTexts.size() - 1) {
            View view = new View(getContext());
            LayoutParams divider = new LayoutParams(dpTpPx(0.5f), ViewGroup.LayoutParams.MATCH_PARENT);
            divider.setMargins(0, 20, 0, 20);
            view.setLayoutParams(divider);
            view.setBackgroundColor(dividerColor);
            tabMenuView.addView(view);
        }
    }

    /**
     * 改变tab文字
     *
     * @param text
     */
    private void setTabText(String text) {
        if (current_tab_position != -1) {
            ((TextView) ((LinearLayout) tabMenuView.getChildAt(current_tab_position)).getChildAt(0)).setText(text);
        }
    }

    public void setTabClickable(boolean clickable) {
        for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2) {
            tabMenuView.getChildAt(i).setClickable(clickable);
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (current_tab_position != -1) {
            ((TextView) ((LinearLayout) tabMenuView.getChildAt(current_tab_position)).getChildAt(0)).setTextColor(textUnselectedColor);
            ((TextView) ((LinearLayout) tabMenuView.getChildAt(current_tab_position)).getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(null, null,
                    ContextCompat.getDrawable(mContext, menuUnselectedIcon), null);
            popupMenuViews.setVisibility(View.GONE);
            popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.drop_down_menu_out));
            maskView.setVisibility(GONE);
            maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.drop_down_menu_mask_out));
            current_tab_position = -1;
        }

    }

    /**
     * DropDownMenu是否处于可见状态
     *
     * @return
     */
    public boolean isShowing() {
        return current_tab_position != -1;
    }

    /**
     * 切换菜单
     *
     * @param target
     */
    private void switchMenu(View target) {
        System.out.println(current_tab_position);
        for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2) {
            if (target == tabMenuView.getChildAt(i)) {
                if (current_tab_position == i) {
                    closeMenu();
                } else {
                    if (current_tab_position == -1) {
                        popupMenuViews.setVisibility(View.VISIBLE);
                        popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.drop_down_menu_in));
                        maskView.setVisibility(VISIBLE);
                        maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.drop_down_menu_mask_in));
                        popupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);
                    } else {
                        popupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);
                    }
                    current_tab_position = i;
                    ((TextView) ((LinearLayout) tabMenuView.getChildAt(i)).getChildAt(0)).setTextColor(textSelectedColor);
                    ((TextView) ((LinearLayout) tabMenuView.getChildAt(i)).getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(null, null,
                            ContextCompat.getDrawable(mContext, menuSelectedIcon), null);
                }
            } else {
                ((TextView) ((LinearLayout) tabMenuView.getChildAt(i)).getChildAt(0)).setTextColor(textUnselectedColor);
                ((TextView) ((LinearLayout) tabMenuView.getChildAt(i)).getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(null, null,
                        ContextCompat.getDrawable(mContext, menuUnselectedIcon), null);
                popupMenuViews.getChildAt(i / 2).setVisibility(View.GONE);
            }
        }
    }

    private int dpTpPx(float value) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5);
    }
}
