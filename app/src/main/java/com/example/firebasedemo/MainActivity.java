package com.example.firebasedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebasedemo.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private EditText name, email, age;
    private Button btnSendData, btnRead;
    private TextView tvRead;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        reference = FirebaseDatabase.getInstance().getReference().child("Post");
        binding.btnSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnSendData();

            }
        });
        binding.btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    btnReadOneTime();
                btnReadTime();


            }
        });

        binding.btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ShowDataActivity.class);
                startActivity(intent);


            }
        });

    }


    private void btnReadTime() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    String post = "name : " + dataSnapshot.child("name").getValue(String.class) + "\n" + "email : " + dataSnapshot.child("email").getValue(String.class) + "\n" + "age : " + dataSnapshot.child("age").getValue(String.class);


                    binding.tvRead.setText(post);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //   reference.child("-NVyMtfhsDG2Cq0PoNxS")
                /*
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        
                        String post = "name : "+snapshot.child("name").getValue(String.class)+"\n"
                                +"email : "+snapshot.child("email").getValue(String.class)+"\n"
                                +"age : "+snapshot.child("age").getValue(String.class);

                        binding.tvRead.setText(post);
                        
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                 */
    }


    private void btnReadOneTime() {

        reference.child("-NVyMtfhsDG2Cq0PoNxS").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String post = "name : " + dataSnapshot.child("name").getValue(String.class) + "\n" + "email : " + dataSnapshot.child("email").getValue(String.class) + "\n" + "age : " + dataSnapshot.child("age").getValue(String.class);

                binding.tvRead.setText(post);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void btnSendData() {

        HashMap<String, Object> map = new HashMap<>();
        map.put("name", binding.edtName.getText().toString());
        map.put("email", binding.edtEmail.getText().toString());
        map.put("age", binding.edtAge.getText().toString());
        map.put("purl",binding.edtPurl.getText().toString());

        reference.push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    binding.edtName.setText("");
                    binding.edtEmail.setText("");
                    binding.edtAge.setText("");
                    binding.edtPurl.setText("");
                    Toast.makeText(MainActivity.this, "Data Saved", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(MainActivity.this, "Data Cancel", Toast.LENGTH_SHORT).show();
                }
                Log.i("jfbvkj", "onComplete: ");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("jfbvkj", "onFailure: " + e.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("jfbvkj", "onSuccess: ");
            }
        });


    }


}