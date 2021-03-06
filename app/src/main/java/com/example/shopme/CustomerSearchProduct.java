package com.example.shopme;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CustomerSearchProduct extends AppCompatActivity {

    private RecyclerView mproductSearchList ;

    private FirebaseAuth mAuth ;

    private DatabaseReference mDatabase ;
    private DatabaseReference mDatabaseWishlist ;
    private Query mQueryCategory ;
    private String searchString ;

    private EditText edit_search ;
    private Button btn_search ;
    private boolean mwishlistChecked = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_search_product);

        edit_search = (EditText)findViewById(R.id.input_search);
        btn_search = (Button)findViewById(R.id.btn_search);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseWishlist = FirebaseDatabase.getInstance().getReference().child("WishList");

        mproductSearchList = (RecyclerView) findViewById(R.id.productSearch_list);
        mproductSearchList.setHasFixedSize(true);
        mproductSearchList.setLayoutManager(new LinearLayoutManager(this));

        mDatabaseWishlist.keepSynced(true);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchString = edit_search.getText().toString().toLowerCase();
                mDatabase = FirebaseDatabase.getInstance().getReference().child("CompanyProducts");
                mQueryCategory = mDatabase.orderByChild("Title").startAt(searchString).endAt(searchString + "~");

                FirebaseRecyclerAdapter<Product , ProductSearchViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Product, ProductSearchViewHolder>(

                        Product.class ,
                        R.layout.clothing_row ,
                        ProductSearchViewHolder.class ,
                        mQueryCategory

                ) {
                    @Override
                    protected void populateViewHolder(ProductSearchViewHolder viewHolder, final Product model, int position) {

                        final String product_key = getRef(position).getKey();
                        final DatabaseReference mDatabaseWishlistProduct = mDatabaseWishlist.child(product_key);

                        mDatabaseWishlistProduct.keepSynced(true);

                        viewHolder.setTitle(model.getTitle());
                        viewHolder.setDescription(model.getDescription());
                        viewHolder.setCompnayName(model.getCompnayName());
                        viewHolder.setDiscount(model.getDiscount());
                        viewHolder.setDiscountUntil(model.getDiscountUntil());
                        viewHolder.setPrice(model.getPrice());
                        viewHolder.setImage(getApplicationContext() , model.getImage());
                        viewHolder.setwishListBtn(product_key , model.getTitle());

                        viewHolder.img_btn_wishlist.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mwishlistChecked = true ;

                                mDatabaseWishlist.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(mwishlistChecked == true){
                                            if(dataSnapshot.hasChild(product_key)){
                                                mDatabaseWishlistProduct.removeValue();
                                                mwishlistChecked = false ;
                                            }
                                            else {
                                                //mDatabaseWishlistProduct.addValueEventListener(new ValueEventListener() {
                                                //@Override
                                                //public void onDataChange(DataSnapshot dataSnapshot) {
                                                mDatabaseWishlist.child(product_key).child("Title").setValue(model.getTitle());
                                                mDatabaseWishlist.child(product_key).child("Description").setValue(model.getDescription());
                                                mDatabaseWishlist.child(product_key).child("Price").setValue(model.getPrice());
                                                mDatabaseWishlist.child(product_key).child("Image").setValue(model.getImage());
                                                mDatabaseWishlist.child(product_key).child("Company'Id").setValue(mAuth.getCurrentUser().getUid());
                                                mDatabaseWishlist.child(product_key).child("Category").setValue(model.getCategory());
                                                mDatabaseWishlist.child(product_key).child("CompnayName").setValue(model.getCompnayName());
                                            /*}

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });*/
                                                mwishlistChecked = false ;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        });

                    }
                };

                mproductSearchList.setAdapter(firebaseRecyclerAdapter);

                edit_search.getText().clear();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    public static class ProductSearchViewHolder extends RecyclerView.ViewHolder{

        View mView ;

        ImageButton img_btn_wishlist ;

        DatabaseReference mDatabaseWishList ;
        FirebaseAuth mAuth ;

        public ProductSearchViewHolder(View itemView) {
            super(itemView);

            mView = itemView ;

            img_btn_wishlist = (ImageButton)mView.findViewById(R.id.img_btn_wishList);

            mDatabaseWishList = FirebaseDatabase.getInstance().getReference().child("WishList");
            mAuth = FirebaseAuth.getInstance();

            mDatabaseWishList.keepSynced(true);
        }

        public void setTitle (String title){
            TextView accessoriesTitle = mView.findViewById(R.id.cust_post_title);
            accessoriesTitle.setText(title);
        }
        public void setDescription (String description){
            TextView accessoriesDesc = mView.findViewById(R.id.cust_post_desc);
            accessoriesDesc.setText(description);
        }
        public void setCompnayName (String companyName){
            TextView accessoriesCompanyName = mView.findViewById(R.id.cust_post_companyName);
            accessoriesCompanyName.setText(companyName);
        }
        public void setDiscount (String discount){
            TextView accessoriesDiscount = mView.findViewById(R.id.cust_post_DiscountPrice);
            accessoriesDiscount.setText(discount);
        }
        public void setDiscountUntil (String discountUntil){
            TextView accessoriesUntil = mView.findViewById(R.id.cust_post_DaysLeft);
            accessoriesUntil.setText(discountUntil);
        }
        public void setPrice (String price){
            TextView accessoriesPrice = mView.findViewById(R.id.cust_post_Price);
            accessoriesPrice.setText(price);
        }
        public void setImage (Context ctx , String image){
            ImageView accessories_post_img = (ImageView)mView.findViewById(R.id.cust_post_img);
            Picasso.with(ctx).load(image).into(accessories_post_img);
        }

        public void setwishListBtn(final String product_key , final String title){

            mDatabaseWishList.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.hasChild(product_key)){

                        img_btn_wishlist.setImageResource(R.drawable.bookmarkcheck);
                    }
                    else {
                        img_btn_wishlist.setImageResource(R.drawable.bookmark);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
}
