package kz.akzh.akmart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.util.HashMap;
/**
 * Created by BULA on 20.04.2017.
 */
public class DB {
    private static final String DB_NAME = "fe.db";
    private static final String TAG= "DBlog";
    public static final String URL_BASE = "http://fe.akzh.kz/" ;
//    public static final String URL_BASE = "http://fe.akzh.kz/0test" ;

    private   int DB_VERSION=2;
    private final Context mCtx;
    public DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public static final String ORDER_HTML_MESSAGE = "order_html_message";
    public static final String DATA = "data";
    // DATA RUBRIC_MAIN TABLE
    public static final String RUBRIC_MAIN_TABLE = "rubric_main";
    public static final String RUBRIC_MAIN_COLUMN_ID = "_id";
    public static final String RUBRIC_MAIN_COLUMN_REAL_ID = "real_id";
    public static final String RUBRIC_MAIN_COLUMN_NAME = "name";



    // DATA FIRM TABLE
    public static final String FIRM_TABLE = "firm";
    public static final String FIRM_COLUMN_ID = "_id";
    public static final String FIRM_COLUMN_REAL_ID = "real_id";
    public static final String FIRM_COLUMN_RUBRIC_MAIN_ID = "rubric_main_id";
    public static final String FIRM_COLUMN_NAME = "name";
    public static final String FIRM_COLUMN_TITLE = "title";
    public static final String FIRM_COLUMN_DESCRIPTION = "description";
    public static final String FIRM_COLUMN_ADRESS = "adress";
    public static final String FIRM_COLUMN_PHONE = "phone";
    public static final String FIRM_COLUMN_SHEDULE = "shedule";
    public static final String FIRM_COLUMN_LINKS = "links";
    public static final String FIRM_COLUMN_VIP = "vip";
    public static final String FIRM_COLUMN_MAP = "map";
    public static final String FIRM_COLUMN_MIN_PRICE = "min_price";
    public static final String FIRM_COLUMN_DELIVERY_PRICE = "delivery_price";
    public static final String FIRM_COLUMN_DELIVERY_FREE_MINPR	 = "delivery_free_minpr";
    public static final String FIRM_COLUMN_DELIVERY_TIME = "delivery_time";
    public static final String FIRM_COLUMN_MENU_RUBRIC = "menu_rubric";

    public static final String FIRM_COLUMN_AD_TEXT = "ad_text";



    public static final String PRODUCTS_TABLE = "FIRM_PRODUCTS";
    public static final String PRODUCTS_COLUMN_ID = "_id";
    public static final String PRODUCTS_COLUMN_REAL_ID = "real_id";
    public static final String PRODUCTS_COLUMN_MENU_RUBRIC = "menu_rubric";
    public static final String PRODUCTS_COLUMN_NAME = "name";
    public static final String PRODUCTS_COLUMN_PARENT_ID = "parent_id";
    public static final String PRODUCTS_COLUMN_MEASURE= "measure";
    public static final String PRODUCTS_COLUMN_PRICE = "price";
    public static final String PRODUCTS_COLUMN_VIP = "vip";
    public static final String PRODUCTS_COLUMN_TOP= "top";
    public static final String PRODUCTS_COLUMN_PATH_IMG = "path_img";
    public static final String PRODUCTS_COLUMN_DESCRIPTION = "description";
    public static final String PRODUCTS_COLUMN_ORDER_CLICK = "order_click";
    public static final String PRODUCTS_COLUMN_ORDER_COUNT = "order_count";

//    USER
    public static final String USER_TABLE = "user_table";
    public static final String USER_ID = "_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_PHONE = "user_phone";
    public static final String USER_STREET = "user_street";
    public static final String USER_HOUSE = "user_house";
    public static final String USER_APARTMENT = "user_apartment";
    public static final String USER_ENTRANCE = "user_entrance";
    public static final String USER_DOMOFON = "user_domofon";
    public static final String USER_CHANGE = "user_change";
    public static final String USER_COMMENT = "user_comment";

