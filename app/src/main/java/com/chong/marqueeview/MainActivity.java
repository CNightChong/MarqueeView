package com.chong.marqueeview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chong.marqueeview.view.MarqueeView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MarqueeView marqueeView;
    private MarqueeView marqueeView1;
    private MarqueeView marqueeView2;
    private MarqueeView marqueeView3;
    private MarqueeView marqueeView4;
    private MarqueeView marqueeView5;
    private Button startBtn;
    private Button endBtn;
    private Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        marqueeView = (MarqueeView) findViewById(R.id.marqueeView);
        marqueeView1 = (MarqueeView) findViewById(R.id.marqueeView1);
        marqueeView2 = (MarqueeView) findViewById(R.id.marqueeView2);
        marqueeView3 = (MarqueeView) findViewById(R.id.marqueeView3);
        marqueeView4 = (MarqueeView) findViewById(R.id.marqueeView4);
        marqueeView5 = (MarqueeView) findViewById(R.id.marqueeView5);

        final List<String> info = new ArrayList<>();
        info.add("1. 大家好，我是xxxxxxxx。");
        info.add("2. 欢迎大家关注我哦！");
        info.add("3. GitHub帐号：xxxxxxxxxxx");
        info.add("4. 新浪微博：xxxxxxxx微博");
        info.add("5. 个人博客：xxxxxxxxxx.com");
        info.add("6. 微信公众号：xxxxxxx");
        marqueeView.startWithList(info);

        marqueeView1.startWithText(getString(R.string.marquee_texts));
        marqueeView2.startWithText(getString(R.string.marquee_texts));
        marqueeView3.startWithText(getString(R.string.marquee_texts));
        marqueeView4.startWithText(getString(R.string.marquee_text));
        marqueeView5.startWithList(info);

        marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                Toast.makeText(getApplicationContext(), "位置：" + String.valueOf(marqueeView.getPosition())
                        + "，文本：" + textView.getText() + "", Toast.LENGTH_SHORT).show();
            }
        });

        marqueeView1.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                Toast.makeText(getApplicationContext(), String.valueOf(marqueeView1.getPosition()) + ". " + textView.getText(), Toast.LENGTH_SHORT).show();
            }
        });


        startBtn = (Button) findViewById(R.id.btn_start);
        endBtn = (Button) findViewById(R.id.btn_end);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                marqueeView.continueFlipping();
            }
        });
        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (marqueeView.isFlipping()) {
                    marqueeView.stop();
                    Toast.makeText(getApplicationContext(), "停止位置：" + String.valueOf(marqueeView.getPosition()), Toast.LENGTH_SHORT).show();
                }
            }
        });

        nextBtn = (Button) findViewById(R.id.btn_next);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RecycleViewActivity.class));
            }
        });
    }
}
