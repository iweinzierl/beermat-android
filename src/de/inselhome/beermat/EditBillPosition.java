package de.inselhome.beermat;

import android.content.Intent;
import android.os.Bundle;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import de.inselhome.beermat.domain.BillPosition;
import de.inselhome.beermat.fragment.EditBillPositionFragment;
import de.inselhome.beermat.intent.EditBillPositionIntent;

public class EditBillPosition extends SherlockFragmentActivity implements EditBillPositionFragment.ActionHandler {

    private EditBillPositionFragment editBillPositionFragment;

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_editbillposition);

        editBillPositionFragment = new EditBillPositionFragment(this, getBillPositionFromIntent());

        getSupportFragmentManager().beginTransaction().add(R.id.editBillPositionFragment, editBillPositionFragment).commit();
    }

    private BillPosition getBillPositionFromIntent() {
        Object obj = getIntent().getSerializableExtra(EditBillPositionIntent.EXTRA_BILLPOSITION_ORIG);
        return obj instanceof BillPosition ? (BillPosition) obj : null;
    }

    @Override
    public void onOkClicked(final BillPosition billPosition) {
        Intent i = new EditBillPositionIntent(this, getBillPositionFromIntent(), billPosition);
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public void onCancelClicked() {
        finish();
    }
}
