package com.kamerlin.leon.todoapp.db.category;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.kamerlin.leon.todoapp.db.TodoRoomDatabase;

import java.util.PrimitiveIterator;

import javax.inject.Inject;

import dagger.android.DaggerIntentService;
import io.reactivex.schedulers.Schedulers;

public class CategoryService extends DaggerIntentService {

    private static final String TAG = CategoryService.class.getSimpleName();
    //Intent actions
    private static final String ACTION_INSERT = TAG + ".INSERT";
    private static final String ACTION_UPDATE = TAG + ".UPDATE";
    private static final String ACTION_DELETE = TAG + ".DELETE";
    private static final String ACTION_DELETE_AND_POPULATE = TAG + ".DELETE_AND_POPULATE";

    // Intent extras
    private static final String EXTRA_CATEGORY = "extra_category";
    private static final String EXTRA_CATEGORY_NAME = "extra_category_name";

    @Inject
    CategoryDao categoryDao;

    public CategoryService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) return;

        String action = intent.getAction();
        if (ACTION_INSERT.equals(action)) {
            handleInsert(intent);
        } else if (ACTION_DELETE.equals(action)) {
            handleDelete(intent);
        } else if (ACTION_UPDATE.equals(action)) {
            handleUpdate(intent);
        } else if (ACTION_DELETE_AND_POPULATE.equals(action)) {
            handleDeleteAndPopulate();
        }
    }



    private void handleInsert(Intent intent) {
        Category category = intent.getParcelableExtra(EXTRA_CATEGORY);
        categoryDao.insert(category);
    }

    private void handleDelete(Intent intent) {
        if (intent.hasExtra(EXTRA_CATEGORY)) {
            Category category = intent.getParcelableExtra(EXTRA_CATEGORY);
            categoryDao.delete(category);
        } else if (intent.hasExtra(EXTRA_CATEGORY_NAME)) {
            String categoryName = intent.getStringExtra(EXTRA_CATEGORY_NAME);
            categoryDao.delete(categoryName);
        }

    }

    private void handleUpdate(Intent intent) {
        Category category = intent.getParcelableExtra(EXTRA_CATEGORY);
        categoryDao.insert(category);
    }

    private void handleDeleteAndPopulate() {
        categoryDao.deleteAndPopulate();
    }

    public static void insert(Context context, Category category) {
        Intent intent = new Intent(context, CategoryService.class);
        intent.setAction(ACTION_INSERT);
        intent.putExtra(EXTRA_CATEGORY, category);
        context.startService(intent);
    }

    public static void delete(Context context, Category category) {
        Intent intent = new Intent(context, CategoryService.class);
        intent.setAction(ACTION_DELETE);
        intent.putExtra(EXTRA_CATEGORY, category);
        context.startService(intent);
    }

    public static void delete(Context context, String categoryName) {
        Intent intent = new Intent(context, CategoryService.class);
        intent.setAction(ACTION_DELETE);
        intent.putExtra(EXTRA_CATEGORY_NAME, categoryName);
        context.startService(intent);
    }

    public static void update(Context context, Category category) {
        Intent intent = new Intent(context, CategoryService.class);
        intent.setAction(ACTION_UPDATE);
        intent.putExtra(EXTRA_CATEGORY, category);
        context.startService(intent);
    }

    public static void deleteAndPopulate(Context context) {
        Intent intent = new Intent(context, CategoryService.class);
        intent.setAction(ACTION_DELETE_AND_POPULATE);
        context.startService(intent);
    }


}
