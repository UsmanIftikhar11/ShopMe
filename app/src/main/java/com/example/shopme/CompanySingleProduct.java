package com.example.shopme;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class CompanySingleProduct extends AppCompatActivity {
    private ImageButton img_btn_select1;

    private CheckBox checkBox1 ;

    private EditText editText_title1 , editText_desc1 , editText_price1 , editText_discount1 , editText_discountDate1 , editText_category1 ;
    private Button btn_submit1 ;
    MaterialSpinner spinner1 ;

    private Uri imageUri1 = null ;
    private boolean click = false;

    private StorageReference mStorage ;
    private DatabaseReference mDatabse ;
    private DatabaseReference mDatabseUsers ;
    private FirebaseAuth mAuth ;
    private FirebaseUser mCurrentUser ;
    private ProgressDialog mProgress ;
    Context ctx ;

    private  String product_Key , title , desc , price , discount , discountUntil , image , category;


    private static final int GALLERY_REQUEST = 1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_single_product);

        product_Key = getIntent().getExtras().getString("Product_id");
        title = getIntent().getExtras().getString("title");
        desc = getIntent().getExtras().getString("Desc");
        price = getIntent().getExtras().getString("price");
        discount = getIntent().getExtras().getString("discount");
        discountUntil = getIntent().getExtras().getString("discountUntil");
        image = getIntent().getExtras().getString("image");
        category = getIntent().getExtras().getString("category");

        Toast.makeText(getApplicationContext() , image , Toast.LENGTH_LONG).show();

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabse = FirebaseDatabase.getInstance().getReference().child("CompanyProducts");

        mDatabseUsers = FirebaseDatabase.getInstance().getReference().child("CompanyData").child(mCurrentUser.getUid());

        mProgress = new ProgressDialog(this);

        spinner1 = (MaterialSpinner) findViewById(R.id.spinner1);
        spinner1.setItems("Choose category" , "Accessories", "Clothing" , "Electronics" , "Sports" , "Others");

        spinner1.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if(item.toString()=="Accessories")
                {
                    editText_category1.setText("Accessories");
                    editText_category1.setClickable(false);
                }
                else if (item.toString()=="Clothing")
                {
                    editText_category1.setText("Clothing");
                    editText_category1.setClickable(false);
                }
                else if(item.toString()=="Electronics")
                {
                    editText_category1.setText("Electronics");
                    editText_category1.setClickable(false);
                }
                else if(item.toString()=="Sports")
                {
                    editText_category1.setText("Sports");
                    editText_category1.setClickable(false);
                }
                else if(item.toString()=="Others")
                {
                    editText_category1.setText("Others");
                    editText_category1.setClickable(false);
                }
                else {
                    editText_category1.setText("Choose category");
                    editText_category1.setClickable(false);
                }
            }
        });

        img_btn_select1 = (ImageButton)findViewById(R.id.imageSelect1);
        editText_title1 = (EditText)findViewById(R.id.input_title1);
        editText_desc1 = (EditText)findViewById(R.id.input_desc1);
        editText_price1 = (EditText)findViewById(R.id.input_price1);
        editText_discount1 = (EditText)findViewById(R.id.input_discount1);
        editText_discountDate1 = (EditText)findViewById(R.id.input_discountDate1);
        editText_category1 = (EditText)findViewById(R.id.input_category1);
        checkBox1 = (CheckBox)findViewById(R.id.checkbox1) ;
        btn_submit1 = (Button)findViewById(R.id.submit_post1);

        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkBox1.isChecked())
                {
                    editText_discount1.setVisibility(View.VISIBLE);
                    editText_discountDate1.setVisibility(View.VISIBLE);

                }
                else
                {
                    editText_discount1.setVisibility(View.INVISIBLE);
                    editText_discountDate1.setVisibility(View.INVISIBLE);
                }
            }
        });

        //String title = getIntent().getExtras().getString("Title");
        editText_title1.setText(title);
        editText_desc1.setText(desc);
        editText_price1.setText(price);
        editText_discount1.setText(discount);
        editText_discountDate1.setText(discountUntil);
        editText_category1.setText(category);
        Picasso.with(ctx).load(image).into(img_btn_select1);
        //imageUri1 = Uri.parse(image);

        img_btn_select1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent , GALLERY_REQUEST);

                click = true ;
            }
        });

        btn_submit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });


    }

    private void startPosting() {

        mProgress.setMessage("Adding Product...");


        final String title_string = editText_title1.getText().toString().trim();
        final String desc_string = editText_desc1.getText().toString().trim();
        final String price_string = editText_price1.getText().toString().trim();
        final String discount_string = editText_discount1.getText().toString().trim();
        final String discountDate_string = editText_discountDate1.getText().toString().trim();
        final String category_string = editText_category1.getText().toString().trim();

        if(click == true) {

            click = false ;
            if (!TextUtils.isEmpty(title_string) && !TextUtils.isEmpty(desc_string) && !TextUtils.isEmpty(price_string) && imageUri1 != null && checkBox1.isChecked() == false && category_string != "Choose category") {

                mProgress.show();

                StorageReference filePath = mStorage.child("Company_Products").child(imageUri1.getLastPathSegment());
                filePath.putFile(imageUri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        @SuppressWarnings("VisibleForTests") final Uri downlaodUrl = taskSnapshot.getDownloadUrl();
                        //editText_title.setText(null);

                        final DatabaseReference newProduct = mDatabse.child(product_Key);

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
                                newProduct.child("CompnayName").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            startActivity(new Intent(CompanySingleProduct.this, CompanyHome.class));

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
            } else if (!TextUtils.isEmpty(title_string) && !TextUtils.isEmpty(desc_string) && !TextUtils.isEmpty(price_string) && imageUri1 != null && checkBox1.isChecked() && !TextUtils.isEmpty(discount_string) && !TextUtils.isEmpty(discountDate_string) && category_string != "Choose category") {
                mProgress.show();

                StorageReference filePath = mStorage.child("Company_Products").child(imageUri1.getLastPathSegment());
                filePath.putFile(imageUri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        @SuppressWarnings("VisibleForTests") final Uri downlaodUrl = taskSnapshot.getDownloadUrl();
                        //editText_title.setText(null);

                        final DatabaseReference newProduct = mDatabse.child(product_Key);

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
                                newProduct.child("Company'Id").setValue(mCurrentUser.getUid());
                                newProduct.child("CompnayName").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            startActivity(new Intent(CompanySingleProduct.this, CompanyHome.class));

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
            } else {
                Toast.makeText(getApplicationContext(), "fill all fields", Toast.LENGTH_LONG).show();
            }
        }

        else {

            if (!TextUtils.isEmpty(title_string) && !TextUtils.isEmpty(desc_string) && !TextUtils.isEmpty(price_string)  && checkBox1.isChecked() == false && category_string != "Choose category") {

                mProgress.show();

                        //editText_title.setText(null);

                        final DatabaseReference newProduct = mDatabse.child(product_Key);

                        mDatabseUsers.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                // String s = String.valueOf(dataSnapshot.child("name").getValue());
                                // final DatabaseReference newProduct = mDatabse.push();


                                newProduct.child("Title").setValue(title_string);
                                newProduct.child("Description").setValue(desc_string);
                                newProduct.child("Price").setValue(price_string);
                                newProduct.child("Image").setValue(image.toString());
                                newProduct.child("Company'Id").setValue(mCurrentUser.getUid());
                                newProduct.child("Category").setValue(category_string);
                                newProduct.child("CompnayName").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            startActivity(new Intent(CompanySingleProduct.this, CompanyHome.class));

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


            } else if (!TextUtils.isEmpty(title_string) && !TextUtils.isEmpty(desc_string) && !TextUtils.isEmpty(price_string)  && checkBox1.isChecked() && !TextUtils.isEmpty(discount_string) && !TextUtils.isEmpty(discountDate_string) && category_string != "Choose category") {
                mProgress.show();

                        //editText_title.setText(null);

                        final DatabaseReference newProduct = mDatabse.child(product_Key);

                        mDatabseUsers.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                // String s = String.valueOf(dataSnapshot.child("name").getValue());
                                // final DatabaseReference newProduct = mDatabse.push();


                                newProduct.child("Title").setValue(title_string);
                                newProduct.child("Description").setValue(desc_string);
                                newProduct.child("Price").setValue(price_string);
                                newProduct.child("Image").setValue(image.toString());
                                newProduct.child("Discount").setValue(discount_string);
                                newProduct.child("DiscountUntil").setValue(discountDate_string);
                                newProduct.child("Category").setValue(category_string);
                                newProduct.child("Company'Id").setValue(mCurrentUser.getUid());
                                newProduct.child("CompnayName").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            startActivity(new Intent(CompanySingleProduct.this, CompanyHome.class));

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

            } else {
                Toast.makeText(getApplicationContext(), "fill all fields", Toast.LENGTH_LONG).show();
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            imageUri1 = data.getData();
            /*img_btn_select.setImageURI(imageUri);*/
            // start picker to get image for cropping and then use the image in cropping activity
            CropImage.activity(imageUri1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(4 , 3)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                img_btn_select1.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
