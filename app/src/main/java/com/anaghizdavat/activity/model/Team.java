package com.anaghizdavat.activity.model;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Team {

    private int id;
    private String name;
    private ObjectListener finishListener;

    private int pawnSteppedId = -1;

    private int seriesPoints = 0;
    private int gameRank = -1;
    private int seriesRank = -1;

    private int line;
    private int column;
    private int position;

    private Pawn pawn;
    private LinearLayout llPanel;
    private LinearLayout llLine;
    private RelativeLayout rlColumn;
    private ImageView imgPosition;

    public Team() {

    }

    public Team(Team team) {
        this.id = team.id;
        this.name = team.name;
        this.finishListener = team.finishListener;
        this.pawnSteppedId = team.pawnSteppedId;
        this.seriesPoints = team.seriesPoints;
        this.gameRank = team.gameRank;
        this.line = team.line;
        this.column = team.column;
        this.position = team.position;
        this.pawn = new Pawn(team.pawn);
        this.llPanel = team.llPanel;
        this.llLine = team.llLine;
        this.rlColumn = team.rlColumn;
        this.imgPosition = team.imgPosition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeriesPoints() {
        return seriesPoints;
    }

    public void setSeriesPoints(int seriesPoints) {
        this.seriesPoints = seriesPoints;
    }

    public Pawn getPawn() {
        return pawn;
    }

    public void setPawn(Pawn pawn) {
        this.pawn = pawn;
    }

    public int getPawnSteppedId() {
        return pawnSteppedId;
    }

    public void setPawnSteppedId(int pawnSteppedId) {
        this.pawnSteppedId = pawnSteppedId;
    }

    private int getLine() {
        return line;
    }

    private int getColumn() {
        return column;
    }

    private int getPosition() {
        return position;
    }

    public void setGameRank(int gameRank) {
        this.gameRank = gameRank;
    }

    public int getSeriesRank() {
        return seriesRank;
    }

    public void setSeriesRank(int seriesRank) {
        this.seriesRank = seriesRank;
    }

    public void setInitialPanel(LinearLayout panel, int position, ObjectListener listener) {
        if ((line != 0 || column != 0)) {
            if (imgPosition != null) {
                this.imgPosition.setImageResource(0);
                this.imgPosition.setTag(-1);
            }
        }

        this.gameRank = 0;
        this.line = 0;
        this.column = 0;
        this.position = position;
        this.pawnSteppedId = -1;
        this.llPanel = panel;
        this.finishListener = listener;

        if (imgPosition != null) {
            this.imgPosition.setImageResource(0);
            this.imgPosition.setTag(-1);
        }


        movePawn();
    }

    public void addNewPosition(int value) {

        //get parity
        int initParity = line % 2;

        //if even => add | if odd => subtract
        column += (initParity == 0 ? 1 : -1) * value;

        if (value > 0) {
            //change the resource if it makes steps forward
            imgPosition.setImageResource(0);
            imgPosition.setTag(-1);

            if ((column > 2 && initParity == 0) || column < 0 && initParity == 1) {
                column = Math.abs(3 + (initParity == 0 ? -1 : 1) * column);
                line++;
                // change parity
                column = Math.abs(2 - column);

                //finish position
                if (line > 4) {
                    line = 4;
                    column = 2;
                }
            }

            position = 2;
        } else if (value < 0) {
            //don't change the resource if it makes steps back
            //because will delete the current player that took the gameRank of this pawn

            if ((column < 0 && initParity == 0) || (column > 2 && initParity == 1)) {
                column = Math.abs(3 + (initParity == 0 ? 1 : -1) * column);
                line--;
                // change parity
                column = Math.abs(2 - column);
                //keep position
            }

            //if just arrived on start column
            if (line == 0 && column == 0) {
                position = 3; //move on last position
            }

            //if was on start position
            if (line < 0) {
                line = 0;
                column = 0;
                position--;
            }

        }

        movePawn();
    }

    private void movePawn() {

//        android.view.View android.view.ViewGroup.getChildAt(int)' on a null object reference
        llLine = (LinearLayout) llPanel.getChildAt(line);
        rlColumn = (RelativeLayout) llLine.getChildAt(column);
        imgPosition = ((ImageView) ((LinearLayout) rlColumn.getChildAt(1)).getChildAt(position));

        //check step on other pawn
        if (imgPosition.getTag() != null) {
            String tag = imgPosition.getTag().toString();
            if (tag.length() > 0) {
                try {
                    pawnSteppedId = Integer.parseInt(tag);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //set new resource
        imgPosition.setImageResource(pawn.getResImage());
        imgPosition.setTag(id);

        //check finish
        checkFinish();
    }

    private void checkFinish() {
        //in test mode
//        if (line > 0 && finishListener != null && true) {
//            setSeriesPoints(getSeriesPoints() + 1);
//            finishListener.getObject(null);
//        }

        //check score
        if (column == 2 && line == 4) {
            this.setGameRank(1);
            setSeriesPoints(getSeriesPoints() + 1);
            if (finishListener != null)
                finishListener.getObject(null);
        }

    }

    public boolean isGraterThen(Team team) {

        int l = team.getLine();
        int c = team.getColumn();
        int p = team.getPosition();

        if (line != l) {
            return line > l;
        } else {
            if (column != c) {
                //if line is odd (because is starts from 0) => the closest column to the finish is from right to left
                return (line % 2 != 0 ? column < c : column > c);
            } else {
                return position > p;
            }
        }
    }
}
