package com.azmeer.knilacrud.models;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;

import com.azmeer.knilacrud.BR;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserModel extends BaseObservable {
    Integer userId;
    String firstName;
    String lastName;
    Bitmap profileImage;
    String email;
    String password;

    public UserModel() {
    }
    @Bindable
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
        notifyPropertyChanged(BR.userId);
    }
    @Bindable
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        notifyPropertyChanged(BR.firstName);
    }
    @Bindable
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        notifyPropertyChanged(BR.lastName);
    }
    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @BindingAdapter({"profileImage"})
    public static void loadImage(CircleImageView view, Bitmap imageUrl) {
//        Glide.with(view.getContext())
//                .load(imageUrl)
//                .apply(RequestOptions.circleCropTransform())
//                .into(view);
        view.setImageBitmap(imageUrl);

        // If you consider Picasso, follow the below
        // Picasso.with(view.getContext()).load(imageUrl).placeholder(R.drawable.placeholder).into(view);
    }

    @Bindable
    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
        notifyPropertyChanged(BR.profileImage);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }
}
