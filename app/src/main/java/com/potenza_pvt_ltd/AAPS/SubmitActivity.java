package com.potenza_pvt_ltd.AAPS;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.potenza_pvt_ltd.AAPS.function.PocketPos;
import com.potenza_pvt_ltd.AAPS.util.FontDefine;
import com.potenza_pvt_ltd.AAPS.util.Printer;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


public class SubmitActivity extends Activity implements AdapterView.OnItemSelectedListener{
    protected EditText driverno;
    protected EditText vehicleno;
    protected TextView date;
    protected Button submitButton;
    private Firebase mRef;
    String transpoter_name,vehicle_type;
    private String typeofuser;
    private String email_operator;
    TextView myLabel;

    // will enable user to enter any text to be printed

    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    private String transporter;
    private String drvrno,vhclno,final_date;
    private String localTime;
    String header1,header2,header3,header4,header5,footer1,footer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        Button openButton = (Button) findViewById(R.id.open);
        myLabel = (TextView) findViewById(R.id.label);
        Button closeButton = (Button) findViewById(R.id.close);
        Firebase ref=new Firebase(Constants.FIREBASE_URL);
        Query query=ref.child("users").child("Slip_Details");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                SlipDetail post = snapshot.getValue(SlipDetail.class);
                Log.d("fppter",post.getFooter1());
                header1=post.getHeader1();
                header2=post.getHeader2();
                header3=post.getHeader3();
                header4=post.getHeader4();
                header5=post.getHeader5();
                footer1=post.getFooter1();
                footer2=post.getFooter2();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        // open bluetooth connection
        openButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    findBT();
                    openBT();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    closeBT();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        typeofuser=preferences.getString("typeofuser",null);
        email_operator=preferences.getString("email",null);
        Button logout=(Button)findViewById(R.id.button_logout);
        driverno= (EditText) findViewById(R.id.editText3);
        vehicleno = (EditText) findViewById(R.id.editText4);
        date= (TextView) findViewById(R.id.textView_date1);
        submitButton= (Button) findViewById(R.id.button_submit);
        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        spinner.setOnItemSelectedListener(this);
        List<String> vcl_typ = new ArrayList<String>();
        vcl_typ.add("Light");
        vcl_typ.add("Heavy");
        vcl_typ.add("asdhha");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, vcl_typ);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        Spinner spinner_transporter = (Spinner) findViewById(R.id.spinner3);
        spinner_transporter.setOnItemSelectedListener(this);
        List<String> trnsptr_typ = new ArrayList<String>();
        trnsptr_typ.add("abc");
        trnsptr_typ.add("aadljfhajkdshj");
        trnsptr_typ.add("asdhha");
        // Creating adapter for spinner_transporter
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, trnsptr_typ);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner_transporter
        spinner_transporter.setAdapter(dataAdapter1);
        final Calendar c = Calendar.getInstance();
        int yy = c.get(Calendar.YEAR);
        int mm = c.get(Calendar.MONTH);
        int dd = c.get(Calendar.DAY_OF_MONTH);

        date.setText(new StringBuilder().append(dd).append("/").append(mm+1).append("/").append(yy));
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        localTime = sdf.format(calendar.getTime());
        Log.d("localtime",localTime);
        // Check Authentication
        mRef = new Firebase(Constants.FIREBASE_URL);
        if (mRef.getAuth() == null) {
            loadLoginView();
        }

        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                transporter = transpoter_name;
                drvrno = driverno.getText().toString();
                vhclno = vehicleno.getText().toString();
                final_date = date.getText().toString();
                Map<String, Object> graceNickname = new HashMap<>();
                graceNickname.put("Transporter", transporter);
                graceNickname.put("Driver Number", drvrno);
                graceNickname.put("Vehicle Number", vhclno);
                graceNickname.put("Date", final_date);
                graceNickname.put("Vehicle Type", vehicle_type);
                graceNickname.put("Time of Arrival",localTime);
                //graceNickname.put("Cost", 200);
                graceNickname.put("email", email_operator);
                graceNickname.put(typeofuser, "true");
                Firebase newpostref = mRef.child("users").child("data").push();
                newpostref.setValue(graceNickname);
                editor.putString("UserID for data", newpostref.getKey());
                AlertDialog.Builder builder = new AlertDialog.Builder(SubmitActivity.this);
                builder.setMessage("You have successfully uploaded the details!")
                        .setTitle(R.string.submit_title)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
                driverno.setText("");
                vehicleno.setText("");
                try {
                    sendData();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
            myLabel.setText("Bluetooth Closed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // this will send text data to be printed by the bluetooth printer
    void sendData() throws IOException{
        try {

            // the text typed by the user
            String titleStr	= "Receipt " + "\n\n";

            StringBuilder contentSb	= new StringBuilder();
            contentSb.append("\n"+header1+ "\n");
            contentSb.append(header2+ "\n");
            contentSb.append(header3+ "\n");
            contentSb.append(header4+ "\n");
            contentSb.append(header5+ "\n");

            contentSb.append("\nVEHICLE NUMBER     :"+ vhclno+ "\n");
            contentSb.append("TRANSPORTER NAME      : "+transporter + "\n");
            contentSb.append("VEHICLE TYPE :"+vehicle_type + "\n");
            contentSb.append("Driver Number : " +drvrno+ "\n");
            contentSb.append("Date : "+final_date + "\n");
            contentSb.append("Time of Arrival : "+localTime + "\n");
            contentSb.append("Cost :" +200+ "\n");
            contentSb.append("Email :" +email_operator+ "\n");
            contentSb.append("\n\n\n\n"+footer1+"\n");
            contentSb.append(footer2+"\n");


            //String jpaRef	= footer1+ "\n";
            //String message	= footer2+ "\n";


            byte[] titleByte	= Printer.printfont(titleStr, FontDefine.FONT_64PX, FontDefine.Align_CENTER,
                    (byte) 0x1A, PocketPos.LANGUAGE_ENGLISH);

            byte[] content1Byte	= Printer.printfont(contentSb.toString(), FontDefine.FONT_48PX,FontDefine.Align_LEFT,
                    (byte)0x1A, PocketPos.LANGUAGE_ENGLISH);

            /*byte[] refByte		= Printer.printfont(jpaRef, FontDefine.FONT_24PX,FontDefine.Align_LEFT,  (byte)0x1A,
                    PocketPos.LANGUAGE_ENGLISH);

            byte[] messageByte	= Printer.printfont(message, FontDefine.FONT_24PX,FontDefine.Align_LEFT,  (byte)0x1A,
                    PocketPos.LANGUAGE_ENGLISH);
*/
            //byte[] dateByte		= Printer.printfont(date, FontDefine.FONT_48PX,FontDefine.Align_LEFT, (byte)0x1A,
             //       PocketPos.LANGUAGE_ENGLISH);

            byte[] totalByte	= new byte[titleByte.length + content1Byte.length/*+refByte.length + messageByte.length*/ ];

            Log.d("Send", Arrays.toString(totalByte));

            int offset = 0;
            System.arraycopy(titleByte, 0, totalByte, offset, titleByte.length);
            offset += titleByte.length;

            System.arraycopy(content1Byte, 0, totalByte, offset, content1Byte.length);
            offset += content1Byte.length;
/*
            System.arraycopy(refByte, 0, totalByte, offset, refByte.length);
            offset += refByte.length;

            System.arraycopy(messageByte, 0, totalByte, offset, messageByte.length);
            offset += messageByte.length;
            */
            //System.arraycopy(dateByte, 0, totalByte, offset, dateByte.length);

            byte[] senddata = PocketPos.FramePack(PocketPos.FRAME_TOF_PRINT, totalByte, 0, totalByte.length);
            Log.d("Send", Arrays.toString(senddata));

            mmOutputStream.write(senddata);

            // tell the user data were sent
            myLabel.setText("Data sent.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if(mBluetoothAdapter == null) {
                myLabel.setText("No bluetooth adapter available");
            }

            if(!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if(pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    if (device.getName().equals("silbt-010")||device.getName().equals("silbt-009")) {
                        mmDevice = device;
                        break;
                    }
                }
            }

            myLabel.setText("Bluetooth device found.");

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    // tries to open a connection to the bluetooth printer device
    void openBT() throws IOException {
        try {

            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();

            myLabel.setText("Bluetooth Opened");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
 * after opening a connection to bluetooth printer device,
 * we have to listen and check if a data were sent to be printed.
 */
    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
                                                myLabel.setText(data);
                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner3 = (Spinner) parent;
        if(spinner3.getId() == R.id.spinner2)
        {
            vehicle_type=parent.getItemAtPosition(position).toString();
        }
        else if(spinner3.getId() == R.id.spinner3)
        {
            transpoter_name=parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    public void onBackPressed()
    {
        finish();   //finishes the current activity and doesnt save in stock
        Intent i = new Intent(SubmitActivity.this, TypeofOperator.class);
        startActivity(i);
    }
}
