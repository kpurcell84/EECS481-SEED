package edu.umich.seedforandroid.doctor.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.api.ApiThread;
import edu.umich.seedforandroid.doctor.patientdata.DoctorViewPatientData;

public class MyAlerts_Frag extends Fragment  {

    private static final String TAG = MyAlerts_Frag.class.getSimpleName();

    private List<DoctorAlertsWrapper> myAlertsList = new ArrayList<DoctorAlertsWrapper>();
    private ArrayAdapter<DoctorAlertsWrapper> adapter;
    private ApiThread mApiThread;

    public MyAlerts_Frag()  {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        return inflater.inflate(R.layout.fragment_my_alerts_, container, false);
    }


    private class AlertsListAdapter extends ArrayAdapter<DoctorAlertsWrapper>  {

        public AlertsListAdapter()  {

            super(getActivity().getApplicationContext(), R.layout.doctor_alerts_list_item, myAlertsList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)  {

            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;

            if (itemView == null)  {

                itemView = getActivity().getLayoutInflater().inflate(R.layout.doctor_alerts_list_item, parent, false);
            }

            // Find the item to work with.
            DoctorAlertsWrapper currentPatient = myAlertsList.get(position);

            // Name
            TextView tvName = (TextView) itemView.findViewById(R.id.tvPatientName);
            final String patientNameTmp = currentPatient.getPatientFirstName().concat(" ").concat(currentPatient.getPatientLastName());
            tvName.setText(patientNameTmp);

            // Phone Number
            final TextView tvPhoneNumber = (TextView) itemView.findViewById(R.id.tvPatientPhoneNumber);
            tvPhoneNumber.setText(currentPatient.getPatientPhoneNumber());
            tvPhoneNumber.setOnClickListener(new View.OnClickListener()  {

                @Override
                public void onClick(View v)  {

                    popUpPhoneCallAlertDialog(tvPhoneNumber.getText().toString(), patientNameTmp);
                }
            });

            // Email
            final TextView tvEmail = (TextView) itemView.findViewById(R.id.tvPatientEmail);
            tvEmail.setText(currentPatient.getPatientEmail());
            tvEmail.setOnClickListener(new View.OnClickListener()  {

                @Override
                public void onClick(View v)  {

                    popUpEmailAlertDialog(tvEmail.getText().toString(), patientNameTmp);
                }
            });

            // ImageView
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewPatientGender);

            // RelativeLayout
            ImageView imageViewNext = (ImageView) itemView.findViewById(R.id.imageViewNextIcon);
            imageViewNext.setOnClickListener(new View.OnClickListener()  {

                @Override
                public void onClick(View v)  {

                    gotoPatientDataPage(tvEmail.getText().toString(), patientNameTmp);
                }
            });
            return itemView;
        }
    }

    private void popUpEmailAlertDialog(final String emailStr, String patientName)  {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Email ".concat(patientName).concat("?"));
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()  {

            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()  {

                    @Override
                    public void onClick(DialogInterface dialog, int id)  {

                        emailPatient(emailStr);
                    }
                });

        // Set the line color
        Dialog d = alertDialog.show();
        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        divider.setBackground(new ColorDrawable(Color.parseColor("#00274c")));
    }

    private void popUpPhoneCallAlertDialog(final String phoneNumber, String patientName)  {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Call ".concat(patientName).concat("?"));
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()  {

            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()  {

                    @Override
                    public void onClick(DialogInterface dialog, int id)  {

                        callPatient(phoneNumber);
                    }
                });

        // Set the line color
        Dialog d = alertDialog.show();
        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        divider.setBackground(new ColorDrawable(Color.parseColor("#00274c")));
    }

    private void emailPatient(String emailStr)  {

        String emailaddress[] = { emailStr };

        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, emailaddress);
        emailIntent.setType("plain/text");
        startActivity(emailIntent);
    }

    private void callPatient(String phoneNumber)  {

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:".concat(phoneNumber)));
        startActivity(intent);
    }

    private void gotoPatientDataPage(String patientEmail, String patientName)  {

        Intent i = new Intent(getActivity(), DoctorViewPatientData.class);
        Bundle extras = new Bundle();
        extras.putString("patient_email", patientEmail);
        extras.putString("patient_name", patientName);
        i.putExtras(extras);
        startActivity(i);
    }
}