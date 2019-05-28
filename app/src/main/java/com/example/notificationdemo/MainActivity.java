package com.example.notificationdemo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

public class MainActivity extends AppCompatActivity {
    public final static int NOTIFICATION_ID = 01;
    public final static String TEXT_REPLY = "text_reply";
    public final static int NOTIFICATION_D_ID = 02;
    public final String CHANNEL_ID = "p_notification";
    public final String CHANNEL_D_ID = "p_d_notification";
    Button btnNotification, btnDProgress, btnCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_main);

        btnNotification = findViewById(R.id.btnNotifiation);

        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createChannel();

                Intent i = new Intent(MainActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pi = PendingIntent.getActivity(MainActivity.this, 0, i, PendingIntent.FLAG_ONE_SHOT);

                Intent yi = new Intent(MainActivity.this, YesActivity.class);
                yi.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent ypi = PendingIntent.getActivity(MainActivity.this, 0, yi, PendingIntent.FLAG_ONE_SHOT);

                Intent ni = new Intent(MainActivity.this, NoActivity.class);
                ni.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent npi = PendingIntent.getActivity(MainActivity.this, 0, ni, PendingIntent.FLAG_ONE_SHOT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID);
                builder.setSmallIcon(R.drawable.ic_notification);
                builder.setContentTitle("Simple Notification");
                builder.setContentText("This is Demo Notification to test Notification.");
                builder.setDefaults(Notification.DEFAULT_ALL);
                builder.setPriority(NotificationManagerCompat.IMPORTANCE_MAX);
                builder.setColor(getResources().getColor(R.color.colorPrimary));
                builder.setAutoCancel(true);
                builder.setContentIntent(pi);
                builder.addAction(R.drawable.ic_notification, "YES", ypi);
                builder.addAction(R.drawable.ic_notification, "NO", npi);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    RemoteInput remoteInput = new RemoteInput.Builder(TEXT_REPLY).setLabel("Reply").build();

                    Intent replyIntent = new Intent(MainActivity.this, RemoteReceiver.class);
                    replyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    PendingIntent replyPendingIntent = PendingIntent.getActivity(MainActivity.this, 0, replyIntent, PendingIntent.FLAG_ONE_SHOT);
                    NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.drawable.ic_notification, "Reply", replyPendingIntent).addRemoteInput(remoteInput).build();
                    builder.addAction(action);
                }

                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.froyo);
                builder.setLargeIcon(bitmap);
                builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null));

                //builder.setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.big_text_notification)));

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MainActivity.this);
                notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
            }
        });

        btnCustom = findViewById(R.id.btnCustom);
        btnCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createChannel();

                Thread th1 = new Thread() {
                    @Override
                    public void run() {
                        try {
                            sleep(3000);

                            RemoteViews custome_normal = new RemoteViews(getPackageName(), R.layout.custom_notification_normal);
                            RemoteViews custome_expanded = new RemoteViews(getPackageName(), R.layout.custom_notification_expanded);

                            Intent i2 = new Intent(MainActivity.this, MainActivity.class);
                            i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            PendingIntent pi2 = PendingIntent.getActivity(MainActivity.this, 0, i2, PendingIntent.FLAG_ONE_SHOT);

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID);
                            builder.setSmallIcon(R.drawable.ic_notification);
                            builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
                            builder.setPriority(NotificationManagerCompat.IMPORTANCE_HIGH);
                            builder.setColor(getResources().getColor(R.color.colorPrimary));
                            builder.setStyle(new NotificationCompat.DecoratedCustomViewStyle());
                            builder.setCustomContentView(custome_normal);
                            builder.setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE);
                            builder.setCustomBigContentView(custome_expanded);
                            builder.setAutoCancel(true);
                            builder.setContentIntent(pi2);

                            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MainActivity.this);
                            notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
                        } catch (InterruptedException ie) {

                        }
                    }
                };
                th1.start();
            }
        });

        btnDProgress = findViewById(R.id.btnDProgress);
        btnDProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createChannel2();

                Intent dpi = new Intent(MainActivity.this, MainActivity.class);
                dpi.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent dppi = PendingIntent.getActivity(MainActivity.this, 0, dpi, PendingIntent.FLAG_ONE_SHOT);

                final NotificationCompat.Builder mDPBuilder = new NotificationCompat.Builder(MainActivity.this, CHANNEL_D_ID);
                mDPBuilder.setSmallIcon(R.drawable.ic_download);
                mDPBuilder.setContentTitle("Image Download");
                mDPBuilder.setContentText("Download in progress.");
                mDPBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                mDPBuilder.setColor(getResources().getColor(R.color.colorAccent));
                mDPBuilder.setAutoCancel(true);
                mDPBuilder.setContentIntent(dppi);

                final NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MainActivity.this);
                final int max_progress = 100;
                final int current_progress = 0;
                //mDPBuilder.setProgress(max_progress,current_progress,false);
                mDPBuilder.setProgress(0, 0, true);
                notificationManagerCompat.notify(NOTIFICATION_D_ID, mDPBuilder.build());

                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        int count = 0;
                        try {
                            while (count <= 100) {
                                count += 10;
                                sleep(1000);
                                //mDPBuilder.setProgress(max_progress,count,false);
                                //notificationManagerCompat.notify(NOTIFICATION_ID,mDPBuilder.build());
                            }
                            mDPBuilder.setContentText("Download Complete.");
                            mDPBuilder.setProgress(0, 0, false);
                            notificationManagerCompat.notify(NOTIFICATION_D_ID, mDPBuilder.build());
                        } catch (InterruptedException ie) {
                        }
                    }
                };
                thread.start();
            }
        });
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Personal Notifications";
            String descrption = "Include al the Personal Notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(descrption);
            notificationChannel.setShowBadge(true);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void createChannel2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Personal Download Notifications";
            String descrption = "Include al the Personal Download Notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel1 = new NotificationChannel(CHANNEL_D_ID, name, importance);
            notificationChannel1.setDescription(descrption);
            NotificationManager notificationManager1 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager1.createNotificationChannel(notificationChannel1);
        }
    }
}
