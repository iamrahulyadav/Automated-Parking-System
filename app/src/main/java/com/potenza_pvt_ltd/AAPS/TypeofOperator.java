package com.potenza_pvt_ltd.AAPS;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class TypeofOperator extends AppCompatActivity {

    private Button receiptbutton,collectionbutton,shiftclose_button;
    protected Button calculateButton;
    int printcode=0;
    String aps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_typeof_operator);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String postid= prefs.getString("PostID for timing", "");
        aps=prefs.getString("aps", "");
        if(aps.contentEquals("Entry Gate 1")||aps.contentEquals("Entry Gate 2")||aps.contentEquals("Entry Gate 3")||aps.contentEquals("Entry Gate 4")){
            printcode=1;
        }
        else if(aps.contentEquals("Exit Gate 1")||aps.contentEquals("Exit Gate 2")||aps.contentEquals("Exit Gate 3")||aps.contentEquals("Exit Gate 4")){
            printcode=2;
        }
        else if(aps.contentEquals("Parking Slot 1 Exit")||aps.contentEquals("Parking Slot 2 Exit")||aps.contentEquals("Parking Slot 3 Exit")||aps.contentEquals("Parking Slot 4 Exit")||aps.contentEquals("Parking Slot 5 Exit")||aps.contentEquals("Parking Slot 6 Exit")||aps.contentEquals("Parking Slot 7 Exit")||aps.contentEquals("Parking Slot 8 Exit")){
            printcode=3;
        }
        else if(aps.contentEquals("Parking Slot 1 Entry")||aps.contentEquals("Parking Slot 2 Entry")||aps.contentEquals("Parking Slot 3 Entry")||aps.contentEquals("Parking Slot 4 Entry")||aps.contentEquals("Parking Slot 5 Entry")||aps.contentEquals("Parking Slot 6 Entry")||aps.contentEquals("Parking Slot 7 Entry")||aps.contentEquals("Parking Slot 8 Entry")){
            printcode=4;
        }
        else{
            printcode=1;
        }
        receiptbutton=(Button)findViewById(R.id.button_mainoperator);
        collectionbutton=(Button)findViewById(R.id.button_collection);
        calculateButton=(Button)findViewById(R.id.button_calculate_fare);
        shiftclose_button=(Button)findViewById(R.id.button_shiftclose);
        receiptbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("printcode", String.valueOf(printcode));
                if(printcode==1) {
                    Intent intent = new Intent(TypeofOperator.this, SubmitActivity.class);
                    intent.putExtra("UniqueID", postid);
                    intent.putExtra("printcode", printcode);
                    startActivity(intent);
                }
                else if(printcode==2){
                    Intent intent = new Intent(TypeofOperator.this, ExitReceipt.class);
                    intent.putExtra("printcode", printcode);
                    intent.putExtra("UniqueID", postid);
                    startActivity(intent);
                }
                else if(printcode==3){
                    Intent intent = new Intent(TypeofOperator.this, PSExitActivity.class);
                    intent.putExtra("printcode", printcode);
                    intent.putExtra("UniqueID", postid);
                    startActivity(intent);
                }

                else if(printcode==4){
                    Intent intent = new Intent(TypeofOperator.this, PSEntryActivity.class);
                    intent.putExtra("printcode", printcode);
                    intent.putExtra("aps",aps);
                    intent.putExtra("UniqueID", postid);
                    startActivity(intent);
                }

            }
        });
        collectionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TypeofOperator.this,Collections.class);
                intent.putExtra("UniqueID",postid);
                intent.putExtra("aps",aps);
                startActivity(intent);
            }
        });
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TypeofOperator.this, CalculateFare.class);
                intent.putExtra("UniqueID",postid);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        shiftclose_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TypeofOperator.this, ShiftClose.class);
                intent.putExtra("UniqueID",postid);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            /*  Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                currenttime = System.currentTimeMillis();
                timetocharge=currenttime-globalmillis;
                calendar.setTimeInMillis(timetocharge);
                String t = sdf.format(calendar.getTime());
                if(timetocharge>=18*60*1000 && timetocharge<24*60*1000){
                    cost+=75;
                }
                else if(timetocharge>=24*60*1000 && timetocharge<48*60*1000){
                    cost+=150;
                }
                Map<String, Object> graceNickname = new HashMap<>();
                graceNickname.put("Cost", cost);
                mRef.child("users").child("data").child(postid).updateChildren(graceNickname);
            }*/
        });
    }
    @Override
    public void onBackPressed()
    {
    }

}
