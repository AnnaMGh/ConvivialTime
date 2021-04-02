package com.anaghizdavat.activity.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.View;

import com.anaghizdavat.activity.others.FocusView;
import com.anaghizdavat.activity.others.GlobalSingleton;

public class TutorialTarget {
    private View view;
    private int radius;
    private String text;
    private int textY;
    private Coordinates viewPosition;
    private FocusView.FocusShape focusShape;

    public TutorialTarget(View view, int radius, String text, int textY, FocusView.FocusShape focusShape) {
        this.view = view;
        this.radius = radius;
        this.text = text;
        this.textY = textY;
        this.focusShape = focusShape;
    }

    public View getView() {
        return view;
    }

    public int getRadius(Activity context) {
        return   (int) GlobalSingleton.getInstance().getScreenMetrics(context).scaledDensity * radius;
    }

    public String getText() {
        return text;
    }

    public int getTextY() {
        return textY;
    }

    public FocusView.FocusShape getFocusShape() {
        return focusShape;
    }

    public Coordinates getViewPosition(Activity context) {
        if (viewPosition != null) {
            return viewPosition;
        }

        float x;
        float y;
        int[] position = new int[2];

        float w = view.getWidth();
        float h = view.getHeight() - GlobalSingleton.getInstance().convertDpToPixel(26, context);
        view.getLocationInWindow(position);
        x = position[0] + (view.getWidth() / 2f);
        x = w;
        y = position[1];
        y = h;
        // y = position[1] - (view.getHeight() / 2f);

        /*if (position[1] < 0 || position[1] > GlobalSingleton.getInstance().getScreenMetrics(context).heightPixels) {
            y = view.getY() + (view.getHeight() / 2f);
        }
        if (position[0] < 0 || position[0] > GlobalSingleton.getInstance().getScreenMetrics(context).widthPixels) {
            x = view.getX() + (view.getWidth() / 2f);
        }*/
//
        float accuracy ;//= GlobalSingleton.getInstance().convertDpToPixel(12 *GlobalSingleton.getInstance().getScreenMetrics(context).scaledDensity, context);
        accuracy = GlobalSingleton.getInstance().convertDpToPixel(24, context);
        float x1 = position[0];
        float y1 = position[1] - accuracy;
        float x2 = position[0] + view.getWidth();
        float y2 = position[1] + view.getHeight() - accuracy;
        if (focusShape == FocusView.FocusShape.Circle) {
            x1 = position[0] + view.getWidth() / 2f;
            y1 = position[1];
            x2 = 0;
            y2 = 0;
        }

        viewPosition = new Coordinates(x1, y1, x2, y2);

        return viewPosition;
    }
}
