package com.vidhyalearning.entamizhnaalkaati;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MonthCalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_calendar);
        final CalendarView datePicker = (CalendarView) findViewById(R.id.datePicker);
        Calendar max = Calendar.getInstance();
        max.set(2019,01,01);
        Calendar min = Calendar.getInstance();
        min.set(2017,11,01);
        datePicker.setMaximumDate(max);
        datePicker.setMinimumDate(min);
         String fromDailyScreen = "";
         if(getIntent().hasExtra("FROMDAILYSCREEN"))
        fromDailyScreen =getIntent().getStringExtra("FROMDAILYSCREEN");
        final EditText noteEditText = (EditText) findViewById(R.id.noteEditText);
        Button button = (Button) findViewById(R.id.addNoteButton);
        if(fromDailyScreen.equals("Y")){
            button.setText("OK");
            noteEditText.setVisibility(View.INVISIBLE);
        }
        else{
            button.setText("SAVE");
            noteEditText.setVisibility(View.VISIBLE);
        }

        final String finalFromDailyScreen = fromDailyScreen;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalFromDailyScreen.equals("Y")==false && noteEditText.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter a Note",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent returnIntent = new Intent();
                Calendar date = datePicker.getSelectedDate();
                Date date1=date.getTime();
                int numDate =date1.getDate();
                String dateStr;// = date.getTime().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("MMM/yyyy");
                dateStr = numDate + "/" + sdf.format(date.getTime());


                SimpleDateFormat sdf1 = new SimpleDateFormat("MMMM");
                String fullMonth = sdf1.format(date.getTime());
                DateFormat formatter = new SimpleDateFormat("ddMMyyyy");

                String dailyCalendar = formatter.format(date.getTime());

                Toast.makeText(getApplicationContext(), dateStr + " " + fullMonth, Toast.LENGTH_SHORT).show();

                returnIntent.putExtra(ImpDaysFragment.RESULT, dateStr);
                returnIntent.putExtra("FULLMONTH", fullMonth);
                returnIntent.putExtra(ImpDaysFragment.NOTE, noteEditText.getText().toString());
                returnIntent.putExtra("DailyCalendar", dailyCalendar);

                setResult(Activity.RESULT_OK, returnIntent);

                finish();
            }
        });
    }

}