    private static final String USER_TABLE_CREATE = "create table "
            + USER_TABLE + "(" + USER_ID
            + " integer primary key autoincrement, "
            + USER_NAME+ " text, "
            + USER_PHONE+ " text, "
            + USER_STREET+ " text, "
            + USER_HOUSE+ " text, "
            + USER_APARTMENT+ " text, "
            + USER_ENTRANCE+ " text, "
            + USER_DOMOFON + " text" + ");";


    // DATA FIRM TABLE
    public static final String CART_TABLE = "cart";
    public static final String CART_COLUMN_ID = "_id";
    public static final String CART_COLUMN_NAME_PRODUCTS = "name";
    public static final String CART_COLUMN_PRODUCTS_REAL_ID = "products_real_id";
    public static final String CART_COLUMN_PARENT_REAL_ID = "parent_id";
    public static final String CART_COLUMN_PRICE = "price";
    public static final String CART_COLUMN_DESCRIPTION = "description";
    public static final String CART_COLUMN_PATH_IMG = "path_img";
    public static final String CART_COLUMN_COUNT = "count";
    public static final String CART_COLUMN_PROD_TOTAL_PRICE = "prod_total_price";
    public static final String CART_COLUMN_TOTAL_PRICE = "total_price";
    public static final String CART_COLUMN_DELIVERY_PRICE = "delivery_price";
    public static final String CART_COLUMN_FIRM_NAME = "firm_name";
    public static final String CART_COLUMN_MIN_PRICE = "min_price";
    public static final String CART_COLUMN_MEASURE = "measure";
    public static final String CART_COLUMN_DELIVERY_FREE_MINPR = "delivery_free_minpr";


    private static final String CART_TABLE_CREATE = "create table "
            + CART_TABLE + "(" + CART_COLUMN_ID
            + " integer primary key autoincrement, "
            + CART_COLUMN_NAME_PRODUCTS+ " text, "
            + CART_COLUMN_FIRM_NAME+ " text, "
            + CART_COLUMN_MEASURE+ " text, "
            + CART_COLUMN_PARENT_REAL_ID+ " integer, "
            + CART_COLUMN_PRODUCTS_REAL_ID+ " integer, "
            + CART_COLUMN_COUNT+ " integer, "
            + CART_COLUMN_PROD_TOTAL_PRICE+ " integer, "
            + CART_COLUMN_PRICE+ " integer, "
            + CART_COLUMN_DELIVERY_FREE_MINPR+ " integer, "
            + CART_COLUMN_DELIVERY_PRICE+ " integer, "
            + CART_COLUMN_MIN_PRICE+ " integer, "
            + CART_COLUMN_DESCRIPTION+ " text, "
            + CART_COLUMN_PATH_IMG + " text" + ");";

    // DATA  таблица истории заказа
    public static final String HISTORY_TABLE = "HISTORY";
    public static final String HISTORY_COLUMN_ID = "_id";
    public static final String HISTORY_COLUMN_ORDER_HTML_MESSAGE = "name";
    public static final String HISTORY_COLUMN_DATA = "products_real_id";
    public static final String HISTORY_COLUMN_ID_ORDER = "id_order";
    public static final String HISTORY_COLUMN_UUID_DEVICE = "uuid_device";
    private static final String HISTORY_TABLE_CREATE = "create table "
            + HISTORY_TABLE + "(" + HISTORY_COLUMN_ID
            + " integer primary key autoincrement, "
            + HISTORY_COLUMN_ORDER_HTML_MESSAGE+ " text, "
            + HISTORY_COLUMN_DATA+ " text, "
            + HISTORY_COLUMN_ID_ORDER+ " text, "
            + HISTORY_COLUMN_UUID_DEVICE + " text" + ");";

    public DB(Context ctx) {
        mCtx = ctx;
    }
    // открываем подключение
    public void open() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }
    // закрываем подключение
    public void close() {

        if (mDBHelper != null)
            mDBHelper.close();
    }



    public class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
        }
 // todo onCreate

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CART_TABLE_CREATE);
            db.execSQL(HISTORY_TABLE_CREATE);
//            db.execSQL(USER_TABLE_CREATE);
        }
// todo onUpgrade
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

    }

    //=========================================================================
