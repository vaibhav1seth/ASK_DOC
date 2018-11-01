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
import com.example.vaibhav.askdoc.Models.User;
import com.example.vaibhav.askdoc.R;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    public Context mContext;
    List<User> mUsers;

    public UsersAdapter(Context mContext,List<User> mUsers){

        this.mUsers = mUsers;
        this.mContext= mContext;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.users_items,parent,false);

        return new UsersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder Holder, int position) {

        final User user = mUsers.get(position);
        Holder.username.setText(user.getUsername());
        if (user.getImageURL().equals("default"))
        {
            Holder.profile_image.setImageResource(R.mipmap.ic_launcher);

        }
        else {

            Glide.with(mContext).load(user.getImageURL()).into(Holder.profile_image);

        }

        Holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,MessageActivity.class);
                intent.putExtra("userid",user.getId());
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profile_image;


        public ViewHolder(View itemView){

            super(itemView);

            username = itemView.findViewById(R.id.username);
            profile_image= itemView.findViewById(R.id.profile_image);

        }
    }
}
