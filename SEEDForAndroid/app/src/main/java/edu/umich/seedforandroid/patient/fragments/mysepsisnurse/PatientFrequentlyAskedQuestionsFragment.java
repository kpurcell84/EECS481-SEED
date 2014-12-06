package edu.umich.seedforandroid.patient.fragments.mysepsisnurse;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.patient.fragments.mysepsisnurse.raq.RaqAdapter;

public class PatientFrequentlyAskedQuestionsFragment extends Fragment  {

    private ExpandableListView mExpandableListView;
    private RaqAdapter mAdapter;

    public PatientFrequentlyAskedQuestionsFragment()  {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        View view = inflater.inflate(R.layout.fragment_my_sepsis_nurse__faq_, container, false);

        initialSetup(view);

        return view;
    }

    private void initialSetup(View view)  {

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle("Frequently Asked Questionss");

        mExpandableListView = (ExpandableListView) view.findViewById(R.id.exp_list);
        mAdapter = new RaqAdapter(getActivity().getApplicationContext(),
                new LinkedHashMap<String, List<String>>());
        mExpandableListView.setAdapter(mAdapter);

        getQuestions();
    }

    private void getQuestions()  {

        HashMap<String, List<String>> questionAnswers = new HashMap<String, List<String>>();
        List<String> question1 = new ArrayList<String>();
        question1.add("Sepsis is a heightened systemic immune response state due to an infection. It is defined as a combination of Systemic Inflammatory Response Syndrome (SIRS), and a confirmed or suspected infection, usually caused by bacteria.");

        List<String> question2 = new ArrayList<String>();
        question2.add("Body temperature below 36 C (degrees Celsius) or above 38 C. Tachycardia, with heart rate above 90 beats per minute. Tachypea (increased respiratory rate), with respiratory rate above 20 per minute, or arterial partial pressure of carbon dioxide (PaCO2) less than 4.3 kPa (kilo Pascals), equivalent to 32 mmHg (millimeters of mercury). White blood cell (WBC) count less than 4,000/mm3(cubic millimeter) or above 12,000/mm3, or the presence of more than 10% immature neutrophils (band forms).");

        List<String> question3 = new ArrayList<String>();
        question3.add("Sepsis can initiate not only through the direct dissemination of pathogens into the bloodstream but also indirectly as a result of postsurgical complications, traumas, burn, hemorrhages, and gut IR-mediated bacterial translocations.");

        List<String> question4 = new ArrayList<String>();
        question4.add("The real cause of death and organ failure in most patients dying of sepsis is unknown.");

        List<String> question5 = new ArrayList<String>();
        question5.add("Susceptibility to sepsis and the clinical course of patients with sepsis are both highly heterogeneous, which raises the strong possibility that the host response to infection is, at least in part, influenced by heritable factors.");

        List<String> question6 = new ArrayList<String>();
        question6.add("Direct costs per sepsis patient for ICU treatment in the United States have been estimated at more than $40,000.");

        questionAnswers.put("What is Sepsis?", question1);
        questionAnswers.put("What are the symptoms of Systemic Inflammatory Response Syndrome (SIRS)?", question2);
        questionAnswers.put("What initiates sepsis?", question3);
        questionAnswers.put("What is the cause of death in patients dying of sepsis?", question4);
        questionAnswers.put("Is sepsis hereditary?", question5);
        questionAnswers.put("What is the average cost of treating sepsis?", question6);

        mAdapter.replaceBackingData(questionAnswers);
        mAdapter.notifyDataSetChanged();
    }
}