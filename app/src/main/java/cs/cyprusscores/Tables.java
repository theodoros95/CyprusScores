package cs.cyprusscores;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Tables extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables);
    }

    public void teamClicked(View view) {
        Toast.makeText(this, "Hello from ONONOIA!", Toast.LENGTH_LONG).show();
    }
}
