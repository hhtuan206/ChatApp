package com.hht.chatapp;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    Button RegBtn;
    EditText UserET;
    EditText PwdET;
    EditText EmailET;

    FirebaseAuth auth;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        //call widgets method
        initWidgets();

        auth = FirebaseAuth.getInstance();

        RegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = UserET.getText().toString();
                String password = PwdET.getText().toString();
                String email = EmailET.getText().toString();

                if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)||TextUtils.isEmpty(email)){
                    Toast.makeText(RegisterActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                }else {
                    RegisterNow(username,email,password);

                }
            }
        });
    }

    private void initWidgets(){
        RegBtn = findViewById(R.id.RegBtn);
        UserET = findViewById(R.id.editUser);
        PwdET = findViewById(R.id.editPwd);
        EmailET = findViewById(R.id.editEmail);
    }

    private void RegisterNow(final String username, String email, String password){
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String userid = firebaseUser.getUid();

                    myRef = FirebaseDatabase.getInstance()
                            .getReference("MyUsers")
                            .child(userid);


                    HashMap<String ,String> hashMap = new HashMap<>();
                    hashMap.put("id",userid);
                    hashMap.put("username", username);
                    hashMap.put("imageUrl","default");
                    myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent i = new Intent(RegisterActivity.this,MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                            }
                        }
                    });
                }else {
                    Toast.makeText(RegisterActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}