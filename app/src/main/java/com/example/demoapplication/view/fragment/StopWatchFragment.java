package com.example.demoapplication.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.example.demoapplication.R;
import com.example.demoapplication.app.Constants;
import com.example.demoapplication.databinding.FragmentStopWatchBinding;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class StopWatchFragment extends BaseFragment{

    private FragmentStopWatchBinding binding;
    private Handler handler;
    private boolean isTimerStarted = false;
    private int seconds=0;
    private long previousSavedMillis = -1;

    @Override
    protected ViewBinding getBinding(LayoutInflater inflater) {
        binding = FragmentStopWatchBinding.inflate(inflater);
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
        return getString(R.string.str_stopwatch);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnClickViews();
        initHandler();
    }

    private void setOnClickViews() {
        binding.imageViewPlay.setOnClickListener(v -> {
            if (!isTimerStarted) {
                startTimer();
            } else {
                pauseTimer();
            }
        });
        binding.imageViewStop.setOnClickListener(v -> resetTimer());
    }

    private void initHandler() {
        handler = new Handler();
    }

    /**
     * Method to start timer
     */
    private void startTimer() {
        start();
        binding.imageViewPlay.setImageResource(R.drawable.ic_pause);
        isTimerStarted = true;
    }

    /**
     * Method to pause timer
     */
    private void pauseTimer() {
        binding.imageViewPlay.setImageResource(R.drawable.ic_play);
        isTimerStarted = false;
        handler.removeCallbacks(runnable);
    }

    /**
     * Method to reset/stop timer
     */
    private void resetTimer() {
        binding.imageViewPlay.setImageResource(R.drawable.ic_play);
        binding.textViewTimer.setText(getString(R.string.str_default_timer));
        seconds = 0;
        isTimerStarted = false;
        handler.removeCallbacks(runnable);
    }

    private void start() {
        handler.postDelayed(runnable, 1000);
    }


    private final Runnable runnable = () -> {
        updateUI();
        start();
    };

    /**
     * Method to update StopWatch timer
     */
    private void updateUI() {
        seconds = seconds + 1;
        int hour = seconds / 3600;
        int min = (seconds % 3600) / 60;
        int sec = seconds % 60;
        binding.textViewTimer.setText(getString(R.string.str_timer, getFormattedString(hour), getFormattedString(min), getFormattedString(sec)));
    }

    private String getFormattedString(int input) {
        return String.format(Locale.ENGLISH, "%02d", input);
    }

    @Override
    public void onResume() {
        super.onResume();
        long currentMillis = System.currentTimeMillis() - previousSavedMillis;
        if (previousSavedMillis != -1 && isTimerStarted) {
            seconds = seconds + (int) TimeUnit.MILLISECONDS.toSeconds(currentMillis);
            updateUI();
            start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        previousSavedMillis = System.currentTimeMillis();
    }
}
