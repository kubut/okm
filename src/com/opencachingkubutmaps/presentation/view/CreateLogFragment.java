package com.opencachingkubutmaps.presentation.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;

import androidx.fragment.app.DialogFragment;

import com.opencachingkubutmaps.R;
import com.opencachingkubutmaps.domain.model.CacheModel;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

public class CreateLogFragment extends DialogFragment {
    private View view;
    private Spinner logTypeSpinner;
    private LinearLayout ratingView, passwordView;
    private RatingBar ratingBar;
    private EditText commentEditText, passwordEditText;

    private String cacheType;
    private boolean cacheRequiredPassword;

    // should be an interface
    private CacheLogsFragment parentFragment;

    public CreateLogFragment(CacheModel cacheModel) {
        this.cacheType = cacheModel.getType().getKey();
        this.cacheRequiredPassword = cacheModel.isPasswordRequired();
    }

    public static CreateLogFragment newInstance(CacheModel cacheModel) {
        return new CreateLogFragment(cacheModel);
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        parentFragment = (CacheLogsFragment) getParentFragment();

        setView();
        setListeners();
        return getDialogView();
    }

    private void setView() {
        LayoutInflater i = Objects.requireNonNull(getActivity()).getLayoutInflater();
        view = i.inflate(R.layout.fragment_create_log, null);

        logTypeSpinner = view.findViewById(R.id.logType);
        ratingView = view.findViewById(R.id.ratingView);
        ratingBar = view.findViewById(R.id.ratingBar);
        commentEditText = view.findViewById(R.id.comment);
        passwordView = view.findViewById(R.id.passwordView);
        passwordEditText = view.findViewById(R.id.password);

        if (!cacheRequiredPassword) {
            passwordView.setVisibility(View.GONE);
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                Objects.requireNonNull(getContext()),
                cacheType.equals("Event") ? R.array.create_log_types_event : R.array.create_log_types_normal,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        logTypeSpinner.setAdapter(adapter);
    }

    private void setListeners() {
        logTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    ratingView.setVisibility(View.VISIBLE);
                } else {
                    ratingView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                ratingView.setVisibility(View.GONE);
            }
        });
    }

    private AlertDialog getDialogView() {
        return new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.create_log_title))
                .setPositiveButton(getString(R.string.ok), this.onAccept())
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                )
                .setView(view)
                .create();
    }

    private DialogInterface.OnClickListener onAccept() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String selectedType = getSelectedLogType();
                int rating = (int) ratingBar.getRating();
                String comment = commentEditText.getText().toString();
                String pass = cacheRequiredPassword ? passwordEditText.getText().toString() : null;

                comment += "\n" + getString(R.string.create_log_comment_footer);

                try {
                    comment = URLEncoder.encode(comment, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                submitLog(selectedType, comment, rating, pass);

                dialog.dismiss();
            }
        };
    }

    private String getSelectedLogType() {
        int arrayResId = cacheType.equals("Event") ? R.array.create_log_types_values_event : R.array.create_log_types_values_normal;

        int selectedTypePosition = logTypeSpinner.getSelectedItemPosition();
        String[] types = getResources().getStringArray(arrayResId);

        return types[selectedTypePosition];
    }

    private void submitLog(String logType, String comment, int rating, String pass) {
        parentFragment.createLogCallback(logType, comment, rating, pass);
    }
}