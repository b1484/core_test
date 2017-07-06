package kz.akzh.akmart;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class MAIN_FIRM_LIST_ADAPTER extends BaseAdapter {
    Context ctx;
    ProgressBar  ob_bs_progressBar;
    private static final String TAG = "OB_BS_ADAPTERlog";
    LayoutInflater lInflater;
    ArrayList datalinks;
    private String isEmptyStr;
    private String isEmptyImg;
    private String vip;

    SimpleDateFormat sdf = new SimpleDateFormat("H:m / dd.MM.yyyy ");
    private long milliseconds;
    private Date resultdate;

    MAIN_FIRM_LIST_ADAPTER(Context context, ArrayList data) {
        ctx = context;
        datalinks=data;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void addAll(ArrayList<HashMap<String, String>> result) {

        if(datalinks==null){
            datalinks = new ArrayList<HashMap<String, String>>();
        }
        datalinks.addAll(result);
        this.notifyDataSetChanged();
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return datalinks.size() ;
    }

    // элемент по позиции
    @Override
    public HashMap getItem(int position) {

        return (HashMap)  datalinks.get(position);
    }
    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }
    // пункт списка
    private class ViewHolder {
        TextView lv_tv,lv_tv1,tv_min_price,tv_delivery_price,tv_time,fl_status;
        ImageView img ;
        LinearLayout ob_bs_ll_visible, comon_ll;
    }
    ViewHolder holder;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = lInflater.inflate(R.layout.firm_list_item, parent, false);
            holder.lv_tv = (TextView) convertView.findViewById(R.id.lv_tv);
            holder.lv_tv1 = (TextView) convertView.findViewById(R.id.lv_tv1);
            holder.tv_min_price = (TextView) convertView.findViewById(R.id.tv_min_price);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.fl_status = (TextView) convertView.findViewById(R.id.fl_status);
            holder.tv_delivery_price = (TextView) convertView.findViewById(R.id.tv_delivery_price);

            holder.img= (ImageView)  convertView.findViewById(R.id.main_img);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
//=========================================================================
// проверка статуса открыто
         holder.fl_status.setVisibility(View.GONE);

 if(!FUNCTION.getOpen((String) getItem(position).get(DB.FIRM_COLUMN_SHEDULE))){
     holder.fl_status.setVisibility(View.VISIBLE);
     holder.fl_status.setText("закрыто");
 }

 /*       holder.fl_status.setVisibility(View.GONE);
        String getOpen = FUNCTION.getOpen((String) getItem(position).get(DB.FIRM_COLUMN_SHEDULE));
        if(getOpen.compareTo(FUNCTION.OPEN)!=0&&
                getOpen.compareTo(FUNCTION.HOLLIDAY)!=0
                ){
            holder.fl_status.setVisibility(View.VISIBLE);
            holder.fl_status.setText("Закрыто. Откроется в "+getOpen);
        }else if (getOpen.compareTo(FUNCTION.HOLLIDAY)==0){
            holder.fl_status.setVisibility(View.VISIBLE);
            holder.fl_status.setText("Закрыто");
        }
*/
//=========================================================================
        holder.lv_tv.setText((String) getItem(position).get(DB.FIRM_COLUMN_NAME));
        holder.lv_tv1.setText(Html.fromHtml((String) getItem(position).get(DB.FIRM_COLUMN_TITLE)));
        holder.tv_delivery_price.setText((String) getItem(position).get(DB.FIRM_COLUMN_DELIVERY_PRICE)+" \u20B8");
        holder.tv_time.setText((String) getItem(position).get(DB.FIRM_COLUMN_DELIVERY_TIME));
        holder.tv_min_price.setText((String) getItem(position).get(DB.FIRM_COLUMN_MIN_PRICE)+" \u20B8");
        Picasso.with(ctx).load(DB.URL_BASE + "firm/" + getItem(position).get(DB.FIRM_COLUMN_REAL_ID) + ".jpg").into(holder.img);
        return convertView;
    }







}