package com.example.demoapplication.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.viewbinding.ViewBinding;
import com.example.demoapplication.app.Constants;
import com.example.demoapplication.view.activity.MainActivity;

public abstract class BaseFragment extends Fragment {

    protected abstract ViewBinding getBinding(LayoutInflater inflater);
    protected boolean isToolbarNeeded() {return true;}
    protected String getActionBarTitle() {return "";}
    protected int getLaunchMode(){return Constants.DEFAULT;}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return getBinding(inflater).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getParentActivity().setActionBarVisibility(isToolbarNeeded());
        getParentActivity().setActionBarTitle(getActionBarTitle());
        getParentActivity().setLaunchMode(getLaunchMode());
    }

    protected MainActivity getParentActivity() {
        return (MainActivity) requireActivity();
    }

    protected void showToastMessage(String message) {
        Toast.makeText(requireActivity(),message,Toast.LENGTH_LONG).show();
    }

    protected void navigateTo(int resId) {
        getParentActivity().navController.navigate(resId);
    }

    protected void navigateUp() {
        getParentActivity().navController.navigateUp();
    }

    protected void navigateWithResult(NavDirections navDirections) {
        getParentActivity().navController.navigate(navDirections);
    }
}
