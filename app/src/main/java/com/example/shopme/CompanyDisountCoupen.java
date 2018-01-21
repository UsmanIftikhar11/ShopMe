package com.example.shopme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class CompanyDisountCoupen extends AppCompatActivity {

    private EditText editText_discCompany , editText_discProduct , editText_discPrice , editText_discDesc;
    private Button btn_submit ;

    private DatabaseReference mDatabse ;
    private DatabaseReference mDatabseUsers ;
    private FirebaseAuth mAuth ;
    private FirebaseUser mCurrentUser ;
    private ProgressDialog mProgress ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_disount_coupen);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mDatabse = FirebaseDatabase.getInstance().getReference().child("DiscountCoupen");

        mDatabseUsers = FirebaseDatabase.getInstance().getReference().child("CompanyData").child(mCurrentUser.getUid());

        mProgress = new ProgressDialog(this);

        editText_discCompany = (EditText)findViewById(R.id.input_discCompany);
        editText_discProduct = (EditText)findViewById(R.id.input_discProduct);
        editText_discPrice = (EditText)findViewById(R.id.input_discPrice);
        editText_discDesc = (EditText)findViewById(R.id.input_discDesc);
        btn_submit = (Button)findViewById(R.id.submit_Disc);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });
    }

    private void startPosting() {

        mProgress.setMessage("Adding Product...");


        final String title_string = editText_discCompany.getText().toString().toLowerCase().trim();
        final String desc_string = editText_discProduct.getText().toString().trim();
        final String price_string = editText_discPrice.getText().toString().trim();
        final String discount_string = editText_discDesc.getText().toString().trim();


        if(!TextUtils.isEmpty(title_string) && !TextUtils.isEmpty(desc_string) && !TextUtils.isEmpty(price_string)&& !TextUtils.isEmpty(discount_string)){

            mProgress.show();



                    final DatabaseReference newProduct = mDatabse.push();

                    mDatabseUsers.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            // String s = String.valueOf(dataSnapshot.child("name").getValue());
                            // final DatabaseReference newProduct = mDatabse.push();



                            newProduct.child("DiscountDetails").setValue(discount_string);
                            newProduct.child("DiscountProduct").setValue(desc_string);
                            newProduct.child("DiscountPrice").setValue(price_string);
                            newProduct.child("DiscountCompany'Id").setValue(mCurrentUser.getUid());
                            newProduct.child("DiscountCompnayName").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        //startActivity(new Intent(CompanyAddProduct.this  , CompanyHome.class));
                                        Intent intent = new Intent(CompanyDisountCoupen.this  , CompanyHome.class);
                                        startActivity(intent);

                                    }
                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    //  newProduct.child("CompanyName").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {

                    mProgress.dismiss();

        }

        else {
            Toast.makeText(getApplicationContext() , "fill all fields" , Toast.LENGTH_LONG).show();
        }

    }
}
