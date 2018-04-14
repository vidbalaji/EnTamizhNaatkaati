package com.vidhyalearning.entamizhnaalkaati;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.support.v4.app.Fragment;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MonthlyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ScaleGestureDetector scaleGDetector;

    public MonthlyFragment() {
        // Required empty public constructor
    }

    public static MonthlyFragment newInstance() {
        MonthlyFragment fragment = new MonthlyFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    float scale=1f;
    ImageView imgView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monthly, container, false);
          imgView = (ImageView) view.findViewById(R.id.imageView4);
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Toast.makeText(parent.getContext(),
                        "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                        Toast.LENGTH_SHORT).show();
                String month = parent.getItemAtPosition(pos).toString();
                month = month.substring(0, 3).toLowerCase();
                if (month.equals("jun") == false) {
                    Picasso.with(getContext()).load("http://ilearntamil.com/wp-content/uploads/2018/01/" + month + ".gif").into(imgView);

                } else {

                    Picasso.with(getContext()).load("http://ilearntamil.com/wp-content/uploads/2018/02/" + month + ".gif").into(imgView);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }

        });

        imgView.setImageResource(R.mipmap.ic_dec);
        Date date = Calendar.getInstance().getTime();

        //
        // Display a date in day, month, year format
        //
        DateFormat formatter = new SimpleDateFormat("MMM");
        String month = formatter.format(date);
        formatter = new SimpleDateFormat("MM");
        String numMonthStr = formatter.format(date);
        int numMonth = Integer.valueOf(numMonthStr);
        spinner.setSelection(numMonth - 1);
        month = month.toLowerCase();
        if (month.equals("jun") == false) {
            Picasso.with(getContext()).load("somewebsirte/" + month + ".gif").into(imgView);

        } else {

            Picasso.with(getContext()).load("somewebsite/" + month + ".gif").into(imgView);
        }

        scaleGDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
        imgView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent ev) {
                scaleGDetector.onTouchEvent(ev);

                return true;

            }
        });


        return view;

    }

    public boolean onTouchEvent(MotionEvent ev) {

        scaleGDetector.onTouchEvent(ev);

        return true;

    }


    private class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {

        public boolean onScaleBegin(ScaleGestureDetector sgd) {


            return true;


        }

        public void onScaleEnd(ScaleGestureDetector sgd) {


        }

        public boolean onScale(ScaleGestureDetector sgd) {

            // Multiply scale factor

            scale *= sgd.getScaleFactor();

            // Scale or zoom the imageview

            imgView.setScaleX(scale);

            imgView.setScaleY(scale);

            Log.i("Main", String.valueOf(scale));

            return true;

        }
    }
}