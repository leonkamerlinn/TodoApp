package com.kamerlin.leon.todoapp.ui.activity.main;

import android.annotation.SuppressLint;

import androidx.arch.core.util.Function;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.kamerlin.leon.todoapp.db.TodoRoomDatabase;
import com.kamerlin.leon.todoapp.db.category.Category;
import com.kamerlin.leon.todoapp.db.category.CategoryService;
import com.kamerlin.leon.todoapp.db.task.Task;
import com.kamerlin.leon.todoapp.state_pattern.SortAlphabetical;
import com.kamerlin.leon.todoapp.state_pattern.SortAlphabeticalInverse;
import com.kamerlin.leon.todoapp.state_pattern.SortCreated;
import com.kamerlin.leon.todoapp.state_pattern.SortCreatedInverse;
import com.kamerlin.leon.todoapp.state_pattern.SortDueDate;
import com.kamerlin.leon.todoapp.state_pattern.SortDueDateInverse;
import com.kamerlin.leon.todoapp.state_pattern.SortFavorite;
import com.kamerlin.leon.todoapp.state_pattern.SortNone;
import com.kamerlin.leon.todoapp.state_pattern.SortPriority;
import com.kamerlin.leon.todoapp.state_pattern.SortState;


import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.ReplaySubject;

public class MainViewModel extends ViewModel implements MainContract.Model {



    public static final String INITIAL_CATEGORY = "All";
    private final MainActivity mMainActivity;
    private final TodoRoomDatabase mDatabase;
    private MutableLiveData<String> mTitle, mColorName, mNewCategory;

    public MutableLiveData<Pair<String, Task.Sort>> mPairMutableLiveData;
    private MainContract.View mView;



    @SuppressLint("CheckResult")
    public MainViewModel(MainActivity mainActivity, TodoRoomDatabase todoRoomDatabase) {
        mMainActivity = mainActivity;
        mDatabase = todoRoomDatabase;
        mView = mainActivity;





        mTitle = new MutableLiveData<>();
        mColorName = new MutableLiveData<>();
        mNewCategory = new MutableLiveData<>();
        mPairMutableLiveData = new MutableLiveData<>();



        mPairMutableLiveData.setValue(new Pair<>(INITIAL_CATEGORY, Task.Sort.NONE));

        setTitle(INITIAL_CATEGORY);

    }



    public LiveData<Boolean> getEnableDragItem() {
        return Transformations.map(mPairMutableLiveData, input -> {
            if (input.second.equals(Task.Sort.NONE)) return true;
            return false;
        });
    }




    public LiveData<List<Task>> getTasks() {
        return Transformations.switchMap(mPairMutableLiveData, input -> {
            if (input != null && input.second != null && input.first != null) {
                return getSortState(input.second, input.first).getTasksLiveData();
            }

            return mDatabase.taskDao().getAlphabeticalTasksLiveData();


        });
    }

    public LiveData<List<Category>> getCategories() {
        return mDatabase.categoryDao().getCategoriesLiveData();
    }





    private SortState getSortState(Task.Sort sort, String selectedCategory) {
        if (sort.equals(Task.Sort.NONE)) {
            return new SortNone(mDatabase, selectedCategory);
        } else if (sort.equals(Task.Sort.PRIORITY)) {
            return new SortPriority(mDatabase, selectedCategory);
        } else if (sort.equals(Task.Sort.FAVORITE)) {
            return new SortFavorite(mDatabase, selectedCategory);
        } else if (sort.equals(Task.Sort.ALPHABETICAL_A_Z)) {
            return new SortAlphabetical(mDatabase, selectedCategory);
        } else if (sort.equals(Task.Sort.ALPHABETICAL_Z_A)) {
            return new SortAlphabeticalInverse(mDatabase, selectedCategory);
        } else if (sort.equals(Task.Sort.CREATED_NEWEST_FIRST)) {
            return new SortCreated(mDatabase, selectedCategory);
        } else if (sort.equals(Task.Sort.CREATED_OLDEST_FIRST)) {
            return new SortCreatedInverse(mDatabase, selectedCategory);
        } else if (sort.equals(Task.Sort.DUE_DATE)) {
            return new SortDueDate(mDatabase, selectedCategory);
        } else if (sort.equals(Task.Sort.DUE_DATE_INVERSE)) {
            return new SortDueDateInverse(mDatabase, selectedCategory);
        }

        return new SortNone(mDatabase, selectedCategory);
    }



    public void setTitle(String title) {
        mTitle.setValue(title);
    }

    public LiveData<String> getTitle() {
        return mTitle;
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







    public String getSelectedCategory() {
        return mPairMutableLiveData.getValue().first;
    }

    public Task.Sort getSort() {
        return mPairMutableLiveData.getValue().second;
    }

    public void setSortBy(Task.Sort sort) {
        String category = getSelectedCategory();
        mPairMutableLiveData.setValue(new Pair<>(category, sort));
    }

    public void setSelectedCategory(String category) {
        Task.Sort sort = getSort();
        mPairMutableLiveData.setValue(new Pair<>(category, sort));
        setTitle(category);
    }

    @SuppressLint("CheckResult")
    @Override
    public void createNewCategory() {
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
                                mView.onCategoryInserted(category);

                            } else {
                                mView.showDialogErrorMessage("This category already exist");
                            }
                        }, System.err::println);

            } else {
                mView.showDialogErrorMessage("Field is required");
            }
        } else {
            mView.showDialogErrorMessage("Field is required");
        }
    }




}
