package com.example.demoapplication.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.example.demoapplication.R;
import com.example.demoapplication.databinding.FragmentChangeLanguageBinding;
import com.example.demoapplication.model.AppConfig;
import com.example.demoapplication.utility.LocaleUtils;
import com.example.demoapplication.view.activity.MainActivity;

public class ChangeLanguageFragment extends BaseFragment{

    private FragmentChangeLanguageBinding binding;

    @Override
    protected ViewBinding getBinding(LayoutInflater inflater) {
        binding = FragmentChangeLanguageBinding.inflate(inflater);
        return binding;
    }

    @Override
    protected boolean isToolbarNeeded() {
        return true;
    }


    @Override
    protected String getActionBarTitle() {
        return getString(R.string.str_change_language);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnClickViews();
        setLanguage();
    }

    private void setLanguage() {
        binding.radioButtonEnglish.setChecked(AppConfig.getInstance().getLocale().equalsIgnoreCase(LocaleUtils.getInstance().getEnglishLocale()));
        binding.radioButtonArabic.setChecked(AppConfig.getInstance().getLocale().equalsIgnoreCase(LocaleUtils.getInstance().getArabicLocale()));
    }

    public void setOnClickViews() {
        binding.buttonSave.setOnClickListener(v -> {
            if(binding.radioButtonEnglish.isChecked()) {
                LocaleUtils.getInstance().setLocale(requireActivity(),getString(R.string.str_english),LocaleUtils.getInstance().getEnglishLocale(),true);
            }
            else if(binding.radioButtonArabic.isChecked()) {
                LocaleUtils.getInstance().setLocale(requireActivity(),getString(R.string.str_arabic),LocaleUtils.getInstance().getArabicLocale(),true);
            }
            getParentActivity().startActivity(new Intent(requireActivity(), MainActivity.class));
            getParentActivity().finishWithTransition();
        });
    }
}
