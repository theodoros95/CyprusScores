package cs.cyprusscores;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.MessageDigest;

public class LoginActivity extends AppCompatActivity {

    private UserLoginTask mAuthTask = null;
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private static final String FILE = "credentials.txt";
    public static final String SPLIT = "\0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Sign In");
        mUsernameView = findViewById(R.id.username);
        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            }
            return false;
        });
        Button mSignInButton = findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(view -> attemptLogin());
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        mUsernameView.setError(null);
        mPasswordView.setError(null);
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError("Username is required");
            mUsernameView.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("Password is required");
            mPasswordView.requestFocus();
            return;
        }

        if (webNotAvailable()) {
            Toast.makeText(getBaseContext(), "Please check your web connection...", Toast.LENGTH_LONG).show();
            return;
        }

        showProgress(true);
        new UserLoginTask(username, password).execute();
    }


    private boolean webNotAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo == null || !activeNetworkInfo.isConnected();
    }

    public void register(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = encryptPassword(password);
        }

        private String encryptPassword(String password) {
            StringBuilder s = new StringBuilder();

            try {
                for (byte b : MessageDigest.getInstance("MD5").digest(password.getBytes())) {
                    s.append(String.format("%02x", b));
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

            return s.toString();
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                return new URL("http://seiis.cut.ac.cy/~website_oyk/verification.php?username=" +
                        mUsername + "&password=" + mPassword).openStream().read() == 49;
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                startActivity(new Intent(getBaseContext(), MainActivity.class));
            } else {
                mUsernameView.setError("Wrong credentials...");
                mUsernameView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        String s = mUsernameView.getText().toString() + SPLIT
                + mPasswordView.getText().toString();

        try {
            OutputStream fout = openFileOutput(FILE, MODE_PRIVATE);
            fout.write(s.replace("\n", "").getBytes());
            fout.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            InputStream in = openFileInput(FILE);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String[] s = br.readLine().split(SPLIT);

            mUsernameView.setText(s[0]);
            mPasswordView.setText(s[1]);

            in.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
