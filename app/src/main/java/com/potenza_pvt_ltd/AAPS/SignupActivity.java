package com.potenza_pvt_ltd.AAPS;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends Activity {
    EditText et1,et2;
    Button b1;
    String email,pass;
    String typeofuser,userid;
    private FirebaseAuth mAuth;
    DatabaseReference reference;
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
                mAuth = FirebaseAuth.getInstance();
                reference = FirebaseDatabase.getInstance().getReference();
                mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("createuser", "createUserWithEmail:onComplete:" + task.isSuccessful());
                                Map<String, Object> value = new HashMap<String, Object>();
                                value.put("emailID", email);
                                reference.child("users").child(typeofuser).child("emailID" + i).setValue(email);
                                i++;
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                    builder.setMessage("asdas")
                                            .setTitle(R.string.login_error_title)
                                            .setPositiveButton(android.R.string.ok, null);
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }

                                // ...
                            }
                        });
                    }
        });
    }

}