// todo FUNCTIONS

    /**
     * Получаем все
     */
    public Cursor getFullData(String table) {
        return mDB.query(table, null, null, null, null, null, null);
    }
    /** проверка корзины на чужую фирму
     * @param parent_id
     */
    public boolean checkCartOtherFirm(String parent_id) {

        Cursor c_total = mDB.query(CART_TABLE, null, null, null, null, null, null);
      Cursor c_parent_id =  mDB.query(CART_TABLE, null, CART_COLUMN_PARENT_REAL_ID + "=" + parent_id, null, null, null, null);
        Log.d(TAG, "checkCartOtherFirm() returned: " + c_total.getCount() + c_parent_id.getCount());
        if(c_total.getCount()>0 && !(c_parent_id.getCount()>0)){
            return true;
        }else {return false;}
    }
/**
     * Получаем объединенные продукты для корзины с подсчетом суммы и количества (c новыми колонками для них CART_COLUMN_PRICE, CART_COLUMN_PROD_TOTAL_PRICE )
     */
    public Cursor getCartDataGroup() {

        String sql = "SELECT *, SUM("+CART_COLUMN_PRICE+") AS "+CART_COLUMN_PROD_TOTAL_PRICE+", COUNT(*) AS "+CART_COLUMN_COUNT+" FROM "+CART_TABLE+" GROUP BY " +CART_COLUMN_PRODUCTS_REAL_ID;
        Cursor crs=  mDB.rawQuery(sql, null);
        return crs;
    }

    /**
     * Проверяем в корзине уже добавленный продукт по id
     * @param id
     * @return
     */
  public Cursor checkProdByID( String id) {
        return mDB.query(CART_TABLE, null, CART_COLUMN_PRODUCTS_REAL_ID+"="+id, null, null, null, null);
    }

    /**
     * Получаем полную сумму от корзины
     * @return int SUM
     */
  public int getTotalPrice() {
      int sum=0;
      Cursor crs=  mDB.rawQuery("SELECT SUM("+CART_COLUMN_PRICE+") FROM " + CART_TABLE, null);

      if(crs.moveToFirst()) {
          sum=crs.getInt(0);

      }
      return sum;
  }

    /**
     * Получаем полную сумму  одного вида товра от корзины
     * @return int SUM
     */
  public int getTotalPriceGroup(String id) {
      int sum=0;
      Cursor crs=  mDB.rawQuery("SELECT SUM(" + CART_COLUMN_PRICE + ") FROM " + CART_TABLE+ " WHERE "+CART_COLUMN_PRODUCTS_REAL_ID +" = "+id, null);
      if(crs.moveToFirst()) {
          sum=crs.getInt(0);
      }
      return sum;
  }
    /** получение общее количество товаров с корзины
     * @param prId
     * @return
     */
