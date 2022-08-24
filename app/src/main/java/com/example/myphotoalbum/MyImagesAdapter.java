package com.example.myphotoalbum;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyImagesAdapter extends RecyclerView.Adapter<MyImagesAdapter.imageViewHolder> {

    List<MyImages> ImageList = new ArrayList<>();
    private onImageClickListener listener;

    public void setListener(onImageClickListener listener) {
        this.listener = listener;
    }

    public void setImageList(List<MyImages> imageList) {
        ImageList = imageList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public imageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.imagecard,parent,false);

        return new imageViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull imageViewHolder holder, int position) {

        MyImages myImages = ImageList.get(position);
        holder.title.setText(myImages.getImg_title());
        holder.desc.setText(myImages.getImg_desc());
        holder.img.setImageBitmap(BitmapFactory.decodeByteArray(myImages.getImage(), 0,myImages.getImage().length));

    }

    public MyImages getPosition(int position){
        return ImageList.get(position);
    }

    public void restoreItem(MyImages myImages) {
        ImageList.add(myImages);

    }

    public interface onImageClickListener{
        void onImageClick(MyImages myImages);
    }

    @Override
    public int getItemCount() {
        return ImageList.size();
    }

    public class imageViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView title,desc;

        public imageViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.imageView);
            title = itemView.findViewById(R.id.textViewTitle);
            desc = itemView.findViewById(R.id.textViewDesc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && listener!=null){
                        listener.onImageClick(ImageList.get(getAdapterPosition()));
                    }
                }
            });

        }
    }
}
