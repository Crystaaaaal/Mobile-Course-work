package com.example.courseworkv2.workerFile;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.courseworkv2.CustomToast;
import com.example.courseworkv2.R;

import java.io.IOException;
import java.util.ArrayList;


public class WorkerListFragment extends Fragment {
    WorkerDataBaseHelper workerDB;
    View rootView;
    ListView list;
    ArrayList<Worker> workerList = new ArrayList<Worker>();
    ;// списко объектов Worker
    ArrayList<String> workersNamesList = new ArrayList<>();// Список имён работников
    EditText editTextName, editTextSecondName, editTextFatherName;
    String editTextNameString, editTextSecondNameString, editTextFatherNameString;
    boolean boolName,boolSecondName,boolFatherName;
    ArrayAdapter<String> usersAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_worker_list, container, false);
        list = rootView.findViewById(R.id.list_view);
        workerDB = new WorkerDataBaseHelper(getContext());
        workerList = workerDB.getAllWorker();

        rootView.findViewById(R.id.buttonSearch).setOnClickListener(this::search);
        rootView.findViewById(R.id.buttonSearchDel).setOnClickListener(this::setAll);

        //Заполняем arrayList именами рабочих из списка WorkerList
        for (int i = 0; i < workerList.size(); i++) {
            String name = workerList.get(i).getName();
            String fatherName = workerList.get(i).getFatherName();
            String secondName = workerList.get(i).getSecondName();
            workersNamesList.add(secondName + " " + name + " " + fatherName + " ");
        }


        usersAdapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, workersNamesList);
        // устанавливаем адаптер для списка
        list.setAdapter(usersAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Worker worker = workerList.get(position);
                WorkerInfoFragment newFragment = new WorkerInfoFragment(); // Создаем новый фрагмент, который будет отображен
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putSerializable("Worker", worker);
                newFragment.setArguments(bundle);

                transaction.replace(R.id.container, newFragment); // Заменяем текущий фрагмент на новый
                transaction.addToBackStack(null); // Добавляем транзакцию в стек возврата
                transaction.commit();
            }
        });
        return rootView;
    }

    protected void search(View view) {
        // Создание диалога
        Dialog dialog = new Dialog(rootView.getContext());
        // Установка макета для диалогового окна
        dialog.setContentView(R.layout.custom_dialog_search);


        // Настройка элементов в макете
        editTextName = dialog.findViewById(R.id.editTextSearchName);
        editTextSecondName = dialog.findViewById(R.id.editTextSearchSecondName);
        editTextFatherName = dialog.findViewById(R.id.editTextSearchFatherName);
        boolName =false;
        boolSecondName = false;
        boolFatherName= false;

        Button buttonYes = dialog.findViewById(R.id.buttonYes);
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    editTextNameString = editTextName.getText().toString();
                    if (!editTextNameString.equals("")) {
                        editTextNameString = firstToBig(editTextNameString);
                        boolName = true;
                    }

                    editTextSecondNameString = editTextSecondName.getText().toString();
                    if (!editTextSecondNameString.equals("")) {
                        editTextSecondNameString = firstToBig(editTextSecondNameString);
                        boolSecondName = true;
                    }

                    editTextFatherNameString = editTextFatherName.getText().toString();
                    if (!editTextFatherNameString.equals("")) {
                        editTextFatherNameString = firstToBig(editTextFatherNameString);
                        boolFatherName = true;
                    }
                if (gapInString(editTextNameString) || gapInString(editTextSecondNameString) || gapInString(editTextFatherNameString)) {
                    CustomToast.showToast(rootView.getContext(), "Уберите пробелы", R.drawable.icon_error, Toast.LENGTH_SHORT);
                    return;
                }
                //Введено полное имя
                if (boolName == true && boolSecondName == true && boolFatherName == true){
                    workerList.clear();
                    workerList = workerDB.getAllWorkerByFullName(editTextNameString,editTextSecondNameString,editTextFatherNameString);
                    refreshList(workerList);
                    dialog.dismiss();
                    return;
                }
                //Введено только имя
                else if (boolName == true){
                    workerList.clear();
                    workerList = workerDB.getAllWorkerByName(editTextNameString);
                    dialog.dismiss();
                }
                //Введено только отчество
                else if(boolSecondName == true){
                    workerList.clear();
                    workerList = workerDB.getAllWorkerBySecondName(editTextSecondNameString);
                    boolSecondName = false;
                    dialog.dismiss();
                }
                //Введена только фамилия
                else if(boolFatherName == true){
                    workerList.clear();

                    workerList = workerDB.getAllWorkerByFatherName(editTextFatherNameString);
                    dialog.dismiss();
                }


                //ввдено имя и фамили или отчестов
                ArrayList<Worker> workerListSecond = new ArrayList<>();
                if(boolSecondName == true && boolName == true && boolFatherName == false){
                    for (int i = 0 ; i < workerList.size();i++){
                        if (workerList.get(i).getSecondName().equals(editTextSecondNameString))
                            workerListSecond.add(workerList.get(i));
                    }
                    refreshList(workerList);
                    return;
                }
                else if(boolFatherName == true && boolName == true && boolSecondName == false){
                    for (int i = 0 ; i < workerList.size();i++){
                        if (workerList.get(i).getFatherName().equals(editTextFatherNameString)) {
                            workerListSecond.add(workerList.get(i));
                        }
                    }
                    refreshList(workerList);
                    return;
                }
                //ведено отчество и фамилия
                if (boolFatherName == true && boolSecondName == true && boolName == false){
                    for (int i = 0 ; i < workerList.size();i++){
                        if (workerList.get(i).getSecondName().equals(editTextSecondNameString)) {
                            workerListSecond.add(workerList.get(i));
                        }
                    }
                    refreshList(workerList);
                    return;
                }
                refreshList(workerList);
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
    protected void refreshList( ArrayList<Worker> workerList){

        //Заполняем arrayList именами рабочих из списка WorkerList
        workersNamesList.clear();
        for (int i = 0; i < workerList.size(); i++) {
            String name = workerList.get(i).getName();
            String fatherName = workerList.get(i).getFatherName();
            String secondName = workerList.get(i).getSecondName();
            workersNamesList.add(secondName + " " + name + " " + fatherName + " ");
        }
        usersAdapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, workersNamesList);
        // устанавливаем адаптер для списка
        list.setAdapter(usersAdapter);
    }
    protected String firstToBig(String str) {
        String firstChar = str.substring(0, 1).toUpperCase();
        String result = firstChar + str.substring(1);
        return result;
    }
    protected void setAll(View view){
        workerList = workerDB.getAllWorker();
        refreshList(workerList);
        CustomToast.showToast(rootView.getContext(), "Поиск отменён", R.drawable.icon_ok, Toast.LENGTH_SHORT);
    }
}