package com.kamerlin.leon.todoapp.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.kamerlin.leon.todoapp.R;
import com.kamerlin.leon.todoapp.databinding.ItemTaskBinding;
import com.kamerlin.leon.todoapp.db.category.Category;
import com.kamerlin.leon.todoapp.db.category.CategoryDao;
import com.kamerlin.leon.todoapp.db.task.Task;
import com.kamerlin.leon.todoapp.db.task.TaskService;
import com.kamerlin.leon.utils.materialpallete.MaterialColor;
import com.kamerlin.leon.utils.materialpallete.MaterialColorFactory;
import com.kamerlin.leon.utils.mjolnir.MjolnirRecyclerAdapter;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.core.util.Pair;
import androidx.recyclerview.widget.DiffUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.ReplaySubject;


public class TaskListAdapter extends MjolnirRecyclerAdapter<Task> {

    private static TaskListAdapter INSTANCE;

    public static TaskListAdapter newInstance(Activity activity, CategoryDao categoryDao) {
        return new TaskListAdapter(activity, categoryDao);
    }

    public static TaskListAdapter getInstance(Activity activity, CategoryDao categoryDao) {
        if (INSTANCE == null) {
            synchronized (TaskListAdapter.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TaskListAdapter(activity, categoryDao);
                }
            }
        }

        return INSTANCE;
    }

    private ReplaySubject<Pair<Task, Task>> mPairReplaySubject;

    @Override
    public void onItemDismiss(int position) {
        Task task = get(position);
        if (task != null) {
            task.cancelReminder();
            TaskService.delete(getContext(), task);
            super.onItemDismiss(position);
        }
    }




    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Task from = get(fromPosition);
        Task to = get(toPosition);
        if (from != null && to != null) {
            super.onItemMove(fromPosition, toPosition);
            mPairReplaySubject.onNext(new Pair<>(from, to));

        }

    }

    public Observable<Pair<Task, Task>> getOnItemMoveObservable() {
        return mPairReplaySubject;
    }

    private final CategoryDao mCategoryDao;

    @SuppressLint("CheckResult")
    public TaskListAdapter(Activity activity, CategoryDao categoryDao) {
        super(activity, Collections.emptyList());
        mCategoryDao = categoryDao;
        mPairReplaySubject = ReplaySubject.create();
        getOnItemMoveObservable()
                .filter(taskTaskPair -> taskTaskPair.first.getId() != taskTaskPair.second.getId())
                .debounce(600, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(taskTaskPair -> {
            TaskService.swapTask(activity, taskTaskPair.first, taskTaskPair.second);
        });
    }

    @Override
    protected TaskListHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.item_task, parent, false);
        View backgroundView = LayoutInflater.from(getContext()).inflate(R.layout.item_remove, null, false);
        return new TaskListHolder(rootView, backgroundView);
    }


    class TaskListHolder extends ItemViewHolder {
        ItemTaskBinding mBinding;
        AppCompatImageView circleImageView;
        ConstraintLayout constraintLayout;



        public TaskListHolder(View itemView, View background) {
            super(itemView, background);
            mBinding = ItemTaskBinding.bind(itemView);
            circleImageView = itemView.findViewById(R.id.circleImage);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
        }

        @SuppressLint("CheckResult")
        @Override
        protected void bind(Task task, int position) {
            super.bind(task, position);
            mBinding.setTask(task);

            mCategoryDao.getCategoryByNameSingle(task.getCategoryName()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(category -> {
                MaterialColor materialColor = MaterialColorFactory.getColor(category.getColorName());
                setBackgroundColor(circleImageView, materialColor.get500());
            });
            MaterialColor materialColor = MaterialColorFactory.getColor(MaterialColor.DEEP_PURPLE);

            switch (task.getPriorityCode()) {
                case Task.PRIORITY_LOW:

                    setBackgroundColor(constraintLayout, materialColor.get50());
                    break;
                case Task.PRIORITY_MEDIUM:

                    setBackgroundColor(constraintLayout, materialColor.get100());
                    break;
                case Task.PRIORITY_HIGH:
                    setBackgroundColor(constraintLayout, materialColor.get200());
                    break;

                default:
                    setBackgroundColor(constraintLayout, R.color.material_white);
                    break;
            }

            if (task.getDueDateFormat() != null) {
                mBinding.dueDate.setText(task.getDueDateFormat());
            } else {
                mBinding.dueDate.setText(getContext().getResources().getText(R.string.not_set));
            }


        }




    }

    public void setBackgroundColor(View view, int resource) {
        Drawable background = view.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(ContextCompat.getColor(getContext(), resource));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(ContextCompat.getColor(getContext(), resource));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(ContextCompat.getColor(getContext(), resource));
        }

    }



}
