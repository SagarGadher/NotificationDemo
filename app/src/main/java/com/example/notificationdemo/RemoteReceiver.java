package com.example.notificationdemo;

import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class RemoteReceiver extends AppCompatActivity {

    TextView tvReply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_remote_receiver);

        tvReply = findViewById(R.id.tvReply);

        Bundle remotReply = RemoteInput.getResultsFromIntent(getIntent());

        if (remotReply != null) {
            String message = remotReply.getCharSequence(MainActivity.TEXT_REPLY).toString();
            tvReply.setText(message);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(MainActivity.NOTIFICATION_ID);
    }
}
