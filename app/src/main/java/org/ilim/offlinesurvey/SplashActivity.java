package org.ilim.offlinesurvey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class SplashActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        Thread thread = new Thread() {

            @Override
            public void run() {
                synchronized (this) {
                    try {
                        wait(2000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        startActivity(new Intent(getBaseContext(), MenuMainActivity.class));
                        finish();
                    }
                }
            }
        };
        thread.start();
    }

    @Override
    public void onBackPressed() {
        //Nothing bro
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }


}
