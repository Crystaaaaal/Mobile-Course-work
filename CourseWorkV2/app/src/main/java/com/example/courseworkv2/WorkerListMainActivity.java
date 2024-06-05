package com.example.courseworkv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.courseworkv2.userFile.User;
import com.example.courseworkv2.userFile.UserInfoFragment;
import com.example.courseworkv2.workerFile.WorkerDataBaseHelper;
import com.example.courseworkv2.workerFile.WorkerInfoFragment;
import com.example.courseworkv2.workerFile.WorkerListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class WorkerListMainActivity extends AppCompatActivity {
     ActionBar actionBar;// верхнее меню
      ActionBarDrawerToggle toggle;
     DrawerLayout myDrawerLayout;

     WorkerDataBaseHelper workerDB;
    SharedPreferences sharedPreferences;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_worker_list);

        workerDB = new WorkerDataBaseHelper(WorkerListMainActivity.this);

        sharedPreferences = getSharedPreferences("saveUser", MODE_PRIVATE);
        //объект Drawer
        myDrawerLayout = findViewById(R.id.drawer_layout_id);
        // Запуск верхнего меню
        actionBarStarter();

        // Установка котейнера
        setContainer();

        connectDrawerLayoutWithActionBar();

        if (!User.getInstance().getIsAdmin()){
            findViewById(R.id.addNew).setVisibility(View.GONE);
        }

    }


    //Привязка меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        //return super.onCreateOptionsMenu(menu);
        return true;
    }


    public void actionBarStarter(){
        //отоброжение верхнего меню
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        //Заполнение меню
        actionBar.setTitle("Список работников");
        actionBar.setDisplayHomeAsUpEnabled(true);



//        //При нажатии на значок, откроется боковая панель
//        if (actionBar != null) {
//            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
//            bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
//                if (item.getItemId() == R.id.account) {
//                    actionBar.setTitle("Акаунт");
//                }
//                else if (item.getItemId() == R.id.exit) {
//                    toPrevios();
//                }
//                return false;
//            });
//        }

        //обработка нажатий кнопок
        NavigationView myNavigationView = findViewById(R.id.navigation_view_id);
        myNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.account) {
                    actionBar.setTitle("Акаунт");
                    toAccount();
                    myDrawerLayout.close();
                }
                if (item.getItemId() == R.id.toList) {
                    actionBar.setTitle("Список работников");
                    toMain();
                    myDrawerLayout.close();
                }
                else if (item.getItemId() == R.id.exit) {
                    toPrevios();
                    myDrawerLayout.close();
                }
                return true;
            }
        });



        //При нажатии на значок, откроется боковая панель
        if (actionBar != null) {
            bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
                if (item.getItemId() == R.id.toMain) {
                    actionBar.setTitle("Главная");
                    toMain();
                }
                else if (item.getItemId() == R.id.addNew) {
                    toAddNew();
                }


                return false;
            });
        }
    }


    // Обработка нажатия на иконку меню в ActionBar для открытия и закрытия Drawer
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }


    public void setContainer(){
//       Bundle bundle = new Bundle();
//        bundle.putSerializable("User", userInp);
//        // set Fragmentclass Arguments
//        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.container,
                WorkerListFragment.class, null).commit();
    }


    public void connectDrawerLayoutWithActionBar(){
        //интеграция ActionBar с DrawerLayout
        toggle = new ActionBarDrawerToggle(WorkerListMainActivity.this, myDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        if (myDrawerLayout != null) {
            myDrawerLayout.addDrawerListener(toggle);
        }
        toggle.syncState();
        //чтобы значок панели навигации всегда отображался на панели действий
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void toPrevios(){
        // Сохранение данных
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Сохранение строкового значения
        editor.putBoolean("UserEnter",  false);
        editor.putString("UserName","");
        editor.putString("UserPassword","");
        editor.putBoolean("UserAdmin",false);
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }


    public void toMain(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        WorkerListFragment workerListFragment = new WorkerListFragment();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, workerListFragment);
        fragmentTransaction.commit();
    }
    public void toAddNew(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        WorkerInfoFragment workerInfoFragment = new WorkerInfoFragment();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, workerInfoFragment);
        fragmentTransaction.commit();
    }
    public void toAccount(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        UserInfoFragment userInfoFragment = new UserInfoFragment();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, userInfoFragment);
        fragmentTransaction.commit();
        }


}