package de.inselhome.beermat;

import android.content.Intent;
import android.os.Bundle;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import de.inselhome.beermat.domain.Bill;
import de.inselhome.beermat.domain.BillPosition;
import de.inselhome.beermat.fragment.BillFragment;
import de.inselhome.beermat.intent.BillDetailIntent;
import de.inselhome.beermat.intent.EditBillPositionIntent;

import java.util.List;

public class BillDetail extends SherlockFragmentActivity implements BillFragment.FragmentCallback {

    private BillFragment billFragment;
    private Bill bill;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_billdetail);

        bill = new BillDetailIntent(getIntent()).getBill();
        billFragment = new BillFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.billFragment, billFragment).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case EditBillPositionIntent.REQUEST_BILLPOSITION:
                if (resultCode == RESULT_OK) {
                    EditBillPositionIntent i = new EditBillPositionIntent(data);
                    editBillPosition(i.getOldBillPosition(), i.getNewBillPosition());
                    return;
                }
        }
    }

    @Override
    public void onRemoveBillPosition(BillPosition billPosition) {
        bill.removeBillPosition(billPosition);
        billFragment.notifyDataChanged();
    }

    @Override
    public void onIncreaseBillPosition(BillPosition billPosition) {
        billPosition.increase();
        billFragment.notifyDataChanged();
    }

    @Override
    public void onDecreaseBillPosition(BillPosition billPosition) {
        billPosition.decrease();
        billFragment.notifyDataChanged();
    }

    @Override
    public void onDetailClick(BillPosition billPosition) {
        Intent i = new EditBillPositionIntent(this, billPosition);
        startActivityForResult(i, EditBillPositionIntent.REQUEST_BILLPOSITION);
    }

    @Override
    public List<BillPosition> getBillPositions() {
        return bill.getImmutableBillPositions();
    }

    @Override
    public Bill getBill() {
        return bill;
    }

    private void editBillPosition(final BillPosition oldBP, final BillPosition newBP) {
        bill.removeBillPosition(oldBP);
        bill.addBillPosition(newBP);
        billFragment.notifyDataChanged();
    }
}
