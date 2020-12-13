package com.swayam.instagramclone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolser> {
    private ArrayList views;
    private Context mContext;

    public Adapter(Context context)  {
        mContext = context;
        views = new ArrayList();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null){
                    for (Object object : objects){
                        addObject(object);
                    }
                }
            }
        });
    }

    private void addObject(Object object){
        views.add(object);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,null);
        return new MyViewHolser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolser holder, int position) {
        ParseObject object = (ParseObject)views.get(position);

        holder.username.setText(object.get("username").toString());
        holder.tagLine.setText(object.get("tagline").toString());

        ParseFile file =  object.getParseFile("image");
        if (file != null){
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeByteArray(file.getData(),0,file.getData().length);
            } catch (ParseException e) {}
            if (bitmap != null){
                holder.imageView.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public int getItemCount() {
        return views.size();
    }

    class MyViewHolser extends RecyclerView.ViewHolder{
        private TextView username,tagLine;
        private ImageView imageView;

        public MyViewHolser(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            tagLine = itemView.findViewById(R.id.tagLine);
            imageView = itemView.findViewById(R.id.imageView);

        }
    }
}
