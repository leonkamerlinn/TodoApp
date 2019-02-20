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
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import com.kamerlin.leon.todoapp.R;
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
import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MediatorLiveData;
import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity implements NavigationFooterView.NavigationFooterListener, MenuAdapter.ItemListener, MainViewModel.EventListener, MjolnirRecyclerAdapter.OnClickListener<Task> {


    public static final String EXTRA_CATEGORY_NAME = "extra_category_name";
    public static final String EXTRA_TASK = "extra_task";
    private DrawerLayout mDrawerLayout;
    private ListView mNavigationListView;
    private ActionBarDrawerToggle mDrawerToggle;
    private TaskListFragment mTaskListFragment;
    private TextInputEditText mTextInputEditText;
    private MenuAdapter mMenuAdapter;
    private MaterialPalettePickerFragmentDialog mMaterialPalettePickerDialog;

    @Inject
    MainViewModel viewModel;
    @Inject
    TaskListAdapter taskListAdapter;





    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!setupActionBar()) return;
        initializeViews();
        setupMenuAdapter();
        setupNavigationListView();
        setupActionBarDrawerToggle();
        setupTaskListFragment(savedInstanceState);
        updateViews();

        MediatorLiveData<String> mediatorLiveData = new MediatorLiveData<>();
        mediatorLiveData.addSource(viewModel.getTitle(), s -> Toast.makeText(this, s, Toast.LENGTH_SHORT).show());

    }

    private void updateViews() {
        viewModel.getCategories().observe(this, mMenuAdapter::setCategories);
        viewModel.getTasks().observe(this, tasks -> {
            taskListAdapter.update(tasks, new TaskDiffUtilCallback(tasks, new ArrayList<>(taskListAdapter.getAll())));


        });
        viewModel.getTitle().observe(this, title -> {
            Objects.requireNonNull(getSupportActionBar()).setTitle(title);
            invalidateOptionsMenu();
        });
        viewModel.getTextInputEditTextError().observe(this, err -> {
            if(mTextInputEditText != null) {
                mTextInputEditText.setError(err);
            }
        });
    }

    private boolean setupActionBar() {
        if (getSupportActionBar() == null) return false;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        return true;
    }

    private void initializeViews() {
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mNavigationListView = findViewById(R.id.navigationListView);
    }

    @SuppressLint("CheckResult")
    private void setupMenuAdapter() {
        mMenuAdapter = new MenuAdapter(this, viewModel.getSelectedCategory().getValue());

    }

    private void setupNavigationListView() {
        View header = LayoutInflater.from(this).inflate(R.layout.navigation_header, null);
        View footer = new NavigationFooterView(this);

        mNavigationListView.addHeaderView(header);
        mNavigationListView.addFooterView(footer);
        mNavigationListView.setAdapter(mMenuAdapter);
    }

    private void setupActionBarDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
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
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    private void setupTaskListFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mTaskListFragment = (TaskListFragment) getInitialFragment();

        } else {
            mTaskListFragment = (TaskListFragment) getSupportFragmentManager().findFragmentByTag(TaskListFragment.TAG);
        }


        assert mTaskListFragment != null;
        mTaskListFragment.setAdapter(taskListAdapter);
    }



    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.delete_category) {
            showDeleteCategoryDialog();
        }

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
                .setTitle("Delete category: "+ viewModel.getSelectedCategory().getValue() + " ?")
                .setMessage("If you delete this category, then all task will be deleted as well")
                .setPositiveButton("Delete", (dialog1, which) -> {
                    CategoryService.delete(this, viewModel.getSelectedCategory().getValue());
                    viewModel.setSelectedCategory(MainViewModel.INITIAL_CATEGORY);
                })
                .setNegativeButton("Cancel", null)
                .create();


        dialog.show();
    }

    private Fragment getInitialFragment() {
        TaskListFragment fragment = TaskListFragment.newInstance(MainViewModel.INITIAL_CATEGORY);
        addFragment(fragment);
        return fragment;
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




    @SuppressLint("CheckResult")
    private void showMaterialColorPicker() {
        // show material color picker
        mMaterialPalettePickerDialog = new MaterialPalettePickerFragmentDialog.Builder()
                .showTextInputEditText(true)
                .setTitle("New category")
                .build();

        mMaterialPalettePickerDialog.getColorNameObservable().subscribe(viewModel::setColorName);
        mMaterialPalettePickerDialog.getTitleObservable().subscribe(viewModel::setNewCategory);
        mMaterialPalettePickerDialog.getTextInputEditTextObservable().subscribe(textInputEditText -> {
            mTextInputEditText = textInputEditText;
            mTextInputEditText.setHint("Enter category name");
        });
        mMaterialPalettePickerDialog.getPositiveButtonClickObservable().subscribe(aBoolean -> viewModel.onPositiveButtonClick());

        mMaterialPalettePickerDialog.show(getSupportFragmentManager(), MaterialPalettePickerFragmentDialog.TAG);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (viewModel.getSelectedCategory().getValue().equals("All"))return false;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }



    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
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
        Intent intent = new Intent(this, TaskActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(View itemView, int position, Category category) {
        viewModel.setTitle(category.getName());
        viewModel.setSelectedCategory(category.getName());
        mDrawerLayout.closeDrawers();

    }

    @Override
    public void onCategoryInserted(Category category) {
        mMaterialPalettePickerDialog.dismiss();
        getSupportFragmentManager().beginTransaction().remove(mMaterialPalettePickerDialog).commit();

    }


    @Override
    public void onClick(int index, Task task) {
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra(EXTRA_TASK, task);
        intent.putExtra(EXTRA_CATEGORY_NAME, viewModel.getSelectedCategory().getValue());
        startActivity(intent);
    }
}
