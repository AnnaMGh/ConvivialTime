package com.anaghizdavat.activity.others;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anaghizdavat.activity.R;
import com.anaghizdavat.activity.model.Coordinates;
import com.anaghizdavat.activity.model.TutorialTarget;


public class WizardUI {
    //ui

    //background layout
    public static RelativeLayout getBackgroundLayout(Context context) {
        RelativeLayout view = new RelativeLayout(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );

        view.setLayoutParams(params);
        return view;

    }


    //text view
    public static TextView getCustomizedTextView(Context context, String title, float x, float y) {
        TextView txtV = new TextView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.addRule(RelativeLayout.CENTER_HORIZONTAL,1);
        params.setMargins(GlobalSingleton.getInstance().convertDpToPixel(10, context),
                GlobalSingleton.getInstance().convertDpToPixel(10, context),
                GlobalSingleton.getInstance().convertDpToPixel(10, context),
                GlobalSingleton.getInstance().convertDpToPixel(10, context));
        txtV.setLayoutParams(params);
        txtV.setPadding(GlobalSingleton.getInstance().convertDpToPixel(20, context),
                GlobalSingleton.getInstance().convertDpToPixel(10, context),
                GlobalSingleton.getInstance().convertDpToPixel(20, context),
                GlobalSingleton.getInstance().convertDpToPixel(10, context));
        txtV.setGravity(Gravity.CENTER);
        txtV.setX(x);
        txtV.setY(y);
        txtV.setText(title);
        txtV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        txtV.setTextColor(context.getResources().getColor(R.color.colorWhite));
        txtV.setBackground(context.getResources().getDrawable(R.drawable.round_corners_gray));

        return txtV;
    }

