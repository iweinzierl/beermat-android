package de.inselhome.beermat.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import de.inselhome.beermat.R;
import de.inselhome.beermat.domain.BillPosition;
import junit.framework.Assert;

public class BillFragment extends Fragment {

    public interface BillListener {
        void onRemoveBillPosition(BillPosition billPosition);
        void onIncreaseBillPosition(BillPosition billPosition);
        void onDecreaseBillPosition(BillPosition billPosition);
    }

    private BillListener billListener;
    private ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bill, container);
        list = (ListView) view.findViewById(R.id.list);

        return view;
    }

    public void setBillListener(BillListener billListener) {
        Assert.assertNotNull(billListener);
        this.billListener = billListener;
    }

    public  void addBillPosition(BillPosition billPosition) {
        // TODO add bill position widget
    }

    public void removeBillPosition(BillPosition billPosition) {
        // TODO remove bill position widget
    }
}
