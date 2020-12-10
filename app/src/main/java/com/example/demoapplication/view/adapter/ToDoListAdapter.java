package com.example.demoapplication.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.demoapplication.databinding.ItemToDoListBinding;
import com.example.demoapplication.model.ToDoListModel;
import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ViewHolder> {

    private final List<ToDoListModel> toDoListModelList;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    private final OnItemClickListener listener;

    public ToDoListAdapter(List<ToDoListModel> toDoListModelList,OnItemClickListener listener) {
        this.toDoListModelList = toDoListModelList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        ItemToDoListBinding binding = ItemToDoListBinding.inflate(LayoutInflater.from(context));
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ToDoListModel toDoListModel = toDoListModelList.get(position);
        binderHelper.bind(holder.binding.swipeLayout, toDoListModel.toString());
        binderHelper.setOpenOnlyOne(true);
        holder.binding.textViewTaskName.setText(toDoListModel.getTaskName());
        holder.binding.textViewTaskDateTime.setText(toDoListModel.getTaskDateTime());
        holder.binding.textViwDelete.setOnClickListener(v -> {
            if(listener!=null) {
                listener.onDeleteClick(toDoListModel,binderHelper);
            }
        });
        holder.binding.relativeTaskDetails.setOnClickListener(v -> binderHelper.openLayout(toDoListModel.toString()));
    }

    @Override
    public int getItemCount() {
        return toDoListModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemToDoListBinding binding;

        ViewHolder(ItemToDoListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnItemClickListener {
        void onDeleteClick(ToDoListModel toDoListModel,ViewBinderHelper binderHelper);
    }
}
