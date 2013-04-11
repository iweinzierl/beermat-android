package de.inselhome.beermat.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockListFragment;
import de.inselhome.beermat.domain.Bill;
import de.inselhome.beermat.widget.adapters.BillAdapter;

import java.util.List;

public class MyBillListFragment extends SherlockListFragment {

    private static final String LOGTAG = "[beermat] MyBillListFragment";

    public interface FragmentCallback {
        List<Bill> getBills();

        void onBillSelected(Bill bill);
    }

    private FragmentCallback fragmentCallback;
    private BillAdapter billAdapter;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        Activity activity = getActivity();
        if (!(activity instanceof FragmentCallback)) {
            throw new IllegalArgumentException("Parent Activity must implement FragmentCallback.");
        }

        billAdapter = new BillAdapter();

        setFragmentCallback((FragmentCallback) activity);
        setListAdapter(billAdapter);

        notifyDataChanged();
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onCreate(bundle);
        billAdapter.add(getFragmentCallback().getBills());
    }

    @Override
    public void onListItemClick(ListView list, View view, int pos, long id) {
        Log.d(LOGTAG, "Clicked bill at position " + pos);
        getFragmentCallback().onBillSelected((Bill) billAdapter.getItem(pos));
    }

    public void setFragmentCallback(FragmentCallback fragmentCallback) {
        this.fragmentCallback = fragmentCallback;
    }

    public FragmentCallback getFragmentCallback() {
        return fragmentCallback;
    }

    public void notifyDataChanged() {
        billAdapter.add(fragmentCallback.getBills());
    }
}
