package com.example.demoapplication.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.example.demoapplication.app.Constants;
import com.example.demoapplication.databinding.FragmentLoginBinding;
import com.example.demoapplication.manager.GoogleAuthManager;
import com.example.demoapplication.model.AppConfig;
import com.example.demoapplication.model.UserInfoModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends BaseFragment implements GoogleAuthManager.OnAuthenticationCallbackListener{

    private FragmentLoginBinding binding;
    private GoogleAuthManager googleAuthManager;

    @Override
    protected ViewBinding getBinding(LayoutInflater inflater) {
        binding = FragmentLoginBinding.inflate(inflater);
        return binding;
    }

    @Override
    protected boolean isToolbarNeeded() {
        return false;
    }

    @Override
    protected int getLaunchMode() {
        return Constants.PRE_DASHBOARD;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        googleAuthManager = new GoogleAuthManager(this, FirebaseAuth.getInstance(),new ProgressDialog(requireActivity()));
        googleAuthManager.setSignInRequestLauncher();
        googleAuthManager.setAuthenticationListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnClickViews();
    }

    private void setOnClickViews() {
        binding.buttonSignIn.setOnClickListener(view -> googleAuthManager.launchSignIn());
    }

    @Override
    public void onAuthenticationSuccess(FirebaseUser firebaseUser) {
        AppConfig.getInstance().setLoggedIn(true);
        UserInfoModel userInfoModel = new UserInfoModel(firebaseUser.getDisplayName(), firebaseUser.getEmail(),firebaseUser.getPhotoUrl().toString());
        LoginFragmentDirections.ActionLoginToEncryptDecrypt action = LoginFragmentDirections.actionLoginToEncryptDecrypt();
        action.setUserInfoModel(userInfoModel);
        navigateWithResult(action);
    }

    @Override
    public void onAuthenticationFail(String msg) {
        showToastMessage(msg);
    }
}
