package com.anaghizdavat.activity.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anaghizdavat.activity.AnalyticsHandler;
import com.anaghizdavat.activity.R;
import com.anaghizdavat.activity.activities.MainActivity;
import com.anaghizdavat.activity.activities.SplashActivity;
import com.anaghizdavat.activity.model.Pawn;
import com.anaghizdavat.activity.model.Team;
import com.anaghizdavat.activity.others.Constants;
import com.anaghizdavat.activity.others.GlobalSingleton;
import com.anaghizdavat.activity.others.KeyboardUtils;
import com.anaghizdavat.activity.others.WordHelper;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameSetupFragment extends BaseFragment {

    @BindView(R.id.scroll)
    ScrollView scroll;
    @BindView(R.id.rl_start)
    RelativeLayout rlStart;
    @BindView(R.id.rl_parent_time)
    RelativeLayout rlParentTime;
    @BindView(R.id.rl_time_01)
    RelativeLayout rlTime01;
    @BindView(R.id.rl_time_02)
    RelativeLayout rlTime02;
    @BindView(R.id.rl_time_03)
    RelativeLayout rlTime03;
    @BindView(R.id.img_type_01)
    ImageView imgType01;
    @BindView(R.id.img_type_02)
    ImageView imgType02;
    @BindView(R.id.img_multiple_01)
    TextView txtMultiple01;
    @BindView(R.id.img_multiple_02)
    TextView txtMultiple02;
    @BindView(R.id.img_multiple_03)
    TextView txtMultiple03;
    @BindView(R.id.l_parent_multiple)
    ViewGroup lParentMultiple;
    @BindView(R.id.ll_parent_team)
    LinearLayout llParentTeam;
    @BindView(R.id.txt_pawn_change)
    TextView txtPawnChange;
    @BindView(R.id.txt_nr_players)
    TextView txtNrPlayers;
    @BindView(R.id.btn_start)
    Button btnStart;

    //private
    MainActivity currentActivity;

    private ArrayList<String> listTalk;
    private ArrayList<String> listMime;
    private ArrayList<String> listDraw;

    private ArrayList<Team> listTeams = new ArrayList<>();

    private int timeLimit = 60000;
    private int timeCode = 1;
    private int gameType = -1;
    private String gameName = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_game_setup, container, false);

        ButterKnife.bind(this, rootView);

        initialize();
        setListeners();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Timer checkKeyboardTimer = new Timer();
        checkKeyboardTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                checkKeyboardTimer.cancel();
                checkKeyboardTimer.purge();

                if (getActivity() != null && getActivity().getCurrentFocus() != null) {
                    KeyboardUtils.hideKeyboard(getActivity(), getActivity().getCurrentFocus());
                }
            }
        }, 250, 250);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() != null && getActivity().getCurrentFocus() != null) {
            KeyboardUtils.hideKeyboard(getActivity(), getActivity().getCurrentFocus());
        }
    }

    @OnClick(R.id.img_arrow_left)
    void onClickLeft() {
        int nr = Integer.parseInt(txtNrPlayers.getText().toString());
        if (nr > 2) {
            nr--;
            txtNrPlayers.setText(String.valueOf(nr));
            llParentTeam.removeViewAt(llParentTeam.getChildCount() - 1);
            listTeams.remove(listTeams.size() - 1);
        }
    }

    @OnClick(R.id.img_arrow_right)
    void onClickRight() {
        int nr = Integer.parseInt(txtNrPlayers.getText().toString());
        if (nr < 4) {
            nr++;
            txtNrPlayers.setText(String.valueOf(nr));

            View cell = getTeamCell();
            if (cell != null) {
                llParentTeam.addView(cell);
            }
        }
    }

    private void initialize() {
        currentActivity = (MainActivity) getActivity();
        if (currentActivity == null) {
            return;
        }

        //set height depending on screen height
        int height = GlobalSingleton.getInstance().getScreenMetrics(getActivity()).heightPixels;
        int width = GlobalSingleton.getInstance().getScreenMetrics(getActivity()).widthPixels;
        int widthDp = (int) GlobalSingleton.getInstance().convertPixelsToDp(width, getActivity());
        int requiredWeightDp = (GlobalSingleton.getInstance().isTablet(getActivity()) ? 510 : 400);
        int dp = GlobalSingleton.getInstance().getScreenMetrics(getActivity()).densityDpi;
        ViewGroup.LayoutParams params = rlStart.getLayoutParams();
        params.height = height - height / 6;
        rlStart.setLayoutParams(params);
        Log.d("Screen Dimensions", (((MainActivity) getActivity()).isTablet() ? "Tablet " : "Phone ") + width + " | " + height + " | " + dp);
//        showToast((((MainActivity) getActivity()).isTablet() ? "Tablet " : "Phone ") + width + " | " + height + " | " + dp);

        if (widthDp < requiredWeightDp) {
            ((TextView) lParentMultiple.getChildAt(0)).setText(getString(R.string.iii));
            ((TextView) lParentMultiple.getChildAt(1)).setText(getString(R.string.v));
            ((TextView) lParentMultiple.getChildAt(2)).setText(getString(R.string.vii));
            if (GlobalSingleton.getInstance().isTablet(getActivity())) {
                RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) rlParentTime.getLayoutParams();
                params2.width = (int) GlobalSingleton.getInstance().convertDpToPixel(240, currentActivity);
                rlParentTime.setLayoutParams(params2);
            }
        }

        //set images depending on language
        if (GlobalSingleton.getInstance().getString(Constants.KEY_LOCALE, getActivity()).equals("ro")) {
            imgType01.setImageResource(R.drawable.single_ro);
            imgType02.setImageResource(R.drawable.series_ro);
        }

        //set time
        if (currentActivity.previousTime != -1) {
            changeTime(currentActivity.previousTime);
            currentActivity.previousTime = -1;
        }

        //set mode
        if (currentActivity.previousMode != -1) {
            gameType = -1;
            changeType(currentActivity.previousMode);
            currentActivity.previousMode = -1;
        } else {
            changeType(Constants.GAME_TYPE_SIMPLE);
        }

        //add teams cell
        int nrOfTeams = currentActivity.previousTeams == null ? 2 : currentActivity.previousTeams.size();
        txtNrPlayers.setText(String.valueOf(nrOfTeams));
        for (int i = 0; i < nrOfTeams; i++) {
            View cell = getTeamCell();
            if (cell != null) {
                cell.findViewById(R.id.edt_team_name).setVisibility(View.INVISIBLE);
                llParentTeam.addView(cell);
            }
        }
        currentActivity.previousTeams = null;

        if (checkData() == null) {
            btnStart.setEnabled(true);
            btnStart.setBackgroundResource(R.drawable.custom_button);
            btnStart.setTextColor(getResources().getColor(R.color.colorWhite));
        }


        Timer timerNew = new Timer();
        timerNew.schedule(new TimerTask() {
            @Override
            public void run() {
                timerNew.cancel();
                timerNew.purge();

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {

                        for (int i = 0; i < llParentTeam.getChildCount(); i++) {
                            llParentTeam.getChildAt(i).findViewById(R.id.edt_team_name).setVisibility(View.VISIBLE);
                        }
                        if (getActivity() != null && getActivity().getCurrentFocus() != null) {
                            KeyboardUtils.hideKeyboard(getActivity(), getActivity().getCurrentFocus());
                        }
                    });

                }


            }
        }, 350, 350);


        Timer timerPawnChange = new Timer();
        timerPawnChange.schedule(new TimerTask() {
            @Override
            public void run() {
                timerPawnChange.cancel();
                timerPawnChange.purge();

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> txtPawnChange.setVisibility(View.GONE));
                }
            }
        }, 5000, 5000);
    }

    private void setListeners() {
        rootView.setOnClickListener(view -> {
            //block previous fragment events
        });

        rlTime01.setOnClickListener(view -> changeTime(0));

        rlTime02.setOnClickListener(view -> changeTime(1));

        rlTime03.setOnClickListener(view -> changeTime(2));

        imgType01.setOnClickListener(view -> changeType(Constants.GAME_TYPE_SIMPLE));

        imgType02.setOnClickListener(view -> changeType(Constants.GAME_TYPE_MULTIPLE));

        txtMultiple01.setOnClickListener(view -> changeType(Constants.GAME_TYPE_MULTIPLE_3));

        txtMultiple02.setOnClickListener(view -> changeType(Constants.GAME_TYPE_MULTIPLE_5));

        txtMultiple03.setOnClickListener(view -> changeType(Constants.GAME_TYPE_MULTIPLE_7));

        imgType02.setOnClickListener(view -> changeType(Constants.GAME_TYPE_MULTIPLE));

        imgType02.setOnClickListener(view -> changeType(Constants.GAME_TYPE_MULTIPLE));

        btnStart.setOnClickListener(view -> {

            if (getActivity() != null && getActivity().getCurrentFocus() != null) {
                KeyboardUtils.hideKeyboard(getActivity(), getActivity().getCurrentFocus());
            }

            String check = checkData();
            if (check != null) {
                showToast(check);
                return;
            }

            btnStart.setEnabled(false);

            WordHelper.getInstance().cleanLists();
            listTalk = WordHelper.getInstance().getList(Constants.TYPE_TALK, getActivity());
            listMime = WordHelper.getInstance().getList(Constants.TYPE_MIME, getActivity());
            listDraw = WordHelper.getInstance().getList(Constants.TYPE_DRAW, getActivity());

            //send event
            Bundle bundle = new Bundle();
            bundle.putString(Constants.EVENT_PARAM_LANG, GlobalSingleton.getInstance().getString(Constants.KEY_LOCALE, currentActivity));
            bundle.putInt(Constants.EVENT_PARAM_TIME, timeLimit);
            bundle.putInt(Constants.EVENT_PARAM_TYPE, gameType == 2 ? Constants.GAME_TYPE_MULTIPLE_3 : gameType);
            bundle.putInt(Constants.EVENT_PARAM_NR_PLAYERS, listTeams.size());
            AnalyticsHandler.sendMessage(getActivity(), Constants.EVENT_START, bundle);

            //go to game fragment
            GameFragment fragment = new GameFragment();
            fragment.listTeams = GlobalSingleton.getInstance().copyArray(listTeams);
            fragment.listTalk = listTalk;
            fragment.listMime = listMime;
            fragment.listDraw = listDraw;
            fragment.timeLimit = timeLimit;
            fragment.timeCode = timeCode;
            fragment.gameType = (gameType == 2 ? Constants.GAME_TYPE_MULTIPLE_3 : gameType);
            fragment.gameName = (gameType == 2 ? getActivity().getResources().getString(R.string.best_of_iii) : gameName);
            currentActivity.previousMode = gameType;
            currentActivity.previousTime = timeCode;
            currentActivity.previousTeams = listTeams;
            addFragmentAsNew(R.id.container, "GameSetupFragment", fragment, "GameFragment");
        });
    }

    private View getTeamCell() {
        View view = null;
        if (getActivity() != null) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater != null) {

                //inflate view
                view = inflater.inflate(R.layout.cell_team, null);

                //get views
                ImageView img = view.findViewById(R.id.img);
                TextView txt = view.findViewById(R.id.txt_team_name);
                EditText edt = view.findViewById(R.id.edt_team_name);

                //add values
                int id = llParentTeam.getChildCount();
                String teamNr = getString(R.string.team) + " " + (id + 1) + ":";
                txt.setText(teamNr);
                Team team = new Team();
                team.setId(id);
                team.setName(currentActivity.previousTeams == null ? "" : currentActivity.previousTeams.get(id).getName());
                team.setPawn(currentActivity.previousTeams == null ? getSelectedPawn(id) : currentActivity.previousTeams.get(id).getPawn());
                img.setImageResource(team.getPawn().getResImage());
                edt.setTextColor(getResources().getColor(team.getPawn().getResColor()));
                edt.setText(team.getName());
                listTeams.add(team);

                View.OnClickListener listener = view12 -> {
                    if (getActivity() != null) {
                        Dialog dialog = new Dialog(getActivity());
                        dialog.setContentView(R.layout.dialog_pawn);

                        RelativeLayout rlParent = dialog.findViewById(R.id.rl_parent_pawn);
                        for (int i = 0; i < rlParent.getChildCount(); i++) {
                            int iFinal = i;
                            rlParent.getChildAt(i).setOnClickListener(view1 -> {
                                        Pawn pawn = getSelectedPawn(iFinal);
                                        listTeams.get(id).setPawn(pawn);
                                        img.setImageResource(pawn.getResImage());
                                        edt.setTextColor(getResources().getColor(pawn.getResColor()));
                                        dialog.dismiss();
                                    }
                            );
                        }

                        if (dialog.getWindow() != null) {
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        }
                        dialog.show();
                    }
                };

                //set listeners
                txt.setOnClickListener(listener);
                img.setOnClickListener(listener);

                //set set name
                edt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        team.setName(edt.getText().toString());

                        if (checkData() == null) {
                            btnStart.setBackgroundResource(R.drawable.custom_button);
                            btnStart.setTextColor(getResources().getColor(R.color.colorWhite));
                        } else {
                            btnStart.setBackgroundResource(R.drawable.round_corners_gray);
                            btnStart.setTextColor(getResources().getColor(R.color.colorBlack));
                        }
                    }
                });

            }


        }

        return view;
    }

    private String checkData() {
        for (int i = 0; i < listTeams.size(); i++) {
            Team team = listTeams.get(i);
            if (team.getName() == null || team.getName().length() < 3) {
                return getString(R.string.team) + " " + (i + 1) + ": " + getString(R.string.team_name_short);
            }
            for (int j = 1; j < listTeams.size(); j++) {
                if (team.getName().equals(listTeams.get(j).getName()) && i != j) {
                    return getString(R.string.team_name_different);
                }
                if (team.getPawn().getResColor() == listTeams.get(j).getPawn().getResColor() && i != j) {
                    return getString(R.string.team_color_different);
                }
            }

        }

        return null;
    }

    private void changeTime(int time) {
        timeCode = time;
        switch (time) {
            case 0: {
                rlTime01.setAlpha(1f);
                ((TextView) rlTime01.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorBlue));
                ((ImageView) rlTime01.getChildAt(1)).setImageResource(R.drawable.circle_blue);

                rlTime02.setAlpha(0.5f);
                ((TextView) rlTime02.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorBlack));
                ((ImageView) rlTime02.getChildAt(1)).setImageResource(R.drawable.circle_black);

                rlTime03.setAlpha(0.5f);
                ((TextView) rlTime03.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorBlack));
                ((ImageView) rlTime03.getChildAt(1)).setImageResource(R.drawable.circle_black);

                timeLimit = 30000;
                break;
            }
            case 1: {
                rlTime01.setAlpha(0.5f);
                ((TextView) rlTime01.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorBlack));
                ((ImageView) rlTime01.getChildAt(1)).setImageResource(R.drawable.circle_black);

                rlTime02.setAlpha(1f);
                ((TextView) rlTime02.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorBlue));
                ((ImageView) rlTime02.getChildAt(1)).setImageResource(R.drawable.circle_blue);

                rlTime03.setAlpha(0.5f);
                ((TextView) rlTime03.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorBlack));
                ((ImageView) rlTime03.getChildAt(1)).setImageResource(R.drawable.circle_black);

                timeLimit = 60000;
                break;
            }
            case 2: {
                rlTime01.setAlpha(0.5f);
                ((TextView) rlTime01.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorBlack));
                ((ImageView) rlTime01.getChildAt(1)).setImageResource(R.drawable.circle_black);

                rlTime02.setAlpha(0.5f);
                ((TextView) rlTime02.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorBlack));
                ((ImageView) rlTime02.getChildAt(1)).setImageResource(R.drawable.circle_black);

                rlTime03.setAlpha(1f);
                ((TextView) rlTime03.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorBlue));
                ((ImageView) rlTime03.getChildAt(1)).setImageResource(R.drawable.circle_blue);

                timeLimit = 90000;
                break;
            }
        }

    }

    private Pawn getSelectedPawn(int id) {

        switch (id) {
            case 0: {
                return new Pawn(R.drawable.pawn_gray, R.color.colorGrayPawn);
            }
            case 1: {
                return new Pawn(R.drawable.pawn_red, R.color.colorRedPawn);
            }
            case 2: {
                return new Pawn(R.drawable.pawn_orange, R.color.colorOrangePawn);
            }
            case 3: {
                return new Pawn(R.drawable.pawn_green, R.color.colorGreenPawn);
            }
            case 4: {
                return new Pawn(R.drawable.pawn_blue, R.color.colorBluePawn);
            }
            case 5: {
                return new Pawn(R.drawable.pawn_purple, R.color.colorPurplePawn);
            }
            default:
                return new Pawn(R.drawable.pawn_gray, R.color.colorGrayPawn);

        }

    }

    private void changeType(int type) {
        if (gameType == type) {
            return;
        }

        if (gameType > Constants.GAME_TYPE_MULTIPLE && type == Constants.GAME_TYPE_MULTIPLE) {
            return;
        }

        gameType = type;
        switch (type) {
            case Constants.GAME_TYPE_SIMPLE: {
                imgType01.setAlpha(1f);
                imgType02.setAlpha(0.5f);

                lParentMultiple.setVisibility(View.GONE);
                break;
            }
            case Constants.GAME_TYPE_MULTIPLE: {
                imgType01.setAlpha(0.5f);
                imgType02.setAlpha(1f);

                txtMultiple01.setAlpha(1f);
                txtMultiple02.setAlpha(0.5f);
                txtMultiple03.setAlpha(0.5f);

                lParentMultiple.setVisibility(View.VISIBLE);
                break;
            }
            case Constants.GAME_TYPE_MULTIPLE_3: {
                imgType01.setAlpha(0.5f);
                imgType02.setAlpha(1f);

                txtMultiple01.setAlpha(1f);
                txtMultiple02.setAlpha(0.5f);
                txtMultiple03.setAlpha(0.5f);
                if (getActivity() != null) {
                    gameName = getActivity().getResources().getString(R.string.best_of_iii);
                }


                lParentMultiple.setVisibility(View.VISIBLE);
                break;
            }
            case Constants.GAME_TYPE_MULTIPLE_5: {
                imgType01.setAlpha(0.5f);
                imgType02.setAlpha(1f);

                txtMultiple01.setAlpha(0.5f);
                txtMultiple02.setAlpha(1f);
                txtMultiple03.setAlpha(0.5f);
                if (getActivity() != null) {
                    gameName = getActivity().getResources().getString(R.string.best_of_v);
                }


                lParentMultiple.setVisibility(View.VISIBLE);
                break;
            }
            case Constants.GAME_TYPE_MULTIPLE_7: {
                imgType01.setAlpha(0.5f);
                imgType02.setAlpha(1f);

                txtMultiple01.setAlpha(0.5f);
                txtMultiple02.setAlpha(0.5f);
                txtMultiple03.setAlpha(1f);
                if (getActivity() != null) {
                    gameName = getActivity().getResources().getString(R.string.best_of_vii);
                }


                lParentMultiple.setVisibility(View.VISIBLE);
                break;
            }
        }
    }


}
