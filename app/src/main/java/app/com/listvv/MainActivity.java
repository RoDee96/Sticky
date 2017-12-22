package app.com.listvv;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> string = new ArrayList<>();
    ArrayList<Integer> idList = new ArrayList<>();

    private ArrayList<EntryBean> beanArrayList = new ArrayList<>();
    private MyAdapter newAdapter;

    ArrayAdapter arrayAdapter;

    ListView listView;
    Dialog dialog_add;
    Dialog dialog;
    Dialog dialog_update;

    int positionTapped=-1;
    int alarmID;
    
    int hour;
    int minute;

    int year;
    int month;
    int day;

    //for Dialog
    private EditText updateEditText;
    private Button showUpdateTimeDialogButton;
    private Button showUpdateDateDialogButton;
    private Button updateButton;
    //for dialog

    //for update dialog
    private EditText editText;
    private Button showTimeDialogButton;
    private Button showDateDialogButton;
    private Button addButton;
    //for update dialog

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        fetchFromDatabase();
        addListeners();
    }

    private void init(){
        fab = (FloatingActionButton) findViewById(R.id.fab);
        listView = (ListView) findViewById(R.id.listView);

        initDialogs();

        editText = (EditText) dialog_add.findViewById(R.id.text);
        addButton = (Button) dialog_add.findViewById(R.id.addButton);
        showDateDialogButton = (Button) dialog_add.findViewById(R.id.setDateButton);
        showTimeDialogButton = (Button) dialog_add.findViewById(R.id.setTimeButton);

        updateEditText = (EditText) dialog_update.findViewById(R.id.text);
        updateButton = (Button) dialog_update.findViewById(R.id.addButton);
        showUpdateDateDialogButton = (Button) dialog_update.findViewById(R.id.setDateButton);
        showUpdateTimeDialogButton = (Button) dialog_update.findViewById(R.id.setTimeButton);
    }

    private void initDialogs(){
        dialog_add = new Dialog(MainActivity.this);
        dialog_add.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_add.setContentView(R.layout.dialog_add);

        dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_design);

        dialog_update = new Dialog(MainActivity.this);
        dialog_update.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_update.setContentView(R.layout.dialog_update);

    }

    private void addListeners(){

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_add.show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                MainActivity.this.positionTapped = pos;
                showLongTappedDialog();
                return true;
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addButtonActions();
            }
        });

        showTimeDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeDialog();
            }
        });

        showDateDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateButtonActions();
            }
        });

        showUpdateTimeDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUpdateTimeDialog();
            }
        });

        showUpdateDateDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUpdateDateDialog();
            }
        });
    }

    private void fetchFromDatabase(){
        alarmID = 0;
        beanArrayList.clear();
        MySqliteOpenHelper mySqliteOpenHelper = new MySqliteOpenHelper(this);
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();

        Cursor cursor = ReminderTable.select(db, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String text = cursor.getString(1);
                int day = cursor.getInt(2);
                int month = cursor.getInt(3);
                int year = cursor.getInt(4);
                int hour = cursor.getInt(5);
                int minute = cursor.getInt(6);

                FormatTIme ft = new FormatTIme(hour, minute);

                EntryBean newBean = new EntryBean(id, text, ft.getTime(),  day + "\\" + ((int)(month+1)) + "\\" + year);

                beanArrayList.add(newBean);
                alarmID++;
                //idList.add(id);
                //string.add(text + "\n" + ft.getTime() +" "+ day + "\\" + ((int)(month+1)) + "\\" + year);
            }
            cursor.close();
            db.close();
        }
        newAdapter = new MyAdapter(MainActivity.this, R.layout.list_item, beanArrayList);

        listView.setAdapter(newAdapter);
    }

    private void showLongTappedDialog() {

        dialog.show();
        ListView lv = (ListView) dialog.findViewById(R.id.listDeleteEdit);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    Toast.makeText(MainActivity.this, "Edit", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    dialog_update.show();
                    updateEntry();

                }
                if(position == 1){
                    deleteEntry();
                    dialog.dismiss();
                }
            }
        });
    }

    private void showDateDialog() {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Toast.makeText(MainActivity.this, "Date = "+year+"-"+month+"-"+day, Toast.LENGTH_SHORT).show();
                MainActivity.this.year = year;
                MainActivity.this.month = month;
                MainActivity.this.day = day;

                int m = month+1;
                showDateDialogButton.setText(day+"\\"+m+"\\"+year);
            }
        }, year, month, day);
        datePickerDialog.show();

    }

    private void showTimeDialog(){

        Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Toast.makeText(MainActivity.this, "Time = "+hour+":"+minute, Toast.LENGTH_SHORT).show();
                MainActivity.this.hour = hour;
                MainActivity.this.minute = minute;

                FormatTIme ft = new FormatTIme(hour, minute);
                showTimeDialogButton.setText(ft.getTime());

            }

        }, 0, 0, false);

        timePickerDialog.show();

    }

    private void addButtonActions(){

        String str = editText.getText().toString();
        if(str.equals("")){
            Toast.makeText(MainActivity.this, "No Text Entered!", Toast.LENGTH_SHORT).show();
        }
        else {
            if(showDateDialogButton.getText().equals("Set date")){
                Toast.makeText(MainActivity.this, "Enter date", Toast.LENGTH_SHORT).show();
            }
            else{
                if(showTimeDialogButton.getText().equals("Set time")){
                    Toast.makeText(MainActivity.this, "Enter Time", Toast.LENGTH_SHORT).show();
                }
                else{
                    FormatTIme ft = new FormatTIme(hour, minute);

                    string.add(str + "\n" + ft.getTime() +" "+ day + "\\" + month + "\\" + year);

                    //database entry;
                    MySqliteOpenHelper mySqliteOpenHelper = new MySqliteOpenHelper(MainActivity.this);
                    SQLiteDatabase db = mySqliteOpenHelper.getWritableDatabase();

                    ContentValues cv = new ContentValues();
                    cv.put(ReminderTable.TEXT, str);
                    cv.put(ReminderTable.DAY, day);
                    cv.put(ReminderTable.MONTH, month);
                    cv.put(ReminderTable.YEAR, year);
                    cv.put(ReminderTable.HOUR, hour);
                    cv.put(ReminderTable.MINUTE, minute);

                    long l = ReminderTable.insert(db, cv);
                    setAlarm();
                    if (l>0) {
                        Toast.makeText(MainActivity.this, "Added", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Unable to add", Toast.LENGTH_SHORT).show();
                    }
                    db.close();
                    editText.setText("");
                    showDateDialogButton.setText("Set date");
                    showTimeDialogButton.setText("Set time");
                    dialog_add.dismiss();
                }
            }

        }
        fetchFromDatabase();


    }

    private void showUpdateDateDialog() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Toast.makeText(MainActivity.this, "Date = "+year+"-"+month+"-"+day, Toast.LENGTH_SHORT).show();
                MainActivity.this.year = year;
                MainActivity.this.month = month;
                MainActivity.this.day = day;

                int m = month+1;
                showUpdateDateDialogButton.setText(day+"\\"+m+"\\"+year);
            }
        }, year, month, day);
        datePickerDialog.show();

    }

    private void showUpdateTimeDialog(){

        Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Toast.makeText(MainActivity.this, "Time = "+hour+":"+minute, Toast.LENGTH_SHORT).show();
                MainActivity.this.hour = hour;
                MainActivity.this.minute = minute;

                FormatTIme ft = new FormatTIme(hour, minute);
                showUpdateTimeDialogButton.setText(ft.getTime());

            }

        }, MainActivity.this.hour, MainActivity.this.minute, false);

        timePickerDialog.show();

    }

    private void updateButtonActions(){

        String str = updateEditText.getText().toString();
        if(str.equals("")){
            Toast.makeText(MainActivity.this, "No Text Entered!", Toast.LENGTH_SHORT).show();
        }
        else {
            if(showUpdateDateDialogButton.getText().equals("Set date")){
                Toast.makeText(MainActivity.this, "Enter date", Toast.LENGTH_SHORT).show();
            }
            else{
                if(showUpdateTimeDialogButton.getText().equals("Set time")){
                    Toast.makeText(MainActivity.this, "Enter Time", Toast.LENGTH_SHORT).show();
                }
                else{
                    FormatTIme ft = new FormatTIme(hour, minute);

                    //database entry;
                    MySqliteOpenHelper mySqliteOpenHelper = new MySqliteOpenHelper(MainActivity.this);
                    SQLiteDatabase db = mySqliteOpenHelper.getWritableDatabase();

                    String whereClause = ReminderTable.ID + " = " + beanArrayList.get(positionTapped).getId();

                    ContentValues cv = new ContentValues();
                    cv.put(ReminderTable.TEXT, str);
                    cv.put(ReminderTable.DAY, day);
                    cv.put(ReminderTable.MONTH, month);
                    cv.put(ReminderTable.YEAR, year);
                    cv.put(ReminderTable.HOUR, hour);
                    cv.put(ReminderTable.MINUTE, minute);

                    int i = ReminderTable.update(db, cv, whereClause);
                    updateAlarm(positionTapped+1);
                    if (i>0) {
                        Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Unable to update", Toast.LENGTH_SHORT).show();
                    }
                    db.close();
                    updateEditText.setText("");
                    showUpdateDateDialogButton.setText("Set date");
                    showUpdateTimeDialogButton.setText("Set time");
                    dialog_update.dismiss();
                }
            }

        }

        fetchFromDatabase();

    }

    private void deleteEntry() {

        String whereClause = ""+ReminderTable.ID+" = "+beanArrayList.get(positionTapped).getId();

        MySqliteOpenHelper sqliteOpenHelper = new MySqliteOpenHelper(MainActivity.this);
        SQLiteDatabase db = sqliteOpenHelper.getWritableDatabase();

        int i = ReminderTable.delete(db, whereClause);
        //newAdapter.notifyDataSetChanged();
        if(i>0){
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Unable to delete!", Toast.LENGTH_SHORT).show();
        }
        fetchFromDatabase();

    }

    private void updateEntry(){
        MySqliteOpenHelper sqliteOpenHelper = new MySqliteOpenHelper(this);
        SQLiteDatabase db = sqliteOpenHelper.getWritableDatabase();

        EntryBean bean = beanArrayList.get(positionTapped);
        int id = bean.getId();

        String selection = ReminderTable.ID+" = "+ id;

        Cursor cursor = ReminderTable.select(db, selection);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id_ = cursor.getInt(0);
                String text = cursor.getString(1);
                int day = cursor.getInt(2);
                int month = cursor.getInt(3);
                int year = cursor.getInt(4);
                int hour = cursor.getInt(5);
                int minute = cursor.getInt(6);

                MainActivity.this.day = day;
                MainActivity.this.month = month;
                MainActivity.this.year = year;
                MainActivity.this.hour = hour;
                MainActivity.this.minute = minute;

                FormatTIme ft = new FormatTIme(hour, minute);

                updateEditText.setText(text);
                showUpdateDateDialogButton.setText(day+"/"+((int)(month+1))+"/"+year);
                showUpdateTimeDialogButton.setText(ft.getTime());

            }
            cursor.close();
            db.close();
        }
    }

    private void setAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent i = new Intent();
        i.setAction("app.alarm");

        int pdID = alarmID+1;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, pdID, i, 0);

        long currentTime = System.currentTimeMillis();
        // setting date on calender
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);

        long mili = calendar.getTimeInMillis();
        long diff = mili - currentTime;

        alarmManager.set(AlarmManager.RTC, currentTime + diff, pendingIntent);
        Toast.makeText(this, "Reminder set"+diff+ " "+pdID, Toast.LENGTH_SHORT).show();

    }

    private void updateAlarm(int pdID) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent i = new Intent();
        i.setAction("app.alarm");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, pdID, i, 0);

        long currentTime = System.currentTimeMillis();

        // setting date on calender
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);

        long mili = calendar.getTimeInMillis();
        Calendar calendarCurrent = Calendar.getInstance();
        long miliCurrent = calendarCurrent.getTimeInMillis();
        long diff = mili - miliCurrent;

        alarmManager.set(AlarmManager.RTC, currentTime + diff, pendingIntent);
        Toast.makeText(this, "Reminder set"+diff+ " "+pdID, Toast.LENGTH_SHORT).show();

    }
}
