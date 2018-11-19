package cs.cyprusscores;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.CalendarView;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class CalendarActivity extends AppCompatActivity {
    protected static final String MESSAGE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setLayoutParams(new LayoutParams(MATCH_PARENT, MATCH_PARENT));
        layout.setGravity(Gravity.CENTER);

        CalendarView calendarView = new CalendarView(this);
        calendarView.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));

        setContentView(layout);
        layout.addView(calendarView);

        calendarView.setOnDateChangeListener((cView, y, m, d) -> startActivity(
                new Intent(this, MainActivity.class).putExtra(MESSAGE, date(y, m, d))));
    }

    protected static String dateNow() {
        return dateToString(Calendar.getInstance());
    }

    protected static String date(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return dateToString(calendar);
    }

    private static String dateToString(Calendar calendar) {
        return new SimpleDateFormat("EEE d MMM yyyy").format(calendar.getTime());
    }
}
