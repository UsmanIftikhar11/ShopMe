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

public class CustomerWishlist extends AppCompatActivity {

    private FirebaseAuth mAuth ;
    private FirebaseAuth.AuthStateListener mAuthListner ;

    private RecyclerView mWishList ;
    private DatabaseReference mDatabase , mDatabaseWishList;
    private DatabaseReference mDatabaseCurrentUser ;
    private Query mQueryCurrentUser ;
    private static  String product_key ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_wishlist);

        //mAuth = FirebaseAuth.getInstance();

        //String curretnUserId = mAuth.getCurrentUser().getUid();

        //mDatabase = FirebaseDatabase.getInstance().getReference().child("CompanyProducts");
        mDatabaseWishList = FirebaseDatabase.getInstance().getReference().child("WishList");
        //mQueryCurrentUser = mDatabase.orderByChild("Company'Id").equalTo(curretnUserId);

        mWishList = (RecyclerView)findViewById(R.id.wish_list);
//        mWishList.setHasFixedSize(true);
        mWishList.setLayoutManager(new LinearLayoutManager(this));

        /*mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() == null) {
                    Intent LoginIntent = new Intent(CustomerWishlist.this , CustomerLogin.class);
                    LoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(LoginIntent);
                }

            }
        };*/
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Product , WishListViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Product, WishListViewHolder>(

                Product.class ,
                R.layout.product_row ,
                WishListViewHolder.class ,
                mDatabaseWishList
        ) {
            @Override
            protected void populateViewHolder(WishListViewHolder viewHolder, final Product model, int position) {

                product_key = getRef(position).getKey();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDescription(model.getDescription());
                viewHolder.setCompnayName(model.getCompnayName());
                viewHolder.setCategory(model.getCategory());
                viewHolder.setDiscount(model.getDiscount());
                viewHolder.setDiscountUntil(model.getDiscountUntil());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setImage(getApplicationContext() , model.getImage());
                /*viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(getApplicationContext() , "Whole click" , Toast.LENGTH_LONG).show();
                        Intent singleProduct = new Intent(CompanyHome.this , CompanySingleProduct.class);
                        singleProduct.putExtra("Product_id" , product_key);
                        singleProduct.putExtra("title" , model.getTitle());
                        singleProduct.putExtra("Desc" , model.getDescription());
                        singleProduct.putExtra("price" , model.getPrice());
                        singleProduct.putExtra("discount" , model.getDiscount());
                        singleProduct.putExtra("discountUntil" , model.getDiscountUntil());
                        singleProduct.putExtra("image" , model.getImage());
                        singleProduct.putExtra("category" , model.getCategory());

                        startActivity(singleProduct);
                    }
                });*/

            }
        };
        mWishList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class WishListViewHolder extends RecyclerView.ViewHolder{

        View mView;
        ImageButton btn_remove ;
        //Context context ;

        private DatabaseReference mDatabaseWishList ;

        public WishListViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            //context = itemView.getContext();

            mDatabaseWishList = FirebaseDatabase.getInstance().getReference().child("WishList");
            mDatabaseWishList.keepSynced(true);

            btn_remove = (ImageButton)mView.findViewById(R.id.img_btn_remove);
            btn_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(mView.getContext() , "remove button click" , Toast.LENGTH_LONG ).show();
                    /*Intent deleteProduct = new Intent(mView.getContext() , CompanyDeleteProduct.class);
                    deleteProduct.putExtra("Product_id" , product_key);
                    context.startActivity(deleteProduct);*/
                    mDatabaseWishList.child(product_key).removeValue();
                }
            });
        }

        public void setTitle(String title){
            TextView post_title = (TextView)mView.findViewById(R.id.post_title);
            post_title.setText(title);
        }
        public void setDescription(String description){
            TextView post_desc = (TextView)mView.findViewById(R.id.post_desc);
            post_desc.setText(description);
        }
        public void setCompnayName(String compnayName){
            TextView post_companyName = (TextView)mView.findViewById(R.id.post_companyName);
            post_companyName.setText(compnayName);
        }
        public void setCategory(String category){
            TextView post_category = (TextView)mView.findViewById(R.id.post_category);
            post_category.setText(category);
        }
        public void setDiscount(String discount){
            TextView post_DiscountPrice = (TextView)mView.findViewById(R.id.post_DiscountPrice);
            post_DiscountPrice.setText(discount);
        }
        public void setPrice(String price){
            TextView post_price = (TextView)mView.findViewById(R.id.post_Price);
            post_price.setText(price);
        }
        public void setDiscountUntil(String discountUntil){
            TextView post_daysLeft = (TextView)mView.findViewById(R.id.post_DaysLeft);
            post_daysLeft.setText(discountUntil);
        }
        public void setImage(Context ctx , String image){
            ImageView post_img = (ImageView)mView.findViewById(R.id.post_img);
            Picasso.with(ctx).load(image).into(post_img);
        }
    }
}
