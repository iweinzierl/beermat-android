package de.inselhome.beermat.persistence;

import android.content.Context;
import com.google.gson.Gson;
import de.inselhome.beermat.domain.Bill;
import de.inselhome.beermat.exception.BillPersistenceException;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BillFileRepository implements BillRepository {

    private static BillFileRepository INSTANCE;
    private Context context;

    private BillFileRepository(Context context) {
        this.context = context;
    }

    public static BillFileRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new BillFileRepository(context);
        }

        return INSTANCE;
    }

    public Bill save(Bill bill) throws BillPersistenceException {
        long id = determineId(bill);
        File persistTo = buildBillFile(id);

        writeToFile(bill, persistTo);

        bill.setId(id);
        return bill;
    }

    public List<Bill> getAll() throws BillPersistenceException {
        List<Long> ids = determineIds();
        List<Bill> bills = new ArrayList<Bill>(ids.size());

        for (Long id: ids) {
            Bill bill = get(id);
            if (bill != null) {
                bills.add(bill);
            }
        }

        return bills;
    }

    public Bill get(long id) throws BillPersistenceException {
        File source = buildBillFile(id);
        return readFromFile(source);
    }

    private long determineId(Bill bill) throws BillPersistenceException {
        if (bill.getId() > 0) {
            return bill.getId();
        }

        return FileUtils.getNextId(FileUtils.getBillDataDirectory(context));
    }

    private List<Long> determineIds() {
        File billData = FileUtils.getBillDataDirectory(context);
        return FileUtils.getIds(billData);
    }

    private File buildBillFile(long id) {
        File billData = FileUtils.getBillDataDirectory(context);
        return new File(billData, String.valueOf(id) + FileUtils.DATA_FILE_ENDING);
    }

    private void writeToFile(Bill bill, File destination) throws BillPersistenceException {
        FileWriter writer = null;

        try {
            Gson gson = new Gson();
            String s = gson.toJson(bill);

            writer = new FileWriter(destination);
            writer.write(s);
            writer.flush();
        } catch (IOException e) {
            throw new BillPersistenceException("Unable to persist bill to file.", e);
        }
        finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) { }
            }
        }
    }

    private Bill readFromFile(File source) throws BillPersistenceException {
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(source);
            String json = IOUtils.toString(inputStream);

            return new Gson().fromJson(json, Bill.class);
        } catch (FileNotFoundException e) {
            throw new BillPersistenceException("Unable to read file: " + source.toString(), e);
        } catch (IOException e) {
            throw new BillPersistenceException("Unable to read file: " + source.toString(), e);
        }
    }
}