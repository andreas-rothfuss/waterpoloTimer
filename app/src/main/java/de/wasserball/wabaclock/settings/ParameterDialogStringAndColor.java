package de.wasserball.wabaclock.settings;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import de.wasserball.wabaclock.R;

/**
 * Created by ajr on 03.12.2016.
 */

@SuppressLint("ValidFragment")
public class ParameterDialogStringAndColor extends AppCompatDialogFragment {

    private StringSetting stringSetting;
    private ColorSetting colorSetting;
    private DialogListener listener;
    private EditText stringEdit;
    private Button buttonTeamColor;

    @SuppressLint("ValidFragment")
    public ParameterDialogStringAndColor(StringSetting setting, ColorSetting colorSetting) {
        this.stringSetting = setting;
        this.colorSetting = colorSetting;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_string_and_color_edit, null);

        builder.setView(view)
                .setTitle(stringSetting.title)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String value = stringEdit.getText().toString();
                        listener.applyValue(stringSetting, value);
                        Drawable buttonBackground = buttonTeamColor.getBackground();
                        ColorDrawable buttonColor = (ColorDrawable) buttonBackground;
                        int color = buttonColor.getColor();
                        listener.applyValue(colorSetting, color);
                    }
                })
                .setNeutralButton("reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.applyValue(stringSetting, stringSetting.defaultVal);
                        listener.applyValue(colorSetting, colorSetting.defaultVal);
                    }
                });

        stringEdit = view.findViewById(R.id.teamName);
        stringEdit.setText(stringSetting.value);
        stringEdit.setTextColor(colorSetting.value);

        buttonTeamColor = view.findViewById(R.id.buttonTeamColor);
        buttonTeamColor.setBackgroundColor(colorSetting.value);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement DialogListener");
        }
    }
}
