package com.example.shopme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CompanyLogin extends AppCompatActivity {

    ImageButton img_close ;
    EditText edit_email , edit_pass ;
    Button btn_login , btn_create ;
    TextView txt_forget ;

    private FirebaseAuth mAuth ;
    private DatabaseReference mDatabasecompany ;
    private ProgressDialog mProgress ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_login);

        mAuth = FirebaseAuth.getInstance();
        mDatabasecompany = FirebaseDatabase.getInstance().getReference().child("CompanyData");
       // mDatabasecompany.keepSynced(true);
        mProgress = new ProgressDialog(this);

        img_close = (ImageButton)findViewById(R.id.img_btn_close);
        edit_email = (EditText) findViewById(R.id.input_email);
        edit_pass = (EditText) findViewById(R.id.input_pass);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_create = (Button) findViewById(R.id.btn_create_acount);
        txt_forget = (TextView) findViewById(R.id.txt_forget);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLogin();
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompanyLogin.this , MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompanyLogin.this , CompanyRegistration.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void checkLogin() {
        String email = edit_email.getText().toString().trim();
        String password = edit_pass.getText().toString().trim();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

            mProgress.setMessage("Checking Login...");
            mProgress.show();

            mAuth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                        mProgress.dismiss();
                        checkUserExist();
                    }
                    else {
                        mProgress.dismiss();
                        Toast.makeText(CompanyLogin.this , "Error Login " , Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
        else {
            Toast.makeText(CompanyLogin.this , "Fill all fields " , Toast.LENGTH_LONG).show();
        }

    }

    private void checkUserExist() {

        final String user_id = mAuth.getCurrentUser().getUid();
        mDatabasecompany.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(user_id)){

                    Intent mainIntent = new Intent(CompanyLogin.this , CompanyHome.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);

                }
                else {
                    // later on give the link of edit page saying you have incomplete data fill details again
                   /* Intent setupIntent = new Intent(CompanyLogin.this , CompanyRegistration.class);
                    setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(setupIntent);*/
                    Toast.makeText(CompanyLogin.this , "Setup your account " , Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CompanyLogin.this , MainActivity.class);
        startActivity(intent);
        finish();
    }
}
