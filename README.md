# ![logo_image] SpiderWebScoreView

![Platform][platform_image]
[![API][min_api_image]][min_api_link]
[![Android Arsenal][android_arsenal_image]][android_arsenal-link]
[![Release Version][version_icon]][version_link]

SpiderWebScoreView 是一个蛛网评分控件，可以方便的显示任意层级任意维度的数据

![sample_image]

## 特点
* 支持`任意个角`以及`任意层级`
* 蛛网图形外边的分数文案支持`任意样式`
* 不管是多少维度都可`左右对称`

## 开始使用

### 1. 从 mavenCentral 导入 SpiderWebScoreView

在 app 的 build.gradle 文件的 dependencies 节点中加入依赖

```kotlin
dependencies{
	implementation("io.github.panpf.spiderwebscoreview:spiderwebscoreview:${LAST_VERSION}")
}
```

`${LAST_VERSION}`: [![Download][version_icon]][version_link] (No include 'v')

最低支持 `Android2.3`

### 2. 在布局中使用

在布局中使用声明 SpiderWebScoreView 和 CircularLayout，如下：

```xml
<FrameLayout
    android:layout_width="match_parent"
    android:layout_height="150dp"
    android:clipChildren="false">

    <me.panpf.swsv.SpiderWebScoreView
        android:id="@+id/spiderWeb_mainActivity_1"
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:layout_gravity="center" />

    <me.panpf.swsv.CircularLayout
        android:id="@+id/layout_mainActivity_circular1"
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:layout_gravity="center"
        android:clipChildren="false"/>
</FrameLayout>

```
详解：
* SpiderWebScoreView 用来显示蛛网图形
* CircularLayout 用来显示四周的文案
* CircularLayout 的 `尺寸` 必须和 SpiderWebScoreView `一致` 并且是层叠关系
* CircularLayout 的子 View 将会显示在圆圈的外面，因此 `CircularLayout` 和其 `父 FrameLayout` 都必须设置 `clipChildren 为 false`
* 父 FrameLayout 还要比 CircularLayout `大一圈`，大的这一圈就是用来显示 CircularLayout 的子 View，具体大多少视你的需求而定

### 3. 在代码中设置分值

在代码中通过 SpiderWebScoreView.setScores(float maxScore, float[] scores) 方法设置最大分数以及所有分值即可

* 有多少个分值（scores.length）蛛网图形就有多少个角
* 默认分 5 层

然后根据你的需求往 CircularLayout 里面添加显示分数的 View 即可，数量和顺序必须同 scores 相同，如下：

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

private static class Score{
    private float score;
    private int iconId;

    private Score(float score, int iconId) {
        this.score = score;
        this.iconId = iconId;
    }

    private Score(float score) {
        this.score = score;
    }
}
```

### 4. 布局属性

SpiderWebScoreView

|属性名|介绍|对应方法|缺省值|
|:--|:--|:--|:--|
|angleCount|设置蛛网图形有多少个角|会在setScores(float, float[])方法中根据scores的长度来覆盖此参数|5|
|hierarchyCount|设置蛛网图形有多少层|setHierarchyCount(int)|5|
|maxScore|最大分值|setScores(float, float[])方法的第一个参数就是maxScore|10f|
|lineColor|蛛网线条的颜色|setLineColor(int)|0xFF000000|
|lineWidth|蛛网线条的宽度|setLineWidth(float)|-1(不设置，Paint默认宽度)|
|scoreColor|分数图形的颜色|setScoreColor(int)|0x80F65801|
|scoreStrokeColor|分数图形描边的颜色|setScoreStrokeColor(int)|0xFFF65801|
|scoreStrokeWidth|分数图形描边的宽度|setScoreStrokeWidth(float)|-1(不设置，Paint默认宽度)
|disableScoreStroke|禁用分数图形描边|setDisableScoreStroke(boolean)|false|

CircularLayout

|属性名|介绍|对应方法|缺省值|
|:--|:--|:--|:--|
|spacing|设置子View与圆圈之间的距离|setSpacing(int)|8dp|

更多示例请参考 sample 源码

## License
    Copyright (C) 2017 Peng fei Pan <sky@panpf.me>

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[logo_image]: https://github.com/panpf/spider-web-score-view/raw/master/docs/icon.png
[platform_image]: https://img.shields.io/badge/Platform-Android-brightgreen.svg
[min_api_image]: https://img.shields.io/badge/API-9%2B-orange.svg
[min_api_link]: https://android-arsenal.com/api?level=9
[android_arsenal_image]: https://img.shields.io/badge/Android%20Arsenal-SpiderWebScoreView-green.svg?style=true
[android_arsenal-link]: https://android_arsenal.com/details/1/4167
[version_icon]: https://img.shields.io/maven-central/v/io.github.panpf.spiderwebscoreview/spiderwebscoreview
[version_link]: https://repo1.maven.org/maven2/io/github/panpf/spiderwebscoreview/
[sample_image]: https://github.com/panpf/spider-web-score-view/raw/master/docs/sample.png
