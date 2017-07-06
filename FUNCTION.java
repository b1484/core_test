package kz.akzh.akmart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.EditText;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by BULA on 20.04.2017.
 */
public class FUNCTION {


    private static final String TAG="FUNCTIONlog";

    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BADGEDRAWABLE badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BADGEDRAWABLE) {
            badge = (BADGEDRAWABLE) reuse;
        } else {
            badge = new BADGEDRAWABLE(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }

    /** возвращает из string чистые string цифры(без пробелов и символов)
     * @param input
     * @return
     */
    public static String stripNonDigits(
            final CharSequence input /* inspired by seh's comment */){
        final StringBuilder sb = new StringBuilder(
                input.length() /* also inspired by seh's comment */);
        for(int i = 0; i < input.length(); i++){
            final char c = input.charAt(i);


            if(c > 47 && c < 58){

                sb.append(c);
            }
        }
        return sb.toString();
    }

    /** разделение цены
      * @param val цена
     * @return цена разделенная плюс ₸
     */
public static String formatterNumber (int val){
    Locale loc = new Locale("ru");
    NumberFormat formatter = NumberFormat.getInstance(loc);
  return   formatter.format(val)+ " \u20B8";
}

    /** интерфейс для AlertDialog для выполнения функции при нажатии ok , требует опрделения метода
     *
     */
    interface AlertDialogFuncPos
    {
        void PositiveButton();
    }

    /**
     *  показ диалога на подтверждения , требует опрделения метода AlertDialogFuncPos
     */
    public static void showDialog(final Context ctx, int resId,final AlertDialogFuncPos func) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);


        builder.setMessage(ctx.getString(resId))
                .setCancelable(false)
                .setPositiveButton("ок", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        func.PositiveButton();

                    }


                }).setNegativeButton("отмена",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }




        });

        AlertDialog alert = builder.create();
        alert.show();
    }


    public static  boolean isNoEmpty(EditText etText) {
        return etText.getText().toString().trim().length() != 0;
    }

    /** получаем дату
      * @param str
     * @return
     */
    public static String getDataFormat(String str){

        SimpleDateFormat sdf = new SimpleDateFormat(str);
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    public static String get_UUID(Context ctx){
        final TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(ctx.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();
        Log.d(TAG, "checkPermission() returned:tmDevice= " + tmDevice);
        Log.d(TAG, "checkPermission() returned:tmSerial= " + tmSerial);
        Log.d(TAG, "checkPermission() returned:androidId= " + androidId);
        Log.d(TAG, "checkPermission() returned:androidId.hashCode()= " + androidId.hashCode());
        Log.d(TAG, "checkPermission() returned: android_id=" +deviceId );
return deviceId;

    }
 public static void showDialog(Context ctx,String str){

     ProgressDialog progressDialog = new ProgressDialog(ctx);
     progressDialog.setCanceledOnTouchOutside(false);
     progressDialog.setMessage(str);
     progressDialog.show();
 }

   public static int getNumberDay(){
       String day[] = { "понедельник","вторник","среда","четверг","пятница","суббота","воскресенье"};
       String curDay= FUNCTION.getDataFormat("EEEE");
       Log.d(TAG, "getNumberDay() returned: " + curDay);
       for (int i = 0; i <= 6; i++) {
           if(day[i].compareTo(curDay)==0){
               return i;
           }
       }
       return -1;
   }

    /** Открыто или закрыто фирма
     * @param strTime - данные строки shedule
     * @return true если открыто
     */
    public static final String HOLLIDAY = "holliday";
    public static final String VIHODNOI = "выходной";
    public static final String OPEN = "open";
    public static boolean getOpen(String strTime){
        try {
            String[] shed_ = (strTime).split(";");
            String strTimeDay = shed_[FUNCTION.getNumberDay()];
            if(strTimeDay.compareTo("выходной")==0){return false;}
            String[] TimeAr= strTimeDay.split("-");
            String fromTime = TimeAr[0];
            String toTime = TimeAr[1];

            long fromTimeMill =   getMillisecHHmm(fromTime);
            long toTimeMill =   getMillisecHHmm(toTime);

            long currentTimeMill = System.currentTimeMillis();

            if (fromTimeMill <= currentTimeMill&& currentTimeMill < toTimeMill){
                return true;
            }else {return false;}
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }



    public static long getMillisecHHmm(String strTime){
        long res=-1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");


        String currentYear =FUNCTION.getDataFormat("yyyy-MM-dd");

        try {
            Date date = sdf.parse(currentYear +" "+ strTime);
          res= date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
     return res;
    }

}
