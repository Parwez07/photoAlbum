package com.example.photoalbum;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyImagesAdapter  extends  RecyclerView.Adapter<MyImagesAdapter.MyViewHolder> {

    List<MyImagesModel> list = new ArrayList<>();
    private  OnImageClickListener listener;

    public void setListener(OnImageClickListener listener) {
        this.listener = listener;
    }

    public void setImagesList(List<MyImagesModel> imagesList){
        this.list = imagesList;
        notifyDataSetChanged();
    }

    public interface  OnImageClickListener{

        void onImageClick(MyImagesModel model);
    }

    public MyImagesModel getPosition(int position){

        return list.get(position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.img_card,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        MyImagesModel myImagesModel =list.get(position);

        holder.tvTitle.setText(myImagesModel.getImgTitle());
        holder.tvDiscription.setText(myImagesModel.getImgDiscription());
        holder.img.setImageBitmap(BitmapFactory.decodeByteArray(myImagesModel.getImage(),
                0,myImagesModel.getImage().length));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class  MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvTitle,tvDiscription;
        private ImageView img ;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDiscription = itemView.findViewById(R.id.tvDiscription);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            img = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(listener !=null && position != RecyclerView.NO_POSITION){

                        listener.onImageClick(list.get(position));
                    }
                }
            });
        }
    }
}
