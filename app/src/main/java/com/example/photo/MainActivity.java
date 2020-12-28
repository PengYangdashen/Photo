package com.example.photo;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.photo.view.ProgressBar;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private onProgressChanged callback;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.pb);
        progressBar.setText("登录中");
        callback = new onProgressChanged() {
            @Override
            public void achieve15() {
                exchange(15);
            }

            @Override
            public void achieve30() {
                exchange(30);
            }

            @Override
            public void achieve60() {
                exchange(60);
            }

            @Override
            public void achieve98() {
                exchange(98);
            }
        };
        findViewById(R.id.btn_15).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                callback.achieve15();
            }
        });
        findViewById(R.id.btn_30).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                callback.achieve30();
            }
        });
        findViewById(R.id.btn_60).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                callback.achieve60();
            }
        });
        findViewById(R.id.btn_98).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                callback.achieve98();
            }
        });
    }

    public void exchange(int progress) {
        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(progressBar.getProgress(), progress);
        valueAnimator.setDuration(3000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                if (currentValue == 98L) {
                    progressBar.setText("登录成功");
                }
                progressBar.setProgress((int) currentValue);
            }


        });
        valueAnimator.start();
    }

    public interface onProgressChanged{
        void achieve15();
        void achieve30();
        void achieve60();
        void achieve98();
    }
}
