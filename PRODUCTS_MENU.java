package kz.akzh.akmart;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;


public class PRODUCTS_MENU extends AppCompatActivity {

    private final String  TAG= "PRODUCTS_MENUlog";
    private DB db;
    View v;
    private RequestParams params;
    private ListView lv;
    private int strNumber=1;
    private boolean flag_loading;
    private ArrayList<Map<String, String>> data;
    private PRODUCTS_MENU_LIST_ADAPTER obcaseadapter;
    private String mail_login;
    private Intent intent;
    private ProgressDialog progressDialog;
    private boolean flafLoadd =true;
    private String menuName;
    private Menu  menu;
    private TextView tv_menu_price;
    private HashMap<String, String> hashMap;
    private String parentIdToServer;
    private String lowerMenuName;
    private MySyncHTML mySyncHTMLclient;
    private String URL;
    private ProgressBar prBar;
    private ArrayList alSize;
    private int alSizeInt;
    private ArrayList<Map<String, String>> data_total;
    private String nameFirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firm__menu_activity_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        params = new RequestParams();
        data_total = new ArrayList<Map<String, String>>();
        db = new DB(this);
        db.open();
        if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
        Intent intent = getIntent();
        menuName  = intent.getStringExtra("menuName");
        setTitle(menuName);
        nameFirm = intent.getStringExtra("nameFirm");
        hashMap  =(HashMap) intent.getSerializableExtra("hashMap");
        parentIdToServer = intent.getStringExtra(DB.PRODUCTS_COLUMN_PARENT_ID);
        tv_menu_price= (TextView) findViewById(R.id.tv_menu_price);
        int sum=    db.getTotalPrice();
        tv_menu_price.setText("" + FUNCTION.formatterNumber(sum));
      lowerMenuName = menuName.toLowerCase();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        params.put(DB.PRODUCTS_COLUMN_MENU_RUBRIC, lowerMenuName);
        params.put(DB.PRODUCTS_COLUMN_PARENT_ID, parentIdToServer);
        Log.d(TAG, "onCreate() returned: " + lowerMenuName+parentIdToServer);
     mySyncHTMLclient = new MySyncHTML();
         URL = "get_products.php";
        LOOPJ_HTTP_CLIENT.post(URL, params, mySyncHTMLclient);
        lv = (ListView) findViewById(R.id.lv_firm_main1);
        View viewPrBar = LayoutInflater.from(this)
                .inflate(R.layout.bs_prbar,
                        null);



        lv.addFooterView(viewPrBar);

        prBar = (ProgressBar) findViewById(R.id._prbar);

        progressDialog=new   ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(
                getString(R.string.wait_connect)
        );
        progressDialog.show();

        //=========================================================================
        // // TODO: 27.03.2017  GOOGLE ANALYTICS !!!!!!!!!!!!!!
        UIAPLICATION.tracker().send(new HitBuilders.EventBuilder("PRODUCTS_MENU", "watsh")
                .setLabel(nameFirm+"/"+lowerMenuName)
                .build());
        //=========================================================================
    }
    /** ПРИ КЛИК  МИНУСА ПРОДУКТА
     * @param v
     */

    public void click_minus(View v) {
        View convertView =(View) v.getParent().getParent();
        HashMap dataHashMap;
        LinearLayout vvv =(LinearLayout)   v.getParent().getParent();
        TextView tv_price = (TextView)   vvv.findViewById(R.id.lv_price);
        //        получаем данные hashmap о продукте
        dataHashMap = (HashMap)tv_price.getTag();
        String strIdPr= (String) dataHashMap.get(DB.PRODUCTS_COLUMN_REAL_ID);
        TextView pr_count = (TextView)   vvv.findViewById(R.id.pr_count);
// удаляем в корзине
        int intDelte=  db.deleteToCart(strIdPr);

        if( intDelte>0) {
            Log.d(TAG, "click_minus() returned:");
            //            полная сумма
            int sum=    db.getTotalPrice();

            tv_menu_price.setText("" + FUNCTION.formatterNumber(sum));

//            полная кол
            int count= db.getTotalPieces(strIdPr);
            String measure =(String) dataHashMap.get(DB.PRODUCTS_COLUMN_MEASURE);
            if(measure.compareTo("pcs")==0){
                pr_count.setText(Integer.toString(count)+" шт");
            }else{pr_count.setText(Integer.toString(count)+" кг");}
            //            ставим hashmap количество , чтоб показывал снова прил скролле
            TextView tv_position= (TextView) convertView.findViewById(R.id.tv_position);
            String strPos= (String) tv_position.getText();
            HashMap hmp = (HashMap) data_total.get(Integer.parseInt(strPos));
            hmp.put(DB.PRODUCTS_COLUMN_ORDER_COUNT, Integer.toString(count));
//            перезапускаем адаптер
            obcaseadapter.notifyDataSetChanged();
        }
    }

    /** ПРИ КЛИК  ДОБАВЛЕНИИ ПРОДУКТА
     * @param v
     */
    //=========================================================================
