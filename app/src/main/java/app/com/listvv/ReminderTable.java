package app.com.listvv;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Rohit on 6/8/2017.
 */

public class ReminderTable {

    public static String TABLE_NAME = "rem_table";

    public static String ID = "id";
    public static String TEXT = "text";
    public static String DAY = "day";
    public static String MONTH = "month";
    public static String YEAR = "year";
    public static String HOUR = "hour";
    public static String MINUTE = "minute";

    private static String createQuery = "CREATE TABLE `rem_table` (\n" +
            "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`text`\tTEXT,\n" +
            "\t`day`\tINTEGER,\n" +
            "\t`month`\tINTEGER,\n" +
            "\t`year`\tINTEGER,\n" +
            "\t`hour`\tINTEGER,\n" +
            "\t`minute`\tINTEGER\n" +
            ");";

    public static void createTable(SQLiteDatabase db){
        db.execSQL(createQuery);
        Log.d("create", "createTable: table created");
    }

    public static void updateTable(SQLiteDatabase db) {
        String sql = "drop table if exists "+TABLE_NAME;
        db.execSQL(sql);

        Log.d("update", "updateTable: table updated");
        createTable(db);
    }

    public static long insert(SQLiteDatabase db, ContentValues cv) {
        return db.insert(TABLE_NAME, null, cv);
    }

    public static Cursor select(SQLiteDatabase db, String selection) {
        return db.query(TABLE_NAME, null, selection, null, null, null, null, null);
    }

    public static int delete(SQLiteDatabase db, String whereClause) {
        return db.delete(TABLE_NAME, whereClause, null);
    }

    public static int update(SQLiteDatabase db, ContentValues cv, String whereClause){
        return db.update(TABLE_NAME, cv, whereClause, null);
    }
}
