package com.example.courseworkv2.userFile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.courseworkv2.CustomToast;
import com.example.courseworkv2.LoginActivity;
import com.example.courseworkv2.R;


public class UserInfoFragment extends Fragment {
    UserDataBaseHelper userDB;
    TextView loginText, PassworText, statusText;
    View rootView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_user_info, container, false);

        //БД
        userDB = new UserDataBaseHelper(rootView.getContext());

        sharedPreferences = getActivity().getSharedPreferences("saveUser", getContext().MODE_PRIVATE);

        //поля для логина, пароля и статуса
        loginText = rootView.findViewById(R.id.editTextLogin);
        loginText.setText(User.getInstance().getLogin());
        loginText.setEnabled(false);


        PassworText = rootView.findViewById(R.id.editTextPassword);
        PassworText.setText(User.getInstance().getPassword());
        PassworText.setEnabled(false);


        statusText = rootView.findViewById(R.id.editTextStatus);
        if (User.getInstance().getIsAdmin() == true)
            statusText.setText("Админ");
        else
            statusText.setText("Пользователь");
        statusText.setEnabled(false);

        //слушатели на кнопки
        rootView.findViewById(R.id.buttonDeleteAccount).setOnClickListener(this::AlertDialogStater);
        rootView.findViewById(R.id.buttonChangePassword).setOnClickListener(this::customDialogStarter);
        return rootView;


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    protected void AlertDialogStater(View view) { //для уточнения удаления аккаунта
        // Создание строителя диалоговых окон
        AlertDialog.Builder builder = new
                AlertDialog.Builder(rootView.getContext());

        // Установка заголовка и сообщения диалогового окна
        builder.setTitle("Подтверждение");
        builder.setMessage("Вы уверены, что хотите удалить аккаунт?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        // Установка кнопки "Да" и ее обработчика
        builder.setPositiveButton("Да", new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Обработка подтверждения
                        deleteAccount();
                    }
                });
        // Установка кнопки "Отмена" и ее обработчика
        builder.setNegativeButton("Отмена", new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Обработка отмены действия

                        dialog.dismiss();
                    }
                });

        // Создание и отображение AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    protected void deleteAccount() { // удаление аккаунта
        if (userDB.deleteUser(User.getInstance().getLogin())) {
            CustomToast.showToast(rootView.getContext(), "Аккаунт удалён", R.drawable.icon_ok, Toast.LENGTH_SHORT);
            Intent intent = new Intent(rootView.getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            editor = sharedPreferences.edit();
            editor.putBoolean("UserEnter", false);
            editor.putString("UserName", "");
            editor.putString("UserPassword", "");
            editor.putBoolean("UserAdmin", false);
            editor.apply();
        } else {
            CustomToast.showToast(rootView.getContext(), "Ошибка при удалении", R.drawable.icon_error, Toast.LENGTH_SHORT);
        }
    }

    protected void customDialogStarter(View view) { //изменение пароля
        // Создание диалога
        Dialog dialog = new Dialog(rootView.getContext());
        // Установка макета для диалогового окна
        dialog.setContentView(R.layout.custom_dialog_change_password);
        // Настройка элементов в макете

        EditText newPassword = dialog.findViewById(R.id.editTextNewPassword);

        Button buttonYes = dialog.findViewById(R.id.buttonYes);
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPasswordString;
                newPasswordString = newPassword.getText().toString();
                if (newPasswordString.isEmpty()) {
                    CustomToast.showToast(rootView.getContext(), "Введите новый пароль", R.drawable.icon_error, Toast.LENGTH_SHORT);
                } else {
                    if (userDB.getPassword(User.getInstance().getLogin()).equals(newPasswordString)) {
                        CustomToast.showToast(rootView.getContext(), "Новый пароль должен отличаться", R.drawable.icon_error, Toast.LENGTH_SHORT);
                    } else if (gapInString(newPasswordString)) {
                        CustomToast.showToast(rootView.getContext(), "Уберите пробелы", R.drawable.icon_error, Toast.LENGTH_SHORT);
                    } else {
                        if (userDB.updateUserPasword(User.getInstance().getLogin(), newPasswordString, User.getInstance().getIsAdmin())) {

                            CustomToast.showToast(rootView.getContext(), "Пароль изменен", R.drawable.icon_ok, Toast.LENGTH_SHORT);

                            User.getInstance().setPassword(newPasswordString);
                            PassworText.setText(User.getInstance().getPassword());
                            PassworText.setEnabled(false);

                            editor = sharedPreferences.edit();
                            editor.putString("UserPassword", User.getInstance().getPassword());
                            editor.apply();

                            dialog.dismiss();
                        } else {
                            CustomToast.showToast(rootView.getContext(), "Ошибка при изменении", R.drawable.icon_error, Toast.LENGTH_SHORT);
                        }
                    }
                }
            }
        });

        Button buttonNo = dialog.findViewById(R.id.buttonNo);
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // Отображение диалогового окна
        dialog.show();
    }

    protected boolean gapInString(String str) {
        if (str.contains(" "))
            //есть пробелы
            return true;
        //нет пробелов
        return false;
    }

}