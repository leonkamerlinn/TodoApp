package com.kamerlin.leon.todoapp.utils;

import androidx.recyclerview.widget.DiffUtil;

import com.kamerlin.leon.todoapp.db.task.Task;

import java.util.List;

public class TaskDiffUtilCallback extends DiffUtil.Callback {
    List<Task> mNewList, mOldList;

    public TaskDiffUtilCallback(List<Task> newList, List<Task> oldList) {
        mNewList = newList;
        mOldList = oldList;
    }

    @Override
    public int getOldListSize() {
        return mOldList != null ? mOldList.size() : 0 ;
    }

    @Override
    public int getNewListSize() {
        return mNewList != null ? mNewList.size() : 0 ;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Task oldItem = mOldList.get(oldItemPosition);
        Task newItem = mNewList.get(newItemPosition);
        return oldItem.equals(newItem);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Task oldItem = mOldList.get(oldItemPosition);
        Task newItem = mNewList.get(newItemPosition);
        return oldItem.equals(newItem);
    }
}
