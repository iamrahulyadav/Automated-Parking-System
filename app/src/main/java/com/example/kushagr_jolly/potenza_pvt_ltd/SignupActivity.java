package com.example.kushagr_jolly.potenza_pvt_ltd;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends Activity {
    EditText et1,et2;
    Button b1;
    String email,pass;
    String typeofuser,userid;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        typeofuser=getIntent().getStringExtra("typeofuser");
        Log.d("asdadasdA",typeofuser);
        userid=getIntent().getStringExtra("UniqueID");
        et1=(EditText)findViewById(R.id.emailField);
        et2=(EditText)findViewById(R.id.passwordField);
        b1=(Button)findViewById(R.id.createuserbutton);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=et1.getText().toString();
                pass=et2.getText().toString();
                final Firebase ref = new Firebase(Constants.FIREBASE_URL);
                ref.createUser(email, pass, new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        Map<String, Object> value = new HashMap<String, Object>();
                        value.put("emailID", email);
                        ref.child("users").child(typeofuser).child("emailID"+i).setValue(email);
                        Log.d("Successfully", String.valueOf(result.get("uid")));
                        i++;
                    }
                    @Override
                    public void onError(FirebaseError firebaseError) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                        builder.setMessage(firebaseError.getMessage())
                                .setTitle(R.string.login_error_title)
                                .setPositiveButton(android.R.string.ok, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }
        });
    }

}
