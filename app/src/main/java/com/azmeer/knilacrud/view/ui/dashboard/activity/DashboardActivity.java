package com.azmeer.knilacrud.view.ui.dashboard.activity;

import static com.azmeer.knilacrud.utils.AppConstant.PERMISSIONS_STORAGE;
import static com.azmeer.knilacrud.utils.AppConstant.REQUEST_EXTERNAL_STORAGE;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.azmeer.knilacrud.R;
import com.azmeer.knilacrud.database.UserDbController;
import com.azmeer.knilacrud.databinding.ActivityDashboardBinding;
import com.azmeer.knilacrud.databinding.DialogUserBinding;
import com.azmeer.knilacrud.models.UserModel;
import com.azmeer.knilacrud.utils.AppConstant;
import com.azmeer.knilacrud.utils.Util;
import com.azmeer.knilacrud.view.ui.dashboard.adapter.DashboardAdapter;
import com.azmeer.knilacrud.view.ui.login.activity.LoginActivity;

import java.io.FileNotFoundException;
import java.util.ArrayList;


public class DashboardActivity extends AppCompatActivity  implements DashboardAdapter.DashboardAdapterListener {

    private ActivityDashboardBinding binding;
    private DashboardAdapter mAdapter;
    UserModel editUser = new UserModel();

    DialogUserBinding dialogMainBinding;
    String role = "user";
    String userId = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        if(getIntent().getExtras() != null)
        {
            role = getIntent().getStringExtra(AppConstant.ROLE);
            userId = getIntent().getStringExtra(AppConstant.USERID);
        }
        setupToolbar();
        setupRecyclerView();
    }
    public void setupToolbar()
    {
        binding.toolbarDashboard.title.setText(R.string.dashboard);
        binding.toolbarDashboard.ivBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
    public void setupRecyclerView()
    {
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerview.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerview.setNestedScrollingEnabled(false);
        mAdapter = new DashboardAdapter(getUsers(), this);
        binding.recyclerview.setAdapter(mAdapter);
    }
    private ArrayList<UserModel> getUsers() {
        ArrayList<UserModel> userModels = new ArrayList<UserModel>();
        UserDbController userDbController = new UserDbController(this);
        userDbController.open();
        if(role.equals("admin"))
        {
            userModels = userDbController.getAllUsers();
        }
        else
        {
            userModels.add(userDbController.getUserById(userId));
        }
        userDbController.close();
        return userModels;
    }

    void editUser(UserModel userModel)
    {
         dialogMainBinding= DialogUserBinding.inflate(getLayoutInflater());
        Dialog dialog=new Dialog(DashboardActivity.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(
                Color.TRANSPARENT
        ));
        dialog.setContentView(dialogMainBinding.getRoot());
        if(userModel.getProfileImage() != null)
        {
            dialogMainBinding.ivProfile.setImageBitmap(userModel.getProfileImage());

        }
        dialogMainBinding.etFname.setText(userModel.getFirstName());
        dialogMainBinding.etLname.setText(userModel.getLastName());
        dialogMainBinding.etPassword.setText(userModel.getPassword());
        dialogMainBinding.etEmail.setText(userModel.getEmail());
        dialogMainBinding.closeBtn.setOnClickListener(view -> {
            dialog.dismiss();
                });
        dialogMainBinding.btnUpdate.setOnClickListener(view -> {
            if (TextUtils.isEmpty(dialogMainBinding.etFname.getText().toString())) {
                dialogMainBinding.etFname.setError(getString(R.string.enter_first_name));
                dialogMainBinding.etFname.setFocusable(true);
                return;
            }
            if (TextUtils.isEmpty(dialogMainBinding.etLname.getText().toString())) {
                dialogMainBinding.etLname.setError(getString(R.string.enter_last_name));
                dialogMainBinding.etLname.setFocusable(true);

                return;
            }
            if (TextUtils.isEmpty(dialogMainBinding.etEmail.getText().toString())) {
                dialogMainBinding.etEmail.setError(getString(R.string.your_email));
                dialogMainBinding.etEmail.setFocusable(true);

                return;
            }
            if (TextUtils.isEmpty(dialogMainBinding.etPassword.getText().toString())) {
                dialogMainBinding.etPassword.setError(getString(R.string.pass));
                dialogMainBinding.etPassword.setFocusable(true);
                return;
            }
            editUser.setUserId(userModel.getUserId());
            editUser.setFirstName(dialogMainBinding.etFname.getText().toString());
            editUser.setLastName(dialogMainBinding.etLname.getText().toString());
            editUser.setEmail(dialogMainBinding.etEmail.getText().toString());
            editUser.setPassword(dialogMainBinding.etPassword.getText().toString());
            UserDbController userDbController = new UserDbController(this);
            userDbController.open();
            int data = userDbController.updateUser(editUser);
            userDbController.close();
            if(data != 0)
            {
                Util.showToast(this,getString(R.string.user_update_success));
                dialog.dismiss();
                setupRecyclerView();
            }
            else
            {
                Util.showToast(this,getString(R.string.failed_to_update));

            }
        });

        dialogMainBinding.ivProfile.setOnClickListener(view -> {
            addProfile();
        });
        dialog.show();
    }

    private void addProfile() {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    (AppCompatActivity) this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            activityResultLaunch.launch(Intent.createChooser(intent,"Choose File to Upload.."));
        }
    }
    ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Uri targetUri = result.getData().getData();
                        Bitmap bitmap;
                        try {
                            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                            editUser.setProfileImage(bitmap);
                            dialogMainBinding.ivProfile.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
    @Override
    public void onItemClicked(UserModel userModel, View view) {
        if(view.getId() == R.id.iv_more){
            PopupMenu popup = new PopupMenu(this, view);
            popup.getMenuInflater().inflate(R.menu.menu_main, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                if(item.getItemId() == R.id.action_edit)
                {
                    editUser(userModel);
                    return true;
                }
                else if(item.getItemId() == R.id.action_delete)
                {
                    UserDbController userDbController = new UserDbController(this);
                    userDbController.open();
                    userDbController.deleteUser(userModel.getUserId());
                    setupRecyclerView();
                    userDbController.close();
                    return true;
                }
                else
                {
                    return true;
                }
            });
            popup.show();
        }
    }

}
