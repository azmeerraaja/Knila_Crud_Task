package com.azmeer.knilacrud.view.ui.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.azmeer.knilacrud.R;
import com.azmeer.knilacrud.database.UserDbController;
import com.azmeer.knilacrud.databinding.ActivityLoginBinding;
import com.azmeer.knilacrud.models.Datum;
import com.azmeer.knilacrud.models.ResultResponse;
import com.azmeer.knilacrud.models.UserModel;
import com.azmeer.knilacrud.network.Api;
import com.azmeer.knilacrud.network.RetrofitClient;
import com.azmeer.knilacrud.utils.AppConstant;
import com.azmeer.knilacrud.utils.Util;
import com.azmeer.knilacrud.view.ui.dashboard.activity.DashboardActivity;
import com.azmeer.knilacrud.view.ui.signup.activity.SignupActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private UserModel user=new UserModel();
    UserDbController userDbController;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userDbController = new UserDbController(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupToolbar();
        binding.tvRegister.setOnClickListener(v->
        {
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
            finish();
        });

        userDbController.open();
        ArrayList<UserModel> userModellist = userDbController.getAllUsers();
        userDbController.close();
        binding.btnLogin.setOnClickListener(v -> {
            if (TextUtils.isEmpty(binding.etEmail.getText().toString())) {
                Util.showToast(this,getString(R.string.enter_your_email));
            }
            if (TextUtils.isEmpty(binding.etPassword.getText().toString())) {
                Util.showToast(this,getString(R.string.enter_your_password));
                return;
            }
            if(binding.etEmail.getText().toString().equals(AppConstant.ADMIN_USER_NAME) && binding.etPassword.getText().toString().equals(AppConstant.ADMIN_PASSWORD))
            {
                gotoDashboardActivity("admin","0");
            }
            else
            {
                userDbController.open();
                UserModel userModel = userDbController.getUserByNamePassword(binding.etEmail.getText().toString(),binding.etPassword.getText().toString());
                userDbController.close();
                if(userModel == null)
                {
                    Util.showToast(this,getString(R.string.invalid_credentials));
                }
                else
                {
                    gotoDashboardActivity("user",userModel.getUserId().toString());
                }

            }


        });

        if(userModellist.size()>0){

        }else{
            adddefaultusers();

        }
    }
    public void gotoDashboardActivity(String type,String userID)
    {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.putExtra(AppConstant.ROLE,type);
        intent.putExtra(AppConstant.USERID,userID);
        startActivity(intent);
        finish();
    }
    public void setupToolbar()
    {
      binding.toolbarLogin.title.setText(R.string.str_login);
      binding.toolbarLogin.ivBack.setVisibility(View.INVISIBLE);
      }

    private void adddefaultusers() {

        Api apiInterface = RetrofitClient.getRetrofitInstance(this).create(Api.class);
        Call<ResultResponse> call = apiInterface.getusers();
        call.enqueue(new Callback<ResultResponse>() {
            @Override
            public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {


                Log.d("TAG",response.code()+"");


                ResultResponse resource = response.body();

                List<Datum> datumList = resource.getData();


                for (Datum datum : datumList) {

                    user.setFirstName(datum.getFirstName());
                    user.setLastName(datum.getLastName());
                    user.setEmail(datum.getEmail());
                    user.setPassword("temp123");
                    UserDbController userDbController = new UserDbController(LoginActivity.this);
                    userDbController.open();
                   userDbController.insert(user);
                }


            }

            @Override
            public void onFailure(Call<ResultResponse> call, Throwable t) {
                call.cancel();
            }
        });
    }


}