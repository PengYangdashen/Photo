package com.example.photo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import org.jetbrains.annotations.Nullable;

public class ProgressBar extends View {
    private int mInnerBackground = Color.BLUE;
    private int mOuterBackground = Color.RED;
    // 10px
    private int mRoundWidth = 10;
    private float mProgressTextSize = 15;
    private int mProgressTextColor = Color.RED;

    private Paint mInnerPaint, mOuterPaint, mTextPaint;

    private int mMax = 100;
    private int mProgress = 0;
    private String text;

    public ProgressBar(Context context) {
        this(context, null);
    }

    public ProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public ProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 获取自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ProgressBar);
        mInnerBackground = array.getColor(R.styleable.ProgressBar_innerBackground, mInnerBackground);
        mOuterBackground = array.getColor(R.styleable.ProgressBar_outerBackground, mOuterBackground);
        mRoundWidth = (int) array.getDimension(R.styleable.ProgressBar_roundWidth, dip2px(10));
        mProgressTextSize = array.getDimensionPixelSize(R.styleable.ProgressBar_progressTextSize,
                sp2px(mProgressTextSize));
        mProgressTextColor = array.getColor(R.styleable.ProgressBar_progressTextColor, mProgressTextColor);

        array.recycle();

        mInnerPaint = new Paint();
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setColor(mInnerBackground);
        mInnerPaint.setStrokeWidth(mRoundWidth-2);
        mInnerPaint.setStyle(Paint.Style.STROKE);

        mOuterPaint = new Paint();
        mOuterPaint.setAntiAlias(true);
        mOuterPaint.setColor(mOuterBackground);
        mOuterPaint.setStrokeWidth(mRoundWidth);
        mOuterPaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mProgressTextColor);
        mTextPaint.setTextSize(mProgressTextSize);

    }
    private int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    private float dip2px(int dip) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 只保证是正方形
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(Math.min(width, height), Math.min(width, height));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 先画内圆
        int radius = getWidth() / 2;
        canvas.drawCircle(radius,radius,radius-mRoundWidth/2,mInnerPaint);
        //画圆弧
        @SuppressLint("DrawAllocation") RectF rectF=new RectF(mRoundWidth/2,mRoundWidth/2,
                getWidth()-mRoundWidth/2,getHeight()-mRoundWidth/2);
        //如果进度为0就不绘制
        if (mProgress == 0) {
            return;
        }
        float percent=(float)mProgress/mMax;
        canvas.drawArc(rectF,0,360*percent,false,mOuterPaint);

        // 画进度文字
        String text = this.text + ((int) (percent * 100)) + "%";
        @SuppressLint("DrawAllocation") Rect rect=new Rect();
        mTextPaint.getTextBounds(text,0,text.length(),rect);
        float dx=getWidth()/2-rect.width()/2;
        @SuppressLint("DrawAllocation") Paint.FontMetricsInt fontMetricsInt=new Paint.FontMetricsInt();
        int dy=(fontMetricsInt.bottom - fontMetricsInt.top)/2-fontMetricsInt.bottom;
        float baseLine=getHeight()/2+dy;
        canvas.drawText(text,dx,baseLine,mTextPaint);

    }
    // 给几个方法
    public synchronized void setMax(int max) {
        if (max < 0) {

        }
        this.mMax = max;
    }

    public synchronized void setProgress(int progress) {
        if (progress < mProgress) {
            return;
        }
        this.mProgress = progress;
        // 刷新 invalidate
        invalidate();
    }

    public synchronized int getProgress() {
       return this.mProgress;
    }

    public synchronized void setText(String text) {
        this.text = text;
    }
}