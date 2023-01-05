package com.example.eaeprojekt;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList book_id, book_author, book_title, book_pages,book_cover,book_read;
    private Animation translate_animation;

    CustomAdapter(Activity activity,  Context context, ArrayList book_id, ArrayList book_title,
                  ArrayList book_author, ArrayList book_pages, ArrayList book_cover, ArrayList book_read){
        this.activity = activity;
        this.context = context;
        this.book_id = book_id;
        this.book_title = book_title;
        this.book_author = book_author;
        this.book_pages = book_pages;
        this.book_cover = book_cover;
        this.book_read = book_read;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);

    }




    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.book_id_txt.setText(String.valueOf(book_id.get(holder.getAdapterPosition())));
        holder.book_title_txt.setText(String.valueOf(book_title.get(holder.getAdapterPosition())));
        holder.book_author_txt.setText(String.valueOf(book_author.get(holder.getAdapterPosition())));
        holder.book_pages_txt.setText(String.valueOf(book_pages.get(holder.getAdapterPosition())));
        boolean readstatus = (Boolean)book_read.get(holder.getAdapterPosition());
        if (readstatus==true){
            holder.read.setBackgroundResource(R.drawable.open_book__1_);
        }else{
            holder.read.setBackgroundResource(R.drawable.book_of_black_cover_closed);
        }
        Book bookToGetCoverFor = new Book();
        bookToGetCoverFor.setCover_i(Integer.valueOf(String.valueOf(book_cover.get(holder.getAdapterPosition()))));
        BookCoverRestRequestor requestor = new BookCoverRestRequestor(context, holder.book_cover_img);
        requestor.execute(bookToGetCoverFor);
        holder.mainLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Buch löschen?");
                builder.setMessage("Sind Sie sicher das Sie das Buch wirklich löschen möchten?");
                //wenn Ja, werden alle Daten gelöscht und die App lädt neu
                builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        DatabaseHelper dbh = new DatabaseHelper(context);
                        dbh.deleteOneRow(String.valueOf(book_id.get(holder.getAdapterPosition())));
                        //Refresh Activity
                        Intent intent = new Intent(context, MainActivity.class);
                        activity.startActivity(intent);
                        ((Activity)context).finish();
                    }
                });
                //wenn Nein dann schließt sich das Fenster und die Daten bleiben bestehen
                builder.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
                return true;

            }
        });


        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("id", String.valueOf(book_id.get(holder.getAdapterPosition())));
                intent.putExtra("title", String.valueOf(book_title.get(holder.getAdapterPosition())));
                intent.putExtra("author", String.valueOf(book_author.get(holder.getAdapterPosition())));
                intent.putExtra("pages", String.valueOf(book_pages.get(holder.getAdapterPosition())));
                intent.putExtra("read", (Boolean) book_read.get(holder.getAdapterPosition()));
                activity.startActivityForResult(intent, 1);
            }
        });

    }




    @Override
    public int getItemCount() {

        return book_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView book_id_txt, book_title_txt, book_author_txt, book_pages_txt;
        ImageView book_cover_img,read;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            book_id_txt = itemView.findViewById(R.id.book_id_txt);
            book_title_txt = itemView.findViewById(R.id.book_title_txt);
            book_author_txt = itemView.findViewById(R.id.book_author_txt);
            book_pages_txt = itemView.findViewById(R.id.book_pages_txt);
            book_cover_img = itemView.findViewById(R.id.book_cover_img);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            read = itemView.findViewById(R.id.book_read);
            //Animation RecyclerView
            translate_animation = AnimationUtils.loadAnimation(context, R.anim.translate_animation);
            mainLayout.setAnimation(translate_animation);
        }
    }
}
