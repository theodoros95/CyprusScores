package cs.cyprusscores;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Locale;

public class NotificationsSettings extends AppCompatActivity {

    String file = "notificationsettings.txt";
    TextToSpeech talk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_settings);

        final RadioGroup rgi = findViewById(R.id.icons);
        final RadioGroup rgc = findViewById(R.id.colors);

        int counter1 = 0;
        String res1;
        String res2;

        try {
            FileInputStream fin = openFileInput(file);
            DataInputStream in = new DataInputStream(fin);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            while ((strLine = br.readLine()) != null) {
                for (int i = 0; i < strLine.indexOf("."); i++) {
                    counter1++;
                }

                res1 = strLine.substring(0, counter1);
                res2 = strLine.substring(counter1 + 1, strLine.length());

                if (res1.equals("ball")) {
                    rgi.check(R.id.icon1);
                } else if (res1.equals("bell")) {
                    rgi.check(R.id.icon2);
                } else if (res1.equals("cfa2")) {
                    rgi.check(R.id.icon3);
                }

                if (res2.equals("red")) {
                    rgc.check(R.id.color1);
                } else if (res2.equals("green")) {
                    rgc.check(R.id.color2);
                } else if (res2.equals("blue")) {
                    rgc.check(R.id.color3);
                } else if (res2.equals("yellow")) {
                    rgc.check(R.id.color4);
                }
            }
            in.close();
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }

        talk = new TextToSpeech(getApplicationContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                talk.setLanguage(Locale.UK);
            }
        });
    }

    public void onResume() {
        startService(new Intent(this, TimerService.class));
        super.onResume();
    }

    public void onPause() {
        stopService(new Intent(this, TimerService.class));
        super.onPause();
    }

    public void saveSettings(View view) {
        Intent apply = new Intent(this, NotificationsActivity.class);
        RadioGroup rgi = findViewById(R.id.icons);
        int selectedic = rgi.getCheckedRadioButtonId();
        RadioGroup rgc = findViewById(R.id.colors);
        int selectedco = rgc.getCheckedRadioButtonId();

        String data1 = "";
        String data2 = "";

        if (selectedic == R.id.icon1) {
            data1 = "ball" + ".";
        } else if (selectedic == R.id.icon2) {
            data1 = "bell" + ".";
        } else if (selectedic == R.id.icon3) {
            data1 = "cfa2" + ".";
        } else {
            data1 = "no" + ".";
        }

        if (selectedco == R.id.color1) {
            data2 = "red";
        } else if (selectedco == R.id.color2) {
            data2 = "green";
        } else if (selectedco == R.id.color3) {
            data2 = "blue";
        } else if (selectedco == R.id.color4) {
            data2 = "yellow";
        } else {
            data2 = "no";
        }

        try {
            FileOutputStream fout = openFileOutput(file, 0);
            fout.write(data1.getBytes());
            fout.write(data2.getBytes());
            fout.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String say = "Settings Saved";
        talk.speak(say, TextToSpeech.QUEUE_FLUSH, null);
        startActivity(apply);
    }

    public void viewSettings(View view) {
        Intent i = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
        i.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        startActivity(i);
    }

}
