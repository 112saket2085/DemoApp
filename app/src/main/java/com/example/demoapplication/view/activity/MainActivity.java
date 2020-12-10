package com.example.demoapplication.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.demoapplication.R;
import com.example.demoapplication.app.App;
import com.example.demoapplication.app.Constants;
import com.example.demoapplication.databinding.ActivityMainBinding;
import com.example.demoapplication.model.AppConfig;
import com.example.demoapplication.model.UserInfoModel;
import com.example.demoapplication.utility.DialogHelper;
import com.example.demoapplication.utility.LocaleUtils;
import com.example.demoapplication.view.fragment.LoginFragmentDirections;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,NavController.OnDestinationChangedListener, NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;
    public NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
        initProgressDialog();
        checkForLoggedIn();
    }

    private void initViews() {
        setSupportActionBar(binding.toolbar);
        navController = NavHostFragment.findNavController(Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.fragment_container_view)));
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.menu_encryption,R.id.menu_to_do,R.id.menu_stopwatch).setOpenableLayout(binding.drawerLayout).build();
        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView,navController);
        NavigationUI.setupWithNavController(binding.bottomNavView,navController);
        navController.addOnDestinationChangedListener(this);
        binding.navView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (isDrawerOpen()) {
            binding.drawerLayout.closeDrawers();
            return false;
        }
        return NavigationUI.navigateUp(navController,appBarConfiguration);
    }

    @Override
    public void onBackPressed() {
        if (isDrawerOpen()) {
            binding.drawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    public void setActionBarVisibility(boolean isToolbarNeeded) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (isToolbarNeeded) {
                actionBar.show();
            } else {
                actionBar.hide();
            }
        }
    }

    public void setActionBarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
    }

    /**
     * Set Default View Visibility based on Launch Mode
     * @param launchMode One of {Constants.PRE_DASHBOARD,Constants.POST_DASHBOARD,Constants.Default}
     */
    public void setLaunchMode(int launchMode) {
        if (launchMode == Constants.PRE_DASHBOARD) {
            setBottomNavVisibility(View.GONE);
            setDrawerMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else if (launchMode == Constants.POST_DASHBOARD) {
            setBottomNavVisibility(View.VISIBLE);
            setDrawerMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }  else if (launchMode == Constants.DEFAULT) {
            setBottomNavVisibility(View.GONE);
            setDrawerMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    private void setDrawerMode(int drawerMode) {
        binding.drawerLayout.setDrawerLockMode(drawerMode);
    }

    private void setBottomNavVisibility(int visibility) {
        binding.bottomNavView.setVisibility(visibility);
        binding.view.setVisibility(visibility);
    }

    public void setUserValues(UserInfoModel userInfoModel) {
        View view = binding.navView.getHeaderView(0);
        TextView textViewName = view.findViewById(R.id.text_view_name);
        TextView textViewEmail = view.findViewById(R.id.text_view_email);
        ImageView imageViewProfilePic = view.findViewById(R.id.image_view_profile_pic);
        textViewName.setText(getString(R.string.str_welcome,userInfoModel.getName()));
        textViewEmail.setText(userInfoModel.getEmail());
        Glide.with(this).load(userInfoModel.getPhotoUrl()).placeholder(R.drawable.ic_user).into(imageViewProfilePic);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        binding.drawerLayout.closeDrawers();
        if (item.getItemId() == R.id.menu_logout) {
            logout();
        }
        else if (item.getItemId() == R.id.menu_change_language) {
            navController.navigate(R.id.menu_change_language, null, getNavOptions());
        }
        return false;
    }

    private NavOptions getNavOptions() {
        return new NavOptions.Builder()
                .setEnterAnim(R.anim.anim_enter_from_right)
                .setExitAnim(R.anim.anim_exit_to_left)
                .setPopEnterAnim(R.anim.anim_enter_from_left)
                .setPopExitAnim(R.anim.anim_exit_to_right)
                .build();
    }


    private void logout() {
        DialogHelper.showGenericDialog(this, false, getString(R.string.str_logout), getString(R.string.str_logout_info), getString(R.string.button_yes), getString(R.string.button_no), new DialogHelper.DialogCallback() {
            @Override
            public void onPositiveButtonClick(View view) {
                AppConfig.getInstance().setLoggedIn(false);
                showProgressDialog(getString(R.string.str_logout));
                App.getInstance().getGoogleSignInClient().signOut().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        dismissProgressDialog();
                        FirebaseAuth.getInstance().signOut();
                        navController.popBackStack(R.id.menu_encryption,true);
                        navController.navigate(R.id.loginFragment, null, getNavOptions());
                    }
                });
            }

            @Override
            public void onNegativeButtonClick(View view) {
            }
        });
    }

    public boolean isDrawerOpen() {
        return binding.drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }

    private void showProgressDialog(String msg) {
        if(progressDialog!=null) {
            progressDialog.setMessage(msg);
            progressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if(progressDialog!=null) {
            progressDialog.dismiss();
        }
    }

    public void finishWithTransition() {
        finish();
        try {
          overridePendingTransition(R.anim.anim_enter_from_left,R.anim.anim_exit_to_right);
        }
        catch (Exception ignore) {}
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase= LocaleUtils.getInstance().setLocale(newBase, AppConfig.getInstance().getLanguage(),AppConfig.getInstance().getLocale(),true);
        super.attachBaseContext(newBase);
    }

    /**
     * Method to check if user is logged In
     */
    private void checkForLoggedIn() {
        if (AppConfig.getInstance().isLoggedIn()) {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            UserInfoModel userInfoModel = new UserInfoModel(firebaseUser.getDisplayName(), firebaseUser.getEmail(), firebaseUser.getPhotoUrl().toString());
            LoginFragmentDirections.ActionLoginToEncryptDecrypt action = LoginFragmentDirections.actionLoginToEncryptDecrypt();
            action.setUserInfoModel(userInfoModel);
            navController.navigate(action);
        }
    }
}