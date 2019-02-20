package com.kamerlin.leon.todoapp.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.kamerlin.leon.todoapp.R;
import com.kamerlin.leon.todoapp.db.task.Task;
import com.kamerlin.leon.todoapp.ui.adapter.TaskListAdapter;
import com.kamerlin.leon.utils.common.ItemTouchCallback;
import com.kamerlin.leon.utils.mjolnir.MjolnirRecyclerAdapter;
import com.kamerlin.leon.utils.mjolnir.MjolnirRecyclerAdapter.OnClickListener;
import com.kamerlin.leon.utils.mjolnir.MjolnirRecyclerView;

import java.time.Instant;

public class TaskListFragment extends Fragment {
    public static final String TAG = TaskListFragment.class.getSimpleName();
    private static final String CATEGORY_ARG = "category_arg";
    private TaskListAdapter mTaskAdapter;
    private MjolnirRecyclerView mRecyclerView;
    private ItemTouchHelper itemTouchHelper;


    public TaskListFragment() {
        System.out.println("new instance");
    }

    public static TaskListFragment newInstance(String category) {
        TaskListFragment menuFragment = new TaskListFragment();
        Bundle bundle =  new Bundle();
        bundle.putString(CATEGORY_ARG, category);
        menuFragment.setArguments(bundle);
        return menuFragment;
    }

    public void setAdapter(TaskListAdapter adapter) {
        mTaskAdapter = adapter;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        String category = args.getString(CATEGORY_ARG, "None");

        View rootView = inflater.inflate(R.layout.fragment_task_list, container, false);
        mRecyclerView = rootView.findViewById(R.id.recycler_view);

        setupRecyclerView();
        setupAdapter();
        return rootView;
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mTaskAdapter);


    }

    private void setupAdapter() {



        ItemTouchCallback swipeToDeleteCallback = new ItemTouchCallback.Builder(getContext())
                .setListener(mTaskAdapter)
                .setBackgroundColor(Color.GREEN)
                .setLeftIcon(R.drawable.ic_delete_white_24)
                .setRightIcon(R.drawable.ic_delete_white_24)
                .enableItemViewSwipe(true)
                .enableLongPressDrag(true)
                .build();


        itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);


        if (getActivity() instanceof OnClickListener) {
            mTaskAdapter.setOnClickListener((OnClickListener<Task>) getActivity());
        }



    }



}
