package com.anaghizdavat.activity.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anaghizdavat.activity.activities.BaseActivity;
import com.anaghizdavat.activity.model.ObjectListener;

public class BaseFragment extends Fragment {

    View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return rootView;
    }

    public void showToast(String text) {
        ((BaseActivity) getActivity()).showToast(text);
    }

    public void showAlert(String message, ObjectListener listenerYes, ObjectListener listenerNo) {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity == null) return;

        activity.showAlert(message, listenerYes, listenerNo);
    }

    public void hideAlert() {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity == null) return;

        activity.hideLoading();
    }

    public void showAlert() {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity == null) return;

        activity.showLoading();
    }

    public void hideLoading() {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity == null) return;

        activity.hideLoading();
    }

    public void addFragment(int containerViewId, Fragment fragment, String fragmentTag) {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity == null) return;

        activity.addFragment(containerViewId, fragment, fragmentTag);
    }

    public void addFragmentAsNew(int containerViewId, String lastFragmentTag, Fragment fragment, String fragmentTag) {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity == null) return;

        activity.addFragmentAsNew(containerViewId, lastFragmentTag, fragment, fragmentTag);
    }

    public void removeFragment(Fragment fragment) {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity == null) return;

        activity.removeFragment(fragment);
    }

    protected void replaceFragment(int containerViewId, Fragment fragment, String fragmentTag, String backStackStateName) {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity == null) return;

        activity.replaceFragment(containerViewId, fragment, fragmentTag, backStackStateName);
    }

    public void updateStatusBarColor(int color) {// Color must be in hexadecimal fromat
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity == null) return;

        activity.updateStatusBarColor(color);
    }
}
