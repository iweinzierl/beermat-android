package de.inselhome.beermat.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import de.inselhome.beermat.domain.Bill;
import de.inselhome.beermat.exception.BillDatabaseException;

public class BillRepository extends SQLiteRepository {

    private static final String LOGTAG = "[beermat] BillRepository";
    private static BillRepository INSTANCE;

    private BillRepository(Context context) {
        super(context);
    }

    public static BillRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new BillRepository(context);
        }

        return INSTANCE;
    }

    public Bill save(Bill bill) throws BillDatabaseException {
        ContentValues values = new ContentValues(1);
        values.put("name", bill.getName());

        SQLiteDatabase db = null;
        long billId = 0;

        try {
            db = getWritableDatabase();
            db.beginTransaction();

            if (bill.getId() > 0) {
                updateBill(db, bill, values);
            }
            else {
                insertBill(db, bill, values);
            }
        }
        catch (Exception e) {
            throw new BillDatabaseException(e.getMessage(), e);
        }
        finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }

        if (billId <= 0) {
            throw new BillDatabaseException("Unable to save bill: " + bill);
        }

        bill.setId(billId);
        return bill;
    }

    private Bill updateBill(SQLiteDatabase db, Bill bill, ContentValues values) throws BillDatabaseException {
        int rows = db.update("bill", values, "id=?", new String[]{String.valueOf(bill.getId())});
        if (rows <= 0) {
            throw new BillDatabaseException("Unable to update bill with id " + bill.getId());
        }

        return bill;
    }

    private Bill insertBill(SQLiteDatabase db, Bill bill, ContentValues values) {
        long billId = db.insertOrThrow("bill", null, values);
        db.setTransactionSuccessful();

        bill.setId(billId);
        Log.d(LOGTAG, String.format("Successfully inserted bill '%s'", billId));

        return bill;
    }
}
