package cs.cyprusscores;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class FeedbackActivity extends AppCompatActivity {

    private static final String SMTP_HOST_NAME = "smtp.gmail.com";
    private static final String SMTP_AUTH_USER = "antonisch.77@gmail.com";
    private static final String SMTP_AUTH_PWD = "antonisafc77";
    private static Message message;
    String file = "feedbackfile.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        final RadioGroup radio = findViewById(R.id.emailanswer);
        final EditText email = findViewById(R.id.emailaddress);
        radio.setOnCheckedChangeListener((group, checkedId) -> {

            View radioButton = radio.findViewById(checkedId);
            int index = radio.indexOfChild(radioButton);

            switch (index) {
                case 0:
                    email.setEnabled(true);
                    email.setHint("Type your email address");
                    break;
                case 1:
                    email.setEnabled(false);
                    email.setHint("You will not receive any email");
                    if (!email.getText().toString().equals(""))
                        email.setText("");
                    break;
            }
        });

        RadioGroup rgl = findViewById(R.id.likeanswer);
        RadioGroup rge = findViewById(R.id.emailanswer);
        EditText rev = findViewById(R.id.reviewtext);
        EditText emailaddress = findViewById(R.id.emailaddress);
        RatingBar rb = findViewById(R.id.rating);
        int counter1 = 0;
        int counter2 = 0;
        int counter3 = 0;
        String res1;
        String res2;
        String res3;

        try {
            FileInputStream fin = openFileInput(file);
            DataInputStream in = new DataInputStream(fin);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            while ((strLine = br.readLine()) != null) {
                for (int i = 0; i < strLine.indexOf("$"); i++) {
                    counter1++;
                }
                for (int ii = counter1 + 1; ii < strLine.indexOf("$$"); ii++) {
                    counter2++;
                }
                for (int iii = counter1 + 1 + counter2 + 2; iii < strLine.indexOf("$$$"); iii++) {
                    counter3++;
                }

                res1 = strLine.substring(0, counter1);
                res2 = strLine.substring(counter1 + 1, counter1 + 1 + counter2);
                rev.setText(strLine.substring(counter1 + 1 + counter2 + 2, counter1 + 1 + counter2 + 2 + counter3));
                res3 = strLine.substring(counter1 + 1 + counter2 + 2 + counter3 + 3, strLine.length());

                float stars = Float.parseFloat(res1);
                rb.setRating(stars);

                if (res2.equals("yes")) {
                    rgl.check(R.id.likeyes);
                } else if (res2.equals("no")) {
                    rgl.check(R.id.likeno);
                }

                if (!res3.equals("no")) {
                    rge.check(R.id.emailyes);
                    emailaddress.setText(res3);
                } else if (res3.equals("no")) {
                    rge.check(R.id.emailno);
                }
            }
            in.close();
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            return (mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting());
        } else {
            return false;
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

    public void sendInfo(View view) {
        Intent sendfeed = new Intent(this, ShowFeedbackActivity.class);
        RadioGroup rg = findViewById(R.id.likeanswer);
        int selected = rg.getCheckedRadioButtonId();
        String ans = "";
        String impr = "";
        String review = "";
        String data1 = "";
        String data2 = "";
        String data3 = "";
        String data4 = "";
        if (selected == R.id.likeyes) {
            ans = "Yes";
            impr = "we are glad to hear that!";
            data2 = "yes" + "$$";
        } else if (selected == R.id.likeno) {
            ans = "No";
            impr = "we are sorry to hear that!";
            data2 = "no" + "$$";
        }
        RatingBar rt = findViewById(R.id.rating);
        double rate = rt.getRating();
        data1 = String.valueOf(rate) + "$";
        EditText rev = findViewById(R.id.reviewtext);
        review = rev.getText().toString();
        data3 = rev.getText().toString() + "$$$";
        RadioGroup rge = findViewById(R.id.emailanswer);
        int emailanswer = rge.getCheckedRadioButtonId();
        EditText email = findViewById(R.id.emailaddress);
        String emailaddress = email.getText().toString();

        if (emailanswer == R.id.emailyes) {
            if (isConnected(FeedbackActivity.this)) {
                if (emailaddress.isEmpty()) {
                    Toast.makeText(this, "You must enter your email address", Toast.LENGTH_SHORT).show();
                } else {

                    String from = "antonisch.77@gmail.com";

                    final String username = SMTP_AUTH_USER;
                    final String password = SMTP_AUTH_PWD;

                    String host = SMTP_HOST_NAME;

                    Properties props = new Properties();
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.starttls.enable", "true");
                    props.put("mail.smtp.host", host);
                    props.put("mail.smtp.port", "587");

                    Session session = Session.getInstance(props,
                            new javax.mail.Authenticator() {
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(username, password);
                                }
                            });

                    try {
                        message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(from));
                        message.setRecipients(Message.RecipientType.TO,
                                InternetAddress.parse(emailaddress));
                        message.setSubject("Our respond to your review");
                        BodyPart messageBodyPart = new MimeBodyPart();
                        if (selected == R.id.likeyes) {
                            messageBodyPart.setContent("Thank you for your review, we are very glad to hear that you like our app. If you have further" +
                                    " suggestions about our application feel free to mention them. We will always try our best to provide you" +
                                    " more content and frequent updates.", "text/html");
                        } else if (selected == R.id.likeno) {
                            messageBodyPart.setContent("Thank you for your review, we are sorry to hear that you dont like our app. " +
                                    "Our team will always welcome creative and constructive ideas to improve our application. We will always try our best to provide you" +
                                    "more content and frequent updates.", "text/html");
                        }
                        Multipart multipart = new MimeMultipart();
                        multipart.addBodyPart(messageBodyPart);
                        message.setContent(multipart);

                        final Thread thread = new Thread(() -> {
                            try {
                                Transport.send(message);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

                        thread.start();
                        Toast.makeText(this, "Sending email...", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    Bundle info = new Bundle();
                    info.putDouble("rate", rate);
                    info.putString("ans", ans);
                    info.putString("impr", impr);
                    info.putString("review", review);
                    sendfeed.putExtras(info);
                    startActivity(sendfeed);
                }
            } else {
                Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT).show();
            }
            data4 = email.getText().toString();
        } else {
            data4 = "no";
            Bundle info = new Bundle();
            info.putDouble("rate", rate);
            info.putString("ans", ans);
            info.putString("impr", impr);
            info.putString("review", review);
            sendfeed.putExtras(info);
            startActivity(sendfeed);
        }

        try {
            FileOutputStream fout = openFileOutput(file, 0);
            fout.write(data1.getBytes());
            fout.write(data2.getBytes());
            fout.write(data3.getBytes());
            fout.write(data4.getBytes());
            fout.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
