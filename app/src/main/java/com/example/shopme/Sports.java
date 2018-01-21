package com.example.shopme;

import android.content.Context;
import android.content.Intent;
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

public class Sports extends Fragment {

    private RecyclerView mSportsList ;

    private DatabaseReference mDatabase ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sports, container, false);

        mSportsList = rootView.findViewById(R.id.sports_list);
        mSportsList.setHasFixedSize(true);
        mSportsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        mDatabase = FirebaseDatabase.getInstance().getReference().child("SportsTypes");
        mDatabase.keepSynced(true);

        return rootView ;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Product, SportsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Product, SportsViewHolder>(

                Product.class,
                R.layout.company_name_row,
                SportsViewHolder.class,
                mDatabase

        ) {
            @Override
            protected void populateViewHolder(SportsViewHolder viewHolder, final Product model, int position) {


                viewHolder.setType(model.getType());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(getActivity() , SingleAccessories.class);
                        intent.putExtra("Type" , model.getType());
                        startActivity(intent);
                    }
                });

            }
        };

        mSportsList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class SportsViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public SportsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setType(String type) {
            TextView accessoriesType = mView.findViewById(R.id.txt_companyName);
            accessoriesType.setText(type);

        }
    }
}
