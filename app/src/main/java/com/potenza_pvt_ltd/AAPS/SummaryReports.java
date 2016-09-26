package com.potenza_pvt_ltd.AAPS;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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

public class SummaryReports extends AppCompatActivity implements View.OnClickListener ,AdapterView.OnItemClickListener {
    private EditText fromDateEtxt;
    private EditText toDateEtxt;
    ArrayList<Integer> arr;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private String datefrom,dateto;
    private SimpleDateFormat dateFormatter;
    ArrayList<String> num;
    final ArrayList<String> code_operator = new ArrayList<String>();
    final ArrayList<String> email_operator = new ArrayList<String>();
    final ArrayList<String> amt_operator = new ArrayList<String>();
    private Button button;
    Firebase ref;
    private Spinner spinner;
    private Spinner spinner2,spinner3;
    final ArrayList<String> code = new ArrayList<String>();
    ArrayList<String> values = new ArrayList<String>();
    private ArrayList<String> name=new ArrayList<>();
    ProgressBar pb,pb1,pb2,pb3;
    private LinearLayout linear_layout;
    int count=1;
    private String globatime;
    private long globalmillis;
    private long timefrom,timeto;
    private String filename;
    private String time_d;
    ListView listview;
    int p1=0,p2=0,p3=0,p4=0,p5=0,p6=0,p7=0,p8=0;
    private CustomAdapter customAdapter;
    private ArrayList<String> psname=new ArrayList<>();
    private String email,vehicle_type_name,transporter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_reports);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        ref=new Firebase(Constants.FIREBASE_URL);
        findViewsById();
        setDateTimeField();
        for(int i=1;i<=8;i++) {
            psname.add("Parking Slot "+i);
        }
        values.add("All");
        code.add("All");
        name.add("All");
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
                vehicle_type_name = parent.getSelectedItem().toString();
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
        Query queryRef4 = ref.child("users").child("data").orderByChild("Time of Departure").equalTo("");
        queryRef4.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("data data", dataSnapshot.getKey());
                TruckDetailsActivity post = dataSnapshot.getValue(TruckDetailsActivity.class);
                if (post.getAPS().contentEquals("Parking Slot 1")) {
                    p1++;
                } else if (post.getAPS().contentEquals("Parking Slot 2")) {
                    p2++;
                } else if (post.getAPS().contentEquals("Parking Slot 3")) {
                    p3++;
                } else if (post.getAPS().contentEquals("Parking Slot 4")) {
                    p4++;
                } else if (post.getAPS().contentEquals("Parking Slot 5")) {
                    p5++;
                } else if (post.getAPS().contentEquals("Parking Slot 6")) {
                    p6++;
                } else if (post.getAPS().contentEquals("Parking Slot 7")) {
                    p7++;
                } else if (post.getAPS().contentEquals("Parking Slot 8")) {
                    p8++;
                }
                num = new ArrayList<>();
                num.add(String.valueOf(p1));
                num.add(String.valueOf(p2));
                num.add(String.valueOf(p3));
                num.add(String.valueOf(p4));
                num.add(String.valueOf(p5));
                num.add(String.valueOf(p6));
                num.add(String.valueOf(p7));
                num.add(String.valueOf(p8));
                if(psname!=null && num!=null){
                    customAdapter = new CustomAdapter(getApplication(), psname,num,10);
                    customAdapter.notifyDataSetChanged();
                    listview.setAdapter(customAdapter);
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

    private void findViewsById() {
        fromDateEtxt = (EditText) findViewById(R.id.etxt_fromdate);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();
        toDateEtxt = (EditText) findViewById(R.id.etxt_todate);
        toDateEtxt.setInputType(InputType.TYPE_NULL);
        button=(Button)findViewById(R.id.button5);
        spinner=(Spinner)findViewById(R.id.spinner6);
        spinner2=(Spinner)findViewById(R.id.spinner7);
        spinner3=(Spinner)findViewById(R.id.spinner5);
        pb=(ProgressBar)findViewById(R.id.progressBar);
        pb1=(ProgressBar)findViewById(R.id.progressBar1);
        pb2=(ProgressBar)findViewById(R.id.progressBar2);
        pb3=(ProgressBar)findViewById(R.id.progressBar3);
        linear_layout=(LinearLayout)findViewById(R.id.linear_layout);
        listview=(ListView)findViewById(R.id.listView2);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    public void export(View view){
        if(datefrom.isEmpty()==false&& dateto.isEmpty()==false) {
            pb3.setVisibility(View.VISIBLE);
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
                    if (transporter.contentEquals("All") && vehicle_type_name.contentEquals("All") && email.contentEquals("All")) {
                        globatime = post.getDate() + " " + post.gettime();
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss aa"); // I assume d-M, you may refer to M-d for month-day instead.
                        Date date = null; // You will need try/catch around this
                        try {
                            date = formatter.parse(globatime);
                            globalmillis = date.getTime();

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Log.d("globaltime", String.valueOf(globalmillis));
                        if (globalmillis > timefrom && globalmillis < timeto) {
                            email_operator.add(post.getEmail());
                            amt_operator.add(post.getCost());
                        }
                        getdata();
                    } else {
                        if (post.getContractorname().contentEquals(transporter) && post.getEmail().contentEquals(email) && post.getVehicleType().contentEquals(vehicle_type_name)) {
                            globatime = post.getDate() + " " + post.gettime();
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss aa"); // I assume d-M, you may refer to M-d for month-day instead.
                            Date date = null; // You will need try/catch around this
                            try {
                                date = formatter.parse(globatime);
                                globalmillis = date.getTime();

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Log.d("globaltime", String.valueOf(globalmillis));
                            if (globalmillis > timefrom && globalmillis < timeto) {
                                email_operator.add(post.getEmail());
                                amt_operator.add(post.getCost());
                            }
                        }
                        getdata();
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
        else{
            Toast.makeText(getApplicationContext(),"Pease enter the date",Toast.LENGTH_LONG);
        }

    }



    public void sendEmailWithAttachment(String to, String subject, String message, String fileAndLocation) {
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
        Intent i = new Intent(SummaryReports.this, ReportsActivity.class);
        startActivity(i);
    }

}