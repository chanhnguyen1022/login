package mock02.taglib.custom;

import java.util.Date;

/*
 *@author: nguyenkhue
 *@version 1.0 Jan 1, 2016
 */
public final class CalculateStatus {
	
	 private CalculateStatus() {}
	 
	 public static String status(Date deadline, Date timeStore) {	 
		double deadlineTime = deadline.getTime();
		double stored = timeStore.getTime();
		if( deadlineTime >= stored){
			Double dateDiff =(deadlineTime - stored)/(1000*60*60*24);
			Integer dateInt = dateDiff.intValue();
			Double hourDiff = (dateDiff - dateInt)*24;
			Integer hourInt = hourDiff.intValue();
			Double minDiff = (hourDiff - hourInt)*60;
			Integer minInt = (int) Math.round(minDiff);
			return "Earlier " + dateInt + " days " + hourInt + " hours " + minInt + " mins ";
		} else{
			Double dateDiff =(stored - deadlineTime)/(1000*60*60*24);
			Integer dateInt = dateDiff.intValue();
			Double hourDiff = (dateDiff - dateInt)*24;
			Integer hourInt = hourDiff.intValue();
			Double minDiff = (hourDiff - hourInt)*60;
			Integer minInt = (int) Math.round(minDiff);
			return "Over due " + dateInt + " days " + hourInt + " hours " + minInt + " mins ";
		}
	 }
}
