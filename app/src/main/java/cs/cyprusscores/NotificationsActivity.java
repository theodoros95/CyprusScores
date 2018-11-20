package cs.cyprusscores;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class NotificationsActivity extends AppCompatActivity {

    String file = "notificationsfile.txt";
    String file2 = "notificationsettings.txt";
    int ncolor;
    Bitmap nicon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nicon = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        ncolor = Color.BLACK;

        final CheckBox c1 = findViewById(R.id.goals);
        final CheckBox c2 = findViewById(R.id.news);
        final CheckBox c3 = findViewById(R.id.scores);
        final CheckBox c4 = findViewById(R.id.fixtures);
        final CheckBox c5 = findViewById(R.id.cards);
        int counter1 = 0;
        int counter2 = 0;
        int counter3 = 0;
        int counter4 = 0;
        int num1 = 0;
        String res1;
        String res2;
        String res3;
        String res4;
        String res5;
        String icon = "";
        String color = "";

        try {
            FileInputStream fin = openFileInput(file);
            DataInputStream in = new DataInputStream(fin);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;


            while ((strLine = br.readLine()) != null) {
                for (int i = 0; i < strLine.indexOf("."); i++) {
                    counter1++;
                }
                for (int ii = counter1 + 1; ii < strLine.indexOf(".."); ii++) {
                    counter2++;
                }
                for (int iii = counter1 + 1 + counter2 + 2; iii < strLine.indexOf("..."); iii++) {
                    counter3++;
                }
                for (int iiii = counter1 + 1 + counter2 + 2 + counter3 + 3; iiii < strLine.indexOf("...."); iiii++) {
                    counter4++;
                }

                res1 = strLine.substring(0, counter1);
                res2 = strLine.substring(counter1 + 1, counter1 + 1 + counter2);
                res3 = strLine.substring(counter1 + 1 + counter2 + 2, counter1 + 1 + counter2 + 2 + counter3);
                res4 = strLine.substring(counter1 + 1 + counter2 + 2 + counter3 + 3, counter1 + 1 + counter2 + 2 + counter3 + 3 + counter4);
                res5 = strLine.substring(counter1 + 1 + counter2 + 2 + counter3 + 3 + counter4 + 4, strLine.length());

                if (res1.equals("yes")) {
                    c1.setChecked(true);
                } else if (res1.equals("no")) {
                    c1.setChecked(false);
                }

                if (res2.equals("yes")) {
                    c2.setChecked(true);
                } else if (res2.equals("no")) {
                    c2.setChecked(false);
                }

                if (res3.equals("yes")) {
                    c3.setChecked(true);
                } else if (res3.equals("no")) {
                    c3.setChecked(false);
                }

                if (res4.equals("yes")) {
                    c4.setChecked(true);
                } else if (res4.equals("no")) {
                    c4.setChecked(false);
                }

                if (res5.equals("yes")) {
                    c5.setChecked(true);
                } else if (res5.equals("no")) {
                    c5.setChecked(false);
                }
            }
            in.close();
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }

        try {
            FileInputStream fin = openFileInput(file2);
            DataInputStream in = new DataInputStream(fin);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            while ((strLine = br.readLine()) != null) {
                for (int i = 0; i < strLine.indexOf("."); i++) {
                    num1++;
                }

                icon = strLine.substring(0, num1);
                color = strLine.substring(num1 + 1, strLine.length());

                if (icon.equals("ball")) {
                    nicon = BitmapFactory.decodeResource(getResources(), R.drawable.football);
                } else if (icon.equals("bell")) {
                    nicon = BitmapFactory.decodeResource(getResources(), R.drawable.bell);
                } else if (icon.equals("cfa2")) {
                    nicon = BitmapFactory.decodeResource(getResources(), R.drawable.cfa2);
                } else {
                    nicon = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
                }

                if (color.equals("red")) {
                    ncolor = Color.RED;
                } else if (color.equals("green")) {
                    ncolor = Color.GREEN;
                } else if (color.equals("blue")) {
                    ncolor = Color.BLUE;
                } else if (color.equals("yellow")) {
                    ncolor = Color.YELLOW;
                } else {
                    ncolor = Color.BLACK;
                }

            }
            in.close();
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }

    }

    public void onResume() {
        startService(new Intent(this, TimerService.class));
        super.onResume();
    }

    public void onPause() {
        stopService(new Intent(this, TimerService.class));
        super.onPause();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notifications, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settings) {
            Intent home = new Intent(this, NotificationsSettings.class);
            startActivity(home);
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendNotification(View view) {
        CheckBox c1 = findViewById(R.id.goals);
        CheckBox c2 = findViewById(R.id.news);
        CheckBox c3 = findViewById(R.id.scores);
        CheckBox c4 = findViewById(R.id.fixtures);
        CheckBox c5 = findViewById(R.id.cards);

        String selected = "";
        String data1 = "";
        String data2 = "";
        String data3 = "";
        String data4 = "";
        String data5 = "";

        if (c1.isChecked()) {
            selected += "Goals";
            data1 = "yes" + ".";
        } else {
            data1 = "no" + ".";
        }

        if (c2.isChecked()) {
            if (selected.equals("")) {
                selected += "News";
            } else {
                selected += ", News";
            }
            data2 = "yes" + "..";
        } else {
            data2 = "no" + "..";
        }

        if (c3.isChecked()) {
            if (selected.equals("")) {
                selected += "Scores (HT/FT)";
            } else {
                selected += ", Scores (HT/FT)";
            }
            data3 = "yes" + "...";
        } else {
            data3 = "no" + "...";
        }


        if (c4.isChecked()) {
            if (selected.equals("")) {
                selected += "Fixture Reminder";
            } else {
                selected += ", Fixture Reminder";
            }
            data4 = "yes" + "....";
        } else {
            data4 = "no" + "....";
        }


        if (c5.isChecked()) {
            if (selected.equals("")) {
                selected += "Yellow Cards / Red Cards";
            } else {
                selected += ", Yellow Cards / Red Cards";
            }
            data5 = "yes";
        } else {
            data5 = "no";
        }

        if ((!c1.isChecked()) && (!c2.isChecked()) && (!c3.isChecked()) && (!c4.isChecked()) && (!c5.isChecked())) {
            selected = "You haven't selected anything";
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "chan1";
            String description = "notification channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel1", name, importance);
            channel.setDescription(description);
            channel.enableVibration(true);

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .build();

            channel.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.unconvinced), audioAttributes);


            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

            Intent changenots = new Intent(this, NotificationsActivity.class);
            PendingIntent gotochange = PendingIntent.getActivity(this,
                    0, changenots, 0);

            NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "channel1")
                    .setSmallIcon(R.drawable.ball)
                    .setContentTitle("Things you want to be notified about")
                    .setContentText("Selected: " + selected)
                    .setLargeIcon(nicon)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("Selected: " + selected).setBigContentTitle("Things you want to be notified about").setSummaryText("Selections"))
                    .setColor(ncolor)
                    .setAutoCancel(true)
                    .addAction(R.mipmap.ic_launcher, "Change", gotochange) //Sto Oreo den dixni icon
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(1, notification.build());
        } else {
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = new Notification.Builder(getApplicationContext())
                    .setSmallIcon(R.drawable.ball)
                    .setContentTitle("Things you want to be notified about")
                    .setContentText(selected).build();
            manager.notify(1, notification);
        }

        try {
            FileOutputStream fout = openFileOutput(file, 0);
            fout.write(data1.getBytes());
            fout.write(data2.getBytes());
            fout.write(data3.getBytes());
            fout.write(data4.getBytes());
            fout.write(data5.getBytes());
            fout.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

