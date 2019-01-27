package com.kamerlin.leon.todoapp.ui.activity.main;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.kamerlin.leon.todoapp.MenuAdapter;
import com.kamerlin.leon.todoapp.R;
import com.kamerlin.leon.todoapp.db.DatabaseRepository;
import com.kamerlin.leon.todoapp.db.pojo.Category;
import com.kamerlin.leon.todoapp.ui.fragment.ContentFragment;
import com.kamerlin.leon.todoapp.ui.views.NavigationFooterView;
import com.kamerlin.leon.utils.common.MaterialPalettePickerDialog;

import javax.inject.Inject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends DaggerAppCompatActivity implements NavigationFooterView.NavigationFooterListener, MenuAdapter.ItemListener {
    private String mCategorySelected = "All";
    private DrawerLayout mDrawerLayout;
    private FrameLayout mContentFrame;
    private ListView mNavigationListView;
    private ActionBarDrawerToggle mDrawerToggle;
    private ContentFragment mContentFragment;
    private String mColorName;
    private String mCategory;
    private TextInputEditText mTextInputEditText;
    private MenuAdapter mMenuAdapter;

    @Inject
    DatabaseRepository mDatabaseRepository;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (setupActionBar()) return;
        initializeViews();
        setupMenuAdapter();
        setupNavigationListView();
        prepareActionBarDrawerToggle();
        setupContentFragment(savedInstanceState);

    }

    @SuppressLint("CheckResult")
    private void setupMenuAdapter() {
        mMenuAdapter = new MenuAdapter(this, mCategorySelected);
        mDatabaseRepository.getCategoriesObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mMenuAdapter::setCategories);
    }

    private void setupContentFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mContentFragment = (ContentFragment) getInitialFragment();
        } else {
            mContentFragment = (ContentFragment) getSupportFragmentManager().findFragmentByTag(ContentFragment.TAG);
        }
    }

    private void setupNavigationListView() {
        View header = LayoutInflater.from(this).inflate(R.layout.navigation_header, null);
        View footer = new NavigationFooterView(this);

        mNavigationListView.addHeaderView(header);
        mNavigationListView.addFooterView(footer);
        mNavigationListView.setAdapter(mMenuAdapter);
    }

    private void handleOnNavigationItemClick(String category) {
        mCategorySelected = category;
        replaceFragment(mCategorySelected);
        mDrawerLayout.closeDrawers();
    }

    private void initializeViews() {
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mContentFrame = findViewById(R.id.contentFrame);
        mNavigationListView = findViewById(R.id.navigationListView);
    }

    private boolean setupActionBar() {
        if (getSupportActionBar() == null) return true;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        return false;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private Fragment getInitialFragment() {
        Fragment fragment = ContentFragment.newInstance("All");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.contentFrame, fragment, ContentFragment.TAG)
                .commit();

        return fragment;
    }

    private void replaceFragment(String category) {
        Fragment fragment = ContentFragment.newInstance(category);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentFrame, fragment, ContentFragment.TAG)
                .commit();
    }

    private void prepareActionBarDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.app_name);
                invalidateOptionsMenu();

            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mCategorySelected);
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }


    @SuppressLint("CheckResult")
    private void showMaterialColorPicker() {
        // show material color picker
        MaterialPalettePickerDialog dialog = new MaterialPalettePickerDialog.Builder()
                .showTextInputEditText(true)
                .setTitle("New category")
                .build();

        dialog.getColorNameObservable().subscribe(colorName -> mColorName = colorName);
        dialog.getTitleObservable()
                .subscribe(category -> mCategory = category);
        dialog.getTextInputEditTextObservable().subscribe(textInputEditText -> {
            mTextInputEditText = textInputEditText;
            mTextInputEditText.setHint("Enter category name");
        });
        dialog.getPositiveButtonClickObservable().subscribe(aBoolean -> {
            if (mCategory != null) {

                if (mCategory.length() == 0) {
                    mTextInputEditText.setError("Field is required");
                    return;
                }
                Category category = new Category();
                category.setColorName(mColorName);
                category.setName(mCategory);


                mDatabaseRepository.getNumberOfCategoriesObservable(mCategory)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(count -> {
                            if(count == 0) {
                                mDatabaseRepository.insertCategory(category);
                                dialog.dismiss();
                            } else {
                                mTextInputEditText.setError("This category already exist");

                            }
                        });


                mCategory = null;
            } else {
                mTextInputEditText.setError("Field is required");
            }
        });

        dialog.show(getSupportFragmentManager(), MaterialPalettePickerDialog.TAG);

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

    }

    @Override
    public void onItemClick(View itemView, int position, Category category) {
        handleOnNavigationItemClick(category.getName());
    }
}
