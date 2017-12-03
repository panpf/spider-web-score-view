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

package me.panpf.swsv.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.TextView;

import me.panpf.swsv.CircularLayout;
import me.panpf.swsv.SpiderWebScoreView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SpiderWebScoreView spiderWebScoreView1 = (SpiderWebScoreView) findViewById(R.id.spiderWeb_mainActivity_1);
        SpiderWebScoreView spiderWebScoreView2 = (SpiderWebScoreView) findViewById(R.id.spiderWeb_mainActivity_2);
        SpiderWebScoreView spiderWebScoreView3 = (SpiderWebScoreView) findViewById(R.id.spiderWeb_mainActivity_3);
        SpiderWebScoreView spiderWebScoreView4 = (SpiderWebScoreView) findViewById(R.id.spiderWeb_mainActivity_4);
        SpiderWebScoreView spiderWebScoreView5 = (SpiderWebScoreView) findViewById(R.id.spiderWeb_mainActivity_5);

        CircularLayout circularLayout1 = (CircularLayout) findViewById(R.id.layout_mainActivity_circular1);
        CircularLayout circularLayout2 = (CircularLayout) findViewById(R.id.layout_mainActivity_circular2);
        CircularLayout circularLayout3 = (CircularLayout) findViewById(R.id.layout_mainActivity_circular3);
        CircularLayout circularLayout4 = (CircularLayout) findViewById(R.id.layout_mainActivity_circular4);
        CircularLayout circularLayout5 = (CircularLayout) findViewById(R.id.layout_mainActivity_circular5);

        setup(spiderWebScoreView1, circularLayout1, new Score(7.0f), new Score(8.0f), new Score(5.0f));
        setup(spiderWebScoreView2, circularLayout2, new Score(7.0f), new Score(8.0f), new Score(5.0f), new Score(5.0f), new Score(8.0f));
        setup(spiderWebScoreView3, circularLayout3, new Score(7.0f), new Score(8.0f), new Score(5.0f), new Score(5.0f), new Score(8.0f), new Score(7.0f), new Score(8.0f), new Score(5.0f));
        setup(spiderWebScoreView4, circularLayout4, new Score(7.0f), new Score(8.0f), new Score(5.0f), new Score(5.0f), new Score(8.0f), new Score(7.0f), new Score(8.0f), new Score(5.0f), new Score(5.0f), new Score(8.0f), new Score(7.0f), new Score(8.0f));
        setup(spiderWebScoreView5, circularLayout5, new Score(7.0f, R.drawable.vip_icon7), new Score(8.0f, R.drawable.vip_icon8), new Score(5.0f, R.drawable.vip_icon5), new Score(5.0f, R.drawable.vip_icon5), new Score(8.0f, R.drawable.vip_icon8), new Score(7.0f, R.drawable.vip_icon7));
    }

    private void setup(SpiderWebScoreView spiderWebScoreView, CircularLayout circularLayout, Score... scores) {
        spiderWebScoreView.setScores(10f, assembleScoreArray(scores));

        circularLayout.removeAllViews();
        for (Score score : scores) {
            TextView scoreTextView = (TextView) LayoutInflater.from(getBaseContext()).inflate(R.layout.score, circularLayout, false);
            scoreTextView.setText(String.valueOf(score.score));
            if (score.iconId != 0) {
                scoreTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, score.iconId, 0);
            }
            circularLayout.addView(scoreTextView);
        }
    }

    private float[] assembleScoreArray(Score... scores) {
        float[] scoreArray = new float[scores.length];
        for (int w = 0; w < scores.length; w++) {
            scoreArray[w] = scores[w].score;
        }
        return scoreArray;
    }

    private static class Score {
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
}
