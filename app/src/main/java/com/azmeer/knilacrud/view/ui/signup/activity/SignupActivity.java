package com.azmeer.knilacrud.view.ui.signup.activity;

import static com.azmeer.knilacrud.utils.AppConstant.PERMISSIONS_STORAGE;
import static com.azmeer.knilacrud.utils.AppConstant.REQUEST_EXTERNAL_STORAGE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.azmeer.knilacrud.R;
import com.azmeer.knilacrud.database.UserDbController;
import com.azmeer.knilacrud.databinding.ActivitySignupBinding;
import com.azmeer.knilacrud.models.UserModel;
import com.azmeer.knilacrud.utils.AppConstant;
import com.azmeer.knilacrud.utils.Util;
import com.azmeer.knilacrud.view.ui.dashboard.activity.DashboardActivity;
import com.azmeer.knilacrud.view.ui.login.activity.LoginActivity;

import java.io.FileNotFoundException;


public class SignupActivity extends AppCompatActivity    {

    private ActivitySignupBinding binding;
    private UserModel user=new UserModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        setOnclickListners();
        setupToolbar();
    }
    public void setOnclickListners()
    {
        binding.ivProfile.setOnClickListener(v -> {
            addProfile();
        });
        binding.btnRegister.setOnClickListener(v -> {
            if (TextUtils.isEmpty(binding.etFname.getText().toString())) {
                binding.etFname.setError(getString(R.string.enter_first_name));
                binding.etFname.setFocusable(true);

                return;
            }
            if (TextUtils.isEmpty(binding.etLname.getText().toString())) {
                binding.etLname.setError(getString(R.string.enter_last_name));
                binding.etLname.setFocusable(true);

                return;
            }
            if (TextUtils.isEmpty(binding.etEmail.getText().toString())) {
                binding.etEmail.setError(getString(R.string.your_email));
                binding.etEmail.setFocusable(true);

                return;
            }
            if (TextUtils.isEmpty(binding.etPassword.getText().toString())) {
                binding.etPassword.setError(getString(R.string.pass));
                binding.etPassword.setFocusable(true);

                return;
            }
            saveRegistrationDetails();
        });
    }
    public void setupToolbar()
    {
        binding.toolbarSignup.title.setText(R.string.str_register);
        binding.toolbarSignup.ivBack.setOnClickListener(v -> {

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
    public void saveRegistrationDetails()
    {

        user.setFirstName(binding.etFname.getText().toString());
        user.setLastName(binding.etLname.getText().toString());
        user.setEmail(binding.etEmail.getText().toString());
        user.setPassword(binding.etPassword.getText().toString());
        UserDbController userDbController = new UserDbController(this);
        userDbController.open();
        int id= userDbController.insert(user);
        if(id != 0)
        {
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.putExtra(AppConstant.ROLE,"user");
            intent.putExtra(AppConstant.USERID,String.valueOf(id));
            startActivity(intent);
            finish();
        }
        else
        {
            Util.showToast(this,"Failed to Register");
        }

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
                            user.setProfileImage(bitmap);
                            binding.ivProfile.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

}
