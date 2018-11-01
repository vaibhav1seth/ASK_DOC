package com.example.vaibhav.askdoc.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.vaibhav.askdoc.MessageActivity;
import com.example.vaibhav.askdoc.Models.Chats;
import com.example.vaibhav.askdoc.Models.User;
import com.example.vaibhav.askdoc.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int MSG_TYPE_LEFT =0;
    public static final int MSG_TYPE_RIGHT =1;
    FirebaseUser fUser;
    public Context mContext;
    List<Chats> mChats;
    private String imageurl;
    public MessageAdapter(Context mContext,List<Chats> mChats,String imageurl){

        this.mChats = mChats;
        this.mContext= mContext;
        this.imageurl=imageurl;

    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_items_right, parent, false);

            return new MessageAdapter.ViewHolder(view);
        } else {

            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_items_left, parent, false);

            return new MessageAdapter.ViewHolder(view);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder Holder, int position) {

        Chats chats = mChats.get(position);
        Holder.show_message.setText(chats.getMessage());

        if (imageurl.equals("default")){

            Holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }

        else {

            Glide.with(mContext).load(imageurl).into(Holder.profile_image);
        }


    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView profile_image;


        public ViewHolder(View itemView){

            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image= itemView.findViewById(R.id.profile_image);

        }
    }

    @Override
    public int getItemViewType(int position) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChats.get(position).getSender().equals(fUser.getUid()))
        {
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }
    }
}
