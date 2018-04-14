package com.vidhyalearning.entamizhnaalkaati;

import java.util.Date;

/**
 * Created by user on 11-Mar-18.
 */

public class ImpDays {
    String year,month,day;
    Date dateOfDay;
public ImpDays(){

}
    public ImpDays(String tempYear, String fullMoStr, String noteStr, Date date1) {
        this.year = tempYear;
        this.month = fullMoStr;
        this.day=noteStr;
        this.dateOfDay=date1;
    }


    public void setYear(String year) {
        this.year = year;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setDateOfDay(Date dateOfDay) {
        this.dateOfDay = dateOfDay;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Date getDateOfDay() {
        return dateOfDay;
    }

    public String getDay() {
        return day;
    }
}
