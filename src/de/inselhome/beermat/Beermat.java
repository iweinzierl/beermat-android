package de.inselhome.beermat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.gson.Gson;
import de.inselhome.beermat.domain.Bill;
import de.inselhome.beermat.domain.BillPosition;
import de.inselhome.beermat.exception.BillPersistenceException;
import de.inselhome.beermat.fragment.BillFragment;
import de.inselhome.beermat.intent.EditBillPositionIntent;
import de.inselhome.beermat.intent.NewBillPositionIntent;
import de.inselhome.beermat.persistence.BillFileRepository;
import de.inselhome.beermat.persistence.BillRepository;
import de.inselhome.beermat.test.TestData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class Beermat extends SherlockFragmentActivity implements BillFragment.FragmentCallback {

    private static final String BUNDLE_BILL = "bundle.save.instance.state.bill";
    private static final String LAST_BILL = "last_bill.json";
    private static final String LOGTAG = "[beermat] Beermat";

    private Bill bill;
    private BillFragment billFragment;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bill = setupBill();

        setContentView(R.layout.activity_beermat);
        billFragment = setupBillFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.billFragment, billFragment).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        saveInstanceState.putSerializable(BUNDLE_BILL, bill);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getSupportMenuInflater().inflate(R.menu.bill, menu);
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.i(LOGTAG, "Pause beermat app");
        billToDisk(bill);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.addBillPosition:
                onAddBillPosition();
                return true;

            case R.id.saveBill:
                onSaveBill();
                return true;

            case R.id.saveProfile:
                onSaveProfile();
                return true;

            case R.id.resetItems:
                onResetItems();
                return true;

            case R.id.resetAmounts:
                onResetAmounts();
                return true;

            case R.id.myBills:
                onMyBills();
                return true;

            case R.id.myProfiles:
                onMyProfiles();
                return true;

            default:
                return false;
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case NewBillPositionIntent.REQUEST_BILLPOSITION:
                if (resultCode == RESULT_OK) {
                    NewBillPositionIntent i = new NewBillPositionIntent(data);
                    addBillPosition(i.getBillPosition());
                    return;
                }

            case EditBillPositionIntent.REQUEST_BILLPOSITION:
                if (resultCode == RESULT_OK) {
                    EditBillPositionIntent i = new EditBillPositionIntent(data);
                    editBillPosition(i.getOldBillPosition(), i.getNewBillPosition());
                    return;
                }
        }
    }

    private Bill setupBill() {
        Bill bill = billFromDisk();

        if (bill == null) {
            bill = new Bill();

            for (BillPosition billPosition : TestData.createBillPositionList()) {
                bill.addBillPosition(billPosition);
            }
        }

        return bill;
    }

    private BillFragment setupBillFragment() {
        BillFragment billFragment = new BillFragment();
        billFragment.setArguments(getIntent().getExtras());
        return billFragment;
    }

    @Override
    public List<BillPosition> getBillPositions() {
        return bill.getImmutableBillPositions();
    }

    @Override
    public void onRemoveBillPosition(final BillPosition billPosition) {
        // TODO implement me
    }

    @Override
    public void onIncreaseBillPosition(final BillPosition billPosition) {
        billPosition.increase();
    }

    @Override
    public void onDecreaseBillPosition(final BillPosition billPosition) {
        billPosition.decrease();
    }

    @Override
    public void onDetailClick(final BillPosition billPosition) {
        Intent i = new EditBillPositionIntent(this, billPosition);
        startActivityForResult(i, EditBillPositionIntent.REQUEST_BILLPOSITION);
    }

    private void onAddBillPosition() {
        Intent i = new NewBillPositionIntent(this);
        startActivityForResult(i, NewBillPositionIntent.REQUEST_BILLPOSITION);
    }

    private void addBillPosition(final BillPosition billPosition) {
        bill.addBillPosition(billPosition);
        billFragment.notifyDataChanged();
    }

    private void editBillPosition(final BillPosition oldBP, final BillPosition newBP) {
        bill.removeBillPosition(oldBP);
        bill.addBillPosition(newBP);
        billFragment.notifyDataChanged();
    }

    private void onSaveBill() {
        BillRepository billRepository = BillFileRepository.getInstance(this);
        bill.setName("Test bill");
        try {
            Bill newBill = billRepository.save(bill);
            Toast.makeText(this, "Saved bill with id " + newBill.getId(), Toast.LENGTH_LONG).show();
        } catch (BillPersistenceException e) {
            Log.e(LOGTAG, e.getMessage(), e);
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void onSaveProfile() {
        // TODO
    }

    private void onResetItems() {
        new AlertDialog.Builder(this).setMessage("Remove all items from bill?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, final int i) {
                bill.removeAllBillPositions();
                billFragment.notifyDataChanged();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, final int i) {
            }
        }).show();
    }

    private void onResetAmounts() {
        new AlertDialog.Builder(this).setMessage("Reset all amounts of bill?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, final int i) {
                bill.resetBillPositionAmounts();
                billFragment.notifyDataChanged();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, final int i) {
            }
        }).show();
    }

    private void onMyBills() {
        Intent i = new Intent(this, MyBillList.class);
        startActivity(i);
    }

    private void onMyProfiles() {
        // TODO
    }

    private File getBillFile() {
        File appDir = getFilesDir();
        return new File(appDir, LAST_BILL);
    }

    private void billToDisk(Bill bill) {
        File lastBill = getBillFile();
        FileWriter writer = null;

        try {
            writer = new FileWriter(lastBill);

            String jsonStr = new Gson().toJson(bill);
            writer.write(jsonStr);
            writer.flush();
        } catch (IOException e) {
            Log.e(LOGTAG, "Unable to persist last bill to disk.", e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private Bill billFromDisk() {
        File lastBill = getBillFile();
        if (!lastBill.exists() || !lastBill.canRead()) {
            return null;
        }

        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(lastBill);
            InputStreamReader reader = new InputStreamReader(inputStream);

            return new Gson().fromJson(reader, Bill.class);
        } catch (FileNotFoundException e) {
            Log.e(LOGTAG, "Unable to read bill from disk.", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }

        return null;
    }
}
