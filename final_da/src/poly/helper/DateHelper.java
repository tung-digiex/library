/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poly.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author admin
 */
public class DateHelper {

    static final SimpleDateFormat DATE_FORMATER = new SimpleDateFormat("MM-dd-yyyy");

    /**
     * Chuyen doi String sang Date
     * @param date la String can chuyen
     * @param pattern la dunh dang thoi gian
     * @return 
     */
    public static Date toDate(String date, String...pattern) {
        try {
            if (pattern.length > 0) {
                DATE_FORMATER.applyPattern(pattern[0]);
            }
            if (date == null) {
                return DateHelper.now();
            }
            return DATE_FORMATER.parse(date);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Chuyen doi tu Date sang String
     * @param date là Date can chuyen doi
     * @param pattern la dinh dang thoi gian
     * @return Date ket qua
     */
    public static String toString(Date date, String...pattern){
        if (pattern.length > 0) {
            DATE_FORMATER.applyPattern(pattern[0]);
        }
        if (date == null) {
            date = DateHelper.now();
        }
        return DATE_FORMATER.format(date);
    }
    
    /**
     * Lay thoi gian hien tai
     * @return ket qua
     */
    public static Date now(){
        return new Date();
    }
    /**
     * Bo sung so ngay vao thoi gian
     * @param date thoi gian hien co
     * @param days so ngay can bo sung vao date
     * @return Date ket qua
     */
    public static Date addDays(Date date, int days){
        date.setTime(date.getTime()+days*24*60*60*1000);
        return date;
    }
    
    /**
     * Bo sung so ngay vao thoi gian hien hanh
     * @param days so ngay can bo sung vao thoi gian hien tai
     * @return ket qua
     */
    public static Date add(int days){
        Date now = DateHelper.now();
        now.setTime(now.getTime()+days*24*60*60*1000);
        return now;
    }
}


