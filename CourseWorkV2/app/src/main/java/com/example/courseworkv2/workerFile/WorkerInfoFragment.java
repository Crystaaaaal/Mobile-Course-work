package com.example.courseworkv2.workerFile;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.courseworkv2.CustomToast;
import com.example.courseworkv2.R;
import com.example.courseworkv2.userFile.User;

import java.util.Calendar;


public class WorkerInfoFragment extends Fragment {
    WorkerDataBaseHelper workerDB;
    View rootView;
    Worker worker;
    EditText editTextName,editTextSecondName,editTextFatherName, editTextPhoneNumber,editTextAge,editTextDateOfBirth,editTextMail;
    String editTextNameString,editTextSecondNameString,editTextFatherNameString, editTextPhoneNumberString,editTextAgeString,editTextDateOfBirthString,editTextMailString;
    Calendar todayDate =Calendar.getInstance();
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_worker_info, container, false);

        //получаем все editText
        editTextName = rootView.findViewById(R.id.editTextWorkerName);
        editTextSecondName = rootView.findViewById(R.id.editTextWorkerSecondName);
        editTextFatherName = rootView.findViewById(R.id.editTextWorkerFatherName);
        editTextPhoneNumber = rootView.findViewById(R.id.editTextWorkerPhoneNumber);
        editTextAge = rootView.findViewById(R.id.editTextWorkerAge);
        editTextMail = rootView.findViewById(R.id.editTextWorkerMail);
        editTextDateOfBirth = rootView.findViewById(R.id.editTextWorkerDateOfBirth);

        //запрет менять в ручную дату и возраст
        editTextDateOfBirth.setEnabled(false);
        editTextAge.setEnabled(false);


        if (User.getInstance().getIsAdmin()) {
            rootView.findViewById(R.id.buttonCalendar).setOnClickListener(this::DatePickerDialogStarter);
        }
        //база данных
        workerDB = new WorkerDataBaseHelper(getContext());

        //принимает объект с другого фрагмента
        Bundle bundle = getArguments();
        if(bundle != null) {
            //присваиваем значение этого объекта
            worker = (Worker) bundle.getSerializable("Worker");

            //вписываем в editText значения из полей полученного объекта
            editTextName.setText(worker.getName());
            editTextSecondName.setText(worker.getSecondName());
            editTextFatherName.setText(worker.getFatherName());
            editTextPhoneNumber.setText(worker.getPhoneNumber());
            editTextAge.setText(Integer.toString(worker.getAge()));
            editTextMail.setText(worker.getMail());
            editTextDateOfBirth.setText(worker.getDateOfBirth());

            //запрещаем менять имя фамилию и отчество
            editTextName.setEnabled(false);
            editTextSecondName.setEnabled(false);
            editTextFatherName.setEnabled(false);
        }
        //кнопка удаления может быть доступна только к тем работников которые уже есть в БД
        else{rootView.findViewById(R.id.buttonDeleteWorker).setVisibility(View.GONE);}
        //Админ или пользователь
        if(User.getInstance().getIsAdmin() == false){
            //если нет скрываем кнопки удалаения и сохранение и запрещаем изменять все editText
            rootView.findViewById(R.id.buttonSaveWorker).setVisibility(View.GONE);
            rootView.findViewById(R.id.buttonDeleteWorker).setVisibility(View.GONE);
            editTextName.setEnabled(false);
            editTextSecondName.setEnabled(false);
            editTextFatherName.setEnabled(false);
            editTextPhoneNumber.setEnabled(false);
            editTextAge.setEnabled(false);
            editTextMail.setEnabled(false);
            editTextDateOfBirth.setEnabled(false);
        }
        else {
            //если нет то ничего запрещаем
            //слушатель на кнопку добавления
            rootView.findViewById(R.id.buttonSaveWorker).setOnClickListener(this::takeData);
            rootView.findViewById(R.id.buttonDeleteWorker).setOnClickListener(this::AlertDialogStater);
        }

        return  rootView;
    }
    protected void takeData(View view){
        //получаем с editText строковые значения
        editTextNameString = editTextName.getText().toString();
        editTextSecondNameString = editTextSecondName.getText().toString();
        editTextFatherNameString= editTextFatherName.getText().toString();
        editTextPhoneNumberString= editTextPhoneNumber.getText().toString();
        editTextMailString = editTextMail.getText().toString();
        editTextAgeString= editTextAge.getText().toString();
        editTextDateOfBirthString = editTextDateOfBirth.getText().toString();
        //если поля пустые
        if (editTextNameString.isEmpty() ||
                editTextSecondNameString.isEmpty() ||
                editTextFatherNameString.isEmpty() ||
                editTextPhoneNumberString.isEmpty() ||
                editTextAgeString.isEmpty() ||
                editTextDateOfBirthString.isEmpty() ||
                editTextMailString.isEmpty()){
            CustomToast.showToast(getContext(),"Введите данные",R.drawable.icon_error,Toast.LENGTH_SHORT);
            return;
        }
        if (gapInString(editTextNameString)||
        gapInString(editTextSecondNameString)||
        gapInString(editTextFatherNameString) ||
        gapInString(editTextPhoneNumberString) ||
        gapInString(editTextDateOfBirthString) ||
        gapInString(editTextMailString)){
            CustomToast.showToast(getContext(),"Уберите пробелы",R.drawable.icon_error,Toast.LENGTH_SHORT);
            return;
        }
        //если не пустые
        editTextNameString = firstToBig(editTextNameString);
        editTextSecondNameString = firstToBig(editTextSecondNameString);
        editTextFatherNameString=firstToBig(editTextFatherNameString);


        //если пользователь есть в базе данных обноляем его параметры
        if (workerDB.workerInBase(editTextNameString, editTextSecondNameString, editTextFatherNameString)) {
            worker.setAge(Integer.parseInt(editTextAgeString));
            worker.setMail(editTextMailString);
            worker.setDateOfBirth(editTextDateOfBirthString);
            worker.setPhoneNumber(editTextPhoneNumberString);
            workerDB.updateWorker(worker);
            CustomToast.showToast(getContext(),"Данные сохранены",R.drawable.icon_ok,Toast.LENGTH_SHORT);
        }
        //если пользователя нет добовляем его в бд
        else{
            Worker worker = new Worker(editTextNameString,
                    editTextFatherNameString,
                    editTextSecondNameString,
                    editTextPhoneNumberString,
                    editTextMailString,
                    Integer.parseInt(editTextAgeString),
                    editTextDateOfBirthString);
            workerDB.addWorker(worker);
            CustomToast.showToast(getContext(),"Данные сохранены",R.drawable.icon_ok,Toast.LENGTH_SHORT);
        }
        //переходи на фрагмент назад
        WorkerListFragment newFragment = new WorkerListFragment(); // Создаем новый фрагмент, который будет отображен
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, newFragment); // Заменяем текущий фрагмент на новый
        transaction.addToBackStack(null); // Добавляем транзакцию в стек возврата
        transaction.commit();
    }

    protected void AlertDialogStater(View view){ //для уточнения удаления рабочего
        // Создание строителя диалоговых окон
        AlertDialog.Builder builder = new
                AlertDialog.Builder(rootView.getContext());

        // Установка заголовка и сообщения диалогового окна
        builder.setTitle("Подтверждение");
        builder.setMessage("Вы уверены, что хотите удалить работника?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        // Установка кнопки "Да" и ее обработчика
        builder.setPositiveButton("Да", new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Обработка подтверждения
                        deleteWorker();
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
    protected void deleteWorker(){
        editTextNameString = editTextName.getText().toString();
        editTextSecondNameString = editTextSecondName.getText().toString();
        editTextFatherNameString= editTextFatherName.getText().toString();
        if (workerDB.deleteWorker(editTextNameString,
                editTextSecondNameString,
                editTextFatherNameString)){
            CustomToast.showToast(getContext(),"Работник удален из БД",R.drawable.icon_ok,Toast.LENGTH_SHORT);

            //переход на фрагмент назад
            WorkerListFragment newFragment = new WorkerListFragment(); // Создаем новый фрагмент, который будет отображен
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, newFragment); // Заменяем текущий фрагмент на новый
            transaction.addToBackStack(null); // Добавляем транзакцию в стек возврата
            transaction.commit();
        }
        else{
            CustomToast.showToast(getContext(),"Ошибка при удалении",R.drawable.icon_error,Toast.LENGTH_SHORT);
        }
    }
    protected boolean gapInString(String str){
        if (str.contains(" "))
            //есть пробелы
            return true;
        //нет пробелов
        return false;
    }

    protected void  DatePickerDialogStarter(View view){
        // Получаем сегодняшнюю дату
        int year = todayDate.get(Calendar.YEAR),
                month = todayDate.get(Calendar.MONTH),
                day = todayDate.get(Calendar.DAY_OF_MONTH);
        // Создание обработчика выбора даты
        DatePickerDialog datePickerDialog = new DatePickerDialog( getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String monthStr = Integer.toString(monthOfYear + 1),
                        dayStr = Integer.toString(dayOfMonth);
                if (monthStr.length() == 1)
                    monthStr = "0"+monthStr;
                if (dayStr.length() == 1)
                    dayStr = "0"+dayStr;

                editTextDateOfBirth.setText(dayStr+"."+monthStr+"."+year);

                // Устанавливаем дату рождения
                Calendar birthDate = Calendar.getInstance();
                birthDate.set(year, monthOfYear, dayOfMonth);

                // Вычисляем возраст
                int age = todayDate.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
                if (todayDate.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
                    age--; // Уменьшаем возраст, если день рождения еще не наступил в этом году
                }
                editTextAge.setText(Integer.toString(age));
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    protected String firstToBig(String str) {
        String firstChar = str.substring(0, 1).toUpperCase();
        String result = firstChar + str.substring(1);
        return result;
    }
}