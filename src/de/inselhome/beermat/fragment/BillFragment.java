package de.inselhome.beermat.fragment;

import java.text.NumberFormat;

import java.util.Locale;

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

public class BillFragment extends Fragment implements BillPositionAdapter.ActionHandler {

    public interface ActionHandler {
        void onRemoveBillPosition(BillPosition billPosition);

        void onIncreaseBillPosition(BillPosition billPosition);

        void onDecreaseBillPosition(BillPosition billPosition);

        void onDetailClick(BillPosition billPosition);
    }

    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.getDefault());

    private ActionHandler actionHandler;
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

    public void setActionHandler(final ActionHandler actionHandler) {
        Assert.assertNotNull(actionHandler);
        this.actionHandler = actionHandler;
    }

    public void addBillPosition(final BillPosition billPosition) {
        billPositionAdapter.add(billPosition);
        sum();
    }

    public void removeBillPosition(final BillPosition billPosition) {
        billPositionAdapter.remove(billPosition);
    }

    @Override
    public void onDecreaseClick(final BillPosition billPosition) {
        actionHandler.onDecreaseBillPosition(billPosition);
        billPositionAdapter.notifyDataSetChanged();
        sum();
    }

    @Override
    public void onIncreaseClick(final BillPosition billPosition) {
        actionHandler.onIncreaseBillPosition(billPosition);
        billPositionAdapter.notifyDataSetChanged();
        sum();
    }

    @Override
    public void onDetailClick(final BillPosition billPosition) {
        actionHandler.onDetailClick(billPosition);
    }

    public void sum() {
        double sum = 0;

        for (int i = 0; i < billPositionAdapter.getCount(); i++) {
            BillPosition bp = (BillPosition) billPositionAdapter.getItem(i);
            sum += bp.sum();
        }

        sumView.setText("Summe: " + formatAmount(sum));
    }

    public void reset() {
        billPositionAdapter.removeAllItems();
        sum();
    }

    private String formatAmount(final double amount) {
        return CURRENCY_FORMAT.format(amount);
    }
}
