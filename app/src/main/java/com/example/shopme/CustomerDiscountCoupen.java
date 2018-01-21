package com.example.shopme;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class CustomerDiscountCoupen extends AppCompatActivity {

    private FirebaseAuth mAuth ;
    private FirebaseAuth.AuthStateListener mAuthListner ;

    private RecyclerView mDiscountList ;
    private DatabaseReference mDatabase ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_discount_coupen);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("DiscountCoupen");

        mDiscountList = (RecyclerView)findViewById(R.id.coupen_list);
        mDiscountList.setHasFixedSize(true);
        mDiscountList.setLayoutManager(new LinearLayoutManager(this));

        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() == null) {
                    Intent LoginIntent = new Intent(CustomerDiscountCoupen.this , CompanyLogin.class);
                    LoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(LoginIntent);
                }

            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Product , CoupenViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Product, CoupenViewHolder>(

                Product.class ,
                R.layout.discount_row ,
                CoupenViewHolder.class ,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(CoupenViewHolder viewHolder, final Product model, int position) {

                viewHolder.setDiscountCompnayName(model.getDiscountCompnayName());
                viewHolder.setDiscountDetails(model.getDiscountDetails());
                viewHolder.setDiscountPrice(model.getDiscountPrice());
                viewHolder.setDiscountProduct(model.getDiscountProduct());



            }
        };
        mDiscountList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class CoupenViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public CoupenViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDiscountCompnayName(String discountCompnayName){
            TextView post_discCompanyName = (TextView)mView.findViewById(R.id.post_discCompanyName);
            post_discCompanyName.setText(discountCompnayName);
        }
        public void setDiscountDetails(String DiscountDetails){
            TextView post_discdesc = (TextView)mView.findViewById(R.id.post_discDesc);
            post_discdesc.setText(DiscountDetails);
        }
        public void setDiscountProduct(String discountProduct){
            TextView post_discProducte = (TextView)mView.findViewById(R.id.post_discProductName);
            post_discProducte.setText(discountProduct);
        }
        public void setDiscountPrice(String discountPrice){
            TextView post_discPrice = (TextView)mView.findViewById(R.id.post_discPrice);
            post_discPrice.setText(discountPrice);
        }
    }
}
