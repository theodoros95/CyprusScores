package cs.cyprusscores;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Switch;

public class TeamActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        WebView webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://en.wikipedia.org/wiki/AC_Omonia");

        //Animation
        ImageView image = findViewById(R.id.imageView);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        image.startAnimation(animation);
    }

    public void play(View view) {

        Switch s = findViewById(R.id.switch1);
        boolean status = s.isChecked();

        if (status) {
            mediaPlayer = MediaPlayer.create(this, R.raw.anthem);
            mediaPlayer.start();
        } else {
            mediaPlayer.stop();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_team, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.stadium) {
            Intent intentStadium = new Intent(this, StadiumActivity.class);
            startActivity(intentStadium);
            return true;
        }

        if (id == R.id.squad) {
            Intent intentSquad = new Intent(this, SquadActivity.class);
            startActivity(intentSquad);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
