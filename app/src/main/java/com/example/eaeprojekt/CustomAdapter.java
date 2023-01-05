package com.example.eaeprojekt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
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
        holder.read_status.setChecked((Boolean)book_read.get(holder.getAdapterPosition()));
        Book bookToGetCoverFor = new Book();
        bookToGetCoverFor.setCover_i(Integer.valueOf(String.valueOf(book_cover.get(holder.getAdapterPosition()))));
        BookCoverRestRequestor requestor = new BookCoverRestRequestor(context, holder.book_cover_img);
        requestor.execute(bookToGetCoverFor);
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateActivity.class);
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
        CheckBox read_status;
        ImageView book_cover_img;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            book_id_txt = itemView.findViewById(R.id.book_id_txt);
            book_title_txt = itemView.findViewById(R.id.book_title_txt);
            book_author_txt = itemView.findViewById(R.id.book_author_txt);
            book_pages_txt = itemView.findViewById(R.id.book_pages_txt);
            book_cover_img = itemView.findViewById(R.id.book_cover_img);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            read_status= itemView.findViewById(R.id.book_read_status);
            //Animation RecyclerView
            translate_animation = AnimationUtils.loadAnimation(context, R.anim.translate_animation);
            mainLayout.setAnimation(translate_animation);
        }
    }
}
