package com.vidhyalearning.entamizhnaalkaati;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.util.Output;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.applandeo.materialcalendarview.EventDay;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


public class DailyFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    ImageView img;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button changeDate;
    private static final int ADD_NOTE = 44;
    public static final String RESULT = "result";
    public static final String EVENT = "event";
    public static final String NOTE = "note";
    static final int DATE_PICKER_ID = 1111;
    private String year;

    public DailyFragment() {
        // Required empty public constructor
    }


    public static DailyFragment newInstance() {
        DailyFragment fragment = new DailyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_daily, container, false);
         img  = view.findViewById(R.id.imageView3);
        changeDate = (Button) view.findViewById(R.id.changeDate);

        changeDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),MonthCalendarActivity.class);
                intent.putExtra("FROMDAILYSCREEN","Y");
                startActivityForResult(intent, ADD_NOTE);
            }

        });
        // downloadIcon();
        Date date = Calendar.getInstance().getTime();

        //
        // Display a date in day, month, year format
        //
        DateFormat formatter = new SimpleDateFormat("ddMMyyyy");

        String today = formatter.format(date);
        Log.d("My Tag Before",today);
         year = today.substring(4,8);
        if(today.charAt(0)=='0')
            today = today.substring(1,8);
       Log.d("My Tag",today);
        Log.d("My Tag",year);
       Picasso.with(getContext()).load("somewebsite" +"/"+ year + "/"+ today +".jpg").into(img);
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_NOTE && resultCode == RESULT_OK) {

            String dateStr = data.getStringExtra("DailyCalendar");

            try {


                Picasso.with(getContext()).load("http://www.tamildailycalendar.com" +"/"+ year + "/"+ dateStr +".jpg").into(img);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
            /*catch (ParseException e) {
                e.printStackTrace();
            }
            //events.add(eventDay);
            //calendarView.setEvents(events);
        }*/

    }
}
