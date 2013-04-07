package de.inselhome.beermat.persistence;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.inselhome.beermat.domain.Bill;
import de.inselhome.beermat.exception.NotImplementedException;

public class SQLiteRepository extends SQLiteOpenHelper {

    public static final String DB_NAME = "beermat.db";
    public static final int DB_VERSION = 1;

    public static final String TBL_BILLITEM = "CREATE TABLE billitem (" +
            "id          INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "description TEXT," +
            "price       FLOAT" +
            ")";

    public static final String TBL_BILLPOSITION = "CREATE TABLE billposition (" +
            "id          INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "billitem_id INTEGER NOT NULL," +
            "amount      INTEGER DEFAULT 0," +
            "FOREIGN KEY (billitem_id) REFERENCES billitem(id)" +
            ")";

    public static final String TBL_BILL = "CREATE TABLE bill (" +
            "id          INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "name        TEXT NOT NULL" +
            ")";

    public static final String TBL_BILL_TO_BILLPOSITION = "CREATE TABLE bill_to_billposition (" +
            "bill_id         INTEGER NOT NULL," +
            "billposition_id INTEGER NOT NULL," +
            "PRIMARY KEY(bill_id, billposition_id)," +
            "FOREIGN KEY (bill_id) REFERENCES bill(id)," +
            "FOREIGN KEY (billposition_id) REFERENCES billposition(id)" +
            ")";

    private static SQLiteRepository INSTANCE;

    public SQLiteRepository(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SQLiteRepository(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public static SQLiteRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SQLiteRepository(context, DB_NAME, null, DB_VERSION);
        }

        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i("[BEERMAT]", "Initialize beermat database");

        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.execSQL(TBL_BILLITEM);
        sqLiteDatabase.execSQL(TBL_BILLPOSITION);
        sqLiteDatabase.execSQL(TBL_BILL);
        sqLiteDatabase.execSQL(TBL_BILL_TO_BILLPOSITION);
        sqLiteDatabase.endTransaction();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        // TODO
        throw new NotImplementedException("SQLiteRepository.onUpgrade");
    }

    public Bill save(Bill bill) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            // TODO
            db.close();
        }
        catch (Exception e) {
            Log.e("[BEERMAT]", "Unable to save bill", e);
        }

        return bill;
    }
}
