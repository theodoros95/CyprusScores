package cs.cyprusscores;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class SquadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_squad);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), TeamActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    public void showPlayer1(View view) {
        Bundle info = new Bundle();
        info.putString("name", "Demetris Christophi");
        info.putString("age", "30");
        info.putString("nat", "Cypriot");
        info.putString("num", "77");
        info.putString("apps", "20");
        info.putString("goals", "8");
        info.putString("assists", "5");

        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtras(info);
        startActivity(intent);
    }

    public void showPlayer2(View view) {
        Bundle info = new Bundle();
        info.putString("name", "Matt Derbyshire");
        info.putString("age", "32");
        info.putString("nat", "British");
        info.putString("num", "27");
        info.putString("apps", "23");
        info.putString("goals", "16");
        info.putString("assists", "3");

        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtras(info);
        startActivity(intent);
    }

}
