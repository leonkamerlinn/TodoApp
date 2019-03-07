package com.kamerlin.leon.todoapp.ui.activity.main;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.kamerlin.leon.todoapp.R;
import com.kamerlin.leon.todoapp.databinding.ActivityMainBinding;
import com.kamerlin.leon.todoapp.db.TodoRoomDatabase;
import com.kamerlin.leon.todoapp.db.category.Category;
import com.kamerlin.leon.todoapp.db.category.CategoryService;
import com.kamerlin.leon.todoapp.db.task.Task;
import com.kamerlin.leon.todoapp.ui.activity.task.TaskActivity;
import com.kamerlin.leon.todoapp.ui.adapter.MenuAdapter;
import com.kamerlin.leon.todoapp.ui.adapter.TaskListAdapter;
import com.kamerlin.leon.todoapp.ui.fragment.TaskListFragment;
import com.kamerlin.leon.todoapp.ui.views.NavigationFooterView;
import com.kamerlin.leon.todoapp.utils.TaskDiffUtilCallback;
import com.kamerlin.leon.utils.dialog.MaterialPalettePickerFragmentDialog;
import com.kamerlin.leon.utils.mjolnir.MjolnirRecyclerAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends DaggerAppCompatActivity implements NavigationFooterView.NavigationFooterListener, MenuAdapter.ItemListener, MjolnirRecyclerAdapter.OnClickListener<Task>, MainContract.View {



    public static final String EXTRA_TASK = "extra_task";
    private ActionBarDrawerToggle mDrawerToggle;
    private TaskListFragment mTaskListFragment;
    private TextInputEditText mTextInputEditText;
    private MenuAdapter mMenuAdapter;
    private MaterialPalettePickerFragmentDialog mMaterialPalettePickerDialog;
    private TaskListAdapter mTaskListAdapter;

    @Inject
    MainViewModel viewModel;

    @Inject
    ActivityMainBinding binding;

    @Inject
    TodoRoomDatabase database;

    @Inject
    MainContract.Model model;



    @SuppressLint({"CheckResult", "RestrictedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        //deleteDatabase(TodoRoomDatabase.DATABASE_NAME);

        if (!setupActionBar()) return;
        mTaskListAdapter = TaskListAdapter.getInstance(this, database.categoryDao());
        mTaskListAdapter.setOnClickListener(this);
        mMaterialPalettePickerDialog = getMaterialPalettePickerDialog();
        setupMenuAdapter();
        setupNavigationListView();
        setupActionBarDrawerToggle();
        setupTaskListFragment(savedInstanceState);
        updateViews();



        binding.fab.setOnClickListener(v -> {
            startTaskActivity();
        });

        mTaskListFragment.setViewModel(viewModel);

    }


    private void updateViews() {
        viewModel.getCategories().observe(this, mMenuAdapter::setCategories);
        viewModel.getTasks().observe(this, tasks -> {
            //Set<Task> taskSet = new HashSet<>(tasks);
            mTaskListAdapter.update(tasks, new TaskDiffUtilCallback(tasks, new ArrayList<>(mTaskListAdapter.getAll())));


        });
        viewModel.getTitle().observe(this, title -> {
            Objects.requireNonNull(getSupportActionBar()).setTitle(title);
            invalidateOptionsMenu();
        });

    }

    private boolean setupActionBar() {
        if (getSupportActionBar() == null) return false;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        return true;
    }



    @SuppressLint("CheckResult")
    private void setupMenuAdapter() {
        mMenuAdapter = new MenuAdapter(this, viewModel.getSelectedCategory());

    }

    private void setupNavigationListView() {
        View header = LayoutInflater.from(this).inflate(R.layout.navigation_header, null);
        View footer = new NavigationFooterView(this);

        binding.navigationListView.addHeaderView(header);
        binding.navigationListView.addFooterView(footer);
        binding.navigationListView.setAdapter(mMenuAdapter);
    }

    private void setupActionBarDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();

            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        binding.drawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    private void setupTaskListFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mTaskListFragment = TaskListFragment.newInstance(MainViewModel.INITIAL_CATEGORY);
            addFragment(mTaskListFragment);

        } else {
            mTaskListFragment = (TaskListFragment) getSupportFragmentManager().findFragmentByTag(TaskListFragment.TAG);
        }



        mTaskListFragment.setAdapter(mTaskListAdapter);
    }



    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        long id = item.getItemId();
        if (id == R.id.delete_category) {
            showDeleteCategoryDialog();
        } else {
            if(id == R.id.menu_none) {
                viewModel.setSortBy(Task.Sort.NONE);
            } else if (id == R.id.menu_priority) {
                viewModel.setSortBy(Task.Sort.PRIORITY);
            } else if (id == R.id.menu_favorite) {
                viewModel.setSortBy(Task.Sort.FAVORITE);
            } else if(id == R.id.menu_alphabetical_a_z) {
                viewModel.setSortBy(Task.Sort.ALPHABETICAL_A_Z);
            } else if(id == R.id.menu_alphabetical_z_a) {
                viewModel.setSortBy(Task.Sort.ALPHABETICAL_Z_A);
            } else if(id == R.id.menu_created_newest_first) {
                viewModel.setSortBy(Task.Sort.CREATED_NEWEST_FIRST);
            } else if(id == R.id.menu_created_oldest_first) {
                viewModel.setSortBy(Task.Sort.CREATED_OLDEST_FIRST);
            } else if(id == R.id.menu_due_date_newest_first) {
                viewModel.setSortBy(Task.Sort.DUE_DATE);
            } else if(id == R.id.menu_due_date_oldest_first) {
                viewModel.setSortBy(Task.Sort.DUE_DATE_INVERSE);
            }

        }

        invalidateOptionsMenu();

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    @SuppressLint("CheckResult")
    private void showDeleteCategoryDialog() {
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("Delete category: "+ viewModel.getSelectedCategory() + " ?")
                .setMessage("If you delete this category, then all task will be deleted as well")
                .setPositiveButton("Delete", (dialog1, which) -> {
                    String categoryName = viewModel.getSelectedCategory();
                    CategoryService.delete(this, categoryName);
                    viewModel.setSelectedCategory(MainViewModel.INITIAL_CATEGORY);
                })
                .setNegativeButton("Cancel", null)
                .create();


        dialog.show();
    }



    private void addFragment(Fragment fragment) {
            getSupportFragmentManager().beginTransaction()
                .add(R.id.contentFrame, fragment, TaskListFragment.TAG)
                .commitAllowingStateLoss();
    }

    private void replaceFragment(String category) {
        Fragment fragment = TaskListFragment.newInstance(category);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentFrame, fragment, TaskListFragment.TAG)
                .commit();
    }


    private MaterialPalettePickerFragmentDialog getMaterialPalettePickerDialog() {
        Fragment dialog = getSupportFragmentManager().findFragmentByTag(MaterialPalettePickerFragmentDialog.TAG);
        if (dialog != null) return (MaterialPalettePickerFragmentDialog) dialog;

        return new MaterialPalettePickerFragmentDialog.Builder()
                .showTextInputEditText(true)
                .setTitle("New category")
                .build();
    }

    @SuppressLint("CheckResult")
    private void showMaterialColorPicker() {
        // show material color picker

        mMaterialPalettePickerDialog.getColorNameObservable().subscribe(viewModel::setColorName);
        mMaterialPalettePickerDialog.getTitleObservable().subscribe(viewModel::setNewCategory);
        mMaterialPalettePickerDialog.getTextInputEditTextObservable().subscribe(textInputEditText -> {
            mTextInputEditText = textInputEditText;
            mTextInputEditText.setHint("Enter category name");
        });
        mMaterialPalettePickerDialog.getPositiveButtonClickObservable().subscribe(aBoolean -> model.createNewCategory());
        mMaterialPalettePickerDialog.getNegativeButtonClickObservable().subscribe(aBoolean -> mMaterialPalettePickerDialog.dismiss());

        mMaterialPalettePickerDialog.show(getSupportFragmentManager(), MaterialPalettePickerFragmentDialog.TAG);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        MenuItem item = menu.findItem(R.id.delete_category);
        if (viewModel.getSelectedCategory() != null && viewModel.getSelectedCategory().equals("All")) {
            item.setVisible(false);
        } else {
            item.setVisible(true);
        }

        Task.Sort sort = viewModel.getSort();
        if (sort == null)return true;
        if (sort.equals(Task.Sort.NONE)) {
            menu.findItem(R.id.menu_none).setChecked(true);
        } else if (sort.equals(Task.Sort.PRIORITY)) {
            menu.findItem(R.id.menu_priority).setChecked(true);
        } else if (sort.equals(Task.Sort.FAVORITE)) {
            menu.findItem(R.id.menu_favorite).setChecked(true);
        } else if (sort.equals(Task.Sort.ALPHABETICAL_A_Z)) {
            menu.findItem(R.id.menu_alphabetical_a_z).setChecked(true);
        } else if (sort.equals(Task.Sort.ALPHABETICAL_Z_A)) {
            menu.findItem(R.id.menu_alphabetical_z_a).setChecked(true);
        } else if (sort.equals(Task.Sort.CREATED_NEWEST_FIRST)) {
            menu.findItem(R.id.menu_created_newest_first).setChecked(true);
        } else if (sort.equals(Task.Sort.CREATED_OLDEST_FIRST)) {
            menu.findItem(R.id.menu_created_oldest_first).setChecked(true);
        } else if (sort.equals(Task.Sort.DUE_DATE)) {
            menu.findItem(R.id.menu_due_date_newest_first).setChecked(true);
        } else if (sort.equals(Task.Sort.DUE_DATE_INVERSE)) {
            menu.findItem(R.id.menu_due_date_oldest_first).setChecked(true);
        }



        return true;
    }




    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawers();
            Toast.makeText(this, "Click back one more time to exit", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public void onNewCategoryClick(View v) {
        showMaterialColorPicker();
    }


    @Override
    public void onSettingsClick(View v) {

    }

    private void startTaskActivity() {
        Intent intent = new Intent(this, TaskActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(View itemView, int position, Category category) {
        viewModel.setTitle(category.getName());
        viewModel.setSelectedCategory(category.getName());
        binding.drawerLayout.closeDrawers();

    }

    @SuppressLint("CheckResult")
    @Override
    public void onCategoryInserted(Category category) {
        mMaterialPalettePickerDialog.dismiss();
    }


    @Override
    public void onClick(int index, Task task) {
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra(EXTRA_TASK, task);
        startActivity(intent);
    }

    @Override
    public void showDialogErrorMessage(String message) {
        if(mTextInputEditText != null) {
            mTextInputEditText.setError(message);
        }
    }
}
