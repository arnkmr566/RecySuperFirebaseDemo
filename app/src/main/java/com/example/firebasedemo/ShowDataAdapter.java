package com.example.firebasedemo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowDataAdapter extends FirebaseRecyclerAdapter<SampleModal, ShowDataAdapter.SampleViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

  //  private Context context;

    public ShowDataAdapter(@NonNull FirebaseRecyclerOptions<SampleModal> options) {
        super(options);
    //    this.context = context;

    }

    @SuppressLint("RecyclerView")
    @Override
    protected void onBindViewHolder(@NonNull SampleViewHolder holder, int position, @NonNull SampleModal model) {

        holder.sampleName.setText(model.getName());
        holder.sampleEmail.setText(model.getEmail());
        holder.sampleAge.setText(model.getAge());

        Glide
                .with(holder.profile_image.getContext())
                .load(model.getPurl())
                .centerCrop()
                .into(holder.profile_image);


        holder.btnEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DialogPlus dialogPlus = DialogPlus.newDialog(holder.profile_image.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true,550)  // This will enable the expand feature, (similar to android L share dialog)
                        .create();
              //  dialog.show();

                View view = dialogPlus.getHolderView();

                EditText name = view.findViewById(R.id.updeteName);
                EditText email = view.findViewById(R.id.updeteEmail);
                EditText age = view.findViewById(R.id.updateAge);
                EditText purl = view.findViewById(R.id.updateImageUrl);

                Button btnUpdate = view.findViewById(R.id.btnUpdate);

                name.setText(model.getName());
                email.setText(model.getEmail());
                age.setText(model.getAge());
                purl.setText(model.getPurl());





                dialogPlus.show();

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Map<String, Object> map = new HashMap<>();
                        map.put("name", name.getText().toString());
                        map.put("email", email.getText().toString());
                        map.put("age", age.getText().toString());
                        map.put("purl", purl.getText().toString());



                        FirebaseDatabase.getInstance().getReference().child("Post")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dialogPlus.dismiss();
                                    }
                                });



                    }
                });
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.sampleName.getContext());
                builder.setTitle("Are you sure");
                builder.setMessage("Delete data can`t be Undo.");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseDatabase.getInstance().getReference().child("Post")

                                .child(getRef(position).getKey()).removeValue();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    //    Toast.makeText(Context, "Canceled", Toast.LENGTH_SHORT).show();

                    }
                });
                builder.show();
            }
        });



    }

    @NonNull
    @Override
    public SampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample, parent, false);
        return new SampleViewHolder(view);

    }


    class SampleViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profile_image;

        TextView sampleName, sampleEmail, sampleAge;
        Button btnEdit, btnDelete;
        public SampleViewHolder(@NonNull View itemView) {
            super(itemView);

            sampleName = itemView.findViewById(R.id.sampleName);
            sampleEmail = itemView.findViewById(R.id.sampleEmail);
            sampleAge = itemView.findViewById(R.id.sampleAge);
            profile_image = itemView.findViewById(R.id.profile_image);

            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
