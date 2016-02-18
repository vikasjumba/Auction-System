package org.sandeep.helpers;
//import java.lang.*;
public class BidTimeHandler {
static  int maxBidTimer = 5 * 60 ;

public static long getRemainingBidDuration(String isBiddingDone, String uploadTime){
	long duration = 0;
	//long currentInterval = Long.parseLong(uploadTime);
	
	long nowSeconds = System.currentTimeMillis() / 1000l;
	if(isBiddingDone.equals("0")){
		duration = maxBidTimer - ((nowSeconds - Long.parseLong(uploadTime)) % maxBidTimer);
	}else{
		duration = maxBidTimer - (nowSeconds - Long.parseLong(uploadTime));
	}
	//System.out.println(duration);
	return Math.max(duration,0l);
}
public static String getUploadTimeIntervalPeriodForFirstTimeBidItem(String uploadTime){
	long nowSeconds = System.currentTimeMillis() / 1000l;
	long uploadSeconds = Long.parseLong(uploadTime);
	long currInterval = nowSeconds - ((nowSeconds - uploadSeconds) % maxBidTimer);
	return String.valueOf(currInterval);
}
//public static long timeSinceUpload(String uploadTime){
//	
//	long nowSeconds = System.currentTimeMillis() / 1000l;
//	long uploadTimeSeconds = Long.parseLong(uploadTime);
//	long timeSinceUpload = maxBidTimer - (nowSeconds - uploadTimeSeconds);
//	return Math.max(timeSinceUpload,0l);
//}
//public static boolean isItemBiddable(boolean isBiddingDone, String uploadTime){
//	boolean result = false;
//	if(!isBiddingDone){
//		result = true;
//	}else{
//		long timeSinceUpload = timeSinceUpload(uploadTime);
//		if(timeSinceUpload < maxBidTimer){
//			result = true;
//		}
//	}
//	return result;
//}
//public static String getCurrentBidIntervalForNotBidItem(String uploadTime){
//// for items which are not even bid once since upload and timer is expired
//   long timeSinceUpload = timeSinceUpload(uploadTime);
//   long nowSeconds = System.currentTimeMillis() / 1000l;
//   long currentInterval = (nowSeconds - timeSinceUpload) % maxBidTimer;
//   return String.valueOf(currentInterval);
//}

}
