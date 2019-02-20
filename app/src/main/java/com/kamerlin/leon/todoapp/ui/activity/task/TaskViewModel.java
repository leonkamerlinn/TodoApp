package com.kamerlin.leon.todoapp.ui.activity.task;

import android.annotation.SuppressLint;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.kamerlin.leon.todoapp.R;
import com.kamerlin.leon.todoapp.db.TodoRoomDatabase;
import com.kamerlin.leon.todoapp.db.category.Category;
import com.kamerlin.leon.todoapp.db.task.Task;
import com.kamerlin.leon.todoapp.db.task.TaskService;
import com.kamerlin.leon.todoapp.ui.dialog.PriorityFragmentDialog;
import com.kamerlin.leon.todoapp.ui.dialog.ReminderFragmentDialog;
import com.kamerlin.leon.utils.data_structures.Pair;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TaskViewModel extends ViewModel implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, TaskContract.Model {

    private final TaskActivity mTaskActivity;
    private final TaskContract.View mView;
    private final TodoRoomDatabase mDatabase;
    private int mHourOfDay = 12, mMinute = 0, mSeconds = 0;
    private Calendar mCalendar;
    private Task mTask;

    private MutableLiveData<Boolean> mIsCompleted, mIsFavorite;
    private MutableLiveData<String> mDueDate, mDueDateTime, mTaskName, mTaskDescription, mTaskNameError;
    private MutableLiveData<Pair<String, Long>> mReminderValue;
    private MutableLiveData<Pair<String, Integer>> mPriorityValue;
    private MutableLiveData<Category[]> mCategories;
    private MutableLiveData<Category> mCategory;

    @SuppressLint("CheckResult")
    public TaskViewModel(TaskActivity taskActivity, TaskContract.View view, TodoRoomDatabase database) {
        mTaskActivity = taskActivity;
        mView = view;
        mDatabase = database;

        mIsCompleted = new MutableLiveData<>();
        mIsFavorite = new MutableLiveData<>();
        mCategory = new MutableLiveData<>();
        mPriorityValue = new MutableLiveData<>();
        mTaskName = new MutableLiveData<>();
        mTaskNameError = new MutableLiveData<>();
        mTaskDescription = new MutableLiveData<>();
        mDueDate = new MutableLiveData<>();
        mDueDateTime = new MutableLiveData<>();
        mReminderValue = new MutableLiveData<>();
        mPriorityValue = new MutableLiveData<>();
        mCategories = new MutableLiveData<>();


        mCalendar = Calendar.getInstance();
        setReminderValue(new Pair<>(taskActivity.getResources().getString(R.string.none), -1L));
        setPriorityValue(new Pair<>(taskActivity.getResources().getString(R.string.none), Task.PRIORITY_NONE));
        setDueDate(mTaskActivity.getResources().getString(R.string.none));



        Observable<List<Category>> categoriesObservable = database.categoryDao().getAlphabetizedCategoriesObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


        categoriesObservable.subscribe(categories -> {
            List<Category> categories1 = Stream.of(categories)
                    .filter(value -> !value.getName().equals("All"))
                    .toList();
            Category[] categories2 = categories1.toArray(new Category[categories1.size()]);

            setCategories(categories2);
            if (mTask == null) {
                setCategoryByName(categories2[0].getName());
            }

        });

    }

    /* Task Name */


    public LiveData<String> getTaskNameError() {
        return mTaskNameError;
    }

    public void setTaskNameError(String taskNameError) {
        mTaskNameError.setValue(taskNameError);
    }

    public LiveData<String> getTaskName() {
        return mTaskName;
    }

    public void setTaskName(String taskName) {
        mTaskName.setValue(taskName);
    }


    /* Task Description */


    public LiveData<String> getTaskDescription() {
        return mTaskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        mTaskDescription.setValue(taskDescription);
    }


    /* Due Date */


    public LiveData<String> getDueDate() {
        return mDueDate;
    }

    public void setDueDate(String dueDate) {
        mDueDate.setValue(dueDate);
    }


    /* Due Date Time */


    public LiveData<String> getDueDateTime() {
        return mDueDateTime;
    }

    public void setDueDateTime(String dueDateTime) {
        mDueDateTime.setValue(dueDateTime);
    }

    public LiveData<Boolean> showDueDateTime() {
        return Transformations.map(mDueDate, input -> isSetDueDate());
    }


    /* Reminder */


    public LiveData<String> getReminder() {
        return Transformations.map(mReminderValue, Pair::getFirstValue);
    }

    public void setReminderValue(Pair<String, Long> value) {
        mReminderValue.setValue(value);
    }

    public LiveData<Boolean> showReminder() {
        return Transformations.map(mDueDate, input -> isSetDueDate());
    }


    /* Priority */


    public LiveData<String> getPriority() {
        return Transformations.map(mPriorityValue, Pair::getFirstValue);
    }

    public void setPriorityValue(Pair<String, Integer> value) {
        mPriorityValue.setValue(value);
    }


    /* Category */


    public LiveData<String> getCategory() {
        return Transformations.map(mCategory, Category::getName);
    }

    public void setCategory(Category category) {
        mCategory.setValue(category);
    }


    @SuppressLint("CheckResult")
    public void setCategoryByName(String category) {
        mDatabase.categoryDao()
                .getCategoryByNameSingle(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setCategory);
    }


    /* Material Color */


    public LiveData<String> getMaterialColor() {
        return Transformations.map(mCategory, Category::getColorName);
    }


    /* Favorite */


    public LiveData<Boolean> getIsFavorite() {
        return mIsFavorite;
    }

    public void setIsFavorite(boolean checked) {
        mIsFavorite.setValue(checked);
    }


    /* Completed */


    public LiveData<Boolean> getIsCompleted() {
        return mIsCompleted;
    }

    public void setIsCompleted(boolean checked) {
        mIsCompleted.setValue(checked);
    }


    /* Other */


    public LiveData<Boolean> showUnsetButton() {
        return Transformations.map(mDueDate, input -> isSetDueDate());
    }



    /* Categories  */


    public void setCategories(Category[] categories) {
        mCategories.setValue(categories);
    }


    public LiveData<Category[]> getCategories() {
        return mCategories;
    }


    public void unset() {
        setDueDate(mTaskActivity.getResources().getString(R.string.none));
    }

    public void insertOrUpdateTask() {
        if (mTaskName.getValue() == null || mTaskName.getValue().isEmpty()) {
            setTaskNameError("Task name is required");
            return;
        }

        if (!isSetCategory()) {
            Toast.makeText(mTaskActivity, "You must select category", Toast.LENGTH_LONG).show();
            return;
        }

        String taskName = mTaskName.getValue();
        String categoryName = getCategory().getValue();

        Task task = (mTask == null) ? new Task(taskName) : mTask;
        task.setCategoryName(categoryName);


        if (mPriorityValue.getValue() != null) {
            int priority = mPriorityValue.getValue().getSecondValue();
            task.setPriorityCode(priority);
        }

        if (mIsCompleted.getValue() != null) {
            boolean isCompleted = mIsCompleted.getValue();
            task.setIsCompleted(isCompleted);

        }

        if (mIsFavorite.getValue() != null) {
            boolean isFavorite = mIsFavorite.getValue();
            task.setIsFavorite(isFavorite);
        }

        if (mTaskDescription.getValue() != null) {
            String taskDescription = mTaskDescription.getValue();
            task.setDescription(taskDescription);
        }

        if (isSetDueDate()) {
            long dueDate = mCalendar.getTimeInMillis();
            task.setDueDate(dueDate);
            if (mReminderValue.getValue() != null) {
                long reminder = mReminderValue.getValue().getSecondValue();
                task.setRemindMe(dueDate - reminder);
            }
        }


        if (task.hasId()) {
            TaskService.update(mTaskActivity, task);
        } else {
            TaskService.insert(mTaskActivity, task);
        }

        mView.onCloseActivity();
    }

    private boolean isSetDueDate() {
        return !mTaskActivity.getResources().getString(R.string.none).equals(getDueDate().getValue());
    }

    private boolean isSetReminder() {
        return !mTaskActivity.getResources().getString(R.string.none).equals(getReminder().getValue());
    }

    private boolean isSetPriority() {
        return !mTaskActivity.getResources().getString(R.string.none).equals(getPriority().getValue());
    }

    private boolean isSetCategory() {
        return !mTaskActivity.getResources().getString(R.string.none).equals(getCategory().getValue());
    }


    public void setTask(Task task) {
        mTask = task;
        setTaskName(task.getName());


        setCategoryByName(task.getCategoryName());


        if (task.hasDescription()) {
            setTaskDescription(task.getDescription());
        }


        if (task.hasDueDate()) {
            long dueDate = task.getDueDate();
            mCalendar.setTimeInMillis(dueDate);

            int year = mCalendar.get(Calendar.YEAR);
            int month = mCalendar.get(Calendar.MONTH);
            int day = mCalendar.get(Calendar.DAY_OF_MONTH);
            int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
            int minute = mCalendar.get(Calendar.MINUTE);

            onDateSet(null, year, month, day);
            onTimeSet(null, hour, minute, mSeconds);
        }


        setIsCompleted(task.isCompleted());
        setIsFavorite(task.isFavorite());



        String remindMeText = ReminderFragmentDialog.getData().inverse().get(task.getRemindMe());
        String priorityText = PriorityFragmentDialog.getData().inverse().get(task.getPriorityCode());
        setReminderValue(new Pair<>(remindMeText, task.getRemindMe()));
        setPriorityValue(new Pair<>(priorityText, task.getPriorityCode()));

    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if (view != null) {
            mView.onShowTimePickerDialog();
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy/mm/dd", Locale.getDefault());
        setDueDate(format.format(mCalendar.getTime()));
        onTimeSet(null, mHourOfDay, mMinute, mSeconds);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

        mHourOfDay = hourOfDay;
        mMinute = minute;
        mSeconds = second;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        mCalendar.set(Calendar.HOUR_OF_DAY, mHourOfDay);
        mCalendar.set(Calendar.MINUTE, mMinute);
        mCalendar.set(Calendar.SECOND, mSeconds);

        String dueDateTime = format.format(mCalendar.getTime());
        setDueDateTime(dueDateTime);
    }


}