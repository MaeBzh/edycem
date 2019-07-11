package com.imie.edycem.view.workingtime;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.imie.edycem.provider.contract.UserContract;
import com.imie.edycem.provider.contract.WorkingTimeContract;
import com.imie.edycem.provider.utils.UserProviderUtils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WorkingTimeFragment extends Fragment implements View.OnClickListener {

    private TextView date;
    private WorkingTimeActivity activity;
    private WorkingTime workingTime;
    private Spinner hours;
    private Spinner minutes;
    private Spinner spinnerName;
    private UserProviderUtils userProviderUtils;
    private ArrayList<User> users = new ArrayList<>();
    private EditText comment;
    private Button nextButton;
    private Button previousButton;
    private User connectedUser;
    private User contributor;

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_working_time, container, false);
        this.initComponents(view);
        return view;
    }

    public void initComponents(View view) {

        this.nextButton = (Button) view.findViewById(R.id.btn_next);
        this.nextButton.setOnClickListener(this);

        this.previousButton = (Button) view.findViewById(R.id.btn_previous);
        this.previousButton.setOnClickListener(this);

        Intent intent = getActivity().getIntent();
        this.connectedUser = intent.getParcelableExtra(UserContract.TABLE_NAME);
        this.workingTime = intent.getParcelableExtra(WorkingTimeContract.TABLE_NAME);

        this.date = (TextView) view.findViewById(R.id.edit_date);
        DateTimeFormatter formatter = DateTimeFormat.forPattern(getString(R.string.date_pattern));
        final DatePickerDialog.OnDateSetListener dateSetListener;
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                WorkingTimeFragment.this.date.setText(String.format("%s/%s/%s", day, month + 1, year));
                DateTimeFormatter fmt= DateTimeFormat.forPattern(getString(R.string.date_pattern));
                DateTime dateTime = fmt.parseDateTime(WorkingTimeFragment.this.date.getText().toString());
                WorkingTimeFragment.this.workingTime.setDate(dateTime);
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

        this.spinnerName = (Spinner) view.findViewById(R.id.spinner_name);

        this.userProviderUtils = new UserProviderUtils(this.getContext());
        this.users.addAll(this.userProviderUtils.queryAll());

        List<String> spinnerNameArray = new ArrayList<String>();
        for (User user : users) {
            spinnerNameArray.add(String.format("%s %s", user.getFirstname(), user.getLastname()));
        }
        ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(
                this.getContext(), android.R.layout.simple_spinner_item, spinnerNameArray);

        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerName.setAdapter(nameAdapter);
        this.spinnerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String userStr = (String) spinnerName.getSelectedItem();
                String[] splitStr = userStr.split(" ");
                String firstname = splitStr[0];
                String lastname = splitStr[1];
                WorkingTimeFragment.this.contributor = WorkingTimeFragment.this.userProviderUtils.queryWithName(firstname, lastname);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_next){
            Intent intent = new Intent(this.getContext(), SummaryActivity.class);
            intent.putExtra(UserContract.TABLE_NAME, (Parcelable) this.connectedUser);
            intent.putExtra("contributor", (Parcelable) this.contributor);
            intent.putExtra(WorkingTimeContract.TABLE_NAME, (Parcelable) this.workingTime);
            startActivity(intent);
        }
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
