package de.inselhome.beermat.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import de.inselhome.beermat.R;
import de.inselhome.beermat.domain.BillPosition;
import de.inselhome.beermat.widget.adapters.BillPositionAdapter;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import static junit.framework.Assert.assertNotNull;

public class BillFragment extends SherlockFragment implements BillPositionAdapter.ActionHandler {

    public interface FragmentCallback {
        void onRemoveBillPosition(BillPosition billPosition);

        void onIncreaseBillPosition(BillPosition billPosition);

        void onDecreaseBillPosition(BillPosition billPosition);

        void onDetailClick(BillPosition billPosition);

        List<BillPosition> getBillPositions();
    }

    private FragmentCallback fragmentCallback;
    private BillPositionAdapter billPositionAdapter;

    private ListView list;
    private TextView sumView;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bill, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getSherlockActivity();
        if (activity instanceof FragmentCallback) {
            setFragmentCallback((FragmentCallback) activity);
            setBillPositionAdapter(new BillPositionAdapter(activity, this));
            updateBillPositionList();
            return;
        }

        throw new IllegalArgumentException("Parent Activity must implement BillFragment.FragmentCallback!");
    }

    private void setBillPositionAdapter(BillPositionAdapter billPositionAdapter) {
        this.billPositionAdapter = billPositionAdapter;

        ListView list = (ListView) getView().findViewById(R.id.list);
        list.setAdapter(billPositionAdapter);
    }

    private BillPositionAdapter getBillPositionAdapter() {
        return billPositionAdapter;
    }

    public void setFragmentCallback(final FragmentCallback fragmentCallback) {
        assertNotNull("FragmentCallback must not be null!", fragmentCallback);
        this.fragmentCallback = fragmentCallback;
    }

    public FragmentCallback getFragmentCallback() {
        return fragmentCallback;
    }

    public void notifyDataChanged() {
        BillPositionAdapter adapter = getBillPositionAdapter();
        adapter.removeAllItems();

        for (BillPosition billPosition: getFragmentCallback().getBillPositions()) {
            adapter.add(billPosition);
        }

        sum();
    }

    public void updateBillPositionList() {
        List<BillPosition> billPositions = fragmentCallback.getBillPositions();

        if (billPositions != null && !billPositions.isEmpty()) {
            for (BillPosition billPosition : billPositions) {
                billPositionAdapter.add(billPosition);
            }

            sum();
        }
    }

    @Override
    public void onDecreaseClick(final BillPosition billPosition) {
        fragmentCallback.onDecreaseBillPosition(billPosition);
        billPositionAdapter.notifyDataSetChanged();
        sum();
    }

    @Override
    public void onIncreaseClick(final BillPosition billPosition) {
        fragmentCallback.onIncreaseBillPosition(billPosition);
        billPositionAdapter.notifyDataSetChanged();
        sum();
    }

    @Override
    public void onDetailClick(final BillPosition billPosition) {
        fragmentCallback.onDetailClick(billPosition);
    }

    public void sum() {
        double sum = 0;

        TextView sumView = (TextView) getView().findViewById(R.id.sum);

        for (int i = 0; i < billPositionAdapter.getCount(); i++) {
            BillPosition bp = (BillPosition) billPositionAdapter.getItem(i);
            sum += bp.sum();
        }

        sumView.setText(formatAmount(sum));
    }

    private String formatAmount(final double amount) {
        return NumberFormat.getCurrencyInstance(Locale.getDefault()).format(amount);
    }
}
