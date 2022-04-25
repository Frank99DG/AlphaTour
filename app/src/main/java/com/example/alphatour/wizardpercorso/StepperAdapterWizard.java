package com.example.alphatour.wizardpercorso;

import android.content.Context;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.alphatour.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

public class StepperAdapterWizard extends AbstractFragmentStepAdapter {

    public StepperAdapterWizard(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm, context);
    }

    public void StepperAdapterWizard() {
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
            case 3:
                return new Step4();
            default: return new Step1();
        }
    }

    @Override
    public int getCount() {
        return 4; //perchè gli step sono 4
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        StepViewModel.Builder builder = new StepViewModel.Builder(context);
        switch (position) {
            case 0:
                builder
                        .setTitle(R.string.create_path)
                        .setEndButtonLabel(R.string.next)
                        .setBackButtonLabel(R.string.dashboard_button_back)
                        .setBackButtonStartDrawableResId(R.drawable.ic_arrow_back_wizard);
                break;
            case 1:
                builder
                        .setTitle(R.string.choose_zone)
                        .setEndButtonLabel(R.string.next)
                        .setBackButtonLabel(R.string.backTo_path);
                break;
            case 2:
                builder
                        .setTitle(R.string.choose_object)
                     // .setEndButtonLabel(R.string.next)
                        .setEndButtonLabel("Riepilogo")
                     // .setBackButtonLabel(R.string.backTo_zone);
                        .setBackButtonLabel("Nuova");
                break;
            case 3:
                builder
                        .setTitle(R.string.review_path)
                        .setBackButtonVisible(false)
                        .setEndButtonLabel(R.string.end);
                break;
            default:
                throw new IllegalArgumentException("Unsupported position: " + position);
        }
        return builder.create();
    }
}
