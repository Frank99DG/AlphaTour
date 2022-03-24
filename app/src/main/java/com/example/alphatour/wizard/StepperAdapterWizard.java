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

            case 0:
                return new Step1();
            case 1:
                return new Step2();
            case 2:
                return new Step3();
            default: return new Step1();

        }
    }

    @Override
    public int getCount() {
        return 3; //perchè gli step sono 3
    }
}
