package com.zap.wheelpicker.demo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zap.picker.DoubleLayout;
import com.zap.picker.DoublePicker;
import com.zap.picker.SingleLayout;
import com.zap.picker.SinglePicker;
import com.zap.picker.ThreeLayout;
import com.zap.picker.ThreePicker;

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

    private SingleLayout singleLayout;
    private DoubleLayout doubleLayout;
    private ThreeLayout threeLayout;
    private TextView tv_result_1;
    private TextView tv_result_2;
    private TextView tv_result_3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        tv_result_1 = (TextView) findViewById(R.id.tv_result_1);
        tv_result_2 = (TextView) findViewById(R.id.tv_result_2);
        tv_result_3 = (TextView) findViewById(R.id.tv_result_3);

        singleLayout = (SingleLayout) findViewById(R.id.singleLayout);
        List<String> singleList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            singleList.add("第" + i + "点");
        }
        singleLayout.setItems(singleList);
        singleLayout.setOnSingleLayoutListener(new SingleLayout.OnSingleLayoutListener() {
            @Override
            public void onSelected(String item) {
                tv_result_1.setText(item);
            }
        });

        doubleLayout = (DoubleLayout) findViewById(R.id.doubleLayout);
        List<String> firstList = new ArrayList<>();
        List<String> secondList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            firstList.add(String.format(Locale.getDefault(), "%02d", i));
        }
        for (int i = 0; i < 60; i++) {
            secondList.add(String.format(Locale.getDefault(), "%02d", i));
        }
        // 正常不联动如下
        //        doubleLayout.setItems(firstList, secondList, "12", "25");
        //设定联动
        doubleLayout.setListAndRelevanceRule(firstList, createRule(), "12", "25");

        doubleLayout.setOnDoubleLayoutListener(new DoubleLayout.OnDoubleLayoutListener() {
            @Override
            public void onSelected(String firstSelectValue, String secondFirstValue) {
                tv_result_2.setText(firstSelectValue + " : " + secondFirstValue);
            }
        });

        threeLayout = (ThreeLayout) findViewById(R.id.threeLayout);
        // 正常不联动如下
        //        threeLayout.setItems(firstList, secondList, secondList, "12", "25", "16");

        //设定联动
        threeLayout.setListAndRelevanceRule(firstList, createRule(), createRule());
        threeLayout.setOnThreeLayoutListener(new ThreeLayout.OnThreeLayoutListener() {
            @Override
            public void onSelected(String firstSelectValue, String secondFirstValue, String threeSelectValue) {
                tv_result_3.setText(firstSelectValue + " 时 " + secondFirstValue + " 分 " + threeSelectValue + " 秒 ");
            }
        });

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
            public void onSelected(String text) {
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
        doublePicker.setListAndRelevanceRule(createList(30), createRule());
        doublePicker.setOnDoublePickListener(new DoublePicker.OnDoublePickListener() {
            @Override
            public void onSelected(String firstSelectValue, String secondSelectValue) {
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
        threePicker.setListAndRelevanceRule(createList(30), createRule(), createRule());
        threePicker.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Toast.makeText(MainActivity.this, "dialog dismiss", Toast.LENGTH_SHORT).show();
            }
        });
        threePicker.setOnThreePickListener(new ThreePicker.OnThreePickListener() {
            @Override
            public void onSelected(String firstSelectValue, String secondSelectValue, String threeSelectValue) {
                Toast.makeText(MainActivity.this, firstSelectValue + " 时 " + secondSelectValue + " 分 " + threeSelectValue + " 秒 ", Toast.LENGTH_SHORT).show();
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
