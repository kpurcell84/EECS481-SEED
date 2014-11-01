package edu.umich.seedforandroid.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.doctor.MainActivity_Doctor;
import edu.umich.seedforandroid.patient.MainActivity_Patient;

public class MainActivity extends Activity implements View.OnClickListener  {

    private Button bLogin;
    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getActionBar().hide();

        bLogin = (Button) findViewById(R.id.bLogin);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

        bLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)  {

        if (v.getId() == R.id.bLogin)  {

            if (etUsername.getText().toString().contentEquals("doctor"))  {

                Intent intent = new Intent(MainActivity.this, MainActivity_Doctor.class);
                startActivity(intent);
            }
            else  {

                Intent intent = new Intent(MainActivity.this, MainActivity_Patient.class);
                startActivity(intent);
            }
        }
    }
}