package com.imie.edycem.view.workingtime;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.imie.edycem.R;

import org.joda.time.DateTime;

import java.util.Calendar;

public class WorkingTimeFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private EditText date;
    final Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_working_time, container, false);

        this.initComponents(view);
        return view;
    }

    public void initComponents(View view) {
        this.date = (EditText) view.findViewById(R.id.edit_date);
        this.date.setHint(DateTime.now().toString());
        this.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), WorkingTimeFragment.this, year, month, day);
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        this.date.setText(String.format("%s/%s/%s", this.day, this.month, this.year));
    }
}
