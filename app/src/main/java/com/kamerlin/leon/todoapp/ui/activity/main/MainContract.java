package com.kamerlin.leon.todoapp.ui.activity.main;

import com.kamerlin.leon.todoapp.db.category.Category;

public interface MainContract {
    interface View {
        void showDialogErrorMessage(String message);
        void onCategoryInserted(Category category);
    }

    interface Model {
        void createNewCategory();
    }
}
