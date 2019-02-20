package com.kamerlin.leon.todoapp.ui.activity.main;

import android.annotation.SuppressLint;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.kamerlin.leon.todoapp.db.TodoRoomDatabase;
import com.kamerlin.leon.todoapp.db.category.Category;
import com.kamerlin.leon.todoapp.db.category.CategoryService;
import com.kamerlin.leon.todoapp.db.task.Task;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends ViewModel {
    public static final String INITIAL_CATEGORY = "All";
    private final MainActivity mMainActivity;
    private final TodoRoomDatabase mDatabase;
    private EventListener mEventListener;

    private MutableLiveData<String> mSelectedCategory, mTitle, mColorName, mNewCategory, mTextInputEditTextError;

    public MainViewModel(MainActivity mainActivity, TodoRoomDatabase todoRoomDatabase) {
        mMainActivity = mainActivity;
        mDatabase = todoRoomDatabase;
        if (mainActivity != null) {
            mEventListener = mainActivity;
        }
        mSelectedCategory = new MutableLiveData<>();
        mTitle = new MutableLiveData<>();
        mColorName = new MutableLiveData<>();
        mNewCategory = new MutableLiveData<>();
        mTextInputEditTextError = new MutableLiveData<>();
        setSelectedCategory(INITIAL_CATEGORY);

    }

    public void setSelectedCategory(String category) {
        mSelectedCategory.setValue(category);
    }

    public LiveData<String> getSelectedCategory() {
        return mSelectedCategory;
    }

    public LiveData<List<Task>> getTasks() {

        return Transformations.switchMap(mSelectedCategory, categoryName -> {
            if (INITIAL_CATEGORY.equals(categoryName)) {
                return mDatabase.taskDao().getTasksLiveData();
            }
            return mDatabase.taskDao().getCategoryTasksLiveData(categoryName);
        });

    }

    public LiveData<List<Category>> getCategories() {
        return mDatabase.categoryDao().getCategoriesLiveData();
    }







    public void setTitle(String title) {
        mTitle.setValue(title);
    }

    public LiveData<String> getTitle() {
        return Transformations.map(mSelectedCategory, input -> input);
    }

    public void setColorName(String colorName) {
        mColorName.setValue(colorName);
    }

    public void setNewCategory(String categoryName) {
        mNewCategory.setValue(categoryName);
    }

    public LiveData<String> getColorName() {
        return mColorName;
    }

    public LiveData<String> getNewCategoryName() {
        return mNewCategory;
    }

    public LiveData<String> getTextInputEditTextError() {
        return mTextInputEditTextError;
    }

    @SuppressLint("CheckResult")
    public void onPositiveButtonClick() {
        String colorName = getColorName().getValue();
        String newCategoryName = getNewCategoryName().getValue();
        if (newCategoryName != null) {
            if (newCategoryName.length() > 0) {
                mDatabase.categoryDao().getCategoriesNumberObservable(newCategoryName)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(count -> {
                            if (count == 0) {
                                Category category = new Category(newCategoryName, colorName);
                                CategoryService.insert(mMainActivity, category);
                                setNewCategory(null);
                                handleInsertEvent(category);
                            } else {
                                setTextInputEditTextError("This category already exist");
                            }
                        });

            } else {
                setTextInputEditTextError("Field is required");
            }
        } else {
            setTextInputEditTextError("Field is required");
        }
    }

    public void setTextInputEditTextError(String errorMessage) {
        mTextInputEditTextError.setValue(errorMessage);
    }

    private void handleInsertEvent(Category category) {
        if (mEventListener != null) {
            mEventListener.onCategoryInserted(category);
        }
    }

    public interface EventListener {
        void onCategoryInserted(Category category);
    }


}
