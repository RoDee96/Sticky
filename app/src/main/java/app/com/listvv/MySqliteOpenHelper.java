package app.com.listvv;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Rohit on 6/8/2017.
 */

public class MySqliteOpenHelper extends SQLiteOpenHelper{

    Context context;

    public MySqliteOpenHelper(Context context) {
        super(context, "MyDb.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Toast.makeText(context, "on Create", Toast.LENGTH_SHORT).show();

        ReminderTable.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Toast.makeText(context, "on Upgrade", Toast.LENGTH_SHORT).show();

        ReminderTable.updateTable(db);

    }
}
