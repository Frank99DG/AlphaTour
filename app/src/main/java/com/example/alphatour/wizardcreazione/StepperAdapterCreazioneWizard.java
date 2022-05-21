package com.example.alphatour.wizardcreazione;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
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

        switch (position) {
            case 0:
                return new CreatePlaceWizard();
            case 1:
                return new CreateZoneWizard();
            case 2:
                return new CreateObjectWizard();
            case 3:
                return new CreateConstraintsWizard();

            default:
                return new CreatePlaceWizard();

        }
    }

    @Override
    public int getCount() {
        return 4; //perchè gli step sono 5
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        StepViewModel.Builder builder = new StepViewModel.Builder(context);
        switch (position) {

            case 0:
                builder
                        .setTitle(R.string.title_creation_1)
                        .setBackButtonLabel(R.string.dashboard_button_back)
                        .setEndButtonLabel(R.string.zones_button_next)
                        .setBackButtonStartDrawableResId(R.drawable.ic_arrow_back_wizard);
                break;

            case 1:
                builder
                        .setTitle(R.string.title_creation_2)
                        .setBackButtonLabel(R.string.place_button_back)
                        .setEndButtonLabel(R.string.objects_button_next);
                        //.setBackButtonStartDrawableResId(R.drawable.ms_back_arrow);
                break;

            case 2:
                builder
                        .setTitle(R.string.title_creation_3)
                        .setBackButtonLabel(R.string.zones_button_back)
                        .setEndButtonLabel(R.string.constraints_button_next);
                break;

            case 3:
                builder
                        .setTitle(R.string.title_creation_4)
                        .setBackButtonVisible(false)
                        //.setBackButtonLabel(R.string.objects_button_back)
                        .setEndButtonLabel(R.string.button_complete);
                break;

            default:
                throw new IllegalArgumentException("Unsupported position: " + position);
        }
        return builder.create();
    }
}
