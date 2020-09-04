package com.anaghizdavat.activity.others;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class WordHelper {

    private static WordHelper mInstance;
    private static Context mContext;
    private ArrayList<String> mimeList;
    private ArrayList<String> talkList;
    private ArrayList<String> drawList;

    public static WordHelper getInstance() {
        if (mInstance == null) {
            mInstance = new WordHelper();
        }

        return mInstance;
    }

    public void cleanLists(){
        if(talkList!=null){
            talkList.clear();
        }
        if(mimeList!=null){
            mimeList.clear();
        }
        if(drawList!=null){
            drawList.clear();
        }
    }

    public ArrayList<String> getList(int type, Context context) {
        mContext = context;

        ArrayList<String> list = null;

        switch (type) {
            case Constants.TYPE_TALK: {
                if (talkList == null || talkList.size()==0)  {
                    talkList = getReadArrayList(Constants.FILE_TALK + "_" + GlobalSingleton.getInstance().getString(Constants.KEY_LOCALE,mContext));
                }
                list = talkList;
                break;
            }
            case Constants.TYPE_MIME: {
                if (mimeList == null|| mimeList.size()==0) {
                    mimeList = getReadArrayList(Constants.FILE_MIME + "_" + GlobalSingleton.getInstance().getString(Constants.KEY_LOCALE,mContext));
                }
                list = mimeList;
                break;
            }
            case Constants.TYPE_DRAW: {
                if (drawList == null || drawList.size()==0) {
                    drawList = getReadArrayList(Constants.FILE_DRAW + "_" + GlobalSingleton.getInstance().getString(Constants.KEY_LOCALE, mContext));
                }
                list = drawList;
                break;
            }

        }
        return list;
    }

    private ArrayList<String> getReadArrayList(String listName) {
        Log.e("LocaleKey", listName);
        ArrayList<String> list = new ArrayList<>();

        //get file

        String a = "" +mContext.getApplicationContext().getResources().getIdentifier("mode", "raw",  mContext.getApplicationContext().getPackageName());
        String s = "" +mContext.getResources().getIdentifier(mContext.getPackageName() +":raw/"+listName, null, null);
        InputStream inputStream = mContext.getResources().openRawResource(mContext.getResources().getIdentifier(listName, "raw", mContext.getPackageName()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));


        //add file content to list
        try {
            while (reader.ready()) {
                String line = reader.readLine();
                if (line!=null&& !line.isEmpty() && !line.contains("//")) {
                    list.add(line.toUpperCase());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

}
