package kz.akzh.akmart;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class PRODUCTS_MENU_LIST_ADAPTER extends BaseAdapter {
    Context ctx;
    ProgressBar  ob_bs_progressBar;
    private static final String TAG = "OB_BS_ADAPTERlog";
    LayoutInflater lInflater;
    ArrayList datalinks;
    private String vip;
    private Menu menu;

    SimpleDateFormat sdf = new SimpleDateFormat("H:m / dd.MM.yyyy ");
    private long milliseconds;
    private Date resultdate;

    PRODUCTS_MENU_LIST_ADAPTER(Context context, ArrayList data, Menu menuf) {
        menu=menuf;

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
    private class ViewHolde {
        TextView lv_tv,lv_tv1,lv_price,buttonorder,pr_count,tv_position;
        Button click_minus,click_plus;
        ImageView img ;
        LinearLayout ll_plus, comon_ll;

        int pos;
    }


    TextView lv_tv,lv_tv1,lv_price,buttonorder,pr_count,tv_position;
    Button click_minus,click_plus;
    ImageView img ;
    LinearLayout ll_plus, comon_ll;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
            convertView = lInflater.inflate(R.layout.product_menu_list_item, parent, false);
           lv_tv = (TextView) convertView.findViewById(R.id.lv_tv);
           lv_tv1 = (TextView) convertView.findViewById(R.id.lv_tv1);
           lv_price = (TextView) convertView.findViewById(R.id.lv_price);
           buttonorder = (TextView) convertView.findViewById(R.id.buttonorder);
           pr_count = (TextView) convertView.findViewById(R.id.pr_count);
           click_plus = (Button) convertView.findViewById(R.id.click_plus);
           click_minus = (Button) convertView.findViewById(R.id.click_minus);
           ll_plus = (LinearLayout) convertView.findViewById(R.id.ll_plus);
           tv_position = (TextView) convertView.findViewById(R.id.tv_position);
           img= (ImageView)  convertView.findViewById(R.id.main_img);

        lv_tv.setText((String) getItem(position).get(DB.PRODUCTS_COLUMN_NAME));
        lv_tv1.setText(Html.fromHtml((String) getItem(position).get(DB.PRODUCTS_COLUMN_DESCRIPTION)) );
        tv_position.setText(Integer.toString(position));
//        prikrepliaem v tag dannie hashmap для обработки при нажатии плюс
        lv_price.setTag(getItem(position));
        lv_price.setText((String) getItem(position).get(DB.PRODUCTS_COLUMN_PRICE)+" \u20B8");
        Picasso.with(ctx).load(DB.URL_BASE  + getItem(position).get(DB.PRODUCTS_COLUMN_PATH_IMG)).into(img);

// если нажата "заказать"
//        получаем  булен нажатии
        Boolean blTagFlag = (Boolean) getItem(position).get(DB.PRODUCTS_COLUMN_ORDER_CLICK);
        //        получаем количество товаров
        String strOrderCount= (String)    getItem(position).get(DB.PRODUCTS_COLUMN_ORDER_COUNT);
        String measure =(String) getItem(position).get(DB.PRODUCTS_COLUMN_MEASURE);
//        если нажата заказать
        if (blTagFlag != null) {
            if( blTagFlag){
                buttonorder.setVisibility(View.GONE);
                ll_plus.setVisibility(View.VISIBLE);
//                ставим количество товаров
                if (strOrderCount != null) {
                    if(measure.compareTo("pcs")==0){
                        pr_count.setText(strOrderCount+ " шт");
                    }else{
                        pr_count.setText(strOrderCount+" кг");
                    }
                }
            }else{
                buttonorder.setVisibility(View.VISIBLE);
                ll_plus.setVisibility(View.GONE);
            }
        }





        return convertView;
    }





}