package com.example.demoapplication.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.demoapplication.R;
import com.example.demoapplication.app.Constants;
import com.example.demoapplication.databinding.FragmentToDoListBinding;
import com.example.demoapplication.model.ToDoListModel;
import com.example.demoapplication.utility.DialogHelper;
import com.example.demoapplication.view.adapter.ToDoListAdapter;
import com.example.demoapplication.viewmodel.TaskInfoViewModel;
import java.util.ArrayList;
import java.util.List;

public class ToDoListFragment extends BaseFragment implements ToDoListAdapter.OnItemClickListener{

    private FragmentToDoListBinding binding;
    private final List<ToDoListModel> toDoListModelList = new ArrayList<>();
    private ToDoListAdapter toDoListAdapter;
    private TaskInfoViewModel viewModel;
    private ToDoListModel toDoListModel;
    private ViewBinderHelper binderHelper;

    @Override
    protected ViewBinding getBinding(LayoutInflater inflater) {
       binding = FragmentToDoListBinding.inflate(inflater);
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
        return getString(R.string.str_to_do);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        initViewModel();
        observeTaskData();
        setOnClickViews();
    }

    private void initViewModel(){
        viewModel = new ViewModelProvider(this).get(TaskInfoViewModel.class);
    }

    private void setOnClickViews() {
        binding.buttonAdd.setOnClickListener(v -> navigateTo(R.id.action_toDoListFragment_to_addTaskFragment));
    }

    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.recyclerViewList.setLayoutManager(linearLayoutManager);
        toDoListAdapter = new ToDoListAdapter(toDoListModelList,this);
        binding.recyclerViewList.setAdapter(toDoListAdapter);
    }

    private void observeTaskData() {
        setNoDataVisibility(View.GONE,"");
        viewModel.getData().observe(getViewLifecycleOwner(), toDoListModelList -> {
            if (toDoListModelList != null && !toDoListModelList.isEmpty()) {
                this.toDoListModelList.clear();
                this.toDoListModelList.addAll(toDoListModelList);
                toDoListAdapter.notifyDataSetChanged();
            }
            else {
                setNoDataVisibility(View.VISIBLE,getString(R.string.str_no_task_added));
            }
        });
    }

    private void setNoDataVisibility(int visibility,String text) {
        this.toDoListModelList.clear();
        toDoListAdapter.notifyDataSetChanged();
        binding.textViewNoData.setVisibility(visibility);
        binding.textViewNoData.setText(text);
    }

    @Override
    public void onDeleteClick(ToDoListModel toDoListModel, ViewBinderHelper binderHelper) {
        this.binderHelper = binderHelper;
        this.toDoListModel = toDoListModel;
        binderHelper.closeLayout(toDoListModel.toString());
        DialogHelper.showGenericDialog(requireActivity(), false, getString(R.string.str_delete_task), getString(R.string.str_delete_task_info), getString(R.string.button_yes), getString(R.string.button_no), new DialogHelper.DialogCallback() {
            @Override
            public void onPositiveButtonClick(View view) {
                viewModel.removeData(toDoListModel);
                observeTaskData();
            }
        });
    }

    @Override
    public void onResume() {
        if(binderHelper!=null) {
            binderHelper.closeLayout(toDoListModel.toString());
        }
        super.onResume();
    }
}
