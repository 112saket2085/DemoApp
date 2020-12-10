package com.example.demoapplication.view.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.example.demoapplication.R;
import com.example.demoapplication.app.Constants;
import com.example.demoapplication.databinding.FragmentEncryptDecryptBinding;
import com.example.demoapplication.model.UserInfoModel;
import com.example.demoapplication.security.Crypto;
import com.example.demoapplication.utility.Util;

public class EncryptDecryptFragment extends BaseFragment{

    private FragmentEncryptDecryptBinding binding;
    private String message,key;

    @Override
    protected ViewBinding getBinding(LayoutInflater inflater) {
        binding = FragmentEncryptDecryptBinding.inflate(inflater);
        return binding;
    }

    @Override
    protected boolean isToolbarNeeded() {
        return true;
    }

    @Override
    protected int getLaunchMode() {
        return Constants.POST_DASHBOARD;
    }

    @Override
    protected String getActionBarTitle() {
        return getString(R.string.str_encryption);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnClickViews();
        getBundleValues();
        binding.editTextMessage.requestFocus();
    }

    private void getBundleValues() {
        Bundle bundle = getArguments();
        if(bundle!=null) {
            EncryptDecryptFragmentArgs args = EncryptDecryptFragmentArgs.fromBundle(bundle);
            UserInfoModel userInfoModel = args.getUserInfoModel();
            if(userInfoModel!=null) {
                getParentActivity().setUserValues(userInfoModel);
            }
        }
    }


    private void setOnClickViews() {
        binding.buttonEncrypt.setOnClickListener(view -> {
            Util.hideKeyboard(requireActivity());
            if (isFieldValidated()) {
                try {
                    String encryptedText = Crypto.encryptAES(Crypto.getSecretKey(key), message);
                    binding.textViewResultText.setText(getString(R.string.str_encrypted_text));
                    binding.textViewResultValue.setText(encryptedText);
                } catch (Exception e) {
                    showToastMessage(getString(R.string.str_default_err_message));
                }
            }

        });
        binding.buttonDecrypt.setOnClickListener(view -> {
            Util.hideKeyboard(requireActivity());
            if (isFieldValidated()) {
                try {
                    String decryptedText = Crypto.decryptAES(Crypto.getSecretKey(key), message);
                    binding.textViewResultText.setText(getString(R.string.str_decrypted_text));
                    binding.textViewResultValue.setText(decryptedText);
                } catch (Exception e) {
                    showToastMessage(getString(R.string.str_default_err_message));
                }
            }
        });
    }

    private boolean isFieldValidated() {
        message = binding.editTextMessage.getText().toString();
        key = binding.editTextKey.getText().toString();

        if(TextUtils.isEmpty(message)) {
            showToastMessage(getString(R.string.str_message));
            return false;
        }
        if(TextUtils.isEmpty(key)) {
            showToastMessage(getString(R.string.str_secret_key));
            return false;
        }
        return true;
    }
}
