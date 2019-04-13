package com.example.prady.walkytalky;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MainFragment extends Fragment {

    private EditText mUserNameEditText;
    private EditText mUserIpEditText;
    private EditText mUserPortNoEditText;
    private ImageView mSubmitImageView;

    Handler handler;
    String msg;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mUserNameEditText = view.findViewById(R.id.userNameEditText);
        mUserIpEditText = view.findViewById(R.id.userIpEditText);
        mUserPortNoEditText = view.findViewById(R.id.userPortNoEditText);
        mSubmitImageView = view.findViewById(R.id.SubmitImageView);
        mSubmitImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitInfo();
            }
        });

        handler = new Handler(getActivity().getMainLooper());
        return view;
    }

    private void submitInfo() {
        Log.d("Frag", "Submmit clicked");
        final String userName = mUserNameEditText.getText().toString();
        final String userIP = mUserIpEditText.getText().toString();
        final String userPortNo = mUserPortNoEditText.getText().toString();
        if (userName.length() == 0)
            mUserNameEditText.setHintTextColor(Color.RED);
        if (userIP.length() == 0)
            mUserIpEditText.setHintTextColor(Color.RED);
        if (userPortNo.length() == 0)
            mUserPortNoEditText.setHintTextColor(Color.RED);

        if (userName.length() > 0 && userIP.length() > 0 && userPortNo.length() > 0) {

            Intent intent = new Intent(getContext(), ClientActivity.class);
            intent.putExtra("USER_NAME",userName);
            intent.putExtra("USER_IP",userIP);
            intent.putExtra("USER_PORT",Integer.parseInt(userPortNo));
            startActivity(intent);


        }
    }

}
