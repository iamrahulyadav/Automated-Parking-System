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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PSEntryActivity extends AppCompatActivity {
    EditText et_search;
    ImageButton search;
    Button b;
    private FirebaseAuth mAuth;
    DatabaseReference reference;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String userkey;
    private String vhlno;
    TextView tv1;
    private String aps;
    private String header1,header2,header3,header4,header5,footer1,footer2;
    private Button openButton,closeButton;
    private String vhclno,transporter,vehicle_type,localTime,email_operator,drvrno;
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
    ProgressBar pb;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psentry);
        aps=getIntent().getStringExtra("aps");
        aps=aps.substring(0, 14);
        et_search=(EditText)findViewById(R.id.editText_search);
        search=(ImageButton)findViewById(R.id.imageButton);
        tv1=(TextView)findViewById(R.id.textView48);
        b=(Button)findViewById(R.id.button8);
        Button openButton = (Button) findViewById(R.id.open);
        myLabel = (TextView) findViewById(R.id.label);
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
        pb=(ProgressBar)findViewById(R.id.progressBar);
        linearLayout=(LinearLayout)findViewById(R.id.linear_layout);
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
                linearLayout.setVisibility(View.GONE);
                vhlno = et_search.getText().toString();
                Log.d("vehicle", vhlno);
                // Attach an listener to read the data at our posts reference
                Query queryRef1 = reference.child("users").child("data").orderByChild("Vehicle Number").equalTo(vhlno);
                queryRef1.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                        userkey = snapshot.getKey();
                        TruckDetailsActivity truck = snapshot.getValue(TruckDetailsActivity.class);
                        tv1.setText(truck.getTransporter());
                        localTime = truck.getToa();
                        vehicle_type = truck.getVtype();
                        vhclno = truck.getVno();
                        transporter = truck.getTransporter();
                        email_operator = truck.getEmail();
                        drvrno = truck.getDno();
                        pb.setVisibility(View.GONE);
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
                });            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userkey!=null) {
                    Map<String, Object> graceNickname = new HashMap<>();
                    graceNickname.put("aps", aps);

                    reference.child("users").child("data").child(userkey).updateChildren(graceNickname);
                    try {
                        sendData();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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

    void sendData() throws IOException {
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
                final ArrayAdapter<String> paired=new ArrayAdapter<String>(PSEntryActivity.this,android.R.layout.select_dialog_singlechoice);
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
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(PSEntryActivity.this);
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
