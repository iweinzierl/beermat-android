package de.inselhome.beermat;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
        billListFragment = new MyBillListFragment();

        if (bundle == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.listFragment, billListFragment).commit();
        }
    }

    @Override
    public List<Bill> getBills() {
        if (billList == null || billList.isEmpty()) {
            try {
                setBillList(BillFileRepository.getInstance(this).getAll());
            } catch (BillPersistenceException e) {
                Log.e(LOGTAG, "Unable to read bills from file system", e);
            }
        }

        return billList;
    }

    private void setBillList(List<Bill> billList) {
        this.billList = billList;
    }

    @Override
    public void onBillSelected(Bill bill) {
        Intent i = new MyBillListIntent(this, bill);
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public void onDeleteBill(Bill bill) {
        BillRepository billRepository = BillFileRepository.getInstance(this);

        try {
            billRepository.delete(bill);
        } catch (BillPersistenceException e) {
            Log.e(LOGTAG, "Unable to delete bill " + bill.getId(), e);
        }

        billListFragment.notifyDataChanged();
    }
}
