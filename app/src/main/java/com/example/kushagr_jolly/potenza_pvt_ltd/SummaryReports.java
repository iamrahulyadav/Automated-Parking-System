package com.example.kushagr_jolly.potenza_pvt_ltd;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SummaryReports extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    private EditText fromDateEtxt;
    private EditText toDateEtxt;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private String datefrom,dateto;
    private SimpleDateFormat dateFormatter;
    Firebase ref;
    private Spinner spinner;
    private Spinner spinner2;
    final ArrayList<String> code_operator = new ArrayList<String>();
    final ArrayList<String> email_operator = new ArrayList<String>();
    final ArrayList<String> amt_operator = new ArrayList<String>();
    private Button button;
    int count=1;
    private String globatime;
    private long globalmillis;
    private long timefrom,timeto;
    private String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_reports);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        ref=new Firebase(Constants.FIREBASE_URL);
        findViewsById();

        setDateTimeField();
        getVehicleType();
        getOperator();

    }

    private void getOperator() {
        spinner2.setOnItemSelectedListener(this);
        final ArrayList<String> values = new ArrayList<String>();
        Query queryRef = ref.child("users").child("Operator");
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("value of", String.valueOf(dataSnapshot.getKey()));
                DetailofUser post = dataSnapshot.getValue(DetailofUser.class);
                Log.d("email", post.getEmail());
                Log.d("pass", post.getPwd());
                values.add(post.getEmail());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
    }

    private void getVehicleType() {
        spinner.setOnItemSelectedListener(this);
        final ArrayList<String> code = new ArrayList<String>();
        Query queryRef = ref.child("users").child("Vehicle_Type");
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("value of", String.valueOf(dataSnapshot.getKey()));
                VehicleTypeDetails post = dataSnapshot.getValue(VehicleTypeDetails.class);
                code.add(post.getVehicle_type());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, code);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);


    }

    private void findViewsById() {
        fromDateEtxt = (EditText) findViewById(R.id.etxt_fromdate);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();
        spinner=(Spinner)findViewById(R.id.spinner6);
        spinner2=(Spinner)findViewById(R.id.spinner7);
        button=(Button)findViewById(R.id.button5);
        toDateEtxt = (EditText) findViewById(R.id.etxt_todate);
        toDateEtxt.setInputType(InputType.TYPE_NULL);
    }

    private void setDateTimeField() {
        fromDateEtxt.setOnClickListener(this);
        toDateEtxt.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
                datefrom=dateFormatter.format(newDate.getTime());
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
                dateto=dateFormatter.format(newDate.getTime());
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View v) {
        if(v == fromDateEtxt) {
            fromDatePickerDialog.show();
        } else if(v == toDateEtxt) {
            toDatePickerDialog.show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void getdata(){
        int k=9;
        // check if available and not read only
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.w("FileUtils", "Storage not available or read only");
        }

        //New Workbook
        Workbook wb = new HSSFWorkbook();

        Cell c = null;

        //Cell style for header row
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.LIME.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        //New Sheet
        Sheet sheet1 = null;
        sheet1 = wb.createSheet("myReport");

        // Generate column headings
        Row row = sheet1.createRow(6);

        c = row.createCell(2);
        c.setCellValue("Date:");
        c.setCellStyle(cs);

        c = row.createCell(3);
        c.setCellValue(datefrom);
        c.setCellStyle(cs);

        c = row.createCell(4);
        c.setCellValue("to");
        c.setCellStyle(cs);

        c = row.createCell(5);
        c.setCellValue(dateto);
        c.setCellStyle(cs);

        Row row1 = sheet1.createRow(7);

        c = row1.createCell(2);
        c.setCellValue("Time:");
        c.setCellStyle(cs);

        c = row1.createCell(3);
        c.setCellValue("12:00 AM");
        c.setCellStyle(cs);

        c = row1.createCell(4);
        c.setCellValue("to");
        c.setCellStyle(cs);

        c = row1.createCell(5);
        c.setCellValue("11:59 PM");
        c.setCellStyle(cs);

        Row row2 = sheet1.createRow(8);

        c = row2.createCell(3);
        c.setCellValue("Code");
        c.setCellStyle(cs);

        c = row2.createCell(4);
        c.setCellValue("Email");
        c.setCellStyle(cs);

        c = row2.createCell(5);
        c.setCellValue("Amount");
        c.setCellStyle(cs);
        for(int i=0;i<email_operator.size();i++){
            Row row3 = sheet1.createRow(k);

            c = row3.createCell(3);
            c.setCellValue(count);
            c.setCellStyle(cs);
            count++;

            c = row3.createCell(4);
            c.setCellValue(email_operator.get(i));
            c.setCellStyle(cs);

            c = row3.createCell(5);
            c.setCellValue(amt_operator.get(i));
            c.setCellStyle(cs);
            k++;

        }


        sheet1.setColumnWidth(0, (15 * 500));
        sheet1.setColumnWidth(1, (15 * 500));
        sheet1.setColumnWidth(2, (15 * 500));

        // Create a path where we will place our List of objects on external storage
        File file = new File(this.getExternalFilesDir(null), "Summary-Reports.xls");
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
            Log.w("FileUtils", "Writing file" + file);
            filename= "/"+String.valueOf(file);

        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }

    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void export(View view){
        if(datefrom.isEmpty()==false&& dateto.isEmpty()==false) {
            new FetchData(this).execute();
        }
        else{
            Toast.makeText(getApplicationContext(),"Pease enter the date",Toast.LENGTH_LONG);
        }

    }

    public static Set<String> findDuplicates(List<String> listContainingDuplicates) {

        final Set<String> setToReturn = new HashSet<String>();
        final Set<String> set1 = new HashSet<String>();

        for (String yourInt : listContainingDuplicates) {
            if (!set1.add(yourInt)) {
                setToReturn.add(yourInt);
            }
        }
        return setToReturn;
    }

    class FetchData extends AsyncTask<Context,String,String> {
        Activity mActivity;
        public FetchData (Activity activity)
        {
            super();
            mActivity = activity;
        }
        @Override
        protected String doInBackground(Context... params) {
            Log.d("Hello","Hello");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeofarrival = datefrom+" 00:00:01";
            Date date = null; // You will need try/catch around this
            try {
                date = sdf.parse(timeofarrival);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            timefrom=date.getTime();
            String timetocal = dateto+" 23:59:59";
            Date date1 = null; // You will need try/catch around this
            try {
                date1 = sdf.parse(timetocal);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            timeto=date1.getTime();
            Log.d("timefrom", String.valueOf(timefrom));
            Log.d("timeto", String.valueOf(timeto));
            Query queryRef = ref.child("users").child("data");
            queryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.d("asda", String.valueOf(dataSnapshot.getChildrenCount()));
                    TruckDetailsActivity post = dataSnapshot.getValue(TruckDetailsActivity.class);
                    //post.setKey(dataSnapshot.getKey());
                    globatime=post.gettime();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // I assume d-M, you may refer to M-d for month-day instead.
                    Date date = null; // You will need try/catch around this
                    try {
                        date = formatter.parse(globatime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    globalmillis= date.getTime();
                    Log.d("globaltime", String.valueOf(globalmillis));
                    if(globalmillis>timefrom && globalmillis<timeto){
                        email_operator.add(post.getEmail());
                        amt_operator.add(post.getCost());
                    }
                /*Set<String> set2 = findDuplicates(email_operator);
                for(int i=0;i<email_operator.size();i++){
                    if(email_operator.get(i).contentEquals(set2.iterator().next().toString())){
                        amt_operator.add(i+1,amt_operator.get(i));
                        email_operator.remove(i);
                        amt_operator.remove(i);
                    }
                }*/
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            getdata();
            sendEmailWithAttachment(Constants.EMAIL_TO, "", "", filename);
            return null;
        }

        @Override
        protected void onPreExecute(){

        }


        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }
    public void sendEmailWithAttachment(String to, String subject, String message, String fileAndLocation)
    {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("application/excel");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{to});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,  subject);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,  message);
        File file = new File(fileAndLocation);
        //  File file = getFileStreamPath();
        if (file.exists())
        {
            Log.v("Farmgraze", "Email file_exists!" );
        }
        else
        {
            Log.v("Farmgraze", "Email file does not exist!" );
        }
        Log.v("FarmGraze", "SEND EMAIL FileUri=" + Uri.parse("file:/" + fileAndLocation));
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:/"+  fileAndLocation));
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }
    @Override
    public void onBackPressed()
    {
        finish();   //finishes the current activity and doesnt save in stock
        Intent i = new Intent(SummaryReports.this, ReportsActivity.class);
        startActivity(i);
    }
}
