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
import de.inselhome.beermat.intent.MyBillListIntent;
import de.inselhome.beermat.intent.MyProfileListIntent;
import de.inselhome.beermat.persistence.BillFileRepository;
import de.inselhome.beermat.persistence.BillRepository;
import de.inselhome.beermat.test.TestData;
import de.inselhome.beermat.widget.SaveBillDialogBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
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

            case EditBillPositionIntent.REQUEST_BILLPOSITION:
                if (resultCode == RESULT_OK) {
                    EditBillPositionIntent i = new EditBillPositionIntent(data);
                    if (i.getOldBillPosition() != null) {
                        editBillPosition(i.getOldBillPosition(), i.getNewBillPosition());
                    }
                    else {
                        addBillPosition(i.getNewBillPosition());
                    }
                    return;
                }

            case MyBillListIntent.REQUEST_BILL:
                if (resultCode == RESULT_OK) {
                    MyBillListIntent i = new MyBillListIntent(data);
                    loadBill(i.getBill());
                    return;
                }

            case MyProfileListIntent.REQUEST_PROFILE:
                if (resultCode == RESULT_OK) {
                    MyProfileListIntent i = new MyProfileListIntent(data);
                    loadProfile(i.getProfile());
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
    public Bill getBill() {
        return bill;
    }

    private void setBill(Bill bill) {
        this.bill = bill;
        billFragment.notifyDataChanged();
    }

    @Override
    public void onRemoveBillPosition(final BillPosition billPosition) {
        getBill().removeBillPosition(billPosition);
        billFragment.notifyDataChanged();
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
        Intent i = new EditBillPositionIntent(this);
        startActivityForResult(i, EditBillPositionIntent.REQUEST_BILLPOSITION);
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
        SaveBillDialogBuilder.build(this, new SaveBillDialogBuilder.SaveListener() {
            @Override
            public void onOk(String name) {
                BillRepository billRepository = BillFileRepository.getInstance(Beermat.this);
                bill.setName(name);
                try {
                    Bill newBill = billRepository.save(bill);
                    Toast.makeText(Beermat.this, "Saved bill with id " + newBill.getId(), Toast.LENGTH_LONG).show();
                    billFragment.notifyDataChanged();
                } catch (BillPersistenceException e) {
                    Log.e(LOGTAG, e.getMessage(), e);
                    Toast.makeText(Beermat.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void onSaveProfile() {
        SaveBillDialogBuilder.build(this, new SaveBillDialogBuilder.SaveListener() {
            @Override
            public void onOk(String name) {
                BillRepository billRepository = BillFileRepository.getInstance(Beermat.this);
                Bill profile = (Bill) getBill().clone();
                profile.setName(name);
                profile.resetBillPositionAmounts();

                try {
                    Bill newProfile = billRepository.saveAsProfile(profile);
                    Toast.makeText(Beermat.this, "Saved profile " + newProfile.getName(),
                            Toast.LENGTH_LONG).show();
                } catch (BillPersistenceException e) {
                    Log.e(LOGTAG, e.getMessage(), e);
                    Toast.makeText(Beermat.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void onResetItems() {
        new AlertDialog.Builder(this).setMessage("Remove all items from bill?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, final int i) {
                bill = new Bill();
                bill.setDate(new Date());
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
        Intent i = new MyBillListIntent(this);
        startActivityForResult(i, MyBillListIntent.REQUEST_BILL);
    }

    private void onMyProfiles() {
        Intent i = new MyProfileListIntent(this);
        startActivityForResult(i, MyProfileListIntent.REQUEST_PROFILE);
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

    private void loadBill(Bill bill) {
        Bill newBill = (Bill) bill.clone();
        setBill(newBill);
    }

    private void loadProfile(Bill profile) {
        Bill newBill = (Bill) profile.clone();
        newBill.resetBillPositionAmounts();
        newBill.setId(0);
        newBill.setName(null);
        setBill(newBill);
    }
}
