package edu.umich.seedforandroid.doctor.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.doctor.patientdata.DoctorViewPatientData;

public class MyPatients_Frag extends Fragment  {

    private List<DoctorPatientWrapper> myPatientList = new ArrayList<DoctorPatientWrapper>();
    ArrayAdapter<DoctorPatientWrapper> adapter;

    public MyPatients_Frag()  {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        View view = inflater.inflate(R.layout.fragment_my_patients_, container, false);

        initialSetup(view);

        return view;
    }

    private void initialSetup(View view)  {

        adapter = new PatientListAdapter();
        ListView list = (ListView) view.findViewById(R.id.patientlistView);
        list.setAdapter(adapter);

        // Testing
        DoctorPatientWrapper tmp = new DoctorPatientWrapper("1", "Andy", "Lee", "734-834-9095", "jinseok@umich.edu");
        myPatientList.add(tmp);
    }

    private void populatePatientList(DoctorPatientWrapper patientData)  {

        myPatientList.add(patientData);
    }

    private class PatientListAdapter extends ArrayAdapter<DoctorPatientWrapper>  {

        public PatientListAdapter()  {

            super(getActivity().getApplicationContext(), R.layout.doctor_patient_list_item, myPatientList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)  {

            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;

            if (itemView == null)  {

                itemView = getActivity().getLayoutInflater().inflate(R.layout.doctor_patient_list_item, parent, false);
            }

            // Find the item to work with.
            DoctorPatientWrapper currentPatient = myPatientList.get(position);

            // ID
            TextView tvID = (TextView) itemView.findViewById(R.id.tvPatientID);
            tvID.setText(currentPatient.getPatientID());

            // Name
            TextView tvName = (TextView) itemView.findViewById(R.id.tvPatientName);
            tvName.setText(currentPatient.getPatientFirstName().concat(" ").concat(currentPatient.getPatientLastName()));

            // Phone Number
            TextView tvPhoneNumber = (TextView) itemView.findViewById(R.id.tvPatientPhoneNumber);
            tvPhoneNumber.setText(currentPatient.getPatientPhoneNumber());

            // Email
            TextView tvEmail = (TextView) itemView.findViewById(R.id.tvPatientEmail);
            tvEmail.setText(currentPatient.getPatientEmail());

            // ImageView
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewPatientGender);

            // RelativeLayout
            RelativeLayout thisLayout = (RelativeLayout) itemView.findViewById(R.id.patientRelativeLayout);
            thisLayout.setOnClickListener(new View.OnClickListener()  {

                @Override
                public void onClick(View v)  {

                    TextView tv = (TextView) v.findViewById(R.id.tvPatientID);
                    gotoPatientDataPage(tv.getText().toString());
                }
            });
            return itemView;
        }
    }

    private void gotoPatientDataPage(String patientID)  {

        Intent i = new Intent(getActivity(), DoctorViewPatientData.class);
        Bundle extras = new Bundle();
        extras.putString("patient_id", patientID);
        i.putExtras(extras);
        startActivity(i);
    }
}