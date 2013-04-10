package de.inselhome.beermat;


import android.content.Intent;
import android.os.Bundle;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import de.inselhome.beermat.domain.Bill;
import de.inselhome.beermat.fragment.MyBillListFragment;
import de.inselhome.beermat.intent.BillDetailIntent;
import de.inselhome.beermat.test.TestData;

import java.util.List;

public class MyBillList extends SherlockFragmentActivity implements MyBillListFragment.FragmentCallback {

    private MyBillListFragment billListFragment;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_mybilllist);

        billListFragment = new MyBillListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.listFragment, billListFragment).commit();
    }

    @Override
    public List<Bill> getBills() {
        return TestData.createBillList();
    }

    @Override
    public void onBillSelected(Bill bill) {
        Intent i = new BillDetailIntent(this, bill);
        startActivity(i);
    }

    private void setup() {
        // TODO load bills
        billListFragment.notifyDataChanged();
    }
}
