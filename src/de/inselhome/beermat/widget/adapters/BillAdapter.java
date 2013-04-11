package de.inselhome.beermat.widget.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.inselhome.beermat.R;
import de.inselhome.beermat.domain.Bill;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BillAdapter extends BaseAdapter {

    private static final String DATE_FORMAT = "EEE, d MMM yyyy HH:mm";

    public List<Bill> bills;

    public BillAdapter() {
        bills = new ArrayList<Bill>();
    }

    @Override
    public int getCount() {
        return bills.size();
    }

    @Override
    public Object getItem(int position) {
        if (position < getCount()) {
            return bills.get(position);
        }

        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.listitem_bill, parent, false);

        TextView date = (TextView) view.findViewById(R.id.date);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView sum = (TextView) view.findViewById(R.id.sum);

        Bill bill = (Bill) getItem(position);
        if (bill == null) {
            return view;
        }

        date.setText(formatDate(bill.getDate()));
        name.setText(bill.getName());
        sum.setText(formatSum(bill.sum()));

        return view;
    }

    public void add(Bill bill) {
        bills.add(bill);
        notifyDataSetChanged();
    }

    public void add(List<Bill> bills) {
        for (Bill bill: bills) {
            add(bill);
        }
    }

    private String formatDate(Date date) {
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }

    private String formatSum(double sum) {
        return NumberFormat.getCurrencyInstance(Locale.getDefault()).format(sum);
    }

    public void clear() {
        bills.clear();
        notifyDataSetChanged();
    }
}
