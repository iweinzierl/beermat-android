package de.inselhome.beermat.intent;

import android.content.Context;
import android.content.Intent;
import de.inselhome.beermat.MyProfileList;
import de.inselhome.beermat.domain.Bill;

public class MyProfileListIntent extends Intent {

    public static final int REQUEST_PROFILE = 41;

    public static final String EXTRA_PROFILE = "extra.myprofilelist.profile";

    public MyProfileListIntent(Context context) {
        super(context, MyProfileList.class);
    }

    public MyProfileListIntent(Intent o) {
        super(o);
    }

    public MyProfileListIntent(Bill profile) {
        super();
        putExtra(EXTRA_PROFILE, profile);
    }

    public Bill getProfile() {
        Object obj = getSerializableExtra(EXTRA_PROFILE);
        return obj instanceof Bill ? (Bill) obj : null;
    }

}