public int getTotalPieces(String prId) {
    Cursor crsDelRaw =mDB.query(CART_TABLE, null, CART_COLUMN_PRODUCTS_REAL_ID + " = "
            + prId, null, null, null, null);
    return crsDelRaw.getCount();

  }
    /** Удаление продукта
     * @param id - id продукта
     * @return
     */
    public int deleteToCart(String id) {
       Cursor crsDelRaw =mDB.query(CART_TABLE, null, CART_COLUMN_PRODUCTS_REAL_ID + " = "
               + id, null, null, null, null);
        if(crsDelRaw.getCount()>0){
            crsDelRaw.moveToFirst();
            String strIdDelRaw=crsDelRaw.getString(crsDelRaw.getColumnIndex(CART_COLUMN_ID));
//            Log.d(TAG, "deleteToCart() returned: " +  mDB.rawQuery("DELETE FROM " + CART_TABLE+ " WHERE  "+CART_COLUMN_ID+" = "+ 359 , null));
            return   mDB.delete(CART_TABLE, CART_COLUMN_ID + "=?", new String[]{strIdDelRaw});
        }
      return -1;

    }
    /** Заносим в корзину товары
     * @param hmp hashMap из сервера
     * @return новый id строки, при ошибке-1
     */
    public long insertToCart(HashMap hmp,HashMap firmhmp) {
        ContentValues cv = new ContentValues();
        String strId= (String) hmp.get(PRODUCTS_COLUMN_REAL_ID);
        int price =Integer.valueOf(FUNCTION.stripNonDigits((String) hmp.get(DB.PRODUCTS_COLUMN_PRICE)));
        cv.put(CART_COLUMN_NAME_PRODUCTS, (String) hmp.get(PRODUCTS_COLUMN_NAME));
        cv.put(CART_COLUMN_PRODUCTS_REAL_ID, strId);
        cv.put(CART_COLUMN_PARENT_REAL_ID, (String) hmp.get(PRODUCTS_COLUMN_PARENT_ID));
        cv.put(CART_COLUMN_PRICE, price);
        cv.put(CART_COLUMN_DESCRIPTION, (String) hmp.get(PRODUCTS_COLUMN_DESCRIPTION));
        cv.put(CART_COLUMN_PATH_IMG, (String) hmp.get(PRODUCTS_COLUMN_PATH_IMG));
        cv.put(CART_COLUMN_MEASURE,(String) hmp.get(PRODUCTS_COLUMN_MEASURE));
//        заносим занные FIRM
        cv.put(CART_COLUMN_FIRM_NAME, (String) firmhmp.get(FIRM_COLUMN_NAME));
        cv.put(CART_COLUMN_MIN_PRICE,(String) firmhmp.get(FIRM_COLUMN_MIN_PRICE));
        cv.put(CART_COLUMN_DELIVERY_FREE_MINPR, (String) firmhmp.get(FIRM_COLUMN_DELIVERY_FREE_MINPR));
        cv.put(CART_COLUMN_DELIVERY_PRICE, (String) firmhmp.get(FIRM_COLUMN_DELIVERY_PRICE));

        return   mDB.insert(CART_TABLE, null, cv);
    }

    /** Дублирование товары  в корзине с CART активити
     * @param id товара

     * @return новый id строки, при ошибке-1
     */
    public long insertCopyPrToCart(HashMap hmp) {

        ContentValues cv = new ContentValues();
        cv.put(CART_COLUMN_NAME_PRODUCTS, (String) hmp.get(CART_COLUMN_NAME_PRODUCTS));
        cv.put(CART_COLUMN_PRODUCTS_REAL_ID, (String) hmp.get(CART_COLUMN_PRODUCTS_REAL_ID));
        cv.put(CART_COLUMN_PARENT_REAL_ID, (String) hmp.get(CART_COLUMN_PARENT_REAL_ID));
        cv.put(CART_COLUMN_PRICE, (String) hmp.get(CART_COLUMN_PRICE));
        cv.put(CART_COLUMN_DESCRIPTION, (String) hmp.get(CART_COLUMN_DESCRIPTION));
        cv.put(CART_COLUMN_PATH_IMG, (String) hmp.get(CART_COLUMN_PATH_IMG));
        cv.put(CART_COLUMN_FIRM_NAME, (String) hmp.get(CART_COLUMN_FIRM_NAME));
        cv.put(CART_COLUMN_MIN_PRICE,(String) hmp.get(CART_COLUMN_MIN_PRICE));
        cv.put(CART_COLUMN_MEASURE,(String) hmp.get(PRODUCTS_COLUMN_MEASURE));
        cv.put(CART_COLUMN_DELIVERY_FREE_MINPR, (String) hmp.get(CART_COLUMN_DELIVERY_FREE_MINPR));


        return   mDB.insert(CART_TABLE, null, cv);
    }


    /**
     * Получаем фирмы по id рубрики
     */
 public Cursor getFirmData(String main_r_id) {
     return mDB.query(FIRM_TABLE, null, FIRM_COLUMN_RUBRIC_MAIN_ID + " = "
             + main_r_id, null, null, null, null);
    }

    /**
     * очистка таблицы
     */
 public int cleareTable(String table) {
     return mDB.delete(table, null, null);
 }

    /**
     * занесение в таблицу истории
     */
    public long insertHistory(String order_html_message, String id_order) {

        ContentValues cv = new ContentValues();
        cv.put(HISTORY_COLUMN_ORDER_HTML_MESSAGE,order_html_message);
        cv.put(HISTORY_COLUMN_ID_ORDER, id_order);

//        return mDB.delete(table, null, null);
        return mDB.insert(HISTORY_TABLE, null, cv);
    }
//=========================================================================


}
