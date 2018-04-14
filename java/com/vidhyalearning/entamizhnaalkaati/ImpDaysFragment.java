package com.vidhyalearning.entamizhnaalkaati;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.ALARM_SERVICE;

public class ImpDaysFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

     CalendarView calendarView;
    // TODO: Rename and change types of parameters

    DBHelper mydb;
    private static final int ADD_NOTE = 44;
    public static final String RESULT = "result";
    public static final String EVENT = "event";
    public static final String NOTE = "note";

    public static final String TAG="NotificationScheduler";
    SwipeMenuListView listView;
    List<EventDay> events;
    private ArrayList<ImpDays> myArrayList;
    HashMap<String,Integer> myReminderKey;
    public ImpDaysFragment() {
        // Required empty public constructor
    }


    public static ImpDaysFragment newInstance() {
        ImpDaysFragment fragment = new ImpDaysFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    int arraySize=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_NOTE && resultCode == RESULT_OK) {
            String dateStr = data.getStringExtra(RESULT);
            String noteStr = data.getStringExtra(NOTE);
            String fullMoStr = data.getStringExtra("FULLMONTH");
            try {
                String tempYear = dateStr.substring(dateStr.length()-4);
                SimpleDateFormat sdf = new SimpleDateFormat("DD/MMM/yyyy");
                Date date1 = sdf.parse(dateStr);
                Toast.makeText(getActivity(), dateStr + " " + noteStr + " "+ tempYear + " " + fullMoStr, Toast.LENGTH_SHORT).show();
                mydb.insertRecord(tempYear, fullMoStr, noteStr, dateStr);
                //calendarView.setDate(eventDay.getCalendar());
                ImpDays tempObj = new ImpDays(tempYear, fullMoStr, noteStr, date1);
                myArrayList.add(tempObj);
                Date tempDate = tempObj.getDateOfDay();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date1);

                EventDay tempEvent = new EventDay(cal,R.drawable.ic_home_black_24dp);
                arraySize++;
                setReminder(getContext(),AlarmReceiver.class,cal,arraySize,noteStr);
                events.add(tempEvent);
                calendarView.setEvents(events);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    String year;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_imp_days, container, false);

        if(!(myArrayList!=null && myArrayList.size()>0)) {
            myArrayList = null;
            myReminderKey = null;

        }
        if(myReminderKey==null)
            myReminderKey = new HashMap<>();
        Button addNoteButton = (Button)view.findViewById(R.id.addNote);
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),MonthCalendarActivity.class);
                startActivityForResult(intent, ADD_NOTE);
            }
        });

        // Inflate the layout for this fragment
        DateFormat formatter = new SimpleDateFormat("ddMMyyyy");
        final Date date = Calendar.getInstance().getTime();
        String today = formatter.format(date);
        Log.d("My Tag Before",today);
        mydb = new DBHelper(getContext());
        Log.d("ROws COunt", " " + mydb.numberOfRows());
        year = today.substring(4,8);
        events = new ArrayList<>();


         if(mydb.getData(year)<=0) {
             String url = "somewebsite=" + year + "&&msg=Pournami%20" + year;

             RetrieveSiteData data = new RetrieveSiteData();
             data.execute(url);

         }
         if(!(myArrayList!=null && myArrayList.size()>0)) {
             myArrayList = mydb.getAllDays(year);
             arraySize = myArrayList.size();
             for (int i = 0; i < myArrayList.size(); i++) {
                 ImpDays tempObj = myArrayList.get(i);
                 Date tempDate = tempObj.getDateOfDay();

                 if (tempDate != null) {

                     Calendar cal = Calendar.getInstance();
                     cal.setTime(tempDate);
                     int imageResource = R.mipmap.daily;
                     if (tempObj.getDay().equals("Amavasai")) {
                         imageResource = R.mipmap.ic_amavasai;
                     } else if (tempObj.getDay().equals("Pournami")) {
                         imageResource = R.mipmap.ic_pournami;
                     } else if (tempObj.getDay().equals("Sankatahara Chathurthi")) {
                         imageResource = R.mipmap.chaturthi;
                     } else if (tempObj.getDay().equals("Pradosham")) {
                         imageResource = R.mipmap.pradosham;
                     } else if (tempObj.getDay().equals("Karthigai")) {
                         imageResource = R.mipmap.kartigai;
                     } else if (tempObj.getDay().equals("Ekadhasi")) {
                         imageResource = R.mipmap.ekadashi;
                     } else if (tempObj.getDay().equals("Sashti")) {
                         imageResource = R.mipmap.sashti;
                     }
                     setReminder(getContext(), AlarmReceiver.class, cal, i, tempObj.getDay());
                     EventDay tempEventDay = new EventDay(cal, imageResource);

                     events.add(tempEventDay);
                 }
             }
         }
        listView =(SwipeMenuListView) view.findViewById(R.id.listView1);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem delItem = new SwipeMenuItem(
                        getContext());
                // set item background
                delItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                delItem.setWidth(90);
                // set item title
                delItem.setTitle("Delete");
                // set item title fontsize
                delItem.setTitleSize(14);
                // set item title font color
                delItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(delItem);


            }
        };

