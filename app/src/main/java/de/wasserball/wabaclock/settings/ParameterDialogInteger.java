package de.wasserball.wabaclock.settings;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import de.wasserball.wabaclock.R;

/**
 * Created by ajr on 03.12.2016.
 */

@SuppressLint("ValidFragment")
public class ParameterDialogInteger extends AppCompatDialogFragment {

    private IntegerSetting setting;
    private DialogListener listener;
    private NumberPicker numberSpinner;

    @SuppressLint("ValidFragment")
    ParameterDialogInteger(IntegerSetting setting) {
       this.setting = setting;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_number_picker, null);

        builder.setView(view)
                .setTitle(setting.title)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int value = numberSpinner.getValue();
                        listener.applyValue(setting, value);
                    }
                });

        numberSpinner = view.findViewById(R.id.numberSpinner);
        numberSpinner.setMinValue(setting.min);
        numberSpinner.setMaxValue(setting.max);
        numberSpinner.setValue(setting.value);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ServerDialogListener");
        }
    }

    public interface DialogListener {
        void applyValue(IntegerSetting setting, int value);
    }
}