// todo click_plpus
//=========================================================================
    public void click_plus(View v) {
        View convertView =(View) v.getParent().getParent();
        HashMap dataHashMap;
        LinearLayout vvv =(LinearLayout)   v.getParent().getParent();
        TextView tv_price = (TextView)   vvv.findViewById(R.id.lv_price);
        TextView pr_count = (TextView) vvv.findViewById(R.id.pr_count);
//        получаем данные hashmap о продукте
        dataHashMap = (HashMap)tv_price.getTag();
        String prId =(String) dataHashMap.get(DB.PRODUCTS_COLUMN_REAL_ID);
        String parent_id =(String) dataHashMap.get(DB.PRODUCTS_COLUMN_PARENT_ID);
//        проверяем корзину на пустоту, если заносим другую фмрму
       boolean bl =   db.checkCartOtherFirm(parent_id);
        if(bl){
            showDialogCartOtherFirm(v);
            return;
        }
// заносим в корзину
        long lng =db.insertToCart(dataHashMap, hashMap);
        if(lng>0){
//            полная сумма
            int sum=    db.getTotalPrice();
            tv_menu_price.setText("" + FUNCTION.formatterNumber(sum));
//            количество продукции
            int count= db.getTotalPieces(prId);
            String measure =(String) dataHashMap.get(DB.PRODUCTS_COLUMN_MEASURE);
            if(measure.compareTo("pcs")==0){
                pr_count.setText(Integer.toString(count) + " шт");
            }else{pr_count.setText(Integer.toString(count)+" кг");}
//            ставим hashmap количество , чтоб показывал снова прил скролле
            TextView tv_position= (TextView) convertView.findViewById(R.id.tv_position);
            String strPos= (String) tv_position.getText();
            HashMap hmp = (HashMap) data_total.get(Integer.parseInt(strPos));
            hmp.put(DB.PRODUCTS_COLUMN_ORDER_COUNT, Integer.toString(count));
//            перезапускаем адаптер
            obcaseadapter.notifyDataSetChanged();
        }
    }
    /** диалог очистить корзину при другой фирме
     * @param v button
     */
    private void   showDialogCartOtherFirm( final View v){
        //===========================================================================
        // //        todo show Dialog !!!!
        FUNCTION.showDialog(this, R.string.cartOtherFirm, new FUNCTION.AlertDialogFuncPos() {
            public void PositiveButton() {
                db.cleareTable(DB.CART_TABLE);
                tv_menu_price.setText("" + "0");
                click_plus(v);
            }
        });
        //============================================================================
    }
    /** при нажатии ЗАКАЗАТЬ
     * @param v
     */
    public void click_buttonorder(View v) {

        View convertView =(View) v.getParent();
        TextView tv_position= (TextView) convertView.findViewById(R.id.tv_position);
        LinearLayout ll_plus= (LinearLayout) convertView.findViewById(R.id.ll_plus);
        String strPos= (String) tv_position.getText();
        v.setVisibility(View.GONE);
//        ставим флаг о нажатии
        HashMap hmp = (HashMap) data_total.get(Integer.parseInt(strPos));
        hmp.put(DB.PRODUCTS_COLUMN_ORDER_CLICK, true);
        //            перезапускаем адаптер

        obcaseadapter.notifyDataSetChanged();
//        pokazivaem plus minus
        ll_plus.setVisibility(View.VISIBLE);

        //=========================================================================
        // // TODO: 27.03.2017  GOOGLE ANALYTICS !!!!!!!!!!!!!!
        UIAPLICATION.tracker().send(new HitBuilders.EventBuilder("PRODUCTS_MENU", "click")
                .setLabel(nameFirm + "/" + hmp.get(DB.PRODUCTS_COLUMN_NAME))
                .build());
        //=========================================================================
    }




    class MySyncHTML extends AsyncHttpResponseHandler {
        //при успешном полученниии ответа с сервера
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            if(flag_loading == false)
            {
                Log.d(TAG, "onSuccess() returned: " + 1);

                setItemToLv(responseBody);
                flag_loading = true;
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();

                }
//                ob_bs_act_progressBar.setVisibility(View.GONE);
            } else{
                Log.d(TAG, "onSuccess() returned: " + 2);
                prBar.setVisibility(View.GONE);
                addItemToLv(responseBody);
            }



        }
        //при успешном полученниии ошибки с сервера
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            lv.setVisibility(View.GONE);
//            ob_bs_act_progressBar.setVisibility(View.GONE);
            showDialog();
        }
    }
    //    first set item to lv
    private void setItemToLv(byte[] responseBody){
        try {
            ArrayList dataToAdapter = getMapFromNet(responseBody);
            Log.d(TAG, "setItemToLv() returned: "+dataToAdapter.size());

            obcaseadapter = new PRODUCTS_MENU_LIST_ADAPTER(this, dataToAdapter,menu);
            lv.setAdapter(obcaseadapter);

            lv.setOnScrollListener(new AbsListView.OnScrollListener() {
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    Log.d(TAG, "onScrollStateChanged() called with: " + "view = [" + view + "], scrollState = [" + scrollState + "]");
                }

                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {
                    Log.d(TAG, "onScroll() called with: " + "view = [" + view + "], firstVisibleItem = [" + firstVisibleItem + "], visibleItemCount = [" + visibleItemCount + "], totalItemCount = [" + totalItemCount + "]");
                    if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                        if (flafLoadd) {
                            prBar.setVisibility(View.VISIBLE);
                            Log.d(TAG, "onScroll() returned: " + strNumber);
                            strNumber++;
                            params.put("limitFromGet", strNumber);
                            LOOPJ_HTTP_CLIENT.post(URL, params, mySyncHTMLclient);
                            flafLoadd = false;
                        }
                    }
                }
            });

        } catch (IOException | XmlPullParserException e){
            e.printStackTrace();
        }
        registerForContextMenu(lv);
    }

    private void addItemToLv(byte[] responseBody) {
        try {
             alSize = getMapFromNet(responseBody);
            Log.d(TAG, "addItemToLv() returned: " + alSize);
            flafLoadd = true;
            if (alSize.size()>0){
                obcaseadapter.addAll(alSize);
                obcaseadapter.notifyDataSetChanged();
                flafLoadd =true;

            }else{
                flafLoadd =false;
            }
        } catch (IOException | XmlPullParserException e){
            e.printStackTrace();
        }
    }


    private  ArrayList getMapFromNet(byte[] responseBody) throws IOException, XmlPullParserException {
        InputStream is;
        is = new ByteArrayInputStream(responseBody);
        data = new ArrayList<>();
        StringBuilder buffer_food = new StringBuilder();
        BufferedReader reader_food = new BufferedReader(new InputStreamReader(is));
        String line_food;
        while ((line_food = reader_food.readLine()) != null) {
            buffer_food.append(line_food);
        }
        Log.d(TAG, "getMapFromNet() returned: " + buffer_food.toString());
        String strBufferTrim = buffer_food.toString().trim();


//        если модем включен, но интернет отключен ↓↓↓↓
        if(!strBufferTrim.contains("real_id")&&!strBufferTrim.isEmpty()){
            Log.d(TAG, "getMapFromNet() returned: contains");
//            если поступает посторонние данные
            showDialog();

        }
/*if(strBufferTrim.compareTo(notResult)==0){
    return null;
}*/

        XmlPullParser xpp = PREPARE_XPP.prepareXpp(buffer_food.toString());
        while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
            if (xpp.getEventType() == XmlPullParser.START_TAG) //начальный тег
            {
                if (xpp.getName().compareTo("data") == 0) {
                    //выборка атрибутов
                    Map<String, String> m = new HashMap<>();
                    for (int i = 0; i < xpp.getAttributeCount(); i++) {
                        m.put(xpp.getAttributeName(i), xpp.getAttributeValue(null, xpp.getAttributeName(i)));
                    }


                    data.add(m);
                    data_total.add(m);

                }
            }
            xpp.next();
        }
        return data;
    }

    //    BUTTON CLICK "NO_CONNECT"
    private void try_agane_click(){

        Intent refresh = new Intent(this, getClass());
        refresh.putExtra("menuName", lowerMenuName);
        refresh.putExtra(DB.PRODUCTS_COLUMN_PARENT_ID, parentIdToServer);
        startActivity(refresh);

        finish();

    }

    /**
     *  пока зываем диалог при отсутствии сети
     */
    private void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setIcon(R.drawable.no_connect);
        builder.setTitle("Проверьте соединение с интернетом")
                .setCancelable(false)
                .setPositiveButton("ПОПРОБОВАТЬ СНОВА", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try_agane_click();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainactivity, menu);
        this.menu = menu;
        MenuItem itemCart = menu.findItem(R.id.action_cart);
        LayerDrawable icon = (LayerDrawable) itemCart.getIcon();
        FUNCTION.setBadgeCount(this, icon, "0");
/*


        MenuItem item = menu.findItem(R.id.action_favorite);
        MenuItemCompat.setActionView(item, R.layout.actionbar_badge_layout);
        RelativeLayout notifCount = (RelativeLayout)   MenuItemCompat.getActionView(item);

        TextView tv = (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);
        tv.setText("12");
*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {



            case R.id.action_cart:

                Cursor crs =db.getFullData(DB.CART_TABLE);
                if(crs.getCount()>0){

                    Intent intent = new Intent("kz.akzh.akmart.CART");


                    startActivity(intent);
                    return true;
                }else{
                    TOAST.TOAST(this,getString(R.string.cart_empty));
                }

            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onResume() {

        int sum=    db.getTotalPrice();
        tv_menu_price.setText("" + FUNCTION.formatterNumber(sum));
        super.onResume();
    }

}
