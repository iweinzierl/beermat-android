package de.inselhome.beermat.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import de.inselhome.beermat.R;
import de.inselhome.beermat.domain.BillPosition;
import de.inselhome.beermat.test.TestData;
import de.inselhome.beermat.widget.adapters.BillPositionAdapter;
import junit.framework.Assert;

import java.text.NumberFormat;
import java.util.Locale;

public class BillFragment extends Fragment implements BillPositionAdapter.ClickListener {

    public interface BillListener {
        void onRemoveBillPosition(BillPosition billPosition);

        void onIncreaseBillPosition(BillPosition billPosition);

        void onDecreaseBillPosition(BillPosition billPosition);
    }

    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.getDefault());

    private BillListener billListener;
    private BillPositionAdapter billPositionAdapter;

    private ListView list;
    private TextView sumView;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
            final Bundle savedInstanceState) {

        billPositionAdapter = new BillPositionAdapter(getActivity(), this, TestData.createBillPositionList());

        View view = inflater.inflate(R.layout.fragment_bill, container, false);
        list = (ListView) view.findViewById(R.id.list);
        list.setAdapter(billPositionAdapter);

        sumView = (TextView) view.findViewById(R.id.sum);

        return view;
    }

    public void setBillListener(final BillListener billListener) {
        Assert.assertNotNull(billListener);
        this.billListener = billListener;
    }

    public void addBillPosition(final BillPosition billPosition) {
        // TODO add bill position widget
    }

    public void removeBillPosition(final BillPosition billPosition) {
        // TODO remove bill position widget
    }

    @Override
    public void onDecreaseClick(BillPosition billPosition) {
        billListener.onDecreaseBillPosition(billPosition);
        billPositionAdapter.notifyDataSetChanged();
        sum();
    }

    @Override
    public void onIncreaseClick(BillPosition billPosition) {
        billListener.onIncreaseBillPosition(billPosition);
        billPositionAdapter.notifyDataSetChanged();
        sum();
    }

    public void sum() {
        double sum = 0;

        for (int i = 0; i < billPositionAdapter.getCount(); i++) {
            BillPosition bp = (BillPosition) billPositionAdapter.getItem(i);
            sum += bp.sum();
        }

        sumView.setText("Summe: " + formatAmount(sum));
    }

    private String formatAmount(double amount) {
        return CURRENCY_FORMAT.format(amount);
    }
}
