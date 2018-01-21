package com.example.shopme;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CompanyDeleteProduct extends AppCompatActivity {

    private TextView  txt_post_title , txt_post_desc , txt_post_price ;
    private ImageView txt_post_img ;
    Button delete;
    private  String product_Key = null ;

    Context ctx ;

    private DatabaseReference mDatabse ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_delete_product);

        mDatabse = FirebaseDatabase.getInstance().getReference().child("CompanyProducts");

        product_Key = getIntent().getExtras().getString("Product_id");

        txt_post_title = (TextView)findViewById(R.id.post_title1);
        txt_post_desc = (TextView)findViewById(R.id.post_desc1);
        txt_post_price  = (TextView)findViewById(R.id.post_price1);
        txt_post_img = (ImageView)findViewById(R.id.post_img1);
        delete =(Button)findViewById(R.id.remove_post);

        mDatabse.child(product_Key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title = (String) dataSnapshot.child("Title").getValue();
                String post_desc = (String) dataSnapshot.child("Description").getValue();
                String post_image = (String) dataSnapshot.child("Image").getValue();
                String post_price = (String) dataSnapshot.child("Price").getValue();

                txt_post_title.setText(post_title);
                txt_post_desc.setText(post_desc);
                txt_post_price.setText(post_price);
                Picasso.with(ctx).load(post_image).into(txt_post_img);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabse.child(product_Key).removeValue();
                Intent intent = new Intent(CompanyDeleteProduct.this , CompanyHome.class);
                startActivity(intent);
            }
        });

    }
}
