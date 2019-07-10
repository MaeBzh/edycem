package com.imie.edycem.view.workingtime;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.imie.edycem.R;
import com.imie.edycem.entity.User;
import com.imie.edycem.entity.WorkingTime;
import com.imie.edycem.provider.utils.UserProviderUtils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;

public class WorkingTimeFragment extends Fragment {

    private TextView date;
    private WorkingTimeActivity activity;
    private WorkingTime workingTime;
    private Spinner hours;
    private Spinner minutes;
    private Button addUser;
    private ListView list;
    private UserProviderUtils userProviderUtils;
    private EditText comment;

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_working_time, container, false);//

        this.initComponents(view);
        return view;
    }

    public void initComponents(View view) {

        this.activity = (WorkingTimeActivity) this.getActivity();
        this.workingTime = this.activity.getWorkingTime();

        this.date = (TextView) view.findViewById(R.id.edit_date);
        DateTimeFormatter formatter = DateTimeFormat.forPattern(getString(R.string.date_pattern));
        final DatePickerDialog.OnDateSetListener dateSetListener;
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                WorkingTimeFragment.this.date.setText(String.format("%s/%s/%s", day, month + 1, year));
            }
        };

        String dateStr = formatter.print(DateTime.now());
        this.date.setText(dateStr);
        this.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        WorkingTimeFragment.this.getContext(),
                        android.R.style.Theme_Holo_Light_Dialog,
                        dateSetListener,
                        year, month, day);
                datePickerDialog.show();

            }
        });

        this.hours = (Spinner) view.findViewById(R.id.spinner_hour);
        this.hours.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                WorkingTimeFragment.this.workingTime.setSpentTime(Integer.parseInt(hours.getSelectedItem().toString()) * 60);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        this.minutes = (Spinner) view.findViewById(R.id.spinner_min);
        this.minutes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                WorkingTimeFragment.this.workingTime.setSpentTime(
                        WorkingTimeFragment.this.workingTime.getSpentTime()
                                + Integer.parseInt(minutes.getSelectedItem().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        this.addUser = (Button) view.findViewById(R.id.add_user_button);
        this.list = (ListView) view.findViewById(R.id.list);
        this.addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WorkingTimeFragment.this.list.getVisibility() == View.GONE) {
                    WorkingTimeFragment.this.list.setVisibility(View.VISIBLE);
                }
            }
        });

        this.userProviderUtils = new UserProviderUtils(this.getContext());
        ArrayList<User> users = userProviderUtils.queryAll();
        ArrayList<String> usernames = new ArrayList<>();
        for (User user : users) {
            usernames.add(String.format("%s %s", user.getFirstname(), user.getLastname()));
        }

        WorkingTimeFragment.ListAdapter arrayAdapter = new WorkingTimeFragment.ListAdapter(this.getContext(), usernames);
        this.list.setAdapter(arrayAdapter);


//       todo : get the date from text view
        this.workingTime.setDate(DateTime.now());

        this.comment = (EditText) view.findViewById(R.id.edit_comment);
        this.comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                WorkingTimeFragment.this.workingTime.setDescription(WorkingTimeFragment.this.comment.getText().toString());
            }
        });
    }

    public class ListAdapter extends ArrayAdapter<String> {

        private final Context context;
        private final ArrayList<String> itemsArrayList;

        public ListAdapter(Context context, ArrayList<String> itemsArrayList) {

            super(context, R.layout.list_item, itemsArrayList);

            this.context = context;
            this.itemsArrayList = itemsArrayList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.list_item, parent, false);

            TextView nameView = (TextView) rowView.findViewById(R.id.username);
            CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.checkbox);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked) {
//                        todo : add contributors to working time
                    }
                }
            });

            nameView.setText(itemsArrayList.get(position));

            return rowView;
        }
    }
}
