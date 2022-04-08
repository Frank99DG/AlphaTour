package com.example.alphatour.wizardcreazione;

import android.content.Context;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.alphatour.wizard.Step1;
import com.example.alphatour.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

public class StepperAdapterCreazioneWizard extends AbstractFragmentStepAdapter {

    public StepperAdapterCreazioneWizard(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {

        switch (position){
            case 0:
                return new CreatePlaceWizard();
            case 1:
                return new CreateZoneWizard();
            case 2:
                return new CreateObjectWizard();

            default: return new Step1();

        }
    }

    @Override
    public int getCount() {
        return 3; //perchè gli step sono 5
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        StepViewModel.Builder builder = new StepViewModel.Builder(context);
        switch (position) {
            case 0:
                builder
                        .setEndButtonLabel(R.string.place_button_next)
                        .setBackButtonLabel(R.string.dashboard_button_back)
                        .setTitle(R.string.title_creation_1)
                        .setBackButtonStartDrawableResId(R.drawable.ic_arrow_back_wizard);
                break;
            case 1:
                builder
                        .setTitle(R.string.title_creation_2)
                        .setEndButtonLabel(R.string.object_button_next)
                        .setBackButtonLabel(R.string.museum_button_back);
                        //.setBackButtonStartDrawableResId(R.drawable.ms_back_arrow);
                break;
            case 2:
                builder
                        .setTitle(R.string.title_creation_3)
                        .setBackButtonLabel(R.string.place_button_back)
                        .setEndButtonLabel(R.string.button_complete);
                break;
            default:
                throw new IllegalArgumentException("Unsupported position: " + position);
        }
        return builder.create();
    }
}
