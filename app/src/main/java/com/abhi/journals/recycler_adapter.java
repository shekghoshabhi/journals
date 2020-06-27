package com.abhi.journals;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.List;

import model.journalModel;

public class recycler_adapter extends RecyclerView.Adapter<recycler_adapter.ViewHolder> {
public Context context ;
public List<journalModel> list ;

    public recycler_adapter(Context context, List<journalModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public recycler_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.journal_row,parent , false) ;


        return new ViewHolder(view,context);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
           journalModel journalModel = list.get(position);

           String imageUrl ;
           holder.Title.setText(journalModel.getTitle());
           holder.description.setText(journalModel.getDescription());
        //   holder.name.setText(journalModel.getUserName());

           imageUrl = journalModel.getImageUrl() ;
           Picasso.get().load(imageUrl).placeholder(R.drawable.seashit).fit().into(holder.imageView);

           String time = (String) DateUtils.getRelativeTimeSpanString(journalModel.getTimeAdded().getSeconds()*1000);

           holder.time.setText(time);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView ;
        public TextView Title ;
        public TextView description , name ;
        public TextView time ;



        String  userName ;
        String userId ;



        public ViewHolder(@NonNull View itemView , Context ctx) {
            super(itemView);
            context = ctx ;


            imageView = itemView.findViewById(R.id.list_image);
            Title = itemView.findViewById(R.id.list_title);
            description = itemView.findViewById(R.id.list_descrip) ;
            time = itemView.findViewById(R.id.list_time);


        }
    }
}
