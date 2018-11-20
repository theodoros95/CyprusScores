package cs.cyprusscores;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ShowFeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_feedback);

        Intent sendfeed = getIntent();
        Bundle info = sendfeed.getExtras();
        double rate = info.getDouble("rate");
        String ans = info.getString("ans");
        String impr = info.getString("impr");
        String review = info.getString("review");

        String message = "You rate our application with: " + String.valueOf(rate) + " stars (out of 5.0)" + "\n\n\nYou like the application: " + ans + ", " + impr + "\n\n\nYour review: " + review;
        TextView t = findViewById(R.id.msg);
        t.setText(message);
    }

    public void onResume() {
        startService(new Intent(this, TimerService.class));
        super.onResume();
    }

    public void onPause() {
        stopService(new Intent(this, TimerService.class));
        super.onPause();
    }

    public void goHome(View view) {
        Intent home = new Intent(this, FeedbackActivity.class);
        startActivity(home);
        Toast.makeText(getApplicationContext(), "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
    }
}
