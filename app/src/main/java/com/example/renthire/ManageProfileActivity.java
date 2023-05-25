package com.example.renthire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.renthire.ErrorsActivity.LoginErrorActivity;
import com.example.renthire.Storage.SharedPrefManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ManageProfileActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_profile);

        //Hooks
        TextView hiThere = findViewById(R.id.hithere);
        TextView sign_out = findViewById(R.id.sign_out);
        TextView editProfile = findViewById(R.id.editProfile);
        TextView txt_fullName = findViewById(R.id.fullName);
        TextView txt_email = findViewById(R.id.email);
        TextView txt_idno = findViewById(R.id.idno);
        TextView txt_phone = findViewById(R.id.phone);
        TextView txt_password = findViewById(R.id.password);
        TextView txt_confirmPassword = findViewById(R.id.confirm_password);
        CircleImageView profileImg = findViewById(R.id.profileImg);
        AppCompatButton btnSave = findViewById(R.id.save);

        String hiThereStr = "Hi there " + SharedPrefManager.getInstance(ManageProfileActivity.this).getFullName() + "!";

        txt_fullName.setText(SharedPrefManager.getInstance(ManageProfileActivity.this).getFullName());
        hiThere.setText(hiThereStr);
        txt_email.setText(SharedPrefManager.getInstance(ManageProfileActivity.this).getEmail());
        txt_idno.setText(SharedPrefManager.getInstance(ManageProfileActivity.this).getIdno());
        txt_phone.setText(SharedPrefManager.getInstance(ManageProfileActivity.this).getPhone());
        txt_password.setText(SharedPrefManager.getInstance(ManageProfileActivity.this).getPassword());
        txt_confirmPassword.setText(SharedPrefManager.getInstance(ManageProfileActivity.this).getPassword());

        if(isLoggedIn){
            String imageUrl = SharedPrefManager.getInstance(ManageProfileActivity.this).getImageurl();
            if (!imageUrl.isEmpty()) {
                Picasso.get().load(imageUrl).into(profileImg);

            }
        }


        //Check first if IsLoggedIn
        isLoggedIn = SharedPrefManager.getInstance(ManageProfileActivity.this).isLoggedIn();
        if (!isLoggedIn) {
            startActivity(new Intent(ManageProfileActivity.this, LoginErrorActivity.class));
            finish();
        }
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager.getInstance(ManageProfileActivity.this).logOut();
                startActivity(new Intent(ManageProfileActivity.this, LoginActivity.class));
                finish();
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_fullName.setEnabled(true);
                txt_password.setEnabled(true);
                txt_confirmPassword.setEnabled(true);

            }
        });



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_fullName.isEnabled()) {
                    String id = SharedPrefManager.getInstance(ManageProfileActivity.this).getUniqueId();
                    String fullName = txt_fullName.getText().toString();
                    String email = txt_email.getText().toString();
                    String idno = txt_idno.getText().toString();
                    String phone = txt_phone.getText().toString();
                    String password = txt_password.getText().toString();
                    String confirmPassword = txt_confirmPassword.getText().toString();

                    if (fullName.isEmpty() || email.isEmpty() || idno.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                        Toast.makeText(ManageProfileActivity.this, "One of the Fields is Empty", Toast.LENGTH_SHORT).show();
                    } else if (!password.equals(confirmPassword)) {
                        Toast.makeText(ManageProfileActivity.this, "The Passwords Don't Match", Toast.LENGTH_SHORT).show();
                    } else {

                            databaseReference.child("users").child(id).child("phone").setValue(phone);
                        databaseReference.child("users").child(id).child("password").setValue(password);

                        SharedPrefManager.getInstance(ManageProfileActivity.this).logOut();
                        startActivity(new Intent(ManageProfileActivity.this, LoginActivity.class));
                        Toast.makeText(ManageProfileActivity.this, "Login Again to Update your details ", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                }


            }
        });


    }
}