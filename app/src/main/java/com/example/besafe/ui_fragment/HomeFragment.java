package com.example.besafe.ui_fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.besafe.R;
import com.example.besafe.inviteConnection;
import com.example.besafe.matchedConnection;
import com.example.besafe.receivedConnection;

import java.util.Objects;

// Home Fragment of the application, where we get the major three buttons,
// i.e, Invite connection button, Receive Connection Button, Matched Connection Button.
public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        ColorDrawable cd = new ColorDrawable(0xFF0F9214);


        if(getActivity() != null) {
            Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setBackgroundDrawable(cd);
            Window window = getActivity().getWindow();
            window.setStatusBarColor(Color.parseColor("#0a5c0c"));
        }

        Button inviteNewConnection = view.findViewById(R.id.inviteConnection);
        Button showConnection = view.findViewById(R.id.showConnection);
        Button receivedConnectionButton = view.findViewById(R.id.receiveConnection);

        // action triggered when the Invite New Connection button is pressed
        inviteNewConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), inviteConnection.class);
                startActivity(intent);
            }
        });

        // action triggered when the show Received Connection button is pressed
        receivedConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), receivedConnection.class);
                startActivity(intent);
            }
        });


        // action triggered when the show connection button is pressed
        showConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), matchedConnection.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
