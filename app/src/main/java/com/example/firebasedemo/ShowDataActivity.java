package com.example.firebasedemo;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.example.firebasedemo.databinding.ActivityShowDataBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


public class ShowDataActivity extends AppCompatActivity {

    ActivityShowDataBinding binding;
    private RecyclerView recyclerView;

    private ShowDataAdapter adapter;
      private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        FirebaseRecyclerOptions<SampleModal> options =
                new FirebaseRecyclerOptions.Builder<SampleModal>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Post"), SampleModal.class)
                        .build();


        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ShowDataAdapter(options);
        binding.recyclerView.setAdapter(adapter);

       // Floating Button
        binding.btnFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowDataActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    // Search menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu with items using MenuInflator
      //  MenuInflater inflater = getMenuInflater();
       // inflater.inflate(R.menu.searchmenu, menu);

        getMenuInflater().inflate(R.menu.searchmenu,menu);

        // Initialise menu item search bar
        // with id and take its object
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                txtSearch(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                txtSearch(query);

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void txtSearch(String query) {

        FirebaseRecyclerOptions<SampleModal> options =
                new FirebaseRecyclerOptions.Builder<SampleModal>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Post").orderByChild("name").startAt(query).endAt(query+"\uf8ff"), SampleModal.class)
                        .build();

        adapter = new ShowDataAdapter(options);
        adapter.startListening();
        binding.recyclerView.setAdapter(adapter);


    }




}