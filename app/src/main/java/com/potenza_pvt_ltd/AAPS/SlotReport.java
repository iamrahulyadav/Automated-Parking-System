package com.potenza_pvt_ltd.AAPS;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.potenza_pvt_ltd.AAPS.R;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SlotReport extends Activity implements View.OnClickListener{
    private EditText fromDateEtxt;
    private EditText toDateEtxt;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private String datefrom,dateto;
    private SimpleDateFormat dateFormatter;
    Firebase ref;
    final ArrayList<String> date_array = new ArrayList<String>();
    final ArrayList<String> time_array = new ArrayList<String>();
    final ArrayList<String> vehicle_no = new ArrayList<String>();
    final ArrayList<String> vehicle_type = new ArrayList<String>();
    final ArrayList<String> email_operator = new ArrayList<String>();
    final ArrayList<String> amt_operator = new ArrayList<String>();
    final ArrayList<String> contractor_name = new ArrayList<String>();
    String transporter;
    List<String> categories = new ArrayList<>();
    ProgressBar pb,pb1,pb2,pb3;
    private LinearLayout linear_layout;
    private Spinner spinner;
    private Spinner spinner2,spinner3;
    final ArrayList<String> code = new ArrayList<String>();
    ArrayList<String> values = new ArrayList<String>();
    private ArrayList<String> name=new ArrayList<>();
    private Button button;
    int count=1;
    private String globatime;
    private long globalmillis;
    private long timefrom,timeto;
    private String filename;
    private String email,vehicle_type_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_report);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        ref=new Firebase(Constants.FIREBASE_URL);
        name.add("All");
        values.add("All");
        code.add("All");
        findViewsById();
        setDateTimeField();
        Query queryRef = ref.child("users").child("Operator");
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("value of", String.valueOf(dataSnapshot.getKey()));
                DetailofUser post = dataSnapshot.getValue(DetailofUser.class);
                Log.d("email", post.getEmail());
                Log.d("pass", post.getPwd());
                values.add(post.getEmail());
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, values);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(dataAdapter);
                pb.setVisibility(View.GONE);
                if(pb1.getVisibility()==View.VISIBLE && pb2.getVisibility()==View.VISIBLE) {
                    linear_layout.setVisibility(View.VISIBLE);
                }

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
        Query queryRef1 = ref.child("users").child("Vehicle_Type");
        queryRef1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("value of", String.valueOf(dataSnapshot.getKey()));
                VehicleTypeDetails post = dataSnapshot.getValue(VehicleTypeDetails.class);
                code.add(post.getVehicle_type());
                ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, code);
                dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter1);
                pb1.setVisibility(View.GONE);
                if(pb.getVisibility()==View.VISIBLE && pb2.getVisibility()==View.VISIBLE) {
                    linear_layout.setVisibility(View.VISIBLE);
                }

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
        Query queryRef3 = ref.child("users").child("Transporter_Details");
        queryRef3.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("value of", String.valueOf(dataSnapshot.getKey()));
                TransporterDetails post = dataSnapshot.getValue(TransporterDetails.class);
                name.add(post.getName());
                ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, name);
                dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner3.setAdapter(dataAdapter3);
                pb2.setVisibility(View.GONE);
                if(pb1.getVisibility()==View.VISIBLE && pb.getVisibility()==View.VISIBLE) {
                    linear_layout.setVisibility(View.VISIBLE);
                }

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
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                email = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                vehicle_type_name=parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                transporter = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void findViewsById() {
        fromDateEtxt = (EditText) findViewById(R.id.etxt_fromdate);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();
        button=(Button)findViewById(R.id.button5);
        spinner=(Spinner)findViewById(R.id.spinner6);
        spinner2=(Spinner)findViewById(R.id.spinner7);
        spinner3=(Spinner)findViewById(R.id.spinner5);
        toDateEtxt = (EditText) findViewById(R.id.etxt_todate);
        toDateEtxt.setInputType(InputType.TYPE_NULL);
        pb=(ProgressBar)findViewById(R.id.progressBar);
        pb1=(ProgressBar)findViewById(R.id.progressBar1);
        pb2=(ProgressBar)findViewById(R.id.progressBar2);
        pb3=(ProgressBar)findViewById(R.id.progressBar3);
        linear_layout=(LinearLayout)findViewById(R.id.linear_layout);
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
    public void export(View view){
        if(toDateEtxt.getText().toString()!=null&& fromDateEtxt.getText().toString()!=null) {
            pb3.setVisibility(View.VISIBLE);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            if(datefrom!=null && dateto!=null) {
                String timeofarrival = datefrom + " 00:00:01";
                Date date = null; // You will need try/catch around this
                try {
                    date = sdf.parse(timeofarrival);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                timefrom = date.getTime();
                String timetocal = dateto + " 23:59:59";
                Date date1 = null; // You will need try/catch around this
                try {
                    date1 = sdf.parse(timetocal);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                timeto = date1.getTime();
                Log.d("timefrom", String.valueOf(timefrom));
                Log.d("timeto", String.valueOf(timeto));
                Query queryRef = ref.child("users").child("data");
                queryRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d("count", String.valueOf(dataSnapshot.getChildrenCount()));
                        Log.d("key", dataSnapshot.getKey());
                        TruckDetailsActivity post = dataSnapshot.getValue(TruckDetailsActivity.class);
                        Log.d("Contractor name", post.getContractorname());
                        Log.d("transporter", transporter);
                        if (transporter.contentEquals("All") && vehicle_type_name.contentEquals("All") && email.contentEquals("All")) {
                            globatime = post.getDate() + " " + post.gettime();
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa"); // I assume d-M, you may refer to M-d for month-day instead.
                            Date d = null; // You will need try/catch around this
                            try {
                                d = formatter.parse(globatime);
                                globalmillis = d.getTime();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Log.d("globaltime", String.valueOf(globalmillis) + " " + String.valueOf(globatime));
                            if (globalmillis > timefrom && globalmillis < timeto) {
                                DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                                DateFormat time = new SimpleDateFormat("hh:mm:ss");
                                date_array.add(date.format(d));
                                time_array.add(time.format(d));
                                email_operator.add(post.getEmail());
                                amt_operator.add(post.getCost());
                                vehicle_no.add(post.getVehicleno());
                                Log.d("abx", post.getEmail());
                                vehicle_type.add(post.getVehicleno());
                                contractor_name.add(post.getContractorname());
                            }
                            getdata();
                        } else {
                            if (post.getContractorname().contentEquals(transporter) && post.getEmail().contentEquals(email) && post.getVehicleType().contentEquals(vehicle_type_name)) {
                                globatime = post.getDate() + " " + post.gettime();
                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa"); // I assume d-M, you may refer to M-d for month-day instead.
                                Date d = null; // You will need try/catch around this
                                try {
                                    d = formatter.parse(globatime);
                                    globalmillis = d.getTime();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Log.d("globaltime", String.valueOf(globalmillis));
                                if (globalmillis > timefrom && globalmillis < timeto) {
                                    DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                                    DateFormat time = new SimpleDateFormat("hh:mm:ss");
                                    date_array.add(date.format(d));
                                    time_array.add(time.format(d));
                                    email_operator.add(post.getEmail());
                                    amt_operator.add(post.getCost());
                                    vehicle_no.add(post.getVehicleno());
                                    Log.d("abx", post.getEmail());
                                    vehicle_type.add(post.getVehicleno());
                                    contractor_name.add(post.getContractorname());
                                }
                                getdata();
                            }
                        }


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
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "Please enter the date", Toast.LENGTH_LONG);
        }

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
        c.setCellValue("Email");
        c.setCellStyle(cs);

        c = row2.createCell(4);
        c.setCellValue("Veh No");
        c.setCellStyle(cs);

        c = row2.createCell(5);
        c.setCellValue("Entry Date");
        c.setCellStyle(cs);

        c = row2.createCell(6);
        c.setCellValue("Entry Time");
        c.setCellStyle(cs);

        c = row2.createCell(7);
        c.setCellValue("Vehicle Type");
        c.setCellStyle(cs);

        c = row2.createCell(8);
        c.setCellValue("Transporter");
        c.setCellStyle(cs);

        c = row2.createCell(9);
        c.setCellValue("Amount");
        c.setCellStyle(cs);


        for(int i=0;i<email_operator.size();i++){
            Log.d(String.valueOf(i),email_operator.get(i));
            Row row3 = sheet1.createRow(k);
            c = row3.createCell(2);
            c.setCellValue(count);
            c.setCellStyle(cs);
            count++;

            c = row3.createCell(3);
            c.setCellValue(email_operator.get(i));
            c.setCellStyle(cs);

            c = row3.createCell(4);
            c.setCellValue(vehicle_no.get(i));
            c.setCellStyle(cs);

            c = row3.createCell(5);
            c.setCellValue(date_array.get(i));
            c.setCellStyle(cs);

            c = row3.createCell(6);
            c.setCellValue(time_array.get(i));
            c.setCellStyle(cs);

            c = row3.createCell(7);
            c.setCellValue(vehicle_type.get(i));
            c.setCellStyle(cs);

            c = row3.createCell(8);
            c.setCellValue(contractor_name.get(i));
            c.setCellStyle(cs);

            c = row3.createCell(9);
            c.setCellValue(amt_operator.get(i));
            c.setCellStyle(cs);
            k++;

        }

        sheet1.setColumnWidth(0, (15 * 500));
        sheet1.setColumnWidth(1, (15 * 500));
        sheet1.setColumnWidth(2, (15 * 500));

        // Create a path where we will place our List of objects on external storage
        File file = new File(this.getExternalFilesDir(null), "Slot-Reports.xls");
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
        pb3.setVisibility(View.GONE);
        sendEmailWithAttachment(Constants.EMAIL_TO, "", "", filename);
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
        finish();
        Intent i = new Intent(SlotReport.this, ReportsActivity.class);
        startActivity(i);
    }
}
