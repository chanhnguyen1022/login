package mock02.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import mock02.model.User;

/*
* TramTran(^^)
*/
public class Validate {

    public static String formatDate(String date){
        SimpleDateFormat source = new SimpleDateFormat("yyyy-mm-dd");
        Date d = null;
        try {
            d = source.parse(date);
            SimpleDateFormat des = new SimpleDateFormat("yyyy-mm-dd");
            date = des.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            date = null;
        }
        
        return date;
    }
    public static String formatUser(User u){
        String regex = "^([a-zA-Z0-9_.+-])+\\@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{2,4})+$";
        Pattern r = Pattern.compile(regex);
         if(u.getEmail() == null || u.getEmail().equals("")){
            return "Email is null";
        }
        else if(!r.matcher(u.getEmail()).find()){
            return "Invalid Email";
        }else if(u.getFullName() == null || u.getFullName().equals("")){
            
            return "Fullname is null.";
        }
        else if(u.getBirthDay()==null || u.getBirthDay().equals("")){
            return "BirthDay is null";
        }
        else if(formatDate(u.getBirthDay())==null){
            return "Invalid birthday";
        }
        else if((Calendar.getInstance().get(Calendar.YEAR)-Integer.parseInt(u.getBirthDay().split("-")[0]))<18){
            return "The student's age must be greater than 17";
        }
        else {
            return null;
        }
            
            
    }
}
