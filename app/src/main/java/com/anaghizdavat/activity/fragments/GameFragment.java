package com.anaghizdavat.activity.fragments;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anaghizdavat.activity.AdHandler;
import com.anaghizdavat.activity.AnalyticsHandler;
import com.anaghizdavat.activity.R;
import com.anaghizdavat.activity.activities.MainActivity;
import com.anaghizdavat.activity.model.Round;
import com.anaghizdavat.activity.model.Team;
import com.anaghizdavat.activity.others.Constants;
import com.anaghizdavat.activity.others.GlobalSingleton;
import com.anaghizdavat.activity.others.KeyboardUtils;
import com.anaghizdavat.activity.others.WordHelper;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameFragment extends BaseFragment {

    //views

    //next team
    @BindView(R.id.rl_next_team)
    RelativeLayout rlNextTeam;
    @BindView(R.id.txt_team_name)
    TextView txtTeamNameTurn;
    @BindView(R.id.img_team)
    ImageView imgTeam;
    @BindView(R.id.btn_continue)
    Button btnContinue;

    //game
    @BindView(R.id.rl_game)
    RelativeLayout rlGame;
    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.btn_correct)
    Button btnCorrect;
    @BindView(R.id.btn_next_action)
    Button btnNextAc;
    @BindView(R.id.img_toggle_show)
    ImageView imgToggleVisibility;
    @BindView(R.id.txt_team)
    TextView txtTeam;
    @BindView(R.id.txt_points)
    TextView txtRoundPoints;
    @BindView(R.id.img_activity)
    ImageView imgActivity;
    @BindView(R.id.txt_activity)
    TextView txtActivity;
    @BindView(R.id.txt_word)
    TextView txtWord;
    @BindView(R.id.txt_word_all)
    TextView txtWordAll;
    @BindView(R.id.txt_time)
    TextView txtTime;
    @BindView(R.id.img_time)
    ImageView imgTime;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;

    //game action moves
    @BindView(R.id.rl_game_action)
    RelativeLayout rlGameAction;
    @BindView(R.id.rl_panel)
    LinearLayout llPanel;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.txt_team_02)
    TextView txtTeam02;
    @BindView(R.id.txt_points_02)
    TextView txtRoundPoints02;
    @BindView(R.id.txt_word_all_team_points)
    TextView txtWordAllTeamPoints;

    //finish
    @BindView(R.id.scroll_finish)
    ScrollView scrollFinish;
    @BindView(R.id.txt_game_ranking_lbl)
    TextView txtGameRankingLbl;
    @BindView(R.id.ll_parent_game_ranking)
    LinearLayout llParentRanking;
    @BindView(R.id.txt_game_series_ranking_lbl)
    TextView txtGameSeriesRankingLbl;
    @BindView(R.id.ll_parent_game_series_ranking)
    LinearLayout llParentGameSeriesRanking;
    @BindView(R.id.ll_vertical)
    LinearLayout llVertical;
    @BindView(R.id.btn_again)
    Button btnPlayAgain;
    @BindView(R.id.txt_game_winner)
    TextView txtGameWinner;

    //finish series ranking
    @BindView(R.id.scroll_series_finish)
    ScrollView scrollSeriesFinish;
    @BindView(R.id.txt_series_ranking)
    TextView txtSeriesRanking;
    @BindView(R.id.ll_pawns_rank_1)
    LinearLayout llPawnsRank1;
    @BindView(R.id.ll_pawns_rank_2)
    LinearLayout llPawnsRank2;
    @BindView(R.id.ll_pawns_rank_3)
    LinearLayout llPawnsRank3;
    @BindView(R.id.ll_parent_series_ranking)
    LinearLayout llParentSeriesRanking;
    @BindView(R.id.btn_series_again)
    Button btnSeriesPlayAgain;

    //from other class
    ArrayList<Team> listTeams = new ArrayList<>();
    ArrayList<String> listTalk;
    ArrayList<String> listMime;
    ArrayList<String> listDraw;
    int timeLimit = 60000;
    int timeCode = 1;
    int gameType = Constants.GAME_TYPE_SIMPLE;
    String gameName = "";

    //private
    int widthDp;
    int requiredWidthDp;
    int normalTextSize;
    int shrinkTextSize;
    int wordWidthThreshold = 60;
    private int currentTeam = -1;
    private Round currentRound;
    private int timePast = timeLimit;
    private Timer timer = new Timer();
    private String lastWord = "";
    private boolean toggleVisibility = false;
    private int id;
    private int points;
    private MediaPlayer mpNext;
    private MediaPlayer mpCorrect;
    private MediaPlayer mpMovement;
    private MediaPlayer mpCountDown;
    private MediaPlayer mpCountDownAlarm;
    private MediaPlayer mpFinish;
    private MediaPlayer mpRoundForAll;
    private boolean isRoundForAll;
    private Dialog pawnDialog;
    private int currentSeries = 1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_game, container, false);

        ButterKnife.bind(this, rootView);

        initialize();
        setListeners();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null && getActivity().getCurrentFocus() != null) {
            KeyboardUtils.hideKeyboard(getActivity(), getActivity().getCurrentFocus());
        }
    }

    private void initialize() {
        MainActivity currentActivity = ((MainActivity) getActivity());
        if (currentActivity == null) {
            return;
        }
        int width = GlobalSingleton.getInstance().getScreenMetrics(currentActivity).widthPixels;
        widthDp = (int) GlobalSingleton.getInstance().convertPixelsToDp(width, currentActivity);
        requiredWidthDp = (GlobalSingleton.getInstance().isTablet(currentActivity) ? 510 : 400);

        int height = GlobalSingleton.getInstance().getScreenMetrics(currentActivity).heightPixels;
        int heightDp = (int) GlobalSingleton.getInstance().convertPixelsToDp(height, currentActivity);
        int requiredHeightDp = (GlobalSingleton.getInstance().isTablet(currentActivity) ? 900 : 690);

        normalTextSize = (GlobalSingleton.getInstance().isTablet(currentActivity) ? 32 : 25);
        shrinkTextSize = (GlobalSingleton.getInstance().isTablet(currentActivity) ? 27 : 20) - (int) GlobalSingleton.getInstance().getCurrentDp(currentActivity);

        if (widthDp < requiredWidthDp) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) btnCorrect.getLayoutParams();
            params.leftMargin = params.rightMargin = (int) GlobalSingleton.getInstance().convertDpToPixel(12, currentActivity);
            btnCorrect.setLayoutParams(params);
            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) btnNextAc.getLayoutParams();
            params2.leftMargin = params2.rightMargin = (int) GlobalSingleton.getInstance().convertDpToPixel(12, currentActivity);
            btnNextAc.setLayoutParams(params2);
            RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams) imgToggleVisibility.getLayoutParams();
            params3.leftMargin = params3.rightMargin = (int) GlobalSingleton.getInstance().convertDpToPixel(12, currentActivity);
            imgToggleVisibility.setLayoutParams(params3);
        }
        if (heightDp < requiredHeightDp) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imgActivity.getLayoutParams();
            int imgHeightDp = (int) GlobalSingleton.getInstance().convertPixelsToDp(params.height, currentActivity);
            int newDimension = imgHeightDp - (requiredHeightDp - heightDp);
            if (newDimension < 0.8 * imgHeightDp) {
                newDimension = (int) (0.8 * imgHeightDp);
            }
            params.height = params.width = (int) GlobalSingleton.getInstance().convertDpToPixel(newDimension, currentActivity);
            imgActivity.setLayoutParams(params);

            //shrink text size
            normalTextSize = shrinkTextSize;
            txtTeam.setTextSize(TypedValue.COMPLEX_UNIT_SP, shrinkTextSize);
            txtWord.setTextSize(TypedValue.COMPLEX_UNIT_SP, shrinkTextSize);
            txtTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, shrinkTextSize - 4);
            txtWordAll.setTextSize(TypedValue.COMPLEX_UNIT_SP, shrinkTextSize - 6);

            //shrink time image & text
            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) imgTime.getLayoutParams();
            params2.height = params2.width = (int) (params2.width - GlobalSingleton.getInstance().convertDpToPixel(5, currentActivity));
            imgTime.setLayoutParams(params2);
            RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams) txtTime.getLayoutParams();
            params3.height = params3.width = (int) (params3.width - GlobalSingleton.getInstance().convertDpToPixel(5, currentActivity));
            txtTime.setLayoutParams(params3);

            //shrink margin top
            RelativeLayout.LayoutParams params4 = (RelativeLayout.LayoutParams) txtTeam.getLayoutParams();
            params4.topMargin = (int) (params4.topMargin - GlobalSingleton.getInstance().convertDpToPixel(25, currentActivity));
            txtTeam.setLayoutParams(params4);

            //shrink margin bottom
            RelativeLayout.LayoutParams params5 = (RelativeLayout.LayoutParams) rlBottom.getLayoutParams();
            params5.height = (int) (params5.height - GlobalSingleton.getInstance().convertDpToPixel(25, currentActivity));
            rlBottom.setLayoutParams(params5);
            rlBottom.setPadding(0, 0, 0, rlBottom.getPaddingBottom() - (int) GlobalSingleton.getInstance().convertDpToPixel(25, currentActivity));
        }

        //load ad
