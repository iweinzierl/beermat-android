package de.inselhome.beermat;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import de.inselhome.beermat.domain.Bill;
import de.inselhome.beermat.exception.BillPersistenceException;
import de.inselhome.beermat.fragment.MyBillListFragment;
import de.inselhome.beermat.intent.MyProfileListIntent;
import de.inselhome.beermat.persistence.BillFileRepository;

import java.util.ArrayList;
import java.util.List;

public class MyProfileList extends SherlockFragmentActivity implements MyBillListFragment.FragmentCallback {

    private static final String LOGTAG = "[beermat] MyProfileList";

    private List<Bill> profileList;
    private MyBillListFragment billListFragment;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_mybilllist);

        profileList = new ArrayList<Bill>();
        billListFragment = new MyBillListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.listFragment, billListFragment).commit();
    }

    @Override
    public List<Bill> getBills() {
        if (profileList.isEmpty()) {
            try {
                setBillList(BillFileRepository.getInstance(this).getAllProfiles());
            } catch (BillPersistenceException e) {
                Log.e(LOGTAG, "Unable to read profiles from file system", e);
            }
        }

        return profileList;
    }

    private void setBillList(List<Bill> profileList) {
        this.profileList = profileList;
        billListFragment.notifyDataChanged();
    }

    @Override
    public void onBillSelected(Bill profile) {
        Intent i = new MyProfileListIntent(profile);
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public void onDeleteBill(Bill profile) {
        Toast.makeText(this, "Profile deletion is currently not implemented", Toast.LENGTH_LONG).show();
    }
}
