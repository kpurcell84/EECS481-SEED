package edu.umich.seedforandroid.doctor;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import edu.umich.seedforandroid.R;

public class AbousUsDoctor extends Activity implements View.OnClickListener  {

    private Button bEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abous_us_doctor);

        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00274c")));
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#FAFAFA")));
        actionBar.setIcon(R.drawable.seed_system_letter_icon);
        actionBar.setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "About Us" + "</font>")));

        bEmail = (Button) findViewById(R.id.bEmail);
        bEmail.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)  {

        getMenuInflater().inflate(R.menu.menu_abous_us_doctor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)  {

        int id = item.getItemId();

        if (id == R.id.home) {

            goToMainActivityDoctor();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void goToMainActivityDoctor()  {

        Intent i = new Intent(AbousUsDoctor.this, MainActivity_Doctor.class);
        Bundle extras = new Bundle();
        extras.putInt("tabSelection", MainActivity_Doctor.MYPATIENTS);
        i.putExtras(extras);
        startActivity(i);
    }

    private void emailSeed()  {

        String emailaddress[] = { "seedsystem00@gmail.com" };

        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, emailaddress);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Concern/Question from Doctor");
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Dear SEED System, ");
        startActivity(emailIntent);
    }

    @Override
    public void onClick(View v)  {

        if (v.getId() == R.id.bEmail)  {

            emailSeed();
        }
    }
}