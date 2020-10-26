package com.lisungui.pharma.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lisungui.pharma.models.Alarm;
import com.lisungui.pharma.models.MedDetails;
import com.lisungui.pharma.models.Notification;
import com.lisungui.pharma.models.OrderDetails;
import com.lisungui.pharma.models.TempOrderPojo;
import com.lisungui.pharma.models.TreatmentModel;
import com.lisungui.pharma.models.UserPojo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

public class SQLiteDataBaseHelper extends SQLiteOpenHelper {

    public static final String ALARM_TABLE = "tbl_alarm";

    //    public static final String COLUMN_DESC = "desc";
    public static final String TREAT_TABLE = "tbl_treat_alarm";
    public static final String ORDER_TABLE = "tbl_order";

    //    private static final String TABLE = "tbl_";
    public static final String TEMP_ORDER_TABLE = "tbl_temp_order";
    public static final String USER_TABLE = "tbl_user";
    public static final String ORDER_DETAILS_TABLE = "tbl_order_details";
    public static final String NOTIFICATION_TABLE = "tbl_notification";
    public static final String ORDER_TYPE_TABLE = "tbl_order_type";
    // TODO ALARM_TABLE
    private static final String COLUMN_ALARM_ID = "_id";
    private static final String COLUMN_ALARM_ACTIVE = "alarm_active";
    private static final String COLUMN_ALARM_TIME = "alarm_time";
    //    public static final String MY_CART_TABLE = "tbl_my_cart";
    private static final String COLUMN_ALARM_DAYS = "alarm_days";
    private static final String COLUMN_ALARM_DIFFICULTY = "alarm_difficulty";
    private static final String COLUMN_ALARM_TONE = "alarm_tone";
    private static final String COLUMN_ALARM_VIBRATE = "alarm_vibrate";
    private static final String COLUMN_ALARM_NAME = "alarm_name";
    private static final String COLUMN_MED_NAME = "treat_med_name";
    private static final String COLUMN_MED_DESC = "treat_med_desc";
    private static final String COLUMN_MED_IMG_SRC = "treat_med_img_src";
    public static final String COLUMN_TREAT_ID = "treat_id";
    private static final String COLUMN_TREAT_ALARM_ACTIVE = "treat_alarm_active";
    // TODO ORDER_TABLE
    private static final String COLUMN_ORDER_ID = "order_id";
    public static final String COLUMN_ORDER_SERVER_ID = "order_server_id";
    public static final String COLUMN_ORDER_MED_ID = "order_med_id";
    public static final String COLUMN_ORDER_MED_NAME = "order_med_name";
    public static final String COLUMN_ORDER_PRICE = "order_price";
    public static final String COLUMN_ORDER_QNTY = "order_qnty";
    public static final String COLUMN_ORDER_TOTAL_PRICE = "order_total_price";
    public static final String COLUMN_ORDER_DATE = "order_date";
    public static final String COLUMN_ORDER_TRACK_STATUS = "order_track_status";
    public static final String COLUMN_ORDER_UPDATE_DATE = "order_update_date";
    // TODO ORDER_DETAILS_TABLE
    private static final String COLUMN_DETAIL_ID = "detail_id";
    public static final String COLUMN_DETAIL_ORDER_ID = "detail_order_id";
    public static final String COLUMN_DETAIL_FK_ORDER_ID = "detail_fk_order_id";
    //    public static final String COLUMN_ORDER_RETURN_DATE = "order_return_date";
//    public static final String COLUMN_ORDER_DELIVERY_DATE = "order_delivery_date";
    public static final String COLUMN_DETAIL_MED_ID = "detail_med_id";
    //    public static final String COLUMN_ORDER_MED_NAME = "detail_med_name";
    public static final String COLUMN_DETAIL_MED_PRICE = "detail_COLUMN_TEMP_ORDER_QTYmed_price";
    public static final String COLUMN_DETAIL_MED_QNTY = "detail_med_qnty";
    public static final String COLUMN_DETAIL_QNTY = "detail_qnty";
    public static final String COLUMN_DETAIL_PRICE = "detail_price";

    public static final String COLUMN_DETAIL_PHARMACY_ID = "pharmacy_id";
    // TODO USER_TABLE
    private static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_SERVER_ID = "user_server_id";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_USER_EMAIL_ID = "user_email_id";
    public static final String COLUMN_USER_GENDER = "user_gender";
    public static final String COLUMN_USER_MB_NO = "user_mb_no";
    public static final String COLUMN_USER_ADDRESS = "user_address";
    public static final String COLUMN_USER_INSURANCE = "user_insurnace";
    // TODO TEMP_ORDER_TABLE
    public static final String COLUMN_TEMP_ORDER_ID = "temp_order_id";
    public static final String COLUMN_TEMP_ORDER_MED_ID = "temp_order_med_id";
    public static final String COLUMN_TEMP_ORDER_MED_NAME = "temp_order_med_name";
    public static final String COLUMN_TEMP_ORDER_MED_PRICE = "temp_order_med_price";
    public static final String COLUMN_TEMP_ORDER_TOTAL_PRICE = "temp_order_total_price";
    public static final String COLUMN_TEMP_ORDER_QTY = "temp_order_qty";
    public static final String COLUMN_TEMP_PHARMACY_ID = "temp_pharmacy_id";
    public static final String COLUMN_TEMP_QTY = "temp_quantity";

    //TODO Notification Table
    private static final String COLUMN_NOT_ID = "not_id";
    public static final String COLUMN_NOT_ORDER_ID = "not_order_id";
    public static final String COLUMN_NOT_DATE = "not_date";
    public static final String COLUMN_NOT_MESSAGE = "not_message";
    private static final String TAG = SQLiteDataBaseHelper.class.getSimpleName();

    //TODO Order Type Table
    public static final String Column_Order_Type="Order_type";
    //Database Version
    private static final int DATABASE_VERSION = 2;
    //Database Name
    private static final String DATABASE_NAME = "my_pharm_db";
    private static final String CREATE_TABLE_NOTIFICATION = "CREATE TABLE IF NOT EXISTS " + NOTIFICATION_TABLE + " ( "
            + COLUMN_NOT_ID + " INTEGER primary key autoincrement, "
            + COLUMN_NOT_ORDER_ID + " INTEGER, "
            + COLUMN_NOT_MESSAGE + " TEXT, "
            + COLUMN_NOT_DATE + " DATETIME)";

    private static final String Create_Table_Order_type="CREATE TABLE IF NOT EXISTS "
            +ORDER_TYPE_TABLE+"("+Column_Order_Type+"TEXT)";

    private static final String CREATE_TABLE_TEMP_ORDER = "CREATE TABLE IF NOT EXISTS " + TEMP_ORDER_TABLE + " ( "
            + COLUMN_TEMP_ORDER_ID + " INTEGER primary key autoincrement, "
            + COLUMN_TEMP_ORDER_MED_ID + " INTEGER, "
            + COLUMN_TEMP_ORDER_MED_NAME + " TEXT, "
            + COLUMN_TEMP_ORDER_MED_PRICE + " DOUBLE, "
            + COLUMN_TEMP_ORDER_TOTAL_PRICE + " DOUBLE, "
            + COLUMN_TEMP_PHARMACY_ID + " INTEGER, "
            + COLUMN_TEMP_QTY + " TEXT, "
            + COLUMN_TEMP_ORDER_QTY + " INTEGER)";

    private static final String CREATE_TABLE_ORDER_DETAILS = "CREATE TABLE IF NOT EXISTS " + ORDER_DETAILS_TABLE + " ( "
            + COLUMN_DETAIL_ID + " INTEGER primary key autoincrement, "
            + COLUMN_DETAIL_ORDER_ID + " INTEGER, "
            + COLUMN_DETAIL_FK_ORDER_ID + " INTEGER, "
            + COLUMN_DETAIL_MED_ID + " INTEGER, "
            + COLUMN_ORDER_MED_NAME + " TEXT, "
            + COLUMN_DETAIL_PHARMACY_ID + " INTEGER, "
            + COLUMN_DETAIL_MED_PRICE + " TEXT, "
            + COLUMN_DETAIL_QNTY + " TEXT, "
            + COLUMN_DETAIL_PRICE + " TEXT, "
            + COLUMN_DETAIL_MED_QNTY + " INTEGER)";


    private static final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS " + USER_TABLE + " ( "
            + COLUMN_USER_ID + " INTEGER primary key autoincrement, "
            + COLUMN_USER_SERVER_ID + " INTEGER, "
            + COLUMN_USER_NAME + " TEXT, "
            + COLUMN_USER_GENDER + " TEXT, "
            + COLUMN_USER_MB_NO + " TEXT, "
            + COLUMN_USER_EMAIL_ID + " TEXT, "
            + COLUMN_USER_INSURANCE + " TEXT, "

            + COLUMN_USER_ADDRESS + " TEXT)";

    private static final String CREATE_TABLE_ORDER = "CREATE TABLE IF NOT EXISTS " + ORDER_TABLE + " ( "
            + COLUMN_ORDER_ID + " INTEGER primary key autoincrement, "
            + COLUMN_ORDER_SERVER_ID + " INTEGER, "
            + COLUMN_ORDER_QNTY + " INTEGER, "
            + COLUMN_ORDER_TOTAL_PRICE + " TEXT, "
            + COLUMN_ORDER_DATE + " DATETIME, "
            + COLUMN_ORDER_UPDATE_DATE + " DATETIME, "
            + COLUMN_ORDER_TRACK_STATUS + " TEXT)";

    private static final String CREATE_TABLE_ALARM = "CREATE TABLE IF NOT EXISTS " + ALARM_TABLE + " ( "
            + COLUMN_ALARM_ID + " INTEGER primary key autoincrement, "
            + COLUMN_TREAT_ID + " INTEGER NOT NULL, "
            + COLUMN_ALARM_ACTIVE + " INTEGER NOT NULL, "
            + COLUMN_ALARM_TIME + " TEXT NOT NULL, "
            + COLUMN_ALARM_DAYS + " BLOB NOT NULL, "
            + COLUMN_ALARM_DIFFICULTY + " INTEGER NOT NULL, "
            + COLUMN_ALARM_TONE + " TEXT NOT NULL, "
            + COLUMN_ALARM_VIBRATE + " INTEGER NOT NULL, "
            + COLUMN_ALARM_NAME + " TEXT NOT NULL)";

    private static final String CREATE_TABLE_TREAT_ALARM = "CREATE TABLE IF NOT EXISTS " + TREAT_TABLE + " ( "
            + COLUMN_TREAT_ID + " INTEGER primary key autoincrement, "
            + COLUMN_TREAT_ALARM_ACTIVE + " TEXT, "
            + COLUMN_MED_NAME + " TEXT NOT NULL, "
            + COLUMN_MED_IMG_SRC + " TEXT, "
            + COLUMN_MED_DESC + " TEXT NOT NULL)";


    public SQLiteDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ALARM);
        db.execSQL(CREATE_TABLE_TREAT_ALARM);
        db.execSQL(CREATE_TABLE_ORDER);
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_ORDER_DETAILS);
        db.execSQL(CREATE_TABLE_TEMP_ORDER);
        db.execSQL(CREATE_TABLE_NOTIFICATION);
        //db.execSQL(Create_Table_Order_type);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ALARM_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TREAT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ORDER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ORDER_DETAILS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + NOTIFICATION_TABLE);
        //db.execSQL("DROP TABLE IF EXISTS " + ORDER_TYPE_TABLE);
        onCreate(db);
    }

    public void DropTables(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS " + ALARM_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TREAT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ORDER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ORDER_DETAILS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + NOTIFICATION_TABLE);
        //db.execSQL("DROP TABLE IF EXISTS " + ORDER_TYPE_TABLE);

    }

    public long insertOrUpdate(String table_name, String selection, String[] col_name, String[] values) {


        long rowId = -1;

        try {
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            for (int i = 0; i < col_name.length; i++) {
                Log.d(TAG, "insertOrUpdate: columnnanme"+col_name[i]);
                Log.d(TAG, "insertOrUpdate: values "+values[i]);
                contentValues.put(col_name[i], values[i]);
            }

            if (selection == null) {
                Log.d(TAG, "insert 189"+rowId);
                rowId = sqLiteDatabase.insert(table_name, null, contentValues);
            } else {
                if (sqLiteDatabase.update(table_name, contentValues, selection, null) == 0) {
                    Log.d(TAG, "insert 193"+rowId);
                    rowId = sqLiteDatabase.insert(table_name, null, contentValues);
                } else {
                    Log.d(TAG, "update 196"+rowId);
                    rowId = sqLiteDatabase.update(table_name, contentValues, selection, null);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Exception");
        } finally {
            Log.d(TAG, "InsertorUpdate() rowId= " + rowId);
        }

        return rowId;
    }

    public UserPojo getUser() {

        UserPojo userPojo = new UserPojo();
        String query = "SELECT * FROM " + USER_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    userPojo.setUser_id(cursor.getInt(0));
                    userPojo.setUser_server_id(cursor.getInt(1));
                    userPojo.setUser_name(cursor.getString(2) == null ? "" : cursor.getString(2));
                    userPojo.setUser_gender(cursor.getString(3) == null ? "" : cursor.getString(3));
                    userPojo.setUser_mb_no(cursor.getString(4) == null ? "" : cursor.getString(4));
                    userPojo.setUser_email_id(cursor.getString(5) == null ? "" : cursor.getString(5));
                    userPojo.setUser_insurnace(cursor.getString(6) == null ? "" : cursor.getString(6));
                    userPojo.setUser_address(cursor.getString(7) == null ? "" : cursor.getString(7));
                }
            }
            cursor.close();
        }

        return userPojo;
    }


    public long insertOrUpdateOrderMyCart(int med_id, String[] col_name, String[] values) {

        long rowId = -1;

        try {
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            for (int i = 0; i < col_name.length; i++) {
                contentValues.put(col_name[i], values[i]);
            }

            if (isAddedorNot(med_id)) {
                Log.d(TAG, "198");
                rowId = sqLiteDatabase.update(ORDER_TABLE, contentValues, COLUMN_ORDER_MED_ID + "=?", new String[]{String.valueOf(med_id)});
            } else {
                Log.d(TAG, "202");
                rowId = sqLiteDatabase.insert(ORDER_TABLE, null, contentValues);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Exception");
        } finally {
            Log.d(TAG, "Update() rowId= " + rowId);
        }

        return rowId;
    }

    private boolean isAddedorNot(int med_id) {

        String selectQuery = "SELECT * FROM " + ORDER_TABLE + " WHERE " + COLUMN_ORDER_MED_ID + "=" + med_id
                + " AND " + COLUMN_ORDER_TRACK_STATUS + "='PENDING'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        return cursor.getCount() > 0;
    }


    public boolean isSameId(String pharm_id) {

        String selectQuery = "SELECT * FROM " + TEMP_ORDER_TABLE + " WHERE " + COLUMN_TEMP_PHARMACY_ID + "=" + pharm_id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() == 0) {
            String selectSecondQuery = "SELECT * FROM " + TEMP_ORDER_TABLE;

            SQLiteDatabase secondDb = this.getReadableDatabase();
            Cursor secondCursor = secondDb.rawQuery(selectSecondQuery, null);
            return secondCursor.getCount() <= 0;


        }
        if (cursor.getCount() > 0) {
            String selectSecondQuery = "SELECT * FROM " + TEMP_ORDER_TABLE;

            SQLiteDatabase secondDb = this.getReadableDatabase();
            Cursor secondCursor = secondDb.rawQuery(selectSecondQuery, null);
            return secondCursor.getCount() == cursor.getCount();
        }


        return cursor.getCount() > 0;
    }


    public long addTreatAlarm(TreatmentModel treatmentModel) {
        long rowId = -1;
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(COLUMN_MED_NAME, treatmentModel.getMed_name());
            values.put(COLUMN_MED_DESC, treatmentModel.getDescription());
            values.put(COLUMN_MED_IMG_SRC, treatmentModel.getImg_source());
            values.put(COLUMN_TREAT_ALARM_ACTIVE, treatmentModel.getToggle());

            if (treatmentModel.getId() == -1) {
                rowId = db.insert(TREAT_TABLE, null, values);
                ArrayList<Alarm> alarms = treatmentModel.getArrayList();
                Log.d(TAG, "158 size : " + alarms.size());
                for (int i = 0; i < alarms.size(); i++) {
                    long j = create(alarms.get(i), rowId);
                }
            } else {
                rowId = db.update(TREAT_TABLE, values, COLUMN_TREAT_ID + "=" + treatmentModel.getId(), null);
                ArrayList<Alarm> alarms = treatmentModel.getArrayList();
                for (int i = 0; i < alarms.size(); i++) {
                    long j = update(alarms.get(i), treatmentModel.getId());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "insert() 176:", e);
        } finally {
            Log.d(TAG, "insert() 178: rowId=" + rowId);
        }
        return rowId;
    }

    public int getUserCount() {
        String query = "SELECT * FROM " + USER_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor.getCount();
    }

    public int getOrderCount() {
        String query = "SELECT * FROM " + TEMP_ORDER_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor.getCount();
    }

    public boolean isSameUser(String selection) {

        String query = "SELECT * FROM " + USER_TABLE + " WHERE " + selection;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();

        return count > 0;
    }

    public ArrayList<TempOrderPojo> getMyCartDetails() {
        ArrayList<TempOrderPojo> tempOrderPojos = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TEMP_ORDER_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d(TAG, "321 Count : " + cursor.getCount());

        if (cursor.moveToFirst()) {

            do {
                TempOrderPojo tempOrderPojo = new TempOrderPojo();


                tempOrderPojo.setTemp_order_id(cursor.getInt(0));
                tempOrderPojo.setTemp_order_med_id(cursor.getInt(1));
                tempOrderPojo.setTemp_order_med_name(cursor.getString(2));
                tempOrderPojo.setTemp_order_med_price(cursor.getDouble(3));
                tempOrderPojo.setTemp_order_total_price(cursor.getDouble(4));

                tempOrderPojo.setTemp_order_qty(cursor.getInt(5));
                tempOrderPojo.setTemp_qty(cursor.getInt(6));
                tempOrderPojo.setTemp_pharma_id(cursor.getString(5));

                tempOrderPojos.add(tempOrderPojo);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return tempOrderPojos;
    }

    public ArrayList<Notification> getAllNotification() {
        ArrayList<Notification> notifications = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + NOTIFICATION_TABLE + " ORDER BY " + COLUMN_NOT_ID + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d(TAG, "371 Count : " + cursor.getCount());

        if (cursor.moveToFirst()) {

            do {
                Notification notification = new Notification();
                notification.setNot_id(cursor.getInt(0));
                notification.setNot_order_id(cursor.getInt(1));
                notification.setNot_msg(cursor.getString(2) == null ? "" : cursor.getString(2));
                notification.setNot_date(cursor.getString(3) == null ? "" : cursor.getString(3));
                notifications.add(notification);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return notifications;
    }

    public ArrayList<Integer> getOrderID() {
        ArrayList<Integer> order_ids = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + ORDER_TABLE + " WHERE " + COLUMN_ORDER_SERVER_ID + "!= 'Delivered'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                order_ids.add(cursor.getInt(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return order_ids;
    }

    /*public ArrayList<OrderDetails> insertOrderTable(){
        ArrayList<OrderDetails> orderDetailsArrayList = new ArrayList<>();

        String insertQuery="Insert into "+Order_Table+

        return orderDetailsArrayList;
    }*/

    public ArrayList<OrderDetails> getAllOrders() {
        ArrayList<OrderDetails> orderDetailsArrayList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + ORDER_TABLE + " ORDER BY " + COLUMN_ORDER_ID + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d(TAG, "187 Count : " + cursor.getCount());

        if (cursor.moveToFirst()) {

            do {

                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setOrder_id(cursor.getInt(cursor.getColumnIndex(COLUMN_ORDER_ID)));
                orderDetails.setOrder_server_id(cursor.getInt(cursor.getColumnIndex(COLUMN_ORDER_SERVER_ID)));
                orderDetails.setOrder_qnty(cursor.getInt(cursor.getColumnIndex(COLUMN_ORDER_QNTY)));
                orderDetails.setOrder_total_price(cursor.getDouble(cursor.getColumnIndex(COLUMN_ORDER_TOTAL_PRICE)));
                orderDetails.setOrder_date(cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_DATE)) == null ? "" : cursor.getString(4));
                orderDetails.setOrder_update_date(cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_UPDATE_DATE)) == null ? "" : cursor.getString(5));
                orderDetails.setOrder_track_status(cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_TRACK_STATUS)) == null ? "" : cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_TRACK_STATUS)));
                orderDetails.setOrder_data(getOrderDetails(cursor.getInt(cursor.getColumnIndex(COLUMN_ORDER_SERVER_ID))));
                orderDetailsArrayList.add(orderDetails);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return orderDetailsArrayList;
    }

    private ArrayList<MedDetails> getOrderDetails(int order_id) {
        ArrayList<MedDetails> medDetailsArrayList = new ArrayList<>();

        String query = "SELECT * FROM " + ORDER_DETAILS_TABLE + " WHERE " + COLUMN_DETAIL_FK_ORDER_ID + "=" + order_id;
        Log.d(TAG, "376 Query : " + query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Log.d(TAG, "286 Count : " + cursor.getCount());

        if (cursor.moveToFirst()) {

            do {
                MedDetails medDetails = new MedDetails();
                medDetails.setDetail_id(cursor.getInt(0));
                medDetails.setDetail_order_id(cursor.getInt(1));
                medDetails.setDetail_fk_order_id(cursor.getInt(2));
                medDetails.setDetail_med_id(cursor.getInt(3));
                medDetails.setMed_name(cursor.getString(4));


                medDetails.setDetail_qnty(cursor.getString(6));
                medDetails.setDetail_med_qnty(cursor.getInt(7));
                medDetails.setDetail_qnty(cursor.getInt(7) + "");
                medDetails.setDetail_med_price(cursor.getDouble(8));
                medDetailsArrayList.add(medDetails);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return medDetailsArrayList;
    }

    public ArrayList<Alarm> getAll(String selection) {
        ArrayList<Alarm> alarms = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + ALARM_TABLE + " " + selection;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d(TAG, "187 Count : " + cursor.getCount());

        if (cursor.moveToFirst()) {

            do {
                Alarm alarm = new Alarm();
                alarm.setId(cursor.getInt(0));
                alarm.setTreat_id(cursor.getInt(1));
                alarm.setAlarmActive(cursor.getInt(2) == 1);
                alarm.setAlarmTime(cursor.getString(3));
                byte[] repeatDaysBytes = cursor.getBlob(4);

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                        repeatDaysBytes);
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(
                            byteArrayInputStream);
                    Alarm.Day[] repeatDays;
                    Object object = objectInputStream.readObject();
                    if (object instanceof Alarm.Day[]) {
                        repeatDays = (Alarm.Day[]) object;
                        alarm.setDays(repeatDays);
                    }
                } catch (StreamCorruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                alarm.setDifficulty(Alarm.Difficulty.values()[cursor.getInt(5)]);
                alarm.setAlarmTonePath(cursor.getString(6));
                alarm.setVibrate(cursor.getInt(7) == 1);
                alarm.setAlarmName(cursor.getString(8));

                alarms.add(alarm);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return alarms;
    }

    private long create(Alarm alarm, long treat_id) {
        Log.d(TAG, "Hrishi238 alarm : " + alarm.getAlarmTimeString());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ALARM_ACTIVE, alarm.getAlarmActive());
        cv.put(COLUMN_TREAT_ID, treat_id);
        cv.put(COLUMN_ALARM_TIME, alarm.getAlarmTimeString());

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(alarm.getDays());
            byte[] buff = bos.toByteArray();

            cv.put(COLUMN_ALARM_DAYS, buff);

        } catch (Exception e) {
            e.printStackTrace();
        }

        cv.put(COLUMN_ALARM_DIFFICULTY, alarm.getDifficulty().ordinal());
        cv.put(COLUMN_ALARM_TONE, alarm.getAlarmTonePath());
        cv.put(COLUMN_ALARM_VIBRATE, alarm.getVibrate());
        cv.put(COLUMN_ALARM_NAME, alarm.getAlarmName());

        Log.d(TAG, "267 alarm id : " + alarm.getId());
        if (alarm.getId() == -1 || alarm.getId() == 0) {
            long k = sqLiteDatabase.insert(ALARM_TABLE, null, cv);
            Log.d(TAG, "271 k : " + k);
            return k;
        } else {
            Log.d(TAG, "273");
            long l = sqLiteDatabase.update(ALARM_TABLE, cv, COLUMN_ALARM_ID + "=" + alarm.getId(), null);
            Log.d(TAG, "277 l : " + l);
            return l;
        }
    }

    private long update(Alarm alarm, long rowId) {
        Log.d(TAG, "Hrishi266 alarm : " + alarm.getAlarmTimeString());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ALARM_ACTIVE, alarm.getAlarmActive());
        cv.put(COLUMN_TREAT_ID, rowId);
        cv.put(COLUMN_ALARM_TIME, alarm.getAlarmTimeString());

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(alarm.getDays());
            byte[] buff = bos.toByteArray();

            cv.put(COLUMN_ALARM_DAYS, buff);

        } catch (Exception e) {
            e.printStackTrace();
        }

        cv.put(COLUMN_ALARM_DIFFICULTY, alarm.getDifficulty().ordinal());
        cv.put(COLUMN_ALARM_TONE, alarm.getAlarmTonePath());
        cv.put(COLUMN_ALARM_VIBRATE, alarm.getVibrate());
        cv.put(COLUMN_ALARM_NAME, alarm.getAlarmName());

        Log.d(TAG, "303 alarm.getId() " + alarm.getId());

        if (alarm.getId() == -1 || alarm.getId() == 0) {
            Log.d(TAG, "304");
            return sqLiteDatabase.insert(ALARM_TABLE, null, cv);
        } else {
            Log.d(TAG, "309");
            return sqLiteDatabase.update(ALARM_TABLE, cv, COLUMN_ALARM_ID + "=" + alarm.getId() + " AND " + COLUMN_TREAT_ID + "=" + rowId, null);
        }

//        return sqLiteDatabase.update(ALARM_TABLE, cv, "_id=" + alarm.getId(), null);
    }

    public ArrayList<TreatmentModel> getAllTreatAlarm(String selections) {
        ArrayList<TreatmentModel> treatList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TREAT_TABLE + " " + selections;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d(TAG, "297 count :" + cursor.getCount());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {

                do {
                    TreatmentModel treatmentModel = new TreatmentModel();
                    treatmentModel.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_TREAT_ID))));
                    treatmentModel.setMed_name(cursor.getString(cursor.getColumnIndex(COLUMN_MED_NAME)));
                    treatmentModel.setToggle(cursor.getString(cursor.getColumnIndex(COLUMN_TREAT_ALARM_ACTIVE)));
                    treatmentModel.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_MED_DESC)));
                    treatmentModel.setImg_source(cursor.getString(cursor.getColumnIndex(COLUMN_MED_IMG_SRC)));

                    String selection = "WHERE " + COLUMN_TREAT_ID + "=" + cursor.getString(cursor.getColumnIndex(COLUMN_TREAT_ID));
                    Log.d(TAG, "311 selection : " + selection);

                    treatmentModel.setArrayList(getAll(selection));

                    treatList.add(treatmentModel);
                } while (cursor.moveToNext());

            }
        }

        cursor.close();
        return treatList;
    }

    public void deleteTreatAlarm(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM tbl_treat_alarm WHERE treat_id=" + id);
        db.execSQL("DELETE FROM tbl_alarm WHERE treat_id=" + id);
        db.close();
    }

    public void deleteAll(String table_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + table_name);
        db.close();
    }

    public void deleteRows(String table_name, String selection) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + table_name + " WHERE " + selection);
        db.close();
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}