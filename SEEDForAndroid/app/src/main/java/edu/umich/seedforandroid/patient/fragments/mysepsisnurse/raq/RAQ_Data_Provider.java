package edu.umich.seedforandroid.patient.fragments.mysepsisnurse.raq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RAQ_Data_Provider  {

    public static Map<String, List<String>> getInfo()  {

        HashMap<String, List<String>> questionAnswers = new HashMap<String, List<String>>();
        List<String> question1 = new ArrayList<String>();
        question1.add("Sepsis is a potentially life-threatening complication of an infection. Sepsis occurs when chemicals released into the bloodstream to fight the infection trigger" +
                " inflammatory responses throughout the body. This inflammation can trigger a cascade of changes that can damage multiple organ systems, causing them to fail.\n" +
                "If sepsis progresses to septic shock, blood pressure drops dramatically, which may lead to death.\n" +
                "Anyone can develop sepsis, but it's most common and most dangerous in older adults or those with weakened immune systems. Early treatment of sepsis, usually with antibiotics " +
                "and large amounts of intravenous fluids, improves chances for survival.");

        List<String> question2 = new ArrayList<String>();
        question2.add("The most common primary sources of infection resulting in sepsis are the lungs, the abdomen, and the urinary tract. Typically, 50% of all sepsis cases start as an infection in the lungs. " +
                "No source is found in one third of cases. The infectious agents are usually bacteria but can also be fungi and viruses. While gram-negative bacteria were previously the most common cause of sepsis, in the last decade, " +
                "gram-positive bacteria, most commonly staphylococci, are thought to cause more than 50% of cases of sepsis.");


        List<String> question3 = new ArrayList<String>();
        question3.add("Severe sepsis and septic shock are conditions at the fulminant end of sepsis, and the mortality rate in most medical centers remains quite high for septic\n" +
                "shock, despite the innovations in antibiotics and supportive managements.1,2 The speed and appropriateness of therapy administered in the initial hours after the syn\n" +
                "drome develops likely influences the outcome.2,3 Rivers et al.3 have demonstrated that goal-directed therapy aimed at normalizing hemodynamic parameters and reversing " +
                "tissue hypoxia within the first six hours of a patient's emergency department (ED) visit significantly decreased the morbidity and mortality in patients with severe sepsis " +
                "and septic shock. However, the clinical manifestations or laboratory findings that can reliably identify the septic patients at the greatest risk for subsequent deterioration are still lacking.");

        List<String> question4 = new ArrayList<String>();
        question4.add("Systemic inflammatory response syndrome [another definition of sepsis] is manifested by two or more of the following four variables:11,12 1) hyperthermia >38.38C or hypothermia <368C; 2) tachycardia " +
                "(rate >90 beats/min); 3) tachypnea (rate >20 breaths/min) or hypoxia (oxygen saturation <90% or need for oxygen supplementation of 0.4 FIO2 or higher to maintain adequate saturation); and 4) leukocytosis (white " +
                "blood cell count >12,000/mm3), leukopenia (white blood cell count <4,000/mm3), or immature forms >10%. In this study, septic shock referred to one or more of three clinical symptoms and signs (hypotension, abrupt " +
                "change in mental status, and acute oliguria [urine <0.5 mL/kg/h]) and associated with persistent arterial hypotension despite adequate volume resuscitation (20-40 mL/kg)\n" +
                "In a study about heart rate variation, The age, gender [among other criteria] were comparable for both groups [those who went into septic shock and those who did not] and yielded no significant p-values.");

        List<String> question5 = new ArrayList<String>();
        question5.add("\t\n" +
                "1. Body temperature below 36 C (degrees Celsius) or above 38 C. 2. Tachycardia, with heart rate above 90 beats per minute. 3. Tachypea (increased respiratory rate), with respiratory rate above 20 per minute, or arterial " +
                "partial pressure of carbon dioxide (PaCO2) less than 4.3 kPa (kilo Pascals), equivalent to 32 mmHg (millimeters of mercury). 4. White blood cell (WBC) count less than 4,000/mm 3 (cubic millimeter) or above 12,000/mm 3 , or " +
                "the presence of more than 10% immature neutrophils (band forms).");

        questionAnswers.put("What is Sepsis?", question1);
        questionAnswers.put("What are the causes of sepsis?", question2);
        questionAnswers.put("How do I know if I am at risk of septic shock?", question3);
        questionAnswers.put("Does a suspicious change in heart rate always indicate sepsis?", question4);
        questionAnswers.put("What are sepsis criteria?", question5);

        return questionAnswers;
    }
}