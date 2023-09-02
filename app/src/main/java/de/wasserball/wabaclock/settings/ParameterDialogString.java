package de.wasserball.wabaclock.settings;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import de.wasserball.wabaclock.R;

/**
 * Created by ajr on 03.12.2016.
 */

@SuppressLint("ValidFragment")
public class ParameterDialogString extends AppCompatDialogFragment {

    private StringSetting setting;
    private DialogListener listener;
    private EditText editText;

    @SuppressLint("ValidFragment")
    public ParameterDialogString(StringSetting setting) {
       this.setting = setting;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_string_edit, null);

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
                        String value = editText.getText().toString();
                        listener.applyValue(setting, value);
                    }
                })
                .setNeutralButton("reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.applyValue(setting, setting.defaultVal);
                    }
                });

        editText = view.findViewById(R.id.master_ip);
        editText.setText(setting.value);

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
        void applyValue(StringSetting setting, String value);
    }
}
