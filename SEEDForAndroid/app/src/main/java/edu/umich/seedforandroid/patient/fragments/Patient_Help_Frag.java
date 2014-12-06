package edu.umich.seedforandroid.patient.fragments;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.util.SharedPrefsUtil;

public class Patient_Help_Frag extends Fragment implements View.OnClickListener  {

    private Button bEmail, bEmergency;
    private SharedPrefsUtil sharedPrefsUtilInst;
    private int appVersion;
    private TextView tvAppVersion;

    public Patient_Help_Frag()  {}

    @Override
    public void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        View view = inflater.inflate(R.layout.fragment_patient__help_, container, false);

        initialSetup(view);

        return view;
    }

    private void initialSetup(View view)  {

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle("Help");
        setHasOptionsMenu(false);
        sharedPrefsUtilInst = new SharedPrefsUtil(getActivity().getApplicationContext());
        appVersion = sharedPrefsUtilInst.getAppVersion();

        tvAppVersion = (TextView) view.findViewById(R.id.tvVersion);
        tvAppVersion.setText(padVersionString());
        bEmail = (Button) view.findViewById(R.id.bEmail);
        bEmergency = (Button) view.findViewById(R.id.bEmergency);
        bEmail.setOnClickListener(this);
        bEmergency.setOnClickListener(this);
    }

    private void emailSeedSystem()  {

        String emailaddress[] = { "strahald@gmail.com" };

        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, emailaddress);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Concern/Question from Client");
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Dear Dr. Smeagol, ");
        startActivity(emailIntent);
    }

    private String padVersionString()  {

        return String.valueOf(appVersion).concat(".0");
    }

    private void emergency()  {

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:911"));
        startActivity(intent);
    }

    @Override
    public void onClick(View v)  {

        if (v.getId() == R.id.bEmail)  {

            emailSeedSystem();
        }
        else if (v.getId() == R.id.bEmergency)  {

            emergency();
        }
    }
}