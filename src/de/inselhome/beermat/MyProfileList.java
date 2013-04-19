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
import de.inselhome.beermat.intent.MyProfileListIntent;
import de.inselhome.beermat.persistence.BillFileRepository;
import de.inselhome.beermat.persistence.BillRepository;

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
        billListFragment = MyBillListFragment.newInstance("de.inselhome.beermat.widget.adapters.ProfileAdapter");
        getSupportFragmentManager().beginTransaction().replace(R.id.listFragment, billListFragment).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadProfiles();
    }

    @Override
    public List<Bill> getBills() {
        return profileList;
    }

    private void loadProfiles() {
        try {
            List<Bill> profiles = BillFileRepository.getInstance(this).getAllProfiles();

            if (profiles != null && !profiles.isEmpty()) {
                setProfiles(profiles);
            }
            else {
                Toast.makeText(this, getString(R.string.myprofilelist_no_profiles), Toast.LENGTH_LONG).show();
            }
        } catch (BillPersistenceException e) {
            Log.e(LOGTAG, "Unable to read profiles from file system", e);
            Toast.makeText(this, getString(R.string.myprofilelist_load_profiles_failed), Toast.LENGTH_LONG).show();
        }
    }

    private void setProfiles(List<Bill> profileList) {
        this.profileList = profileList;
        billListFragment.notifyDataChanged();
    }

    @Override
    public void onBillSelected(final Bill profile) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setMessage(R.string.mybilllist_really_load);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new MyProfileListIntent(profile);
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
    public void onDeleteBill(Bill profile) {
        deleteProfile(profile);
    }

    private void deleteProfile(Bill profile) {
        BillRepository billRepository = BillFileRepository.getInstance(this);

        try {
            billRepository.deleteProfile(profile);
            profileList.remove(profile);
        } catch (BillPersistenceException e) {
            Log.e(LOGTAG, "TODO: Unable to delete profile " + profile.getId(), e);
        }

        billListFragment.notifyDataChanged();
    }
}
