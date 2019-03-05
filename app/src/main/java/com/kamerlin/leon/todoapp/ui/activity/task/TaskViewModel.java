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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.work.WorkManager;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.ReplaySubject;

public class TaskViewModel extends ViewModel implements TaskContract.Model {

    private final TaskActivity mTaskActivity;
    private final TaskContract.View mView;
    private final TodoRoomDatabase mDatabase;
    private int mYear, mMonth, mDay, mHourOfDay = 12, mMinute = 0, mSeconds = 0;
    private Calendar mCalendar;
    private Task mTask;

    private ReplaySubject<Boolean> mOnCreateSubjectReplay;

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
        mOnCreateSubjectReplay = ReplaySubject.create();

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
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        setReminderValue(new Pair<>(getNoneString(taskActivity), -1L));
        setPriorityValue(new Pair<>(getNoneString(taskActivity), Task.PRIORITY_NONE));
        setDueDate(getNoneString(mTaskActivity));



        Observable<List<Category>> categoriesObservable = database.categoryDao().getCategoriesObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


        Observable.combineLatest(categoriesObservable, mOnCreateSubjectReplay, (categories, aBoolean) -> categories).subscribe(categories -> {
            List<Category> categoryList = Stream.of(categories)
                    .filter(value -> !value.getName().equals("All"))
                    .toList();
            Category[] categoriesArray = categoryList.toArray(new Category[categoryList.size()]);

            setCategories(categoriesArray);
            if (mTask == null) {
                if (categoriesArray.length > 0) {
                    String categoryName = categoriesArray[0].getName();
                    setCategoryByNameAsync(categoryName);
                }

            }
        });

    }

    private String getNoneString(TaskActivity taskActivity) {
        return taskActivity.getResources().getString(R.string.none);
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
    public void setCategoryByNameAsync(String category) {
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
        setDueDate(getNoneString(mTaskActivity));
        if (mTask != null) {
            mTask.setDueDate(Task.NOT_SET);
            mTask.setRemindMe(Task.NOT_SET);

        }
    }

    @Override
    public void insertOrUpdateTask() {
        if (mTaskName.getValue() == null || mTaskName.getValue().isEmpty()) {
            setTaskNameError("Task name is required");
            return;
        }

        if (!isSetCategory()) {
            Toast.makeText(mTaskActivity, "You must select category", Toast.LENGTH_LONG).show();
            return;
        }



        getCategory().observeForever(categoryName -> {
            String taskName = mTaskName.getValue();
            Task task = (mTask == null) ? new Task() : mTask;
            task.setName(taskName);
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

            if (task.hasWorkId()) {
                WorkManager.getInstance().cancelWorkById(UUID.fromString(task.getWorkerId()));
            }


            if (isSetDueDate()) {
                long dueDate = mCalendar.getTimeInMillis();
                task.setDueDate(dueDate);
                if (isSetReminder() && mReminderValue.getValue() != null) {
                    long reminder = mReminderValue.getValue().getSecondValue();
                    long remindMeAt = dueDate - reminder;
                    task.setRemindMe(remindMeAt);
                    task.scheduleReminder();

                } else {
                    task.setRemindMe(Task.NOT_SET);
                    task.cancelReminder();
                }
            } else {
                task.setRemindMe(Task.NOT_SET);
                task.cancelReminder();
            }



            if (task.hasId()) {
                TaskService.update(mTaskActivity, task);
            } else {
                TaskService.insert(mTaskActivity, task);
            }

            mView.onCloseActivity();
        });



    }

    private boolean isSetDueDate() {
        return !getNoneString(mTaskActivity).equals(getDueDate().getValue());
    }

    private boolean isSetReminder() {
        return !getNoneString(mTaskActivity).equals(getReminder().getValue());
    }

    private boolean isSetPriority() {
        return !getNoneString(mTaskActivity).equals(getPriority().getValue());
    }

    private boolean isSetCategory() {
        return !getNoneString(mTaskActivity).equals(getCategory().getValue());
    }

    @Override
    public void setTask(Task task) {
        mTask = task;
        setTaskName(task.getName());



        setCategoryByNameAsync(task.getCategoryName());


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

            onDateSet(year, month, day);
            onTimeSet(hour, minute, mSeconds);
        }

        if (task.isScheduled()) {
            setReminderValue(new Pair<>(getNoneString(mTaskActivity), -1L));
        }


        setIsCompleted(task.isCompleted());
        setIsFavorite(task.isFavorite());



        String remindMeText = ReminderFragmentDialog.getData().inverse().get(task.getRemindDiff());
        String priorityText = PriorityFragmentDialog.getData().inverse().get(task.getPriorityCode());
        setReminderValue(new Pair<>(remindMeText, task.getRemindDiff()));
        setPriorityValue(new Pair<>(priorityText, task.getPriorityCode()));

    }

    @Override
    public void onCreate() {
        mOnCreateSubjectReplay.onNext(true);
    }


    @Override
    public void onDateSet(int year, int monthOfYear, int dayOfMonth) {

        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, monthOfYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        setDueDate(format.format(mCalendar.getTime()));
        onTimeSet(mHourOfDay, mMinute, mSeconds);
    }

    @Override
    public int getHour() {
        return mHourOfDay;
    }

    @Override
    public int getMinute() {
        return mMinute;
    }

    @Override
    public int getSeconds() {
        return mSeconds;
    }

    @Override
    public int getYear() {
        return mYear;
    }

    @Override
    public int getMonth() {
        return mMonth;
    }

    @Override
    public int getDay() {
        return mDay;
    }

    @Override
    public void onTimeSet(int hourOfDay, int minute, int second) {
        mHourOfDay = hourOfDay;
        mMinute = minute;
        mSeconds = second;
        mCalendar.set(Calendar.HOUR_OF_DAY, mHourOfDay);
        mCalendar.set(Calendar.MINUTE, mMinute);
        mCalendar.set(Calendar.SECOND, mSeconds);


        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        String dueDateTime = format.format(mCalendar.getTime());
        setDueDateTime(dueDateTime);
    }


}
