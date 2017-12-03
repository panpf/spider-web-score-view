/*
 * Copyright (C) 2017 Peng fei Pan <sky@panpf.me>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.panpf.swsv;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 圆形布局，所有子View按照圆形排列并且在圆圈的外面
 * <br>值的注意的是直径多大CircularLayout的measure尺寸就是多大，因此你需要将CircularLayout以及他的父ViewGroup的clipChild都设为false，并且让父ViewGroup大一些才能显示出CircularLayout的子View
 */
public class CircularLayout extends ViewGroup {
    private static final int LOCATION_EAST = 1;
    private static final int LOCATION_WEST = 2;
    private static final int LOCATION_SOUTH = 3;
    private static final int LOCATION_NORTH = 4;
    private static final int LOCATION_EAST_NORTH = 5;
    private static final int LOCATION_EAST_SOUTH = 6;
    private static final int LOCATION_WEST_NORTH = 7;
    private static final int LOCATION_WEST_SOUTH = 8;

    private int spacing;    // 子View与圆环之间的间距

    private float centerX;    // 中心点X坐标
    private float centerY;    // 中心点Y坐标
    private float radius;   // 半径
    private int childCount; // 子View个数

    public CircularLayout(Context context) {
        super(context);
        init(context, null);
    }

    public CircularLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircularLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if(attrs != null){
            parseAttrs(context, attrs);
        }
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
        int defaultSpacing = (int) (context.getResources().getDisplayMetrics().density * 8 + 0.5f);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircularLayout);

        setSpacing((int) typedArray.getDimension(R.styleable.CircularLayout_spacing, defaultSpacing));

        typedArray.recycle();
    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        super.addView(child, index, params);
        childCount = getChildCount();
        reset();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        reset();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void reset(){
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        centerX = viewWidth / 2;
        centerY = viewHeight / 2;
        radius = Math.min(viewWidth, viewHeight) / 2;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if(childCount == 0){
            return;
        }

        // 循环处理每个位置的子View，先计算出子View在圆上所处的位置，然后按照其子View的宽高偏移一定的距离，保证子View全部在圆圈之外，并且子View的中心点和其圆上的点连同圆心在一条直线上
        View childView;
        float nextAngle;
        float nextRadians;
        float nextPointX;
        float nextPointY;
        int childViewMeasuredWidth;
        int childViewMeasuredHeight;
        float childViewLeft;
        float childViewTop;
        float averageAngle = childCount > 0 ? 360 / childCount : 0;
        float offsetAngle = averageAngle > 0 && childCount % 2 == 0 ? averageAngle / 2 : 0;    // 偏移角度，有助于让整个图形左右对称
        for (int position = 0, size = getChildCount(); position < size; position++) {
            childView = getChildAt(position);
            nextAngle = offsetAngle + (position * averageAngle);
            nextRadians = (float) Math.toRadians(nextAngle);
            nextPointX = (float) (centerX + Math.sin(nextRadians) * radius);
            nextPointY = (float) (centerY - Math.cos(nextRadians) * radius);

            childViewMeasuredWidth = childView.getMeasuredWidth();
            childViewMeasuredHeight = childView.getMeasuredHeight();
            childViewLeft = nextPointX;
            childViewTop = nextPointY;
            switch (calculateLocationByAngle(nextAngle)){
                case LOCATION_NORTH :
                    childViewLeft -= childViewMeasuredWidth / 2;
                    childViewTop -= childViewMeasuredHeight;

                    childViewTop -= spacing;
                    break;
                case LOCATION_EAST_NORTH :
                    childViewTop -= childViewMeasuredHeight / 2;

                    childViewLeft += spacing;
                    childViewTop -= spacing;
                    break;
                case LOCATION_EAST :
                    childViewTop -= childViewMeasuredHeight / 2;

                    childViewLeft += spacing;
                    break;
                case LOCATION_EAST_SOUTH :
                    childViewLeft += spacing;
                    childViewTop += spacing;
                    break;
                case LOCATION_SOUTH :
                    childViewLeft -= childViewMeasuredWidth / 2;

                    childViewTop += spacing;
                    break;
                case LOCATION_WEST_SOUTH :
                    childViewLeft -= childViewMeasuredWidth;

                    childViewLeft -= spacing;
                    childViewTop += spacing;
                    break;
                case LOCATION_WEST :
                    childViewLeft -= childViewMeasuredWidth;
                    childViewTop -= childViewMeasuredHeight / 2;

                    childViewLeft -= spacing;
                    break;
                case LOCATION_WEST_NORTH :
                    childViewLeft -= childViewMeasuredWidth;
                    childViewTop -= childViewMeasuredHeight / 2;

                    childViewLeft -= spacing;
                    childViewTop -= spacing;
                    break;
            }

            childView.layout((int) childViewLeft, (int) childViewTop, (int) (childViewLeft + childViewMeasuredWidth), (int) (childViewTop + childViewMeasuredHeight));
        }
    }

    /**
     * 根据角度判断所处的方位，CircularLayout把一个圆分成了8个方位（东、南、西、北、西北、东北、西南、东南），不同的方位有不同的偏移方式
     * @param angle 角度
     * @return 方位
     */
    private int calculateLocationByAngle(float angle) {
        if ((angle >= 337.5f && angle <= 360f) || (angle >= 0f && angle <= 22.5f)) {
            return LOCATION_NORTH;
        } else if (angle >= 22.5f && angle <= 67.5f) {
            return LOCATION_EAST_NORTH;
        } else if (angle >= 67.5f && angle <= 112.5f) {
            return LOCATION_EAST;
        } else if (angle >= 112.5f && angle <= 157.5) {
            return LOCATION_EAST_SOUTH;
        } else if (angle >= 157.5 && angle <= 202.5) {
            return LOCATION_SOUTH;
        } else if (angle >= 202.5 && angle <= 247.5) {
            return LOCATION_WEST_SOUTH;
        } else if (angle >= 247.5 && angle <= 292.5) {
            return LOCATION_WEST;
        } else if (angle >= 292.5 && angle <= 337.5) {
            return LOCATION_WEST_NORTH;
        }else{
            throw new IllegalArgumentException("error angle " + angle);
        }
    }

    /**
     * 设置内容和圆圈之间的距离，默认为8dp
     * @param spacing 内容和圆圈之间的距离
     */
    public void setSpacing(int spacing) {
        this.spacing = spacing;
        requestLayout();
    }
}