//        AdHandler.addInterstitialAd(currentActivity, Constants.adInterstitialUnitId);


        //get audio source
        mpNext = MediaPlayer.create(currentActivity.getApplicationContext(), R.raw.fail_sound_ex);
        mpCorrect = MediaPlayer.create(currentActivity.getApplicationContext(), R.raw.success_sound_ex);
        mpMovement = MediaPlayer.create(currentActivity.getApplicationContext(), R.raw.movement);
        mpCountDown = MediaPlayer.create(currentActivity.getApplicationContext(), R.raw.countdown_clock);
        mpCountDownAlarm = MediaPlayer.create(currentActivity.getApplicationContext(), R.raw.countdown_alarm);
        mpFinish = MediaPlayer.create(currentActivity.getApplicationContext(), R.raw.game_finish_shorten);
        mpRoundForAll = MediaPlayer.create(currentActivity.getApplicationContext(), R.raw.game_tension_ex);


        //set dialog
        setDialog();

        //reset
        reset();

        //next
        next();

        //hide keyboard if showed
        if (currentActivity.getCurrentFocus() != null) {
            KeyboardUtils.hideKeyboard(currentActivity, currentActivity.getCurrentFocus());
        }

    }

    private void setListeners() {
        rootView.setOnClickListener(view -> {
            //block previous fragment listener
        });

        btnContinue.setOnClickListener(view -> {
            rlNextTeam.setVisibility(View.GONE);
            rlGame.setVisibility(View.VISIBLE);

            if (isRoundForAll) {
                mpRoundForAll.setVolume(1f, 1f);
                mpRoundForAll.start();
            } else {
                mpRoundForAll.setVolume(0f, 0f);
            }
        });

        btnStart.setOnClickListener(view -> {
            btnStart.setVisibility(View.GONE);
            btnNextAc.setVisibility(View.GONE);
            btnCorrect.setVisibility(View.VISIBLE);
            imgToggleVisibility.setVisibility(View.VISIBLE);
            toggleVisibility(false);
            setTimer();
        });

        btnCorrect.setOnClickListener(view -> {
            btnCorrect.setEnabled(false);


            mpCountDown.setVolume(0, 0);
            mpCountDownAlarm.setVolume(0, 0);

            if (isRoundForAll) {
                if (getActivity() != null) {
                    pawnDialog.show();

                    if (getActivity().getCurrentFocus() != null) {
                        KeyboardUtils.hideKeyboard(getActivity(), getActivity().getCurrentFocus());
                    }
                }
            } else {
                onClickCorrect(currentTeam, currentRound.getPoints());
            }
        });

        btnNextAc.setOnClickListener(view -> {
            mpNext.start();
            new Handler().postDelayed(() -> {
                btnNext.setEnabled(true);
                onClickShowAction(currentTeam, 0);
            }, 1000);
        });


        imgToggleVisibility.setOnClickListener(view -> toggleVisibility(!toggleVisibility));

        btnNext.setOnClickListener(view -> next());
        btnPlayAgain.setOnClickListener(view -> {
            mpFinish.setVolume(0f, 0f);
            if (gameType == Constants.GAME_TYPE_SIMPLE) {
                if (getActivity() != null) {
                    //finish animation
                    btnPlayAgain.setEnabled(false);
                    MainActivity currentActivity = ((MainActivity) getActivity());
                    if (currentActivity != null) {
                        //send event
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.EVENT_PARAM_LANG, GlobalSingleton.getInstance().getString(Constants.KEY_LOCALE, currentActivity));
                        bundle.putInt(Constants.EVENT_PARAM_TIME, timeLimit);
                        bundle.putInt(Constants.EVENT_PARAM_TYPE, gameType);
                        bundle.putInt(Constants.EVENT_PARAM_NR_PLAYERS, listTeams.size());
                        AnalyticsHandler.sendMessage(currentActivity, Constants.EVENT_PLAY_AGAIN, bundle);

                        //back to game setup
                        currentActivity.showAlert = false;
                        currentActivity.onBackPressed();
                    }
                }
            } else {
                if (currentSeries > gameType) {

                    //set title
                    String text = "";
                    if (getActivity() != null) {
                        text = getActivity().getResources().getString(R.string.mode); //+
                        text = text.substring(0, text.length() - 1);
                        String ranking = getActivity().getResources().getString(R.string.ranking);
                        if (GlobalSingleton.getInstance().getString(Constants.KEY_LOCALE, getActivity()).equals("ro")) {
                            ranking = ranking.substring(0, ranking.length() - 2);
                        }
                        text += " " + gameName.replace("\n", "") + "\n" + ranking;
                    }
                    txtSeriesRanking.setText(text);

                    //sort teams
                    ArrayList<Team> sortedTeams = sortTeamsByPoints(listTeams);

                    //set podium
                    int lastIndex = setPawnsPodium(0, sortedTeams, llPawnsRank1);
                    lastIndex = setPawnsPodium(lastIndex, sortedTeams, llPawnsRank2);
                    setPawnsPodium(lastIndex, sortedTeams, llPawnsRank3);

                    //set series ranking list
                    llParentSeriesRanking.removeAllViews();
                    for (int i = 0; i < sortedTeams.size(); i++) {
                        View cell = getTeamPlaceCell(i, sortedTeams, true);
                        if (cell != null) {
                            llParentSeriesRanking.addView(cell);
                        }
                    }

                    //show layout
                    scrollSeriesFinish.setVisibility(View.VISIBLE);
                } else {
                    //dialog
                    setDialog();
                    //reset data
                    reset();
                    //next
                    next();
                }
            }
        });

        btnSeriesPlayAgain.setOnClickListener(view -> {
            if (getActivity() != null) {
                //finish animation
                btnSeriesPlayAgain.setEnabled(false);
                MainActivity currentActivity = ((MainActivity) getActivity());
                if (currentActivity != null) {
                    //send event
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.EVENT_PARAM_LANG, GlobalSingleton.getInstance().getString(Constants.KEY_LOCALE, currentActivity));
                    bundle.putInt(Constants.EVENT_PARAM_TIME, timeLimit);
                    bundle.putInt(Constants.EVENT_PARAM_TYPE, gameType);
                    bundle.putInt(Constants.EVENT_PARAM_NR_PLAYERS, listTeams.size());
                    AnalyticsHandler.sendMessage(currentActivity, Constants.EVENT_PLAY_AGAIN, bundle);

                    //back to game setup
                    currentActivity.showAlert = false;
                    currentActivity.onBackPressed();
                }
            }
        });
    }

    private void onClickCorrect(int receivedTeam, int receivedPoints) {
        if (receivedPoints > 0) {
            mpCorrect.start();
        }

        new Handler().postDelayed(() -> {
            onClickShowAction(receivedTeam, receivedPoints);

            id = receivedTeam;
            points = receivedPoints;
            btnNext.setEnabled(false);

            Timer moveTimer = new Timer();
            moveTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() ->
                        {
                            if (id == -1) {
                                if (scrollFinish.getVisibility() == View.GONE) {
                                    btnNext.setEnabled(true);
                                }
                                try {
                                    moveTimer.cancel();
                                    moveTimer.purge();
                                    if (getActivity() != null && getActivity().getCurrentFocus() != null) {
                                        KeyboardUtils.hideKeyboard(getActivity(), getActivity().getCurrentFocus());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                id = checkPawnStepped(id, points);
                                points = -1;

                            }
                        });
                    }
                }
            }, 1000, 1000);
        }, 1000);


    }

    private void onClickShowAction(int teamWhoWinPoints, int pointReached) {
        rlGame.setVisibility(View.GONE);
        rlGameAction.setVisibility(View.VISIBLE);

        String roundPoints;
        if (isRoundForAll && teamWhoWinPoints != currentTeam && pointReached > 0) {
            roundPoints = getString(R.string.round) + " " + currentRound.getNumber() + " | " + getString(R.string.steps) + ": " + 0;

            String string = getResources().getString(R.string.team_optains_points);
            Team team = listTeams.get(teamWhoWinPoints);
            string = string.replace("xColorx", "#" + getResources().getString(team.getPawn().getResColor()).substring(3));
            string = string.replace("xTeamx", team.getName().toUpperCase());
            string = string.replace("xPointsx", String.valueOf(pointReached));
            string = string.replace("xPointsNamex", (pointReached == 1 ? getResources().getString(R.string.point) : getResources().getString(R.string.seriesPoints)).toLowerCase());
            txtWordAllTeamPoints.setText(Html.fromHtml(string));
            txtWordAllTeamPoints.setVisibility(View.VISIBLE);
        } else {
            txtWordAllTeamPoints.setVisibility(View.GONE);
            roundPoints = getString(R.string.round) + " " + currentRound.getNumber() + " | " + getString(R.string.steps) + ": " + pointReached;
        }
        txtRoundPoints02.setText(roundPoints);

        //stop timer if works
        try {
            timePast = timeLimit;
            timer.cancel();
            timer.purge();
        } catch (
                Exception ignored) {
        }
    }


    private void toggleVisibility(boolean isShowing) {
        toggleVisibility = isShowing;
        if (toggleVisibility) {
            txtWord.setText(lastWord);
            txtWord.setHorizontallyScrolling(false);
            if (GlobalSingleton.getInstance().calculateStringWidth(txtWord) > widthDp - wordWidthThreshold) {
                txtWord.setLines(2);
            } else {
                txtWord.setLines(1);
            }

            imgToggleVisibility.setImageResource(R.drawable.show);
        } else {
            StringBuilder hideWord = new StringBuilder();
            for (int i = 0; i < lastWord.length(); i++) {
                hideWord.append("x");
            }
            txtWord.setText(hideWord.toString());
            txtWord.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

            txtWord.setHorizontallyScrolling(false);
            if (GlobalSingleton.getInstance().calculateStringWidth(txtWord) > widthDp - wordWidthThreshold) {
                txtWord.setLines(2);
            } else {
                txtWord.setLines(1);
            }

            imgToggleVisibility.setImageResource(R.drawable.hide);
        }

    }

    private void reset() {
        //set data
        currentTeam = -1;
        timePast = timeLimit;
        currentRound = new Round(1, 3);

        //set screens
        rlNextTeam.setVisibility(View.VISIBLE);
        rlGame.setVisibility(View.GONE);
        rlGameAction.setVisibility(View.GONE);
        scrollFinish.setVisibility(View.GONE);
        scrollSeriesFinish.setVisibility(View.GONE);

        for (int i = 0; i < listTeams.size(); i++) {
            int finalI = i;
            //if a team finish the game show popup
            listTeams.get(i).setInitialPanel(llPanel, 3 - i, obj -> {
                btnNext.setEnabled(false);

                //set winner text
                ArrayList<Team> sortedTeamsByPlaces = sortTeamsByPlaces(finalI, listTeams);
                String text = "";
                if (getActivity() != null) {
                    text = getActivity().getResources().getString(R.string.game_winner);
                    text = text.replace(" xNumberx", (gameType > 1 ? " " + currentSeries + "/" + gameType : ""));
                    text = text.replace("xColorx", "#" + getResources().getString(sortedTeamsByPlaces.get(0).getPawn().getResColor()).substring(3));
                    text = text.replace("xTeamx", sortedTeamsByPlaces.get(0).getName().toUpperCase());
                }
                txtGameWinner.setText(Html.fromHtml(text));

                //change label text
                String textGameRanking = "";
                if (getActivity() != null) {
                    textGameRanking = getActivity().getResources().getString(R.string.game_ranking);
//                    textRanking = textRanking.replace("xNumberx", (gameType > 1 ? currentSeries + "/" + gameType : ""));
                    textGameRanking = textGameRanking.replace(" xNumberx", "");
                }
                txtGameRankingLbl.setText(textGameRanking);

                //set game places list
                llParentRanking.removeAllViews();
                for (int i1 = 0; i1 < sortedTeamsByPlaces.size(); i1++) {
                    View cell = getTeamPlaceCell(i1, sortedTeamsByPlaces, false);
                    if (cell != null) {
                        llParentRanking.addView(cell);
                    }
                }

                //change game series label text
                String textGameSeriesRanking = (gameName.isEmpty() ? "" : gameName.replace("\n", ""));
                if (getActivity() != null && !textGameSeriesRanking.isEmpty()) {
                    textGameSeriesRanking = textGameSeriesRanking.substring(0, 1).toLowerCase() + textGameSeriesRanking.substring(1);
                    String textStr = getActivity().getResources().getString(R.string.ranking);
                    textGameSeriesRanking = textStr + " " + textGameSeriesRanking;
                }
                txtGameSeriesRankingLbl.setText(textGameSeriesRanking);

                //set series ranking list
                ArrayList<Team> sortedTeamsByPoints = sortTeamsByPoints(listTeams);
                llParentGameSeriesRanking.removeAllViews();
                for (int ij = 0; ij < sortedTeamsByPoints.size(); ij++) {
                    View cell = getTeamPlaceCell(ij, sortedTeamsByPoints, true);
                    if (cell != null) {
                        llParentGameSeriesRanking.addView(cell);
                    }
                }


                //change button text
                if (gameType == Constants.GAME_TYPE_SIMPLE) {
                    btnPlayAgain.setText(getActivity().getResources().getString(R.string.play_again));
                    txtGameSeriesRankingLbl.setVisibility(View.GONE);
                    llParentGameSeriesRanking.setVisibility(View.GONE);
                    llVertical.setVisibility(View.GONE);
                } else {
                    if (currentSeries == gameType) {
                        btnPlayAgain.setText(getActivity().getResources().getString(R.string.final_ranking));
                        mpFinish = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.game_finish_ex);
                    } else {
                        btnPlayAgain.setText(getActivity().getResources().getString(R.string.next));
                    }
                }

                //show layout
                int height = GlobalSingleton.getInstance().getScreenMetrics(getActivity()).heightPixels;
                scrollFinish.setY(-height);
                scrollFinish.setVisibility(View.VISIBLE);
                ObjectAnimator animation = ObjectAnimator.ofFloat(scrollFinish, "translationY", 0);
                animation.setDuration(3000);
                animation.start();

                //increase series
                currentSeries++;

                //change places and remove steppedId
                Team firstTeam = listTeams.get(0);
                firstTeam.setPawnSteppedId(-1);
                firstTeam.setId(listTeams.size() - 1);
                for (int j = 0; j < listTeams.size() - 1; j++) {
                    Team nextTeam = listTeams.get(j + 1);
                    nextTeam.setPawnSteppedId(-1);
                    nextTeam.setId(j);
                    listTeams.set(j, nextTeam);
                }
                listTeams.set(listTeams.size() - 1, firstTeam);

                //audio
                if (gameType == Constants.GAME_TYPE_SIMPLE || (currentSeries - 1) == gameType) {
                    mpFinish.setVolume(1f, 1f);
                    mpFinish.start();
                }

            });
        }
    }

    private void next() {
        rlNextTeam.setVisibility(View.VISIBLE);
        rlGame.setVisibility(View.GONE);
        rlGameAction.setVisibility(View.GONE);

        btnStart.setVisibility(View.VISIBLE);
        btnCorrect.setVisibility(View.GONE);
        btnCorrect.setEnabled(true);
        btnNextAc.setVisibility(View.GONE);
        imgToggleVisibility.setVisibility(View.GONE);
        toggleVisibility(true);

        txtTime.setTextColor(getResources().getColor(R.color.colorGrayPawn));
        imgTime.setImageResource(R.drawable.circle_black);
        imgTime.setAlpha(0.5f);
        //set current team
        currentTeam++;
        if (currentTeam == listTeams.size()) {
            currentTeam = 0;
            currentRound.setNumber(currentRound.getNumber() + 1);
            // currentRound.setSeriesPoints(getRandomPoints());

            int points = currentRound.getPoints();
            points--;
            if (points == 0) {
                points = 3;
            }
            currentRound.setPoints(points);
        }

        imgTeam.setImageResource(listTeams.get(currentTeam).getPawn().getResImage());

        String team = listTeams.get(currentTeam).getName();
        txtTeamNameTurn.setText(team);
        txtTeamNameTurn.setTextColor(getResources().getColor(listTeams.get(currentTeam).getPawn().getResColor()));
        team = getString(R.string.team) + " " + listTeams.get(currentTeam).getName();
        txtTeam.setText(team);
        txtTeam.setTextColor(getResources().getColor(listTeams.get(currentTeam).getPawn().getResColor()));
        txtTeam02.setText(team);
        txtTeam02.setTextColor(getResources().getColor(listTeams.get(currentTeam).getPawn().getResColor()));

        String roundPoints = getString(R.string.round) + " " + currentRound.getNumber() + " | " + getString(R.string.seriesPoints) + " " + currentRound.getPoints();
        txtRoundPoints.setText(roundPoints);
        txtRoundPoints02.setText(roundPoints);

        //set data
        switch (getRandomActivity()) {

            case Constants.TYPE_TALK: {
                if (listTalk.size() == 0) {
                    listTalk = WordHelper.getInstance().getList(Constants.TYPE_TALK, getActivity());
                }
                imgActivity.setImageResource(R.drawable.talk);
                txtActivity.setText(getString(R.string.activity_talk));
                int randomWordNr = getRandomWord(listTalk.size() - 1);
                String word = listTalk.get(randomWordNr);
                listTalk.remove(randomWordNr);
                txtWord.setText(word);
                lastWord = word;
                break;
            }
            case Constants.TYPE_MIME: {
                if (listMime.size() == 0) {
                    listMime = WordHelper.getInstance().getList(Constants.TYPE_MIME, getActivity());
                }
                imgActivity.setImageResource(R.drawable.mime);
                txtActivity.setText(getString(R.string.activity_mime));
                int randomWordNr = getRandomWord(listMime.size() - 1);
                String word = listMime.get(randomWordNr);
                listMime.remove(randomWordNr);
                txtWord.setText(word);
                lastWord = word;

                break;
            }
            case Constants.TYPE_DRAW: {
                if (listDraw.size() == 0) {
                    listDraw = WordHelper.getInstance().getList(Constants.TYPE_DRAW, getActivity());
                }
                imgActivity.setImageResource(R.drawable.draw);
                txtActivity.setText(getString(R.string.activity_draw));
                int randomWordNr = getRandomWord(listDraw.size() - 1);
                String word = listDraw.get(randomWordNr);
                listDraw.remove(randomWordNr);
                txtWord.setText(word);
                lastWord = word;
                break;
            }
        }

        //in test mode
//        lastWord = "Test sentence that takes 2 rows, \ntablet also :D";
//        lastWord = "Professional Footballer";
//        lastWord = "Professional Footballer pt2ra 1";
//        txtWord.setText(lastWord);


        toggleVisibility(true);

        //stop timer if works
        try {
            timePast = timeLimit;
            timer.cancel();
            timer.purge();
        } catch (Exception ignored) {
        }

        txtTime.setText(String.valueOf(timeLimit / 1000));

        isRoundForAll = (getRandomNumber(1, 100) < 10);

        //in test mode
//        isRoundForAll = true;

        if (isRoundForAll) {
            txtWord.setTextColor(getResources().getColor(R.color.colorRed));
            txtWordAll.setVisibility(View.VISIBLE);
            btnCorrect.setTextColor(getResources().getColor(R.color.colorRed));
            btnCorrect.setBackgroundResource(R.drawable.custom_button_gray);
            btnStart.setTextColor(getResources().getColor(R.color.colorRed));
            btnStart.setBackgroundResource(R.drawable.custom_button_gray);
        } else {
            txtWord.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            txtWordAll.setVisibility(View.GONE);
            btnCorrect.setTextColor(getResources().getColor(R.color.colorWhite));
            btnCorrect.setBackgroundResource(R.drawable.custom_button);
            btnStart.setTextColor(getResources().getColor(R.color.colorWhite));
            btnStart.setBackgroundResource(R.drawable.custom_button);
        }
    }

    private void setTimer() {
        //stop timer if works
        try {
            timePast = timeLimit;
            timer.cancel();
            timer.purge();
        } catch (Exception ignored) {
        }

        btnNextAc.setVisibility(View.GONE);
        imgToggleVisibility.setVisibility(View.VISIBLE);
        toggleVisibility(false);

        //set new timer
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timePast -= 1000;
//                if (timePast < 0) {
//                    timePast = timeLimit;
//                    timer.cancel();
//                    timer.purge();
//                    if (getActivity() != null) {
//                        getActivity().runOnUiThread(() -> {
//                            mpCountDown.setVolume(0, 0);
//                            mpCountDownAlarm.setVolume(0, 0);
//                            txtTime.setTextColor(getResources().getColor(R.color.colorRed));
//                            imgTime.setImageResource(R.drawable.circle_red);
//                            imgTime.setAlpha(1f);
//                            btnNextAc.setVisibility(View.VISIBLE);
//                            imgToggleVisibility.setVisibility(View.GONE);
//                            toggleVisibility(true);
//                        });
//                    }
//
//                    return;
//                }


                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        txtTime.setText(String.valueOf(timePast / 1000));
                        if (timePast == 0) {
                            timePast = timeLimit;
                            timer.cancel();
                            timer.purge();

                            mpCountDown.setVolume(0, 0);
                            mpCountDownAlarm.setVolume(1, 1);
                            mpCountDownAlarm.start();

                            txtTime.setTextColor(getResources().getColor(R.color.colorRed));
                            imgTime.setImageResource(R.drawable.circle_red);
                            imgTime.setAlpha(1f);
                            btnNextAc.setVisibility(View.VISIBLE);
                            imgToggleVisibility.setVisibility(View.GONE);
                            toggleVisibility(true);
                        } else if (timePast < 6000) {
                            mpCountDown.seekTo(20);
                            mpCountDown.setVolume(0.9f - (timePast / 10000f), 0.9f - (timePast / 10000f));
                            mpCountDown.start();
                        }
                    });
                }

            }
        }, 0, 1000);

    }

    private int getRandomActivity() {
        int activityMin = Constants.TYPE_TALK;
        int activityMax = Constants.TYPE_DRAW;

        return getRandomNumber(activityMin, activityMax);
    }

    private int getRandomWord(int max) {
        int min = 0;
        return getRandomNumber(min, max);
    }

    private int getRandomNumber(int min, int max) {
        int random;

        Random rand = new Random();
        random = rand.nextInt((max - min) + 1) + min;

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            random = ThreadLocalRandom.current().nextInt(min, max + 1); // idk why but this repeats exact same nrs every session
//        } else {
//            Random rand = new Random();
//            random = rand.nextInt((max - min) + 1) + min;
//        }

        return random;
    }

    private int checkPawnStepped(int id, int points) {

        //add sound effect
        // if (getActivity() != null) {
        //MediaPlayer mp = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.movement);
        mpMovement.start();
        // }

        //change positions
        Team cTeam = listTeams.get(id);
        cTeam.addNewPosition(points);

        //get id
        int idStepped = cTeam.getPawnSteppedId();
        //clear id
        cTeam.setPawnSteppedId(-1);

        //send id
        return idStepped;
    }

    private View getTeamPlaceCell(int id, ArrayList<Team> teams, boolean showPoints) {
        View view = null;
        if (getActivity() != null) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater != null) {
                //inflate
                view = inflater.inflate(R.layout.cell_team_places, null);

                //get views
                TextView txtPlace = view.findViewById(R.id.txt_place);
                ImageView img = view.findViewById(R.id.img);
                TextView txtName = view.findViewById(R.id.txt);
                TextView txtPoints = view.findViewById(R.id.txt_points);
                View vBackgorund = view.findViewById(R.id.v_bg);

                //set values
                Team team = teams.get(id);
                txtPlace.setText(String.valueOf(id + 1));
                img.setImageResource(team.getPawn().getResImage());
                txtName.setText(team.getName());
                txtName.setTextColor(getResources().getColor(team.getPawn().getResColor()));

                if (showPoints) {
                    txtPlace.setText(String.valueOf(team.getSeriesRank()));
                    txtPoints.setText(String.valueOf(team.getSeriesPoints() + "p"));
                    txtPoints.setVisibility(View.VISIBLE);
                    if (team.getSeriesRank() == 1) {
                        vBackgorund.setBackgroundColor(getResources().getColor(R.color.colorGolden));
                    } else if (team.getSeriesRank() == 2) {
                        vBackgorund.setBackgroundColor(getResources().getColor(R.color.colorGray));
                    } else if (team.getSeriesRank() == 3) {
                        vBackgorund.setBackgroundColor(getResources().getColor(R.color.colorBronze));
                    } else {
                        vBackgorund.setBackgroundColor(getResources().getColor(R.color.colorGrayDarkTransparent));
                    }

//                    if (team.getSeriesRank() == 1) {
//                        view.setBackgroundColor(getResources().getColor(R.color.colorGolden));
//                    } else if (team.getSeriesRank() == 2) {
//                        view.setBackgroundColor(getResources().getColor(R.color.colorGray));
//                    } else if (team.getSeriesRank() == 3) {
//                        view.setBackgroundColor(getResources().getColor(R.color.colorBronze));
//                    }
                } else {
                    txtPoints.setVisibility(View.GONE);
                }

            }
        }
        return view;
    }

    private void setDialog() {
        if (getActivity() == null) {
            return;
        }
        pawnDialog = new Dialog(getActivity());
        pawnDialog.setContentView(R.layout.dialog_pawn);

        RelativeLayout rlParent = pawnDialog.findViewById(R.id.rl_parent_pawn);

        //remove extra images from layout
        for (int i = rlParent.getChildCount(); i > listTeams.size(); i--) {
            rlParent.removeViewAt(i - 1);
        }

        //set the pawn colors
        for (int i = 0; i < rlParent.getChildCount(); i++) {
            ImageView pawnChild = (ImageView) rlParent.getChildAt(i);
            pawnChild.setImageResource(listTeams.get(i).getPawn().getResImage());

            if (i == rlParent.getChildCount() - 1 && i % 2 == 0) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pawnChild.getLayoutParams();
                params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                pawnChild.setLayoutParams(params);
            }

            int iFinal = i;
            //set the listeners
            pawnChild.setOnClickListener(view1 -> {
                        onClickCorrect(iFinal, currentRound.getPoints());
                        pawnDialog.dismiss();
                        if (getActivity() != null && getActivity().getCurrentFocus() != null) {
                            KeyboardUtils.hideKeyboard(getActivity(), getActivity().getCurrentFocus());
                        }
                    }
            );
        }

        //on cancel
        pawnDialog.setOnCancelListener(dialogInterface -> {
            btnCorrect.setEnabled(true);
            if (getActivity() != null && getActivity().getCurrentFocus() != null) {
                KeyboardUtils.hideKeyboard(getActivity(), getActivity().getCurrentFocus());
            }
        });

        //make dialog background transparent
        if (pawnDialog.getWindow() != null) {
            pawnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    private ArrayList<Team> sortTeamsByPlaces(int firstPlace, ArrayList<Team> unsortedList) {
        //only for game
        ArrayList<Team> sortedList = new ArrayList<>(unsortedList);

        //set first team points
        sortedList.get(firstPlace).setGameRank(1);

        //set first order
        if (firstPlace != 0) {
            Team otherPlace = sortedList.get(0);
            otherPlace.setGameRank(2);
            sortedList.set(0, sortedList.get(firstPlace));
            sortedList.set(firstPlace, otherPlace);
        }

        //if listTeam.size>2
        for (int i = 1; i < sortedList.size() - 1; i++) {
            for (int j = 2; j < sortedList.size(); j++) {
                if (sortedList.get(j).isGraterThen(sortedList.get(i))) {
                    Team team = sortedList.get(i);
                    team.setGameRank(j + 1);
                    sortedList.set(i, sortedList.get(j));
                    sortedList.set(j, team);
                }
            }
        }

        return sortedList;
    }

    private ArrayList<Team> sortTeamsByPoints(ArrayList<Team> unsortedList) {
        //only for series of games
        ArrayList<Team> sortedList = new ArrayList<>(unsortedList);

        //bubble sort teams by points number
        for (int i = 0; i < sortedList.size() - 1; i++) {
            for (int j = 0; j < sortedList.size() - i - 1; j++) {
                if (sortedList.get(j).getSeriesPoints() < sortedList.get(j + 1).getSeriesPoints()) {
                    Team tempTeam = sortedList.get(j);
                    sortedList.set(j, sortedList.get(j + 1));
                    sortedList.set(j + 1, tempTeam);
                }
            }
        }

        //add series places
        int lastRank = 1;
        for (int i = 0; i < sortedList.size(); i++) {
            sortedList.get(i).setSeriesRank(lastRank);
            //check if next rank is lower
            if (i < sortedList.size() - 1 && sortedList.get(i + 1).getSeriesPoints() < sortedList.get(i).getSeriesPoints()) {
                lastRank++;
            }
        }

        return sortedList;
    }

    private int setPawnsPodium(int index, ArrayList<Team> sortedTeams, LinearLayout layoutRank) {
        int lastIndex = index;
        if (index >= 0) {
            int layoutChildIndex = 0;
            int placePoints = sortedTeams.get(index).getSeriesPoints();
            for (int i = index; i < sortedTeams.size(); i++) {
                lastIndex = (i == index ? -1 : i);
                if (sortedTeams.get(i).getSeriesPoints() == placePoints) {
                    layoutRank.getChildAt(layoutChildIndex).setVisibility(View.VISIBLE);
                    ((ImageView) layoutRank.getChildAt(layoutChildIndex)).setImageResource(sortedTeams.get(i).getPawn().getResImage());
                    layoutChildIndex++;
                    //the last index just got value on podium => lastIndex = -1
                    lastIndex = (i == sortedTeams.size() - 1 ? -1 : i);
                } else {
                    break;
                }
            }
        }
        return lastIndex;
    }
}
