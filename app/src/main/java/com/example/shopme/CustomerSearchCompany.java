package com.example.shopme;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class CustomerSearchCompany extends AppCompatActivity {

    private RecyclerView mcompanyNameList ;

    private DatabaseReference mDatabase ;
    private Query mQueryCompany ;

    private EditText et_search ;
    private Button btn_searchCompany ;

    private String searchString ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_search_company);

        mcompanyNameList = (RecyclerView)findViewById(R.id.company_search_list);
        mcompanyNameList.setHasFixedSize(true);
        mcompanyNameList.setLayoutManager(new LinearLayoutManager(this));

        et_search = (EditText)findViewById(R.id.inputSearch);
        btn_searchCompany = (Button)findViewById(R.id.btn_companySearch);



        btn_searchCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchString = et_search.getText().toString().toLowerCase();
                mDatabase = FirebaseDatabase.getInstance().getReference().child("CompanyData");
                mQueryCompany = mDatabase.orderByChild("adress").startAt(searchString).endAt(searchString + "~");

                FirebaseRecyclerAdapter<Product , CompanySearchViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Product, CompanySearchViewHolder>(

                        Product.class ,
                        R.layout.company_name_row ,
                        CompanySearchViewHolder.class ,
                        mQueryCompany

                ) {
                    @Override
                    protected void populateViewHolder(CompanySearchViewHolder viewHolder, final Product model, int position) {

                        viewHolder.setCompnayName(model.getName());
                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(CustomerSearchCompany.this , CustomerSingleCompanyProducts.class);
                                intent.putExtra("CompName" , model.getName());
                                startActivity(intent);
                            }
                        });

                    }
                };

                mcompanyNameList.setAdapter(firebaseRecyclerAdapter);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public static class CompanySearchViewHolder extends RecyclerView.ViewHolder{

        View mView ;

        public CompanySearchViewHolder(View itemView) {
            super(itemView);

            mView = itemView ;
        }
        public void setCompnayName (String name){
            TextView searchCompanyName = mView.findViewById(R.id.txt_companyName);
            searchCompanyName.setText(name);
        }



    }
}
