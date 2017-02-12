package com.potenza_pvt_ltd.AAPS;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.potenza_pvt_ltd.AAPS.function.PocketPos;
import com.potenza_pvt_ltd.AAPS.util.FontDefine;
import com.potenza_pvt_ltd.AAPS.util.Printer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ExitReceipt extends AppCompatActivity {
    EditText et_search;
    ImageButton search;
    List<ArrayList<String>> tararr;
    private String[][] tar_arr1;
    Button b;
    String postid;
    private FirebaseAuth mAuth;
    DatabaseReference reference;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String userkey;
    TruckDetailsActivity truck;
    private int[][] arr;
    private String vhlno;
    private String localtime;
    private String truck_type;
    private int hours;
    private long cost;
    int partial=0;
    TextView tv1;
    private String header1,header2,header3,header4,header5,footer1,footer2;
    private Button openButton;
    private Button closeButton;
    private String vhclno,transporter,vehicle_type,localTime,email_operator,drvrno,aps;
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
    private TextView myLabel;
    private String localTime1;
    private String mon_pass;
    private String transporter_key;
    private String amt;
    private ProgressBar pb1,pb,pb2;
    LinearLayout linearLayout;
    private int flag=0;
    private String departure_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit_receipt);
        postid=getIntent().getStringExtra("UniqueID");
        et_search=(EditText)findViewById(R.id.editText_search);
        search=(ImageButton)findViewById(R.id.imageButton);
        tv1=(TextView)findViewById(R.id.textView46);
        b=(Button)findViewById(R.id.button8);
        Button openButton = (Button) findViewById(R.id.open);
        myLabel = (TextView) findViewById(R.id.label);
        pb=(ProgressBar)findViewById(R.id.progressBar);
        pb1=(ProgressBar)findViewById(R.id.progressBar1);
        pb2=(ProgressBar)findViewById(R.id.progressBar2);
        linearLayout=(LinearLayout)findViewById(R.id.linear_layout);
        Button closeButton = (Button) findViewById(R.id.close);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("User", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("User", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        Query query=reference.child("users").child("Slip_Details");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                SlipDetail post = snapshot.getValue(SlipDetail.class);
                Log.d("fppter", post.getFooter1());
                header1 = post.getHeader1();
                header2 = post.getHeader2();
                header3 = post.getHeader3();
                header4 = post.getHeader4();
                header5 = post.getHeader5();
                footer1 = post.getFooter1();
                footer2 = post.getFooter2();
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                pb1.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                vhlno = et_search.getText().toString();
                Log.d("vehicle", vhlno);
                // Attach an listener to read the data at our posts reference
                Query queryRef1 = reference.child("users").child("data").orderByChild("vno").equalTo(vhlno);
                queryRef1.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                        userkey = snapshot.getKey();
                        Log.d("userkey",userkey);
                        TruckDetailsActivity truck = snapshot.getValue(TruckDetailsActivity.class);
                        partial= truck.getPap();
                        localtime = truck.getToa();
                        localTime=truck.getToa();
                        vehicle_type=truck.getVtype();
                        departure_time=truck.getTod();
                        vhclno=truck.getVno();
                        transporter=truck.getTransporter();
                        email_operator=truck.getEmail();
                        drvrno=truck.getDno();
                        aps=truck.getAps();
                        truck_type = truck.getVtype();
                        Log.d("trucktype", truck_type);
                        array(truck_type);
                        pb.setVisibility(View.GONE);
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
                    public void onCancelled(DatabaseError firebaseError) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ExitReceipt.this);
                        builder.setMessage(firebaseError.getMessage())
                                .setTitle(R.string.login_error_title)
                                .setPositiveButton(android.R.string.ok, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
                if(userkey==null){
                    pb.setVisibility(View.GONE);
                    pb1.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (departure_time.contentEquals("")) {
                    pb2.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    Log.d("Cost", String.valueOf(cost));
                    if (cost != 0) {
                        try {
                            if (vehicle_type.contentEquals("Trailer with Monthly Pass")) {
                                //Search for transporter Name using a new Query and then update the Amt with that userkey
                                final Query queryRef1 = reference.child("users").child("Transporter_Details").orderByChild("name").equalTo(transporter);
                                queryRef1.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        transporter_key = String.valueOf(dataSnapshot.getKey());
                                        TransporterDetails transporterDetails = dataSnapshot.getValue(TransporterDetails.class);
                                        amt = transporterDetails.getAmt();
                                        int amount = (int) (Integer.parseInt(amt) - cost);
                                        Map<String, Object> map = new HashMap<String, Object>();
                                        map.put("Amt", String.valueOf(amount));
                                        reference.child("users").child("Transporter_Details").child(transporter_key).updateChildren(map);
                                        pb2.setVisibility(View.GONE);
                                        linearLayout.setVisibility(View.VISIBLE);
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
                                    public void onCancelled(DatabaseError firebaseError) {

                                    }
                                });
                            } else {
                                pb2.setVisibility(View.GONE);
                                linearLayout.setVisibility(View.VISIBLE);
                            }
                            Calendar calendar = Calendar.getInstance();
                            final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
                            localTime1 = sdf.format(calendar.getTime());
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("cost", String.valueOf(cost));
                            map.put("tod", localTime1);
                            Log.d("user", userkey);
                            reference.child("users").child("data").child(userkey).updateChildren(map);
                            AlertDialog.Builder builder = new AlertDialog.Builder(ExitReceipt.this);
                            builder.setMessage("The data has been updated.")
                                    .setTitle("Message")
                                    .setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            sendData(String.valueOf(cost));
                            et_search.setText("");
                            tv1.setText("");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ExitReceipt.this);
                    builder.setMessage("The Vehicle is already left the Parking Lot.")
                            .setTitle("Message")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
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
    }

    private void array(String type) {
        Log.d("checkpoint","in array function");
            Query queryRef = reference.child("users").child("Tariff_Details").orderByChild("vehicle_type").equalTo(type);
            queryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                    TariffDetails truck = snapshot.getValue(TariffDetails.class);
                    tararr=truck.getArr();
                    Log.d("tararr",tararr.toString());
                    tar_arr1= new String[tararr.size()][];
                    for (int i = 0; i < tararr.size(); i++) {
                        ArrayList<String> row = tararr.get(i);
                        tar_arr1[i] = row.toArray(new String[row.size()]);
                        Log.d("tararr1", String.valueOf(tar_arr1.length));
                    }
                    arr=new int[tar_arr1.length][3];
                    for(int i=0;i<tar_arr1.length;i++){
                        for(int j=0;j<3;j++){
                            Log.d("i,j", String.valueOf(Integer.parseInt(tar_arr1[i][j])));
                            arr[i][j]=Integer.parseInt(tar_arr1[i][j]);
                        }
                    }
                    Log.d("arr", Arrays.deepToString(arr));
                    flag = 1;
                    calculate(localtime);
                    pb1.setVisibility(View.GONE);
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
                public void onCancelled(DatabaseError firebaseError) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ExitReceipt.this);
                    builder.setMessage(firebaseError.getMessage())
                            .setTitle(R.string.login_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
            if (flag == 1) {
                pb1.setVisibility(View.GONE);
            }
    }


    private void calculate(String localtime) {
        Date date1=null,date2 = null;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        String current = dateFormat.format(calendar.getTime());
        try {
            date1 = dateFormat.parse(localtime);
            date2 = dateFormat.parse(current);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(date1!=null && date2!=null) {
            long difference = date2.getTime() - date1.getTime();
            int days = (int) (difference / (1000 * 60 * 60 * 24));
            hours = (int) (difference / (60 * 60 * 1000));
            int min = (int) (difference/(60 * 1000) % 60);
            int sec= (int) (difference / 1000 % 60);
            hours = (hours < 0 ? -hours : hours);
            Log.i("======= Hours", " :: " + hours + "::" + min);
        }
        for(int i=0;i<arr.length;i++){
            if(hours>=arr[i][0]&& hours<arr[i][1]){
                cost=arr[i][2];
            }
        }
        Log.d("cost", String.valueOf(cost));
        cost=cost-partial;
        tv1.setText(String.valueOf(cost));
        if(pb.getVisibility()==View.GONE && pb1.getVisibility()==View.GONE){
            linearLayout.setVisibility(View.VISIBLE);
        }
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

    void sendData(String finalcost) throws IOException {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        String current = dateFormat.format(calendar.getTime());
        try {
            // the text typed by the user
            String titleStr	= "Receipt \n";
            StringBuilder contentSb	= new StringBuilder();
            contentSb.append(header1+ "\n");
            contentSb.append(header2+ "\n");
            contentSb.append(header3+ "\n");
            contentSb.append(header4+ "\n");
            contentSb.append(header5+ "\n");
            contentSb.append("\nVehicle Number  :   "+ vhclno+ "\n");
            contentSb.append("Transporter Name  :   "+transporter + "\n");
            contentSb.append("Vehicle Type  :   "+vehicle_type + "\n");
            contentSb.append("Driver Number :   " +drvrno+ "\n");
            contentSb.append("Aps  :   "+aps + "\n");
            contentSb.append("AMT  :   "+finalcost+"\n");
            contentSb.append("Time of Arrival   :   "+current + "\n");
            contentSb.append("Email :   " +email_operator+ "\n");
            contentSb.append("\n\n\n\n"+footer1+"\n");
            contentSb.append(footer2+"\n");


            byte[] titleByte	= Printer.printfont(titleStr, FontDefine.FONT_64PX_UNDERLINE, FontDefine.Align_CENTER,
                    (byte) 0x1A, PocketPos.LANGUAGE_ENGLISH);

            byte[] content1Byte	= Printer.printfont(contentSb.toString(), FontDefine.FONT_48PX_UNDERLINE,FontDefine.Align_LEFT,
                    (byte)0x1A, PocketPos.LANGUAGE_ENGLISH);
            /*byte[] refByte		= Printer.printfont(jpaRef, FontDefine.FONT_24PX,FontDefine.Align_LEFT,  (byte)0x1A,
                    PocketPos.LANGUAGE_ENGLISH);

            byte[] messageByte	= Printer.printfont(message, FontDefine.FONT_24PX,FontDefine.Align_LEFT,  (byte)0x1A,
                    PocketPos.LANGUAGE_ENGLISH);
*/
            //byte[] dateByte		= Printer.printfont(date, FontDefine.FONT_48PX,FontDefine.Align_LEFT, (byte)0x1A,
            //       PocketPos.LANGUAGE_ENGLISH);

            byte[] totalByte	= new byte[titleByte.length +content1Byte.length/*+refByte.length + messageByte.length*/ ];

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

            mmOutputStream.write(senddata);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if(mBluetoothAdapter == null) {
                Toast.makeText(getApplicationContext(), "No bluetooth adapter available", Toast.LENGTH_LONG);
            }

            if(!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if(pairedDevices.size() > 0) {
                final ArrayAdapter<String> paired=new ArrayAdapter<String>(ExitReceipt.this,android.R.layout.select_dialog_singlechoice);
                final ArrayList dev= new ArrayList();
                for (BluetoothDevice device : pairedDevices) {
                    paired.add(device.getName());
                    dev.add(device);
                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    if (device.getName().equals("silbt-010")||device.getName().equals("silbt-009")) {
                        mmDevice = device;
                        break;
                    }
                }
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(ExitReceipt.this);
                builderSingle.setTitle("Select One Name:-");

                builderSingle.setNegativeButton(
                        "cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builderSingle.setAdapter(
                        paired,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mmDevice = (BluetoothDevice) dev.get(which);
                            }
                        });
                builderSingle.show();
            }

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


}
