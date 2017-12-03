/*
 * Copyright (C) 2016 Peng fei Pan <sky@panpf.me>
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
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * 蛛网评分图，支持任意条边以及任意层级
 */
public class SpiderWebScoreView extends View {
    private int angleCount = 5; // 整个蛛网有几个角
    private int hierarchyCount = 5;  // 整个蛛网分多少层（例如最大分数是10分，分5层，那么每层就代表2分）
    private int lineColor = 0xFF000000; // 蛛网线条的颜色
    private float lineWidth = -1; // 蛛网线条的宽度

    private float maxScore = 10f;   // 最大分数
    private float[] scores;  // 分数列表
    private int scoreColor = 0x80F65801; // 分数图形的颜色
    private int scoreStrokeColor = 0xFFF65801; // 分数图形描边的颜色
    private float scoreStrokeWidth = -1; // 分数图形描边的宽度
    private boolean disableScoreStroke; // 禁用分数图形的描边
    private Paint scorePaint;
    private Paint scoreStrokePaint;

    private float centerX;    // 中心点X坐标
    private float centerY;    // 中心点Y坐标
    private float radius; // 整个蛛网图的半径
    private Paint linePaint;
    private Path path;

    public SpiderWebScoreView(Context context) {
        super(context);
        init(context, null);
    }

    public SpiderWebScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SpiderWebScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if(attrs != null){
            parseAttrs(context, attrs);
        }

