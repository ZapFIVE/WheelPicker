package com.zap.wheelpicker.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import com.zap.picker.DoublePicker;
import com.zap.picker.SinglePicker;
import com.zap.picker.ThreePicker;
import com.zap.picker.WheelPicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Demo
 * <p>
 * Created by Will on 2016/10/17.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
    }

    public void singleOnClick(View view) {
        String[] a = new String[]{
                "天南地北",
                "云里雾里",
                "半藏",
                "源氏",
                "狂鼠",
                "禅雅塔",
                "守望先锋",
                "马里奥",
                "地头杀",
                "天蝎座",
                "麦克雷",
                "猩猩",
                "奶"
        };
        SinglePicker singlePicker = new SinglePicker(this, Arrays.asList(a));
        singlePicker.setOffSet(2);
        singlePicker.setLabel("分钟");
        singlePicker.setMaskVisible(true);
        singlePicker.setTextSize(15, 25);
        singlePicker.setSelectedIndex(a.length / 2);
        singlePicker.setOnSinglePickListener(new SinglePicker.OnSinglePickListener() {
            @Override
            public void onSinglePick(String text) {
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });

        singlePicker.show();
    }

    public void doubleOnClick(View view) {
        DoublePicker doublePicker = new DoublePicker(this);
        doublePicker.setLabel("时", "分");
        doublePicker.setMaskVisible(false);
        doublePicker.setTextSize(20, 30);
        doublePicker.setFirstItemList(createList(30));
        doublePicker.setSupportRelevance(true);
        doublePicker.setRelevanceType(WheelPicker.RelevanceType.STAY_IN_PLACE);
        doublePicker.setRelevanceRule(createRule());
        doublePicker.setOnDoublePickListener(new DoublePicker.OnDoublePickListener() {
            @Override
            public void onDoublePicked(String firstSelectValue, String secondSelectValue) {
                Toast.makeText(MainActivity.this, firstSelectValue + "时" + secondSelectValue + "分", Toast.LENGTH_SHORT).show();
            }
        });

        doublePicker.show();
    }

    public void threeOnClick(View view) {
        ThreePicker threePicker = new ThreePicker(this);
        threePicker.setLabel("时", "分", "秒");
        threePicker.setMaskVisible(false);
        threePicker.setTextSize(20, 30);
        threePicker.setFirstList(createList(30));
        threePicker.setSupportRelevance(true);
        threePicker.setRelevanceRule(createRule(), createRule());
        threePicker.setOnThreePickListener(new ThreePicker.OnThreePickListener() {
            @Override
            public void onThreePicked(String firstSelectValue, String secondSelectValue, String threeSelectValue) {
                Toast.makeText(MainActivity.this, firstSelectValue + "时" + secondSelectValue + "分" + threeSelectValue + "秒", Toast.LENGTH_SHORT).show();
            }
        });
        threePicker.show();
    }

    private SparseArray<List<String>> createRule() {
        SparseArray<List<String>> rule = new SparseArray<>();
        for (int i = 0; i < 30; i++) {
            rule.put(i, createList(20));
        }
        Log.w("num", "rule   " + rule.toString());
        return rule;
    }

    private List<String> createList(int num) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            list.add(String.format(Locale.getDefault(), "%02d", (int) (Math.random() * 99)));
        }
        return list;
    }
}
