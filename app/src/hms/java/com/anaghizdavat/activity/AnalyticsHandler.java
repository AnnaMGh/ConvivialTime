package com.anaghizdavat.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.huawei.agconnect.crash.AGConnectCrash;
import com.huawei.hms.analytics.HiAnalytics;
import com.huawei.hms.analytics.HiAnalyticsInstance;
import com.huawei.hms.analytics.HiAnalyticsTools;

import java.util.ArrayList;

public class AnalyticsHandler {

    public static final String ITEM_ID = "item_id";
    public static final String ITEM_NAME = "item_name";
    public static final String ITEM_CATEGORY = "item_category";
    public static final String ITEM_VARIANT = "item_variant";
    public static final String ITEM_BRAND = "item_brand";
    public static final String PRICE = "price";
    public static final String CURRENCY = "currency";
    public static final String INDEX = "index";
    public static final String QUANTITY = "quantity";

    public static final String TRANSACTION_ID = "transaction_id";
    public static final String AFFILIATION = "affiliation";
    public static final String VALUE = "value";
    public static final String TAX = "tax";
    public static final String SHIPPING = "shipping";
    public static final String COUPON = "coupon";

    public static final String VIEW_SEARCH_RESULTS = "view_search_results";
    public static final String ITEM_LIST = "item_list";
    public static final String SELECT_CONTENT = "select_content";
    public static final String VIEW_ITEM = "view_item";
    public static final String ECOMMERCE_PURCHASE = "ecommerce_purchase";

    private static HiAnalyticsInstance analyticsInstance;

    public static void enableCrashlytics(Context context){
        AGConnectCrash.getInstance().enableCrashCollection(true);
    }

    public static void forceCrash(Context context){
        AGConnectCrash.getInstance().testIt(context); // Force a crash Huawei Style
    }

    public static void registerAnalytics(Context context) {
        HiAnalyticsTools.enableLog();
        analyticsInstance = HiAnalytics.getInstance(context);
        analyticsInstance.setAnalyticsEnabled(true);
        analyticsInstance.setAutoCollectionEnabled(true);
        analyticsInstance.regHmsSvcEvent();
    }

    public static void unregisterAnalytics() {
        analyticsInstance.unRegHmsSvcEvent();
    }

    public static void sendMessage(Context context, String title, Bundle bundle) {
        if (analyticsInstance == null) {
            registerAnalytics(context);
        }
        analyticsInstance.onEvent(title, bundle);

//        sendProductClicksEvent(context);
        sendProductImpressionsEvent(context, "saved");
    }


    public static void sendProductClicksEvent(Context context) {

        if (analyticsInstance == null) {
            registerAnalytics(context);
        }
        Bundle product = new Bundle();


        product.putString(ITEM_ID, "id"); // id-ul unic al job-ului
        product.putString(ITEM_NAME, "job.titlu"); // numele job-ului
        product.putString(ITEM_CATEGORY, "context.cropString(job.getOrase())"); // orasul pentru care a fost publicat job-ul, se va trimite ca string. Exemplu pentru un job valabil in 4 locatii: “Bucuresti, Ploiesti, Pitesti, Remote”
        product.putString(ITEM_VARIANT, "job.getNivelCariera()"); //ex. Entry-level
        product.putString(ITEM_BRAND, "job.firma"); // ex. Vodafone
        product.putString(PRICE, "0"); // empty string
        product.putString(CURRENCY, "0"); // empty string
        product.putLong(INDEX, 1); // pozitia in lista a job-ului


        String debugText = product.getString(ITEM_ID)
                + " | " + product.getString(ITEM_NAME)
                + " | " + product.getString(ITEM_CATEGORY)
                + " | " + product.getString(ITEM_VARIANT)
                + " | " + product.getString(ITEM_BRAND)
                + " | " + product.getString(PRICE)
                + " | " + product.getString(CURRENCY)
                + " | " + product.getLong(INDEX);


        Bundle ecommerceBundle = new Bundle();
        ecommerceBundle.putBundle("items", product);
        ecommerceBundle.putString(ITEM_LIST, "listName"); //numele listei in care job-ul a fost afisat (“search” sau “saved”)

        analyticsInstance.onEvent(SELECT_CONTENT, ecommerceBundle);

        Log.i("bla", debugText);
    }

    public static void sendProductImpressionsEvent(Context context, String listName) {
        if (analyticsInstance == null) {
            registerAnalytics(context);
        }
        ArrayList items = new ArrayList();

        for (int i = 0; i < 2; i++) {

            Bundle product = new Bundle();

            if (listName.equals("search")) {
                product.putString(ITEM_ID, "String.valueOf(job.id)"); // id-ul unic al job-ului
                product.putString(ITEM_NAME, "job.title"); // numele job-ului
                product.putString(ITEM_CATEGORY, "context.cropString(job.getCitiesAsString())"); // orasul pentru care a fost publicat job-ul, se va trimite ca string. Exemplu pentru un job valabil in 4 locatii: “Bucuresti, Ploiesti, Pitesti, Remote”
                product.putString(ITEM_VARIANT, "job.getCareerLevelsAsString()"); //ex. Entry-level
                product.putString(ITEM_BRAND, "job.company.getName()"); // ex. Vodafone
                product.putString(PRICE, "0"); // empty string
                product.putString(CURRENCY, "0"); // empty string
                product.putLong(INDEX, i + 1 ); // pozitia in lista a job-ului
            } else if (listName.equals("saved")) {
                product.putString(ITEM_ID, "String.valueOf(job.id)"); // id-ul unic al job-ului
                product.putString(ITEM_NAME, "job.titlu"); // numele job-ului
                product.putString(ITEM_CATEGORY, "context.cropString(job.getCitiesAsString())"); // orasul pentru care a fost publicat job-ul, se va trimite ca string. Exemplu pentru un job valabil in 4 locatii: “Bucuresti, Ploiesti, Pitesti, Remote”
                product.putString(ITEM_VARIANT, "job.getCareerAsString()"); //ex. Entry-level
                product.putString(ITEM_BRAND, "job.companie"); // ex. Vodafone
                product.putString(PRICE, "0"); // empty string
                product.putString(CURRENCY, "0"); // empty string
                product.putLong(INDEX, i + 1); // pozitia in lista a job-ului
            }

            String debugText = product.getString(ITEM_ID)
                    + " | " + product.getString(ITEM_NAME)
                    + " | " + product.getString(ITEM_CATEGORY)
                    + " | " + product.getString(ITEM_VARIANT)
                    + " | " + product.getString(ITEM_BRAND)
                    + " | " + product.getString(PRICE)
                    + " | " + product.getString(CURRENCY)
                    + " | " + product.getLong(INDEX);

            Log.i("bla", debugText);
            items.add(product);
        }

        Bundle ecommerceBundle = new Bundle();
        ecommerceBundle.putParcelableArrayList("items", items);
        ecommerceBundle.putString(ITEM_LIST, listName); //numele listei in care job-ul a fost afisat (“search” sau “saved”)

        analyticsInstance.onEvent(VIEW_SEARCH_RESULTS, ecommerceBundle);


    }

}
