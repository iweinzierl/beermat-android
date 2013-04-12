package de.inselhome.beermat.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import de.inselhome.beermat.domain.Bill;
import de.inselhome.beermat.domain.BillItem;
import de.inselhome.beermat.domain.BillPosition;
import de.inselhome.beermat.exception.BillDatabaseException;
import de.inselhome.beermat.exception.BillPersistenceException;
import de.inselhome.beermat.exception.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class BillDatabaseRepository extends SQLiteRepository implements BillRepository {

    private static final String LOGTAG = "[beermat] BillDatabaseRepository";
    private static BillDatabaseRepository INSTANCE;

    private BillDatabaseRepository(Context context) {
        super(context);
    }

    public static BillDatabaseRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new BillDatabaseRepository(context);
        }

        return INSTANCE;
    }

    public List<Bill> getAll() throws BillDatabaseException {
        List<Bill> bills = new ArrayList<Bill>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("bill", new String[]{"id", "name"}, null, new String[0], null, null, "name");
        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {
            bills.add(readBill(db, cursor));
            cursor.moveToNext();
        }

        return bills;
    }

    @Override
    public Bill get(long id) throws BillPersistenceException {
        throw new NotImplementedException("BillDatabaseRepository.get(long) not implemented");
    }

    @Override
    public Bill save(Bill bill) throws BillDatabaseException {
        ContentValues values = new ContentValues(1);
        values.put("name", bill.getName());

        SQLiteDatabase db = null;

        try {
            db = getWritableDatabase();
            db.beginTransaction();

            if (bill.getId() > 0) {
                updateBill(db, bill, values);
            } else {
                insertBill(db, bill, values);
                insertBillPositions(db, bill);
            }
        } catch (Exception e) {
            throw new BillDatabaseException(e.getMessage(), e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }

        if (bill.getId() <= 0) {
            throw new BillDatabaseException("Unable to save bill: " + bill);
        }

        return bill;
    }

    public void delete(Bill bill) throws BillPersistenceException {
        throw new NotImplementedException("BillDatabaseRepository.delete");
    }


    public Bill saveAsProfile(Bill bill) throws BillPersistenceException {
        throw new NotImplementedException("BillDatabaseRepository.saveAsProfile");
    }

    private List<BillPosition> getBillPositionByBillId(SQLiteDatabase db, long billId) {
        List<BillPosition> billPositions = new ArrayList<BillPosition>();

        Cursor cursor = db.query("bill_to_billposition", new String[] {"bill_id", "billposition_id"}, "bill_id",
                new String[] {String.valueOf(billId)}, null, null, null);
        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {
           billPositions.add(getBillPositionById(cursor.getLong(0)));
            cursor.moveToNext();
        }

        return billPositions;
    }

    private BillPosition getBillPositionById(long billPositionId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("billposition", new String[] {"id", "billitem_id", "amount"}, "id",
                new String[] {String.valueOf(billPositionId)}, null, null, "id");
        cursor.moveToFirst();

        return readBillPosition(cursor);
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

    private Bill insertBillPositions(SQLiteDatabase db, Bill bill) throws BillDatabaseException {
        for (BillPosition billPosition : bill.getImmutableBillPositions()) {
            insertBillItem(db, billPosition.getBillItem());
            BillPosition inserted = insertBillPosition(db, billPosition);

            if (inserted.getId() <= 0) {
                throw new BillDatabaseException("Unable to insert bill position '" + billPosition.getBillItem().getDescription() + "'");
            }

            insertBillToBillPosition(db, bill, inserted);
        }

        return bill;
    }

    private BillPosition insertBillPosition(SQLiteDatabase db, BillPosition billPosition) throws BillDatabaseException {
        ContentValues values = new ContentValues();
        values.put("amount", billPosition.getAmount());
        values.put("billitem_id", billPosition.getBillItem().getId());

        try {
            long billPositionId = db.insert("billposition", null, values);

            if (billPositionId <= 0) {
                throw new BillDatabaseException("Unable to insert bill position '" + billPosition.getBillItem().getDescription() + "'");
            }

            billPosition.setId(billPositionId);

            return billPosition;
        } catch (Exception e) {
            throw new BillDatabaseException("Unable to insert bill position '" + billPosition.getBillItem().getDescription() + "'");
        }
    }

    private BillItem insertBillItem(SQLiteDatabase db, BillItem billItem) throws BillDatabaseException {
        ContentValues values = new ContentValues();
        values.put("description", billItem.getDescription());
        values.put("price", billItem.getPrice());

        try {
            long billItemId = db.insert("billitem", null, values);
            billItem.setId(billItemId);

            if (billItem.getId() <= 0) {
                throw new BillDatabaseException("Unable to insert bill item '" + billItem.getDescription() + "'");
            }

            return billItem;
        } catch (Exception e) {
            throw new BillDatabaseException("Unable to insert bill item '" + billItem.getDescription() + "'", e);
        }
    }

    private Bill insertBillToBillPosition(SQLiteDatabase db, Bill bill, BillPosition billPosition) throws BillDatabaseException {
        ContentValues values = new ContentValues();
        values.put("bill_id", bill.getId());
        values.put("billposition_id", billPosition.getId());

        try {
            long id = db.insert("bill_to_billposition", null, values);
            if (id <= 0) {
                throw new BillDatabaseException("Unable to insert bill to billposition " + bill.getId() + " <-->" +
                        billPosition.getId());
            }

            return bill;
        } catch (Exception e) {
            throw new BillDatabaseException("Unable to insert bill to billposition " + bill.getId() + " <-->" +
                    billPosition.getId(), e);
        }
    }

    private Bill readBill(SQLiteDatabase db, Cursor cursor) {
        long id = cursor.getLong(0);
        String name = cursor.getString(1);

        Bill bill = new Bill();
        bill.setId(id);
        bill.setName(name);

        for (BillPosition billPosition : getBillPositionByBillId(db, id)) {
            bill.addBillPosition(billPosition);
        }

        return bill;
    }

    private BillPosition readBillPosition(Cursor cursor) {
        long billItemId = cursor.getLong(1);

        BillPosition billPosition = new BillPosition();
        billPosition.setId(cursor.getLong(0));
        billPosition.setAmount(cursor.getInt(2));
        billPosition.setBillItem(getBillItemById(billItemId));

        return billPosition;
    }

    private BillItem getBillItemById(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("billitem", new String[]{"id", "description", "price"}, "id",
                new String[] {String.valueOf(id)}, null, null, null);
        cursor.moveToFirst();

        BillItem billItem = new BillItem();
        billItem.setId(cursor.getLong(0));
        billItem.setDescription(cursor.getString(1));
        billItem.setPrice(cursor.getFloat(2));

        return billItem;
    }
}
