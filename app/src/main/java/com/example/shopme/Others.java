package com.example.shopme;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by mAni on 01/09/2017.
 */

public class Others extends Fragment {

    private RecyclerView mOthersList ;

    private FirebaseAuth mAuth ;

    private DatabaseReference mDatabase ;
    private DatabaseReference mDatabaseWishlist ;
    private Query mQueryCategory ;

    private boolean mwishlistChecked = false ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.others, container, false);

        mOthersList = rootView.findViewById(R.id.others_list);
        mOthersList.setHasFixedSize(true);
        mOthersList.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("CompanyProducts");
        mDatabaseWishlist = FirebaseDatabase.getInstance().getReference().child("WishList");
        mQueryCategory = mDatabase.orderByChild("Category").equalTo("Others");

        return rootView ;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Product , OthersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Product, OthersViewHolder>(

                Product.class ,
                R.layout.clothing_row ,
                OthersViewHolder.class ,
                mQueryCategory

        ) {
            @Override
            protected void populateViewHolder(OthersViewHolder viewHolder, final Product model, int position) {

                final String product_key = getRef(position).getKey();
                final DatabaseReference mDatabaseWishlistProduct = mDatabaseWishlist.child(product_key);

                mDatabaseWishlistProduct.keepSynced(true);

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDescription(model.getDescription());
                viewHolder.setCompnayName(model.getCompnayName());
                viewHolder.setDiscount(model.getDiscount());
                viewHolder.setDiscountUntil(model.getDiscountUntil());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setImage(getActivity() , model.getImage());
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

        mOthersList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class OthersViewHolder extends RecyclerView.ViewHolder{

        View mView ;

        ImageButton img_btn_wishlist ;

        DatabaseReference mDatabaseWishList ;
        FirebaseAuth mAuth ;

        public OthersViewHolder(View itemView) {
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