        linePaint = new Paint();
        linePaint.setColor(lineColor);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);
        if(lineWidth > 0){
            linePaint.setStrokeWidth(lineWidth);
        }

        scorePaint = new Paint();
        scorePaint.setColor(scoreColor);
        scorePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        scorePaint.setAntiAlias(true);

        path = new Path();

        if(isInEditMode()){
            float[] randomScoreArray = new float[]{7.0f, 8.0f, 5.0f, 5.0f, 8.0f};
            float[] testScores = new float[angleCount];
            int index = 0;
            for(int w = 0; w < angleCount; w++){
                testScores[w] = randomScoreArray[index++% randomScoreArray.length];
            }
            setScores(10f, testScores);
        }
    }

    private void parseAttrs(Context context, AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SpiderWebScoreView);

        setAngleCount(typedArray.getInt(R.styleable.SpiderWebScoreView_angleCount, angleCount));
        setHierarchyCount(typedArray.getInt(R.styleable.SpiderWebScoreView_hierarchyCount, hierarchyCount));
        setMaxScore(typedArray.getFloat(R.styleable.SpiderWebScoreView_maxScore, maxScore));

        setLineColor(typedArray.getColor(R.styleable.SpiderWebScoreView_lineColor, lineColor));
        setLineWidth(typedArray.getDimension(R.styleable.SpiderWebScoreView_lineWidth, lineWidth));
        setScoreColor(typedArray.getColor(R.styleable.SpiderWebScoreView_scoreColor, scoreColor));
        setScoreStrokeColor(typedArray.getColor(R.styleable.SpiderWebScoreView_scoreStrokeColor, scoreStrokeColor));
        setScoreStrokeWidth(typedArray.getDimension(R.styleable.SpiderWebScoreView_scoreStrokeWidth, scoreStrokeWidth));
        setDisableScoreStroke(typedArray.getBoolean(R.styleable.SpiderWebScoreView_disableScoreStroke, disableScoreStroke));

        typedArray.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        reset();
    }

    private void reset(){
        if(angleCount != 0 && hierarchyCount != 0){
            int viewWidth = getWidth();
            int viewHeight = getHeight();
            centerX = viewWidth / 2;
            centerY = viewHeight / 2;
            radius = Math.min(viewWidth, viewHeight) / 2;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawAllHierarchy(canvas);
        drawAllLine(canvas);
        drawScore(canvas);
    }

    /**
     * 绘制所有的层
     * @param canvas Canvas
     */
    private void drawAllHierarchy(Canvas canvas) {
        float averageRadius = radius / hierarchyCount;
        for(int w = 0; w < hierarchyCount; w++){
            drawHierarchyByRadius(canvas, averageRadius *(w+1));
        }
    }

    /**
     * 根据半径绘制一层
     * @param canvas Canvas
     * @param currentRadius 当前半径
     */
    private void drawHierarchyByRadius(Canvas canvas, float currentRadius) {
        path.reset();

        float nextAngle;
        float nextRadians;
        float nextPointX;
        float nextPointY;
        float averageAngle = 360 / angleCount;
        float offsetAngle = averageAngle > 0 && angleCount % 2 == 0 ? averageAngle / 2 : 0;
        for (int position = 0; position < angleCount; position++) {
            nextAngle = offsetAngle + (position * averageAngle);
            nextRadians = (float) Math.toRadians(nextAngle);
            nextPointX = (float) (centerX + Math.sin(nextRadians) * currentRadius);
            nextPointY = (float) (centerY - Math.cos(nextRadians) * currentRadius);

            if(position == 0){
                path.moveTo(nextPointX, nextPointY);
            }else{
                path.lineTo(nextPointX, nextPointY);
            }
        }

        path.close();
        canvas.drawPath(path, linePaint);
    }

    /**
     * 绘制所有的线
     * @param canvas Canvas
     */
    private void drawAllLine(Canvas canvas){
        float nextAngle;
        float nextRadians;
        float nextPointX;
        float nextPointY;
        float averageAngle = 360 / angleCount;
        float offsetAngle = averageAngle > 0 && angleCount % 2 == 0 ? averageAngle / 2 : 0;
        for(int position = 0; position < angleCount; position++){
            nextAngle = offsetAngle + (position * averageAngle);
            nextRadians = (float) Math.toRadians(nextAngle);
            nextPointX = (float) (centerX + Math.sin(nextRadians) * radius);
            nextPointY = (float) (centerY - Math.cos(nextRadians) * radius);

            canvas.drawLine(centerX, centerY, nextPointX, nextPointY, linePaint);
        }
    }

    /**
     * 绘制分数图形
     * @param canvas Canvas
     */
    private void drawScore(Canvas canvas){
        if(scores == null || scores.length <= 0){
            return;
        }

        path.reset();

        float nextAngle;
        float nextRadians;
        float nextPointX;
        float nextPointY;
        float currentRadius;
        float averageAngle = 360 / angleCount;
        float offsetAngle = averageAngle > 0 && angleCount % 2 == 0 ? averageAngle / 2 : 0;
        for (int position = 0; position < angleCount; position++) {
            currentRadius = (scores[position] / maxScore) * radius;
            nextAngle = offsetAngle + (position * averageAngle);
            nextRadians = (float) Math.toRadians(nextAngle);
            nextPointX = (float) (centerX + Math.sin(nextRadians) * currentRadius);
            nextPointY = (float) (centerY - Math.cos(nextRadians) * currentRadius);

            if(position == 0){
                path.moveTo(nextPointX, nextPointY);
            }else{
                path.lineTo(nextPointX, nextPointY);
            }
        }

        path.close();
        canvas.drawPath(path, scorePaint);

        // 绘制描边
        if(!disableScoreStroke){
            if(scoreStrokePaint == null){
                scoreStrokePaint = new Paint();
                scoreStrokePaint.setColor(scoreStrokeColor);
                scoreStrokePaint.setStyle(Paint.Style.STROKE);
                scoreStrokePaint.setAntiAlias(true);
                if(scoreStrokeWidth > 0){
                    scoreStrokePaint.setStrokeWidth(scoreStrokeWidth);
                }
            }
            canvas.drawPath(path, scoreStrokePaint);
        }
    }

    /**
     * 设置蛛网有多少个角
     * @param angleCount 蛛网有多少个角
     */
    private void setAngleCount(int angleCount) {
        if(angleCount <= 2){
            throw new IllegalArgumentException("angleCount Can not be less than or equal to 2");
        }
        this.angleCount = angleCount;
        reset();
        postInvalidate();
    }

    /**
     * 设置最大分数
     * @param maxScore 最大分数
     */
    private void setMaxScore(float maxScore) {
        if(maxScore <= 0){
            throw new IllegalArgumentException("maxScore Can not be less than or equal to 0");
        }
        this.maxScore = maxScore;
    }

    /**
     * 设置分数，有多少个人数就有多少个角
     * @param maxScore 最大分数
     * @param scores 分数
     */
    public void setScores(float maxScore, float[] scores) {
        if(scores == null || scores.length == 0){
            throw new IllegalArgumentException("scores Can't be null or empty");
        }
        setMaxScore(maxScore);
        this.scores = scores;
        this.angleCount = scores.length;
        reset();
        postInvalidate();
    }

    /**
     * 设置有整个蛛网有多少层
     * @param hierarchyCount 层数
     */
    public void setHierarchyCount(int hierarchyCount) {
        if(hierarchyCount <= 0){
            throw new IllegalArgumentException("hierarchyCount Can not be less than or equal to 0");
        }
        this.hierarchyCount = hierarchyCount;
        reset();
        postInvalidate();
    }

    /**
     * 设置蛛网线的颜色
     * @param lineColor 蛛网线的颜色
     */
    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
        if(linePaint != null){
            linePaint.setColor(lineColor);
        }
        postInvalidate();
    }

    /**
     * 设置蛛网线的宽度
     * @param lineWidth 蛛网线的宽度
     */
    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        if(linePaint != null){
            linePaint.setStrokeWidth(lineWidth);
        }
        postInvalidate();
    }

    /**
     * 设置分数图形的颜色
     * @param scoreColor 分数图形的颜色
     */
    public void setScoreColor(int scoreColor) {
        this.scoreColor = scoreColor;
        if(scorePaint != null){
            scorePaint.setColor(scoreColor);
        }
        postInvalidate();
    }

    /**
     * 设置分数图形描边的颜色
     * @param scoreStrokeColor 分数图形描边的颜色
     */
    public void setScoreStrokeColor(int scoreStrokeColor) {
        this.scoreStrokeColor = scoreStrokeColor;
        if(scoreStrokePaint != null){
            scoreStrokePaint.setColor(scoreStrokeColor);
        }
        postInvalidate();
    }

    /**
     * 设置分数图形描边的宽度
     * @param scoreStrokeWidth 分数图形描边的宽度
     */
    public void setScoreStrokeWidth(float scoreStrokeWidth) {
        this.scoreStrokeWidth = scoreStrokeWidth;
        if(scoreStrokePaint != null){
            scoreStrokePaint.setStrokeWidth(scoreStrokeWidth);
        }
        postInvalidate();
    }

    /**
     * 设置禁用分数图形的描边
     * @param disableScoreStroke 是否禁用分数图形的描边
     */
    public void setDisableScoreStroke(boolean disableScoreStroke) {
        this.disableScoreStroke = disableScoreStroke;
        postInvalidate();
    }
}
