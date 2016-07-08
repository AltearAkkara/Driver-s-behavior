package com.akkaratanapat.altear.driversbehavior;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import au.com.bytecode.opencsv.CSVWriter;

public class LogActivity extends AppCompatActivity {

    DBHelper dbHelper;
    private static String[] COLUMN ={Constant._ID,Constant.COLUMN_DATE,Constant.COLUMN_DATE,Constant.COLUMN_ACC_X
            ,Constant.COLUMN_ACC_Y,Constant.COLUMN_ACC_Z,Constant.COLUMN_GYRO_X,Constant.COLUMN_GYRO_Y,Constant.COLUMN_GYRO_Z
            ,Constant.COLUMN_SPEED,Constant.COLUMN_ANGLE,Constant.COLUMN_EVENT};
    private static String ORDERBY = Constant.COLUMN_DATE + " ASC";
    Button buttonCreateText;
    TextView log;
    EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        dbHelper = new DBHelper(this);

        log = (TextView)findViewById(R.id.textView17);
        buttonCreateText = (Button)findViewById(R.id.ButtonCreateText);
        name = (EditText)findViewById(R.id.editText);
        buttonCreateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportDB(name.getText().toString());
            }
        });

        try{
            Cursor cursor = getAllNote();
            showNote(cursor);
        }
        finally {
            dbHelper.close();
        }
    }

    public Cursor getAllNote(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(Constant.TABLE_NAME,COLUMN,null,null,null,null,ORDERBY);
        return  cursor;
    }

    public void showNote(Cursor cursor){
        StringBuilder stringBuilder = new StringBuilder("LOG :+\n");
        int count = 0;
        while(cursor.moveToNext()){
            count++;
            long id = cursor.getLong(0);
            String date = cursor.getString(1);
            String accX = cursor.getString(3);
            String accY = cursor.getString(4);
            String accZ = cursor.getString(5);
            String gyroX = cursor.getString(6);
            String gyroY = cursor.getString(7);
            String gyroZ = cursor.getString(8);
            String speed = cursor.getString(9);
            String angle = cursor.getString(10);
            String event = cursor.getString(11);

            stringBuilder.append("ID : ").append(id).append("\n").append(date).append("\nAccX : ").append(accX).append("\nAccY : ")
                    .append(accY).append("\nAccZ : ").append(accZ).append("\nGyroX : ").append(gyroX).append("\nGyroY :").append(gyroY).append("\nGyroZ :")
                    .append(gyroZ).append("\nSpeed :").append(speed).append("\nAngle :").append(angle).append("\nEvent :").append(event).append("\n");
        }
        log.setText(count + "\n" + stringBuilder);
    }

    private void writeToFile(String name, String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getBaseContext().openFileOutput(name + ".txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);

            outputStreamWriter.close();
            Log.i("Complete", "File write success");
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    private void exportDB(String name) {

        File dbFile = getDatabasePath("driver.db");
        DBHelper dbhelper = new DBHelper(getApplicationContext());
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, name + ".csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.query(Constant.TABLE_NAME,COLUMN,null,null,null,null,ORDERBY);
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {
                //Which column you want to exprort
                String arrStr[] = {curCSV.getString(0), curCSV.getString(1), curCSV.getString(2), curCSV.getString(3), curCSV.getString(4), curCSV.getString(5),
                        curCSV.getString(6), curCSV.getString(7), curCSV.getString(8), curCSV.getString(9), curCSV.getString(10), curCSV.getString(11)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
            Toast.makeText(getBaseContext(),"ok", Toast.LENGTH_SHORT).show();
        } catch (Exception sqlEx) {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }
}
