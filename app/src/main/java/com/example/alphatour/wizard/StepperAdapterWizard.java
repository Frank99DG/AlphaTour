package com.example.alphatour.wizard;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;

public class StepperAdapterWizard extends AbstractFragmentStepAdapter {

    public StepperAdapterWizard(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {

        switch (position){

            case 1:
                return new Step2();
            case 2:
                return new Step3();
            case 3:
                return new Step4();
            case 4:
                return new Step5();
            default: return new Step1();

        }
    }

    @Override
    public int getCount() {
        return 5; //perchè gli step sono 5
    }
}
