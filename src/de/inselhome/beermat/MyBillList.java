package de.inselhome.beermat;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import de.inselhome.beermat.domain.Bill;
import de.inselhome.beermat.exception.BillPersistenceException;
import de.inselhome.beermat.fragment.MyBillListFragment;
import de.inselhome.beermat.intent.MyBillListIntent;
import de.inselhome.beermat.persistence.BillFileRepository;
import de.inselhome.beermat.persistence.BillRepository;

import java.util.ArrayList;
import java.util.List;

public class MyBillList extends SherlockFragmentActivity implements MyBillListFragment.FragmentCallback {

    private static final String LOGTAG = "[beermat] MyBillList";

    private List<Bill> billList;
    private MyBillListFragment billListFragment;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_mybilllist);

        billList = new ArrayList<Bill>();
        billListFragment = MyBillListFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.listFragment, billListFragment).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadBills();
    }

    @Override
    public List<Bill> getBills() {
        return billList;
    }

    private void loadBills() {
        try {
            List<Bill> bills = BillFileRepository.getInstance(this).getAll();

            if (bills != null && !bills.isEmpty()) {
                setBillList(bills);
            } else {
                Toast.makeText(this, getString(R.string.mybilllist_no_bills), Toast.LENGTH_LONG).show();
            }
        } catch (BillPersistenceException e) {
            Log.e(LOGTAG, "Unable to read bills from file system", e);
            Toast.makeText(this, getString(R.string.mybilllist_load_bills_failed), Toast.LENGTH_LONG).show();
        }
    }

    private void setBillList(List<Bill> billList) {
        this.billList = billList;
        billListFragment.notifyDataChanged();
    }

    @Override
    public void onBillSelected(final Bill bill) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setMessage(R.string.mybilllist_really_load);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new MyBillListIntent(MyBillList.this, bill);
                setResult(RESULT_OK, i);
                finish();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
            }
        });

        builder.show();
    }

    @Override
    public void onDeleteBill(Bill bill) {
        BillRepository billRepository = BillFileRepository.getInstance(this);

        try {
            billRepository.delete(bill);
            billList.remove(bill);
        } catch (BillPersistenceException e) {
            Log.e(LOGTAG, "TODO: Unable to delete bill " + bill.getId(), e);
        }

        billListFragment.notifyDataChanged();
    }
}
