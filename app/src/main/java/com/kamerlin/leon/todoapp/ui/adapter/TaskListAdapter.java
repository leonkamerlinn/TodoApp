package com.kamerlin.leon.todoapp.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.kamerlin.leon.todoapp.R;
import com.kamerlin.leon.todoapp.databinding.ItemTaskBinding;
import com.kamerlin.leon.todoapp.db.category.CategoryDao;
import com.kamerlin.leon.todoapp.db.task.Task;
import com.kamerlin.leon.utils.materialpallete.MaterialColor;
import com.kamerlin.leon.utils.materialpallete.MaterialColorFactory;
import com.kamerlin.leon.utils.mjolnir.ActionModeRecyclerViewAdapter;
import com.kamerlin.leon.utils.mjolnir.MjolnirViewHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class TaskListAdapter extends ActionModeRecyclerViewAdapter<Task> {


    private final CategoryDao mCategoryDao;

    @Inject
    public TaskListAdapter(Activity context, CategoryDao categoryDao) {
        super(context, Collections.emptyList());
        mCategoryDao = categoryDao;

    }

    @Override
    protected TaskListHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskListHolder(rootView);
    }


    class TaskListHolder extends ItemViewHolder {
        ItemTaskBinding mBinding;
        AppCompatImageView circleImageView;
        ConstraintLayout constraintLayout;



        public TaskListHolder(View itemView) {
            super(itemView);
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
