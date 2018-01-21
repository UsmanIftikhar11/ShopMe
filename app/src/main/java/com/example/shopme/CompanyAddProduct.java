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
import android.widget.CompoundButton;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class CompanyAddProduct extends AppCompatActivity {
    private ImageButton img_btn_select;

    private CheckBox checkBox ;

    private EditText editText_title , editText_desc , editText_price , editText_discount , editText_discountDate , editText_category , editText_categoryType;
    private Button btn_submit ;
    MaterialSpinner spinner , spinner1 , spinner2 ;


    private Uri imageUri = null ;

    private StorageReference mStorage ;
    private DatabaseReference mDatabse ;
    private DatabaseReference mDatabseUsers ;
    private FirebaseAuth mAuth ;
    private FirebaseUser mCurrentUser ;
    private ProgressDialog mProgress ;


    private static final int GALLERY_REQUEST = 1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_add_product);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabse = FirebaseDatabase.getInstance().getReference().child("CompanyProducts");

        mDatabseUsers = FirebaseDatabase.getInstance().getReference().child("CompanyData").child(mCurrentUser.getUid());


        spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinner.setItems("Choose category" , "Accessories", "Clothing" , "Electronics" , "Sports" , "Others");

        spinner1 = (MaterialSpinner)findViewById(R.id.spinner1);
        spinner2 = (MaterialSpinner)findViewById(R.id.spinner2);
        spinner2.setItems("Select Discount" , "10%", "20%" , "30%" , "40%" , "50%" , "60%" , "70%" , "80%" , "90%");

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if(item.toString()=="Accessories")
                {
                    editText_category.setText("Accessories");
                    editText_category.setClickable(false);
                    spinner1.setItems("Choose Type" , "Men Accessories" , "Women Accessories" , "Children Accessories" , "Beauty And Health" , "Others");

                    spinner1.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                            if(item.toString() == "Men Accessories"){
                                editText_categoryType.setText("Men Accessories");
                                editText_categoryType.setClickable(false);
                            }
                            else if(item.toString() == "Women Accessories"){
                                editText_categoryType.setText("Women Accessories");
                                editText_categoryType.setClickable(false);
                            }
                            else if(item.toString() == "Children Accessories"){
                                editText_categoryType.setText("Children Accessories");
                                editText_categoryType.setClickable(false);
                            }
                            else if(item.toString() == "Beauty And Health"){
                                editText_categoryType.setText("Beauty And Health");
                                editText_categoryType.setClickable(false);
                            }
                            else{
                                editText_categoryType.setText("Others");
                                editText_categoryType.setClickable(false);
                            }
                        }
                    });
                }
                else if (item.toString()=="Clothing")
                {
                    editText_category.setText("Clothing");
                    editText_category.setClickable(false);
                    spinner1.setItems("Choose Type" , "Men" , "Women" , "Children" , "Western", "Others");

                    spinner1.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                            if(item.toString() == "Men"){
                                editText_categoryType.setText("Men");
                                editText_categoryType.setClickable(false);
                            }
                            else if(item.toString() == "Women"){
                                editText_categoryType.setText("Women");
                                editText_categoryType.setClickable(false);
                            }
                            else if(item.toString() == "Children"){
                                editText_categoryType.setText("Children");
                                editText_categoryType.setClickable(false);
                            }
                            else if(item.toString() == "Western"){
                                editText_categoryType.setText("Western");
                                editText_categoryType.setClickable(false);
                            }
                            else{
                                editText_categoryType.setText("Others");
                                editText_categoryType.setClickable(false);
                            }
                        }
                    });

                }
                else if(item.toString()=="Electronics")
                {
                    editText_category.setText("Electronics");
                    editText_category.setClickable(false);
                    spinner1.setItems("Choose Type" , "Computer And Laptop" , "Gaming" , "Home Appliances" , "Phones And Tablets" , "Others");

                    spinner1.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                            if(item.toString() == "Computer And Laptop"){
                                editText_categoryType.setText("Computer And Laptop");
                                editText_categoryType.setClickable(false);
                            }
                            else if(item.toString() == "Gaming"){
                                editText_categoryType.setText("Gaming");
                                editText_categoryType.setClickable(false);
                            }
                            else if(item.toString() == "Home Appliances"){
                                editText_categoryType.setText("Home Appliances");
                                editText_categoryType.setClickable(false);
                            }
                            else if(item.toString() == "Phones And Tablets"){
                                editText_categoryType.setText("Phones And Tablets");
                                editText_categoryType.setClickable(false);
                            }
                            else{
                                editText_categoryType.setText("Others");
                                editText_categoryType.setClickable(false);
                            }
                        }
                    });

                }
                else if(item.toString()=="Sports")
                {
                    editText_category.setText("Sports");
                    editText_category.setClickable(false);
                    spinner1.setItems("Choose Type" , "Exercise And Fitness" , "Equipment" , "Shoes And Clothing" , "Fitness Accessories", "Others");

                    spinner1.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                            if(item.toString() == "Exercise And Fitness"){
                                editText_categoryType.setText("Exercise And Fitness");
                                editText_categoryType.setClickable(false);
                            }
                            else if(item.toString() == "Equipment"){
                                editText_categoryType.setText("Equipment");
                                editText_categoryType.setClickable(false);
                            }
                            else if(item.toString() == "Shoes And Clothing"){
                                editText_categoryType.setText("Shoes And Clothing");
                                editText_categoryType.setClickable(false);
                            }
                            else if(item.toString() == "Fitness Accessories"){
                                editText_categoryType.setText("Fitness Accessories");
                                editText_categoryType.setClickable(false);
                            }
                            else{
                                editText_categoryType.setText("Others");
                                editText_categoryType.setClickable(false);
                            }
                        }
                    });

                }
                else {
                    editText_category.setText("Others");
                    editText_category.setClickable(false);
                    spinner1.setItems("None");

                    editText_categoryType.setText("None");
                    editText_categoryType.setClickable(false);
                }
            }
        });


        mProgress = new ProgressDialog(this);

        img_btn_select = (ImageButton)findViewById(R.id.imageSelect);
        editText_title = (EditText)findViewById(R.id.input_title);
        editText_desc = (EditText)findViewById(R.id.input_desc);
        editText_price = (EditText)findViewById(R.id.input_price);
        editText_discount = (EditText)findViewById(R.id.input_discount);
        editText_discountDate = (EditText)findViewById(R.id.input_discountDate);
        editText_category = (EditText)findViewById(R.id.input_category);
        editText_categoryType = (EditText)findViewById(R.id.input_categoryType);
        checkBox = (CheckBox)findViewById(R.id.checkbox) ;
        btn_submit = (Button)findViewById(R.id.submit_post);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkBox.isChecked())
                {
                    editText_discount.setVisibility(View.VISIBLE);
                    editText_discountDate.setVisibility(View.VISIBLE);
                    spinner2.setVisibility(View.VISIBLE);

                    spinner2.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                            if(item.toString() == "10%"){
                                editText_discount.setText("10%");
                                editText_discount.setClickable(false);
                            }
                            else if(item.toString() == "20%"){
                                editText_discount.setText("20%");
                                editText_discount.setClickable(false);
                            }
                            else if(item.toString() == "30%"){
                                editText_discount.setText("30%");
                                editText_discount.setClickable(false);
                            }
                            else if(item.toString() == "40%"){
                                editText_discount.setText("40%");
                                editText_discount.setClickable(false);
                            }
                            else if(item.toString() == "50%"){
                                editText_discount.setText("50%");
                                editText_discount.setClickable(false);
                            }
                            else if(item.toString() == "60%"){
                                editText_discount.setText("60%");
                                editText_discount.setClickable(false);
                            }
                            else if(item.toString() == "70%"){
                                editText_discount.setText("70%");
                                editText_discount.setClickable(false);
                            }
                            else if(item.toString() == "90%"){
                                editText_discount.setText("80%");
                                editText_discount.setClickable(false);
                            }
                            else{
                                editText_discount.setText("90%");
                                editText_discount.setClickable(false);
                            }
                        }
                    });

                }
                else
                {
                    editText_discount.setVisibility(View.INVISIBLE);
                    editText_discountDate.setVisibility(View.INVISIBLE);
                    spinner2.setVisibility(View.INVISIBLE);
                }
            }
        });

        img_btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent , GALLERY_REQUEST);

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });

    }

    private void startPosting() {

        mProgress.setMessage("Adding Product...");


        final String title_string = editText_title.getText().toString().toLowerCase().trim();
        final String desc_string = editText_desc.getText().toString().trim();
        final String price_string = editText_price.getText().toString().trim();
        final String discount_string = editText_discount.getText().toString().trim();
        final String discountDate_string = editText_discountDate.getText().toString().trim();
        final String category_string = editText_category.getText().toString().trim();
        final String categoryType_string = editText_categoryType.getText().toString().trim();


        if(!TextUtils.isEmpty(title_string) && !TextUtils.isEmpty(desc_string) && !TextUtils.isEmpty(price_string) && imageUri != null && checkBox.isChecked() == false && !TextUtils.isEmpty(category_string) && !TextUtils.isEmpty(categoryType_string)){

            mProgress.show();

            StorageReference filePath = mStorage.child("Company_Products").child(imageUri.getLastPathSegment());
            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    @SuppressWarnings("VisibleForTests") final Uri downlaodUrl = taskSnapshot.getDownloadUrl();
                    //editText_title.setText(null);

                    final DatabaseReference newProduct = mDatabse.push();

                    mDatabseUsers.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                           // String s = String.valueOf(dataSnapshot.child("name").getValue());
                            // final DatabaseReference newProduct = mDatabse.push();



                            newProduct.child("Title").setValue(title_string);
                            newProduct.child("Description").setValue(desc_string);
                            newProduct.child("Price").setValue(price_string);
                            newProduct.child("Image").setValue(downlaodUrl.toString());
                            newProduct.child("Company'Id").setValue(mCurrentUser.getUid());
                            newProduct.child("Category").setValue(category_string);
                            newProduct.child("Type").setValue(categoryType_string);
                            newProduct.child("CompnayName").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        //startActivity(new Intent(CompanyAddProduct.this  , CompanyHome.class));
                                        Intent intent = new Intent(CompanyAddProduct.this  , CompanyHome.class);
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
            });
        }

        else if (!TextUtils.isEmpty(title_string) && !TextUtils.isEmpty(desc_string) && !TextUtils.isEmpty(price_string) && imageUri != null && checkBox.isChecked() && !TextUtils.isEmpty(discount_string) && !TextUtils.isEmpty(discountDate_string) && !TextUtils.isEmpty(category_string) && !TextUtils.isEmpty(categoryType_string)){
            mProgress.show();

            StorageReference filePath = mStorage.child("Company_Products").child(imageUri.getLastPathSegment());
            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    @SuppressWarnings("VisibleForTests") final Uri downlaodUrl = taskSnapshot.getDownloadUrl();
                    //editText_title.setText(null);

                    final DatabaseReference newProduct = mDatabse.push();

                    mDatabseUsers.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            // String s = String.valueOf(dataSnapshot.child("name").getValue());
                            // final DatabaseReference newProduct = mDatabse.push();



                            newProduct.child("Title").setValue(title_string);
                            newProduct.child("Description").setValue(desc_string);
                            newProduct.child("Price").setValue(price_string);
                            newProduct.child("Image").setValue(downlaodUrl.toString());
                            newProduct.child("Discount").setValue(discount_string);
                            newProduct.child("DiscountUntil").setValue(discountDate_string);
                            newProduct.child("Category").setValue(category_string);
                            newProduct.child("Type").setValue(categoryType_string);
                            newProduct.child("Company'Id").setValue(mCurrentUser.getUid());
                            newProduct.child("CompnayName").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        //startActivity(new Intent(CompanyAddProduct.this  , CompanyHome.class));
                                        Intent intent = new Intent(CompanyAddProduct.this  , CompanyHome.class);
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
            });
        }

        else {
            Toast.makeText(getApplicationContext() , "fill all fields" , Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            imageUri = data.getData();
            /*img_btn_select.setImageURI(imageUri);*/
            // start picker to get image for cropping and then use the image in cropping activity
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(4 , 3)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                img_btn_select.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