    //text view
    public static void updateCustomizedTextView(Context context, TextView txtV, String title, float x, float y) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(GlobalSingleton.getInstance().convertDpToPixel(10, context), GlobalSingleton.getInstance().convertDpToPixel(10, context), GlobalSingleton.getInstance().convertDpToPixel(10, context), GlobalSingleton.getInstance().convertDpToPixel(10, context));
        txtV.setLayoutParams(params);
        txtV.setPadding(GlobalSingleton.getInstance().convertDpToPixel(10, context), GlobalSingleton.getInstance().convertDpToPixel(10, context), GlobalSingleton.getInstance().convertDpToPixel(10, context), GlobalSingleton.getInstance().convertDpToPixel(10, context));
        txtV.setGravity(Gravity.CENTER);
        txtV.setX(x);
        txtV.setY(y);
        txtV.setText(title);
        txtV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
    }


    //get simple view
    public static View getBackgroundView(Context context, float x, float y) {
        View view = new View(context);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );

        view.setX(x);
        view.setY(y);
        view.setLayoutParams(params);

        return view;
    }


    //focus view
    public static FocusView getFocusView(Context context, float x, float y, int radius, FocusView.FocusShape shape) {
        FocusView view = new FocusView(context);
        view.setPosition(x, y);
        view.setRadius(radius);
        view.setShape(shape);
        return view;
    }

    //focus view
    public static FocusView getFocusView(Context context, Coordinates coord, int radius, FocusView.FocusShape shape) {
        FocusView view = new FocusView(context);
        view.setPosition(coord);
        view.setRadius(radius);
        view.setShape(shape);
        return view;
    }

    //focus view
    public static void updateFocusView(FocusView view, float x, float y, int radius) {
        view.setPosition(x, y);
        view.setRadius(radius);
    }

    //button done/next
    public static Button getButton(Context context, float x, float y, int width, int height) {
        Button btn = new Button(context);
        btn.setX(x - GlobalSingleton.getInstance().convertDpToPixel(15, context));
        btn.setY(y);
        btn.setWidth(width);
        btn.setHeight(height);
        btn.setTextColor(context.getResources().getColor(R.color.colorWhite));
        btn.setBackground(context.getResources().getDrawable(R.drawable.round_corners_gray));
        return btn;
    }

    public static TextView getTextBtn(Context context, float x, float y, int width, int height) {

        TextView txt = new TextView(context);
        txt.setX(x - GlobalSingleton.getInstance().convertDpToPixel(15, context));
        txt.setY(y);
        txt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        txt.setGravity(Gravity.CENTER);
        txt.setTextSize(16);
        txt.setWidth(width);
        txt.setHeight(height);
        txt.setTextColor(context.getResources().getColor(R.color.colorWhite));
        txt.setBackground(context.getResources().getDrawable(R.drawable.round_corners_gray));

        return txt;
    }

    //progress images
    public static ImageView[] getProgressButtons(Context context, int numberOfImages, float x, float y) {
        ImageView[] imageViews = new ImageView[numberOfImages];

        float currentX = x + GlobalSingleton.getInstance().convertDpToPixel(5, context);

        for (int i = 0; i < numberOfImages; i++) {
            imageViews[i] = new ImageView(context);
            imageViews[i].setX(currentX);


            if (i == 0) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(40, 40);
                imageViews[i].setLayoutParams(params);
                imageViews[i].setImageResource(R.drawable.circle_fill_gray);
                imageViews[i].setY(y - 5);
            } else {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(30, 30);
                imageViews[i].setLayoutParams(params);
                imageViews[i].setImageResource(R.drawable.circle_fill_gray_darker);
                imageViews[i].setY(y);
            }

            currentX += 60;
        }

        //if it is just one dot make it invisible
        if (numberOfImages == 1) {
            imageViews[0].setVisibility(View.GONE);
        }

        return imageViews;
    }

    //add progress images to relative layout
    public static void addProgressButtons(RelativeLayout layout, ImageView[] images) {
        for (int i = 0; i < images.length; i++) {
            layout.addView(images[i]);
        }
    }

    //add progress images to linear layout
    public static void addProgressButtons(LinearLayout layout, ImageView[] images) {
        for (int i = 0; i < images.length; i++) {
            layout.addView(images[i]);
        }
    }

    //remove progress images from relative layout
    public static void removeProgressButtons(RelativeLayout layout, ImageView[] images) {
        for (int i = 0; i < images.length; i++) {
            layout.removeView(images[i]);
        }
    }

    //remove progress images from linear layout
    public static void removeProgressButtons(LinearLayout layout, ImageView[] images) {
        for (int i = 0; i < images.length; i++) {
            layout.removeView(images[i]);
        }
    }

    public static void updateProgressButtons(ImageView[] images, int currentStep) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) images[currentStep - 1].getLayoutParams();
        params.width = 30;
        params.height = 30;
        images[currentStep - 1].setLayoutParams(params);
        images[currentStep - 1].setY(images[currentStep - 1].getY() + 5);
        images[currentStep - 1].setImageResource(R.drawable.circle_fill_gray_darker);

        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) images[currentStep].getLayoutParams();
        params2.width = 40;
        params2.height = 40;
        images[currentStep].setLayoutParams(params2);
        images[currentStep].setY(images[currentStep].getY() - 5);
        images[currentStep].setImageResource(R.drawable.circle_fill_gray);
    }

    //set wizard screen
    public static void setWizardScreen(final Activity context, final RelativeLayout wizardLayout, final TutorialTarget targetView) {

        wizardLayout.removeAllViews();

        ViewTreeObserver viewTreeObserver = targetView.getView().getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    //remove observer
                    targetView.getView().getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    //background view
                    RelativeLayout backgroundView = WizardUI.getBackgroundLayout(context);
                    wizardLayout.addView(backgroundView);

                    //add focus view
                    int radius  = (targetView.getFocusShape() == FocusView.FocusShape.Circle?
                            targetView.getRadius(context):
                            targetView.getView().getHeight()+GlobalSingleton.getInstance().convertDpToPixel(targetView.getRadius(context), context));
                    FocusView view = WizardUI.getFocusView(context,
                            targetView.getViewPosition(context),
                            radius, targetView.getFocusShape());
                    view.setOnClickListener(view1 -> { /*block?*/});
                    backgroundView.addView(view, 0);

                    //add text
                    TextView txtView = WizardUI.getCustomizedTextView(context, targetView.getText(), 0, targetView.getTextY());
                    wizardLayout.addView(txtView, 1);

                }
            });
        }
    }

    public static void initializeViewsLayout(Context context, RelativeLayout wizardLayout) {

        //background view
        RelativeLayout backgroundView = WizardUI.getBackgroundLayout(context);

        //add focus view
        FocusView view = WizardUI.getFocusView(context, 0, 0, 150, FocusView.FocusShape.Circle);
        backgroundView.addView(view);
        wizardLayout.addView(backgroundView);

        //add text
        TextView txtView = WizardUI.getCustomizedTextView(context, "", 0, 0);
        wizardLayout.addView(txtView);
    }

    //keep temporary views
    public static View[] keepTemporalViews(RelativeLayout layout) {
        View[] views = new View[layout.getChildCount()];
        for (int i = 0; i < layout.getChildCount(); i++) {
            views[i] = layout.getChildAt(i);
        }
        return views;
    }

    // remove temporary views
    public static void removeTemporalViews(RelativeLayout layout, View[] views) {
        for (View view : views) {
            layout.removeView(view);
        }
    }


}
