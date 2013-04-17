package de.inselhome.beermat.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockListFragment;
import de.inselhome.beermat.domain.Bill;
import de.inselhome.beermat.widget.BillListContextDialogBuilder;
import de.inselhome.beermat.widget.adapters.BillAdapter;

import java.util.List;

public class MyBillListFragment extends SherlockListFragment {

    private static final String LOGTAG = "[beermat] MyBillListFragment";

    public interface FragmentCallback {
        List<Bill> getBills();

        void onBillSelected(Bill bill);

        void onDeleteBill(Bill bill);
    }

    private FragmentCallback fragmentCallback;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        Activity activity = getActivity();
        if (!(activity instanceof FragmentCallback)) {
            throw new IllegalArgumentException("Parent Activity must implement FragmentCallback.");
        }

        setFragmentCallback((FragmentCallback) activity);
        setListAdapter(new BillAdapter());
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onCreate(bundle);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                MyBillListFragment.this.onItemLongClick((Bill) getListAdapter().getItem(position));
                return true;
            }
        });
    }

    @Override
    public void onListItemClick(ListView list, View view, int pos, long id) {
        Log.d(LOGTAG, "Clicked bill at position " + pos);
        getFragmentCallback().onBillSelected((Bill) getListAdapter().getItem(pos));
    }

    private void onItemLongClick(final Bill item) {
        BillListContextDialogBuilder.build(getActivity(), new BillListContextDialogBuilder.Callback() {
            @Override
            public void onDelete() {
                getFragmentCallback().onDeleteBill(item);
            }
        }).show();
    }

    public void setFragmentCallback(FragmentCallback fragmentCallback) {
        this.fragmentCallback = fragmentCallback;
    }

    public FragmentCallback getFragmentCallback() {
        return fragmentCallback;
    }

    public void notifyDataChanged() {
        BillAdapter billAdapter = (BillAdapter) getListAdapter();
        billAdapter.clear();
        billAdapter.add(fragmentCallback.getBills());
    }
}
