package de.inselhome.beermat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import de.inselhome.beermat.domain.BillPosition;
import de.inselhome.beermat.fragment.BillFragment;

public class Beermat extends Activity implements BillFragment.BillListener {

    private BillFragment billFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beermat);

        billFragment = buildBillFragment();
        getFragmentManager().beginTransaction().add(R.id.billFragment, billFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bill, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.addBillPosition:
                onAddBillPosition();
                return true;
            case R.id.saveProfile:
                onSaveProfile();
                return true;
            case R.id.reset:
                onReset();
                return true;
            default:
                return false;
        }
    }


    private BillFragment buildBillFragment() {
        BillFragment billFragment = new BillFragment();
        billFragment.setArguments(getIntent().getExtras());
        billFragment.setBillListener(this);
        return billFragment;
    }

    @Override
    public void onRemoveBillPosition(BillPosition billPosition) {
        // TODO implement me
    }

    @Override
    public void onIncreaseBillPosition(BillPosition billPosition) {
        billPosition.increase();
    }

    @Override
    public void onDecreaseBillPosition(BillPosition billPosition) {
        billPosition.decrease();
    }

    private void onAddBillPosition() {
        // TODO
    }

    private void onSaveProfile() {
        // TODO
    }

    private void onReset() {
        new AlertDialog.Builder(this).setMessage("Really reset bill?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                billFragment.reset();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).show();
    }
}
