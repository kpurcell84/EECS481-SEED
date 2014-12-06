package edu.umich.seedforandroid.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import edu.umich.seedforandroid.R;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        runThread();
    }

    private void runThread()  {

        Thread timer = new Thread()  {

            public void run()  { //Thread look for "run" method

                try   {

                    sleep(1000);// how many miliseconds you want to sleep
                } catch (InterruptedException e)  {

                    e.printStackTrace();
                }
                finally  {

                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };

        timer.start();
    }
}