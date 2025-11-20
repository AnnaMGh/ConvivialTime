package com.anaghizdavat.activity.model;

import android.app.Activity;
import android.view.View;

import com.anaghizdavat.activity.others.FocusView;
import com.anaghizdavat.activity.others.GlobalSingleton;

public class TutorialTarget {
    private final View view;
    private final int radius;
    private final String text;
    private final int textY;
    private Coordinates viewPosition;
    private final FocusView.FocusShape focusShape;

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
        return (int) GlobalSingleton.getInstance().getScreenMetrics(context).scaledDensity * radius;
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

    public Coordinates getViewPosition() {
        if (viewPosition != null) {
            return viewPosition;
        }

        int[] position = new int[2];
        // Use window coordinates to match your overlay canvas
        view.getLocationInWindow(position);

        float left = position[0];
        float top = position[1];
        float right = left + view.getWidth();
        float bottom = top + view.getHeight();

        if (focusShape == FocusView.FocusShape.Circle) {
            float cx = left + view.getWidth() / 2f;
            float cy = top + view.getHeight() / 2f;
            viewPosition = new Coordinates(cx, cy, 0, 0);
        } else {
            viewPosition = new Coordinates(left, top, right, bottom);
        }

        return viewPosition;
    }

}
