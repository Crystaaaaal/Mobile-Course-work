package com.example.courseworkv2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.courseworkv2.userFile.User;
import com.example.courseworkv2.userFile.UserDataBaseHelper;
import com.example.courseworkv2.workerFile.WorkerDataBaseHelper;

public class LoginActivity extends AppCompatActivity {
    UserDataBaseHelper userDB;
    WorkerDataBaseHelper workerDB;

    EditText EditTextLogin,EditTextPassword;
    String login,password;

    SharedPreferences sharedPreferences;
    Drawable icon;
    Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);

        User user;

        icon = getResources().getDrawable(R.drawable.icon_error);


        userDB = new UserDataBaseHelper(LoginActivity.this);
        workerDB = new WorkerDataBaseHelper(LoginActivity.this);

        sharedPreferences = getSharedPreferences("saveUser", MODE_PRIVATE);
        userEnter();

        EditTextLogin =(EditText)findViewById(R.id.editTextLogin);
        EditTextPassword = (EditText)findViewById(R.id.editTextPasword);

        findViewById(R.id.buttonLogin).setOnClickListener(this::onNextActivity);
        findViewById(R.id.buttonRegister).setOnClickListener(this::registerUser);


    }
    protected void userEnter(){
        Boolean userEnter = sharedPreferences.getBoolean("UserEnter", false);
        if (userEnter) {
            User.getInstance().setLogin(sharedPreferences.getString("UserName",""));
            User.getInstance().setPassword(sharedPreferences.getString("UserPassword",""));
            User.getInstance().setIsAdmin(sharedPreferences.getBoolean("UserAdmin",false));

            Intent intent = new Intent(LoginActivity.this, WorkerListMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
    protected void onNextActivity(View view){

        login = EditTextLogin.getText().toString();
        password = EditTextPassword.getText().toString();
        if (gapInString(login) || gapInString(password)) {
            CustomToast.showToast(this,"Уберите пробелы",R.drawable.icon_error,Toast.LENGTH_SHORT);
            return;
        }
        if (login.isEmpty() || password.isEmpty()) {
            CustomToast.showToast(this,"Введите данные",R.drawable.icon_error,Toast.LENGTH_SHORT);
            return;
        }
            else {
                if (userDB.userInBase(login)) {
                    if (password.equals(userDB.getPassword(login))) {
                        User.getInstance().setIsAdmin(userDB.getStatus(login));
                        User.getInstance().setLogin(login);
                        User.getInstance().setPassword(password);

                        CustomToast.showToast(this,"Добро пожаловать",R.drawable.icon_ok,Toast.LENGTH_SHORT);

                        // Сохранение данных
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        // Сохранение строкового значения
                        editor.putBoolean("UserEnter", true);
                        editor.putString("UserName", User.getInstance().getLogin());
                        editor.putString("UserPassword", User.getInstance().getPassword());
                        editor.putBoolean("UserAdmin", User.getInstance().getIsAdmin());
                        editor.apply();

                        Intent intent = new Intent(this, WorkerListMainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        CustomToast.showToast(this,"Неверный пароль",R.drawable.icon_error,Toast.LENGTH_SHORT);


                    }
                } else {
                    CustomToast.showToast(this,"Такой пользователь не зарегестрирован",R.drawable.icon_error,Toast.LENGTH_SHORT);

                }
            }
    }
    protected void registerUser(View view){
        login = EditTextLogin.getText().toString();
        password = EditTextPassword.getText().toString();
        if (gapInString(login) || gapInString(password)) {
            CustomToast.showToast(this,"Уберите пробелы",R.drawable.icon_error,Toast.LENGTH_SHORT);
            return;
        }
        if (login.isEmpty() || password.isEmpty()) {
            CustomToast.showToast(this,"Введите данные",R.drawable.icon_error,Toast.LENGTH_SHORT);

        }
        else{
            if (userDB.userInBase(login)){
                CustomToast.showToast(this,"Такой логин уже зарегестрирован",R.drawable.icon_error,Toast.LENGTH_SHORT);
            }
            else{
                if (userDB.isDatabaseEmpty() == true) {
                    User.getInstance().setIsAdmin(true);
                }
                else{
                    User.getInstance().setIsAdmin(false);
                }
                    User.getInstance().setLogin(login);
                    User.getInstance().setPassword(password);
                    userDB.addUser(User.getInstance());
                    CustomToast.showToast(this,"Вы зарегестрированы", R.drawable.icon_ok,Toast.LENGTH_SHORT);


                }
        }
    }
    protected boolean gapInString(String str){
        if (str.contains(" "))
            //есть пробелы
            return true;
        //нет пробелов
        return false;
    }
}
