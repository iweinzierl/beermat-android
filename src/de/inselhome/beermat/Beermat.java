package de.inselhome.beermat;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;

import de.inselhome.beermat.domain.BillPosition;
import de.inselhome.beermat.fragment.BillFragment;
import de.inselhome.beermat.intent.EditBillPositionIntent;
import de.inselhome.beermat.intent.NewBillPositionIntent;

public class Beermat extends Activity implements BillFragment.ActionHandler {

    private BillFragment billFragment;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beermat);

        billFragment = buildBillFragment();
        getFragmentManager().beginTransaction().add(R.id.billFragment, billFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.bill, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.addBillPosition:
                onAddBillPosition();
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

    private BillFragment buildBillFragment() {
        BillFragment billFragment = new BillFragment();
        billFragment.setArguments(getIntent().getExtras());
        billFragment.setActionHandler(this);
        return billFragment;
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
        billFragment.addBillPosition(billPosition);
    }

    private void editBillPosition(final BillPosition oldBP, final BillPosition newBP) {
        billFragment.removeBillPosition(oldBP);
        billFragment.addBillPosition(newBP);
    }

    private void onSaveProfile() {
        // TODO
    }

    private void onResetItems() {
        new AlertDialog.Builder(this).setMessage("Remove all items from bill?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, final int i) {
                billFragment.removeAllItems();
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
                billFragment.resetAmounts();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, final int i) {
            }
        }).show();
    }
}
