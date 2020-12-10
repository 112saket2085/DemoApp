package com.example.demoapplication.view.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;
import com.example.demoapplication.R;
import com.example.demoapplication.databinding.FragmentAddTaskBinding;
import com.example.demoapplication.model.ToDoListModel;
import com.example.demoapplication.utility.DateConverterManager;
import com.example.demoapplication.utility.DialogHelper;
import com.example.demoapplication.utility.Util;
import com.example.demoapplication.viewmodel.TaskInfoViewModel;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskFragment extends BaseFragment{

    private FragmentAddTaskBinding binding;
    private String taskDescription,taskDate,taskTime;
    private TaskInfoViewModel viewModel;


    @Override
    protected ViewBinding getBinding(LayoutInflater inflater) {
        binding = FragmentAddTaskBinding.inflate(inflater);
        return binding;
    }

    @Override
    protected boolean isToolbarNeeded() {
        return true;
    }

    @Override
    protected String getActionBarTitle() {
        return getString(R.string.str_add_task);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
        setOnClickViews();
        binding.editTextTaskName.requestFocus();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(TaskInfoViewModel.class);
    }

    private void setOnClickViews() {
        binding.editTextTaskDate.setOnClickListener(v -> {
            Util.hideKeyboard(requireActivity());
            createDatePicker();
        });
        binding.editTextTaskTime.setOnClickListener(v -> {
            Util.hideKeyboard(requireActivity());
            createTimePicker();
        });
        binding.buttonSave.setOnClickListener(v -> {
            Util.hideKeyboard(requireActivity());
            if (isFieldValidated()) {
                viewModel.setData(new ToDoListModel(taskDescription, DateConverterManager.getInstance().getTaskDateString(taskDate) + " " + taskTime));
                DialogHelper.showOkDialog(requireActivity(), false, getString(R.string.str_task_added), getString(R.string.str_task_added_success), new DialogHelper.DialogCallback() {
                    @Override
                    public void onPositiveButtonClick(View view) {
                        clearFields();
                        navigateUp();
                    }
                });
            }
        });
        binding.buttonClear.setOnClickListener(v -> clearFields());
    }

    private boolean isFieldValidated() {
        taskDescription = binding.editTextTaskName.getText().toString();

        if(TextUtils.isEmpty(taskDescription)) {
            showToastMessage(getString(R.string.str_enter_task));
            return false;
        }
        if(TextUtils.isEmpty(taskDate)) {
            showToastMessage(getString(R.string.str_select_task_date));
            return false;
        }
        if(TextUtils.isEmpty(taskTime)) {
            showToastMessage(getString(R.string.str_select_task_time));
            return false;
        }
        return true;
    }

    private void clearFields() {
        binding.editTextTaskName.setText("");
        binding.editTextTaskDate.setText("");
        binding.editTextTaskTime.setText("");
    }

    private void createDatePicker() {
        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(locale);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getParentActivity(), (view, year1, month1, dayOfMonth) -> {
            calendar.set(year1, month1, dayOfMonth);
            taskDate = DateConverterManager.getInstance().getInputDateString(calendar.getTime());
            binding.editTextTaskDate.setText(DateConverterManager.getInstance().getOutputDateString(taskDate));
        }, year, month, day);
        Window window = datePickerDialog.getWindow();
        if(window!=null) {
            window.getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);
        }
        datePickerDialog.show();
    }

    private void createTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getParentActivity(), (view, hourOfDay, minute) -> {
            String amPM = hourOfDay < 12 ? "AM" : "PM";
            String formattedMin = minute > 9 ? String.valueOf(minute) : "0" + minute;
            taskTime = getString(R.string.str_formatted_time, String.valueOf(hourOfDay), formattedMin, amPM);
            binding.editTextTaskTime.setText(taskTime);

        }, hour, min, false);
        timePickerDialog.show();
    }
}
