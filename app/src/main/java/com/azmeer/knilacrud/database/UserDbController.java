package com.azmeer.knilacrud.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.azmeer.knilacrud.models.UserModel;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class UserDbController extends DBController {

    private final String TAG = getClass().getSimpleName();

    public static final String USER_TABLE_NAME = "users";
    public static final String USER_ID = "id"; //int
    public static final String USER_FIRST_NAME = "first_name";
    public static final String USER_LAST_NAME = "last_name";
    public static final String USER_PASSWORD = "password";
    public static final String USER_EMAIL = "email";
    public static final String USER_PROFILE = "profile";

    public static final String USER_TABLE_CREATE =
            "CREATE TABLE " + USER_TABLE_NAME + " (" +
                    USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    USER_FIRST_NAME + " TEXT, " +
                    USER_LAST_NAME + " TEXT, " +
                    USER_PASSWORD + " TEXT, " +
                    USER_EMAIL + " TEXT, " +
                    USER_PROFILE + " BLOB ) ;" ;

    public static final String USER_TABLE_DROP = "DROP TABLE IF EXISTS " + USER_TABLE_NAME + ";";

    public UserDbController(Context pContext) {
        super(pContext);
    }

    public int insert(UserModel userModel) {
        // Create a new map of values, where column names are the keys
        ContentValues pValues = new ContentValues();
        pValues.put(USER_FIRST_NAME, userModel.getFirstName());
        pValues.put(USER_LAST_NAME, userModel.getLastName());
        pValues.put(USER_EMAIL, userModel.getEmail());
        pValues.put(USER_PASSWORD,userModel.getPassword());
        if (userModel.getProfileImage() != null)
            pValues.put(USER_PROFILE, getBitmapAsByteArray(userModel.getProfileImage()));
        int newRowId = (int) mDb.insert(USER_TABLE_NAME, null, pValues);
        return newRowId;
    }

    public byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, outputStream);
        return outputStream.toByteArray();
    }
    public int updateUser(UserModel userModel) {
        // Create a new map of values, where column names are the keys
        ContentValues pValues = new ContentValues();
        pValues.put(USER_FIRST_NAME, userModel.getFirstName());
        pValues.put(USER_LAST_NAME, userModel.getLastName());
        pValues.put(USER_EMAIL, userModel.getEmail());
        pValues.put(USER_PASSWORD,userModel.getPassword());
        if (userModel.getProfileImage() != null)
            pValues.put(USER_PROFILE, getBitmapAsByteArray(userModel.getProfileImage()));
        int newRowId = mDb.update(USER_TABLE_NAME, pValues, USER_ID + " = '" + userModel.getUserId() + "'", null);
        // mDb.close();
        return newRowId;
    }
    public UserModel getUserByNamePassword(String userEmail,String password) {
        UserModel userModel = null;
        String selectQuery = "SELECT  * FROM " + USER_TABLE_NAME + " WHERE " + USER_EMAIL + " = '" + userEmail+"' AND "+ USER_PASSWORD + " = '"+ password+"'";
        Cursor cursor = mDb.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
             userModel = new UserModel();
            userModel.setUserId(cursor.getInt(0));
            userModel.setFirstName(cursor.getString(1));
            userModel.setLastName(cursor.getString(2));
            userModel.setEmail(cursor.getString(4));
            if (cursor.getBlob(5) != null)
                userModel.setProfileImage(BitmapFactory.decodeByteArray(cursor.getBlob(5), 0, cursor.getBlob(5).length));
            userModel.setPassword(cursor.getString(3));
        }
        return userModel;

    }
    public UserModel getUserById(String id) {
        UserModel userModel = null;
        String selectQuery = "SELECT  * FROM " + USER_TABLE_NAME + " WHERE " + USER_ID + " = '" + id+"'";
        Cursor cursor = mDb.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
             userModel = new UserModel();
            userModel.setUserId(cursor.getInt(0));
            userModel.setFirstName(cursor.getString(1));
            userModel.setLastName(cursor.getString(2));
            userModel.setEmail(cursor.getString(4));
            if (cursor.getBlob(5) != null)
                userModel.setProfileImage(BitmapFactory.decodeByteArray(cursor.getBlob(5), 0, cursor.getBlob(5).length));
            userModel.setPassword(cursor.getString(3));
        }
        return userModel;

    }

    public ArrayList<UserModel> getAllUsers() {
        ArrayList<UserModel> userModels = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + USER_TABLE_NAME;
        Cursor cursor = mDb.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                UserModel userModel = new UserModel();
                userModel.setUserId(cursor.getInt(0));
                userModel.setFirstName(cursor.getString(1));
                userModel.setLastName(cursor.getString(2));
                userModel.setEmail(cursor.getString(4));
                if (cursor.getBlob(5) != null)
                    userModel.setProfileImage(BitmapFactory.decodeByteArray(cursor.getBlob(5), 0, cursor.getBlob(5).length));
                userModel.setPassword(cursor.getString(3));
                userModels.add(userModel);

            } while (cursor.moveToNext());

        }
        return userModels;
    }

    public void deleteUser(int id) {
        mDb.execSQL("delete from " + USER_TABLE_NAME + " WHERE " + USER_ID + " = " + id);
    }

}
