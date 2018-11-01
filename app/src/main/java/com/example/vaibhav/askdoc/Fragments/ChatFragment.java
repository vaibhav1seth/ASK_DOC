package com.example.vaibhav.askdoc.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vaibhav.askdoc.Adapter.UsersAdapter;
import com.example.vaibhav.askdoc.Models.Chats;
import com.example.vaibhav.askdoc.Models.User;
import com.example.vaibhav.askdoc.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private UsersAdapter usersAdapter;
    private List<User> mUsers;
    FirebaseUser fuser;
    DatabaseReference reference;
    private List<String> usersList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_chat,container,false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                usersList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Chats chats = snapshot.getValue(Chats.class);

                    if(chats.getSender().equals(fuser.getUid())){

                        usersList.add(chats.getReciever());
                    }

                    if (chats.getReciever().equals(fuser.getUid())){

                        usersList.add(chats.getSender());
                    }
                }

                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    private void readChats(){

        mUsers = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    User user = snapshot.getValue(User.class);
                    //displays 1 user from chats

                    for (String id :usersList){

                        if (user.getId().equals(id)){

                            if (mUsers.size()!=0){

                                for (User user1 : mUsers){

                                    if (!user.getId().equals(user1.getId())){

                                        mUsers.add(user);
                                    }
                                }
                            }
                            else {
                                mUsers.add(user);
                            }
                        }
                    }
                }

                usersAdapter = new UsersAdapter(getContext(),mUsers);
                recyclerView.setAdapter(usersAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