// set creator
        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        String tempStr = (String)listView.getAdapter().getItem(position);
                        String leftStr = tempStr.substring(0,tempStr.indexOf('-')).trim();
                        String rightStr = tempStr.substring(tempStr.indexOf('-')+1,tempStr.length()).trim();
                        Toast.makeText(getContext(),leftStr + " " + rightStr,Toast.LENGTH_SHORT).show();
                        Integer isSucess = mydb.deleteRow(leftStr,rightStr);
                        if( myReminderKey.containsKey(tempStr)) {
                            int id = myReminderKey.get(tempStr);
                            cancelReminder(getContext(),AlarmReceiver.class,id);
                        }
                        break;

                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        // Right
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);

        // Left
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        DateFormat formatter1 = new SimpleDateFormat("MMMM");
        final Date date1 = Calendar.getInstance().getTime();
        String month = formatter1.format(date1);
        ArrayList<String> monthList = mydb.getAllDaysInMonth(year,month);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, monthList);
        listView.setAdapter(arrayAdapter);

        calendarView = (CalendarView) view.findViewById(R.id.calendarView2);
        Calendar calendar = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        max.set(2019,01,01);
        Calendar min = Calendar.getInstance();
        min.set(2017,11,01);
        calendarView.setMaximumDate(max);
        calendarView.setMinimumDate(min);
        events.add(new EventDay(calendar, R.drawable.ic_home_black_24dp));
        calendarView.setEvents(events);


        try {
            calendarView.setDate(Calendar.getInstance().getTime());
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                int tempDay =clickedDayCalendar.get(Calendar.DAY_OF_MONTH);
                String tempDayStr  =String.valueOf(tempDay);
                if(tempDayStr.length()==1)
                tempDayStr = "0" + tempDayStr;
                ArrayList<String> localMonthList = mydb.getMonthsList();
                int index=-1;
                if(localMonthList.contains(tempDayStr)){
                    index = localMonthList.indexOf(tempDayStr);
                    listView.setSelection(index);
                    Toast.makeText(getActivity(),"Day:"+  listView.getAdapter().getItem(index),Toast.LENGTH_SHORT).show();
                }


            }
        });
        calendarView.setOnForwardPageChangeListener(new OnCalendarPageChangeListener() {

            @Override
            public void onChange() {
                Calendar cal = calendarView.getCurrentPageDate();
                Date date1 =cal.getTime();
                DateFormat formatter1 = new SimpleDateFormat("MMMM");

                String month = formatter1.format(date1);
                ArrayList<String> monthList = mydb.getAllDaysInMonth(year,month);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, monthList);
                listView.setAdapter(arrayAdapter);
            }

        });
        calendarView.setOnPreviousPageChangeListener(new OnCalendarPageChangeListener() {

            @Override
            public void onChange() {
                Calendar cal = calendarView.getCurrentPageDate();
                Date date1 = cal.getTime();
                DateFormat formatter1 = new SimpleDateFormat("MMMM");
                String month = formatter1.format(date1);
                ArrayList<String> monthList = mydb.getAllDaysInMonth(year, month);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, monthList);
                listView.setAdapter(arrayAdapter);
            }

        });

        return view;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public  void setReminder(Context context, Class<?> cls, Calendar setcalendar, int id, String day)
    {
        Calendar calendar = Calendar.getInstance();
        setcalendar.set(Calendar.HOUR_OF_DAY,04 );
        setcalendar.set(Calendar.MINUTE, 24);
        setcalendar.set(Calendar.SECOND, 0);

       Date date1 =  setcalendar.getTime();
        // cancel already scheduled reminders
        cancelReminder(context,cls,id);
       long time1=date1.getTime();
        if(setcalendar.before(calendar))
           return;
        DateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy");

        String dailyCalendar = formatter.format(date1);
        String tempKey = dailyCalendar +" - "  + day;
        myReminderKey.put(tempKey,id);
        // Enable a receiver

        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);


        Intent intent1 = new Intent(context, cls);
        intent1.putExtra("id",id);
        intent1.putExtra("day",day);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
       // am.setExact(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
    //    am.setInexactRepeating(AlarmManager.RTC_WAKEUP, setcalendar.getTimeInMillis(), AlarmManager.ELAPSED_REALTIME, pendingIntent);

    }

    public  void cancelReminder(Context context,Class<?> cls,int id)
    {
        // Disable a receiver

        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();
    }


    public class RetrieveSiteData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder builder = new StringBuilder(100000);

            for (String url : urls) {
                try {
                    String htmlString = getHtml(url);
                    int startIndex = htmlString.indexOf("<strong>Year");
                    int endIndex = htmlString.indexOf("</table>", startIndex);
                    String calString = htmlString.substring(startIndex, endIndex);
                    calString = calString.replaceAll("<td[ .*]>" ,"<td>");
                    calString = calString.replaceAll("<tr[ .*]>" ,"<tr>");
                    calString = calString.replace( "<td></td>","");
                    calString = calString.replace("</td>", "</td>\n");
                    calString = calString.replace("</tr>", "</tr>\n");
                    calString = calString.replace("<tr>", "<tr>\n");
                    calString = calString.replace("<td>", "\n<td>");
                    calString = calString.replace("<br />" ,"");

                    calString = calString.replace("<strong>" ,"");
                    calString = calString.replace("</strong>" ,"");
                    calString = "<tr>\n" +
                            "<td>" + calString;
                    LinkedHashMap<Integer,ArrayList<String>> arrayLists = new LinkedHashMap<>();
                    BufferedReader reader = new BufferedReader(new StringReader(calString));
                    StringBuilder calValues = new StringBuilder();
                    int row = -1,col=-1;
                    Log.d("HTML String",htmlString);
                    Log.d("cal String",calString);

                    for (String line; (line = reader.readLine()) != null; ) {
                        //html.append(line);

                        Log.d("string", line);
                        if(line.contains("<td>December</td>")){
                            Log.d("here","");
                            col=-1;
                        }
                        if (line.contains("<tr>") || line.contains("<tr >")) {
                            row++;
                            col=-1;
                          //  storeRowList.clear();
                            continue;

                        }else if (line.contains("</tr>" ))
                        {

                            continue;
                        }
                        else if(line.trim().isEmpty()==false) {

                            String rowString = line;
                            rowString.trim();
                            Pattern p = Pattern.compile("<td>(\\S+)</td>");
                            Matcher m = p.matcher(rowString);

                            // if we find a match, get the group
                            if (m.find()) {

                                // get the matching group
                                rowString = m.group(1);

                                rowString.trim();
                                //Log.d("rowString ", storeRowList.size() + " " + rowString);


                            }

                            {
                                rowString =  rowString.replace("<td>","");
                                rowString =  rowString.replace("</td>","");

                            }


                            col++;

                            if(row==0){

                                ArrayList<String> storeRowList = new ArrayList();
                                if(rowString.contains(">")) {
                                    rowString = rowString.substring(rowString.indexOf(">")+1, rowString.length());
                                }

                                storeRowList.add(rowString);
                                arrayLists.put(col,storeRowList);

                                Log.d("rowString ", col+" " + storeRowList.size() + " " + rowString);
                            }
                            else
                            {
                                ArrayList<String>  tempList =   arrayLists.get(col);
                                if(tempList!=null) {
                                    tempList.add(rowString);
                                    Log.d("rowString ", col + " " + tempList.size() + " " + rowString);
                                }
                            }
                        }

                    }
                    for(int i=1;i<arrayLists.size();i++){
                        Log.d("Printing ROw",i + "List ");
                        for(int j=1;j<arrayLists.get(i).size();j++){
                            Log.d("-->",j+ arrayLists.get(i).get(j));
                            String temp = arrayLists.get(i).get(j);
                            String fullMonth = arrayLists.get(0).get(j);
                            String fullDay = arrayLists.get(i).get(0);
                            if(temp.equals("N/A")==false) {
                                String tempMonth = temp.substring(0, 3);
                                temp = temp.replaceAll("[^0-9]", " ");
                                StringTokenizer st = new StringTokenizer(temp," ");
                                while (st.hasMoreTokens()) {
                                    String dayDate = st.nextToken();
                                    String dateStr = dayDate + "/" + tempMonth + "/" + year;
                                    mydb.insertRecord(year, fullMonth, fullDay, dateStr);
                                }
                                    /*for (int i1 = 0; i1 < temp.length(); i1 = i1 + 2) {
                                        String dayDate = temp.substring(i1, 1);
                                        String dateStr = dayDate + "/" + tempMonth + "/" + year;
                                        mydb.insertRecord(year, fullMonth, fullDay, dateStr);

                                    }*/
                            }
                            else
                            {
                                mydb.insertRecord(year, fullMonth, fullDay, temp);
                            }

                            Log.d("temp value",temp);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return builder.toString();
        }

        public String getHtml(String url) throws IOException {
            // Build and set timeout values for the request.
            URLConnection connection = (new URL(url)).openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();

            // Read and store the result line by line then return the entire string.
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder html = new StringBuilder();
            for (String line; (line = reader.readLine()) != null; ) {
                html.append(line);
            }
            in.close();

            return html.toString();
        }
    }

}
