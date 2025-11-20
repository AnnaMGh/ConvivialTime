package com.anaghizdavat.activity.others;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.anaghizdavat.activity.R;
import com.anaghizdavat.activity.model.Coordinates;


public class FocusView extends View {

    public enum FocusShape {Circle, Rectangle}

    private Paint mTransparentPaint;
    private Paint mSemiBlackPaint;
    private Path mPath = new Path();

    private float x = 0;
    private float y = 0;
    private float x2 = 0;
    private float y2 = 0;
    private int radius = 100;
    private FocusShape shape = FocusShape.Circle;

    public FocusView(Context context) {
        super(context);
        initPaints();
    }

    public FocusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaints();
    }

    public FocusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaints();
    }

    private void initPaints() {
        mTransparentPaint = new Paint();
        mTransparentPaint.setColor(Color.TRANSPARENT);
        mTransparentPaint.setStrokeWidth(10);

        mSemiBlackPaint = new Paint();
        mSemiBlackPaint.setColor(Color.TRANSPARENT);
        mSemiBlackPaint.setStrokeWidth(10);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setPosition(Coordinates coord) {
        this.x = coord.x1;
        this.y = coord.y1;
        this.x2 = coord.x2;
        this.y2 = coord.y2;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setShape(FocusShape shape) {
        this.shape = shape;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPath.reset();

        if (shape == FocusShape.Circle) {
            mPath.addCircle(x, y, radius, Path.Direction.CW);
        } else {
            RectF rect = new RectF(x, y, x2, y2);
            mPath.addRoundRect(rect, 50, 50, Path.Direction.CW);
        }

        mPath.setFillType(Path.FillType.INVERSE_EVEN_ODD);

        canvas.drawPath(mPath, mSemiBlackPaint);
        canvas.clipPath(mPath);
        canvas.drawColor(getResources().getColor(R.color.colorBlackTransparent));
//        canvas.drawColor(Color.parseColor("#CC000000"));
//        canvas.drawColor(Color.parseColor("#A6000000"));
    }

}