package de.inselhome.beermat.persistence;

import android.content.Context;
import android.util.Log;
import de.inselhome.beermat.exception.BillPersistenceException;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FileUtils {

    public static final String BILL_DATA_DIRECTORY = "bill_data";
    public static final String DATA_FILE_ENDING = ".json";
    private static final String LOGTAG = "[beermat] FileUtils";

    public static long getNextId(File directory) throws BillPersistenceException {
        List<String> filenames = getOrderedFilenames(directory);
        String lastFilename = !filenames.isEmpty() ? filenames.get(filenames.size()-1) : "0" + DATA_FILE_ENDING;

        return getIdFromFileName(lastFilename);
    }

    public static List<Long> getIds(File directory) {
        List<Long> ids = new ArrayList<Long>();
        List<String> fileNames = getOrderedFilenames(directory);

        for (String fileName: fileNames) {
            try {
                long id = getIdFromFileName(fileName);
                ids.add(id);
            }
            catch (BillPersistenceException e) {
                Log.e(LOGTAG, "Unable to get ID from file: " + fileName, e);
            }
        }

        return ids;
    }

    public static File getBillDataDirectory(Context context) {
        File billData = new File(context.getFilesDir(), BILL_DATA_DIRECTORY);
        if (!billData.exists()) {
            billData.mkdir();
        }

        return billData;
    }

    public static List<String> getOrderedFilenames(File directory) {
        String[] files = directory.list(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.endsWith(DATA_FILE_ENDING);
            }
        });

        List<String> fileNames = Arrays.asList(files);
        Collections.sort(fileNames);

        return fileNames;
    }

    private static long getIdFromFileName(String fileName) throws BillPersistenceException {
        try {
            String[] components = fileName.split("\\.");
            return Long.valueOf(components[0]);
        }
        catch (NumberFormatException e) {
            throw new BillPersistenceException("Unable to parse id from filename: " + fileName, e);
        }
    }
}
