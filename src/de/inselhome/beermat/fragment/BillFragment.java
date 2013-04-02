package de.inselhome.beermat.fragment;

import android.app.Fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;

import de.inselhome.beermat.R;
import de.inselhome.beermat.domain.BillPosition;
import de.inselhome.beermat.test.TestData;
import de.inselhome.beermat.widget.adapters.BillPositionAdapter;

import junit.framework.Assert;

public class BillFragment extends Fragment {

    public interface BillListener {
        void onRemoveBillPosition(BillPosition billPosition);

        void onIncreaseBillPosition(BillPosition billPosition);

        void onDecreaseBillPosition(BillPosition billPosition);
    }

    private BillListener billListener;
    private BillPositionAdapter billPositionAdapter;
    private ListView list;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
            final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bill, container, false);
        list = (ListView) view.findViewById(R.id.list);
        billPositionAdapter = new BillPositionAdapter(getActivity(), TestData.createBillPositionList());

        list.setAdapter(billPositionAdapter);

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
}
