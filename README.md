#![Logo](https://github.com/xiaopansky/SpiderWebScoreView/raw/master/docs/icon.png) SpiderWebScoreView

SpiderWebScoreView是用于Android上的一个蛛网评分控件

![sample](https://github.com/xiaopansky/SpiderWebScoreView/raw/master/docs/sample.png)

###Features
>* 支持`任意个角`以及`任意层级`
>* 蛛网图形外边的分数文案支持`任意样式`
>* 不管是多少维度都可`左右对称`

###Usage Guide

####1. Import SpiderWebScoreView
add gradle dependency
```groovy
dependencies{
	compile 'me.xiaopan:spiderwebscoreview:lastVersionName'
}
```
`lastVersionName`是最新版本名称的意思，你可以在[release](https://github.com/xiaopansky/SpiderWebScoreView/releases)页面看到最新的版本名称
最低支持`Android2.2`

####2. Use in layout
首先在布局中使用声明SpiderWebScoreView和CircularLayout
```xml
<FrameLayout
    android:layout_width="match_parent"
    android:layout_height="150dp"
    android:clipChildren="false">

    <me.xiaopan.swsv.SpiderWebScoreView
        android:id="@+id/spiderWeb_mainActivity_1"
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:layout_gravity="center" />

    <me.xiaopan.swsv.CircularLayout
        android:id="@+id/layout_mainActivity_circular1"
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:layout_gravity="center"
        android:clipChildren="false"/>
</FrameLayout>
```
详解：
>* SpiderWebScoreView用来显示蛛网图形
>* CircularLayout用来显示四周的文案
>* CircularLayout的`尺寸必须`和SpiderWebScoreView`一致`并且是层叠关系
>* CircularLayout的子View将会显示在圆圈的外面，因此`CircularLayout`和其`父FrameLayout`都必须设置`clipChildren为false`
>* 父FrameLayout还要比CircularLayout`大一圈`，大的这一圈就是用来显示CircularLayout的子View，具体大多少视你的需求而定

####3. Run time settings
然后在代码中通过SpiderWebScoreView.setScores(float maxScore, float[] scores)方法设置最大分数以及所有分值即可
>* 有多少个分值（scores.length）蛛网图形就有多少个角
>* 默认分5层

最后根据你的需求往CircularLayout里面添加显示分数的View即可，数量和顺序必须同scores相同

如下：
```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    SpiderWebScoreView spiderWebScoreView1 = (SpiderWebScoreView) findViewById(R.id.spiderWeb_mainActivity_1);

    CircularLayout circularLayout1 = (CircularLayout) findViewById(R.id.layout_mainActivity_circular1);

	Score[] scores = new Score[]{
		new Score(7.0f, R.drawable.vip_icon7),
		new Score(8.0f, R.drawable.vip_icon8),
		new Score(5.0f, R.drawable.vip_icon5),
		new Score(5.0f, R.drawable.vip_icon5),
		new Score(8.0f, R.drawable.vip_icon8),
		new Score(7.0f, R.drawable.vip_icon7),
	};
    setup(spiderWebScoreView1, circularLayout1, scores);
}

private void setup(SpiderWebScoreView spiderWebScoreView, CircularLayout circularLayout, Score... scores){
	float[] scoreArray = new float[scores.length];
    for(int w = 0; w < scores.length; w++){
        scoreArray[w] = scores[w].score;
    }
    spiderWebScoreView.setScores(10f, scoreArray);

    circularLayout.removeAllViews();
    for(Score score : scores){
        TextView scoreTextView = (TextView) LayoutInflater.from(getBaseContext()).inflate(R.layout.score, circularLayout, false);
        scoreTextView.setText(score.score+"");
        if(score.iconId != 0){
            scoreTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, score.iconId, 0);
        }
        circularLayout.addView(scoreTextView);
    }
}

public static class Score{
    public float score;
    public int iconId;

    public Score(float score, int iconId) {
        this.score = score;
        this.iconId = iconId;
    }

    public Score(float score) {
        this.score = score;
    }
}
```

####4. Attributes
SpiderWebScoreView
|Name|介绍|对应方法|缺省值|
|--|--|--|--|
|dimensionCount|设置蛛网图形有多少个角|会在setScores(float, float[])方法中根据scores的长度设置|5|
|hierarchyCount|设置蛛网图形有多少层|setHierarchyCount(int)|5|
|maxScore|最大分值|setScores(float, float[])方法的第一个参数就是maxScore|10f|
|lineColor|蛛网线条的颜色|setLineColor(int)|<font color=#000000>0xFF000000</font>|
|lineWidth|蛛网线条的宽度|setLineWidth(float)|-1(不设置，Paint默认宽度)|
|scoreColor|分数图形的颜色|setScoreColor(int)|<font color=#F65801>0x80F65801</font>|
|scoreStrokeColor|分数图形描边的颜色|setScoreStrokeColor(int)|<font color=#F65801>0xFFF65801</font>|
|scoreStrokeWidth|分数图形描边的宽度|setScoreStrokeWidth(float)|-1(不设置，Paint默认宽度)
|disableScoreStroke|禁用分数图形描边|setDisableScoreStroke(boolean)|false|

CircularLayout
|Name|介绍|对应方法|缺省值|
|--|--|--|--|
|spacing|设置子View与圆圈之间的距离|setSpacing(int)|8dp|

更多示例请参考sample源码

License
-------
	Copyright (C) 2015 Peng fei Pan <sky@xiaopan.me>

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	  http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and limitations under the License.