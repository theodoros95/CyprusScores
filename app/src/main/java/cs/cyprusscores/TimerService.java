package cs.cyprusscores;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class TimerService extends Service {

    public static final long interval = 15 * 1000;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;

    private Timer timer = new Timer();
    private int count = 0;

    public TimerService() {
    }

    public void onCreate() {
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            mTimer = new Timer();
        }
        mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, interval);
    }

    class TimeDisplay extends TimerTask {
        public void run() {
            mHandler.post(() -> {

                if (count == 15) {
                    Toast.makeText(getApplicationContext(), "15 seconds passed",
                            Toast.LENGTH_SHORT).show();
                } else if (count > 29) {
                    Intent dialogIntent = new Intent(getApplicationContext(), MainActivity.class);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);
                    Toast.makeText(getApplicationContext(),
                            "30 seconds passed. Redirecting to main page...",
                            Toast.LENGTH_SHORT).show();
                    stopSelf();
                }
            });
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int onStartCommand(Intent intent, int flag, int startId) {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Seconds running: " + count);
                count++;
            }
        }, 0, 1000);

        return super.onStartCommand(intent, flag, startId);
    }

    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
            mTimer.cancel();
            System.out.println("Service Stopped");
        }
        super.onDestroy();
    }
}
