package com.example.prady.walkytalky;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class ClientActivity extends AppCompatActivity {

    private EditText mInputEditText;
    private TextView mMessageTextView;
    private ImageView mSendImageView;
    private Client mClient;

    String clientName;
    String clientIP;
    int clientPort;

    Handler handler;
    String msg;

    boolean isActive;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_main);
        isActive = true;
        handler = new Handler(getMainLooper());
        Intent intent = getIntent();
         clientName = intent.getStringExtra("USER_NAME");
         clientIP = intent.getStringExtra("USER_IP");
         clientPort = intent.getIntExtra("USER_PORT",12345);


        mInputEditText = findViewById(R.id.InputEditText);
        mMessageTextView = findViewById(R.id.MessageTextView);
        mSendImageView = findViewById(R.id.SendImageView);
        mSendImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                mClient = new Client(clientIP,clientPort);
                while (isActive)
                {
                    try {
                        if(mClient != null)
                        {
                            msg = mClient.din.readUTF();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mMessageTextView.append("\n"+msg);
                                    Log.i("CLIENT",msg);
                                }
                            });
                        }
                        else
                            Log.i("CLIENT","Unconnected....");
                    } catch (IOException e) {
                        //e.printStackTrace();
                    }
                }
                Log.i("Client","closed");
                try {
                    mClient.clientSocket.close();
                } catch (IOException e) {
                    //
                }
            }
        });

        t.start();

    }


    private void sendMessage()
    {
        final String msg = mInputEditText.getText().toString();
        mMessageTextView.append("\n\t\t\t\t\t\t\t\t\t\t\t"+msg);
        Toast.makeText(getApplicationContext(),msg , Toast.LENGTH_LONG).show();
        mInputEditText.setText("");
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mClient.dout.writeUTF(msg);
                } catch (IOException e) {
                    //
                }
            }
        });
        t.start();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        isActive = false;
        finishAffinity();
    }
}
