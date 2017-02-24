package com.bloodshare.bloodshareandroid.data;

import com.bloodshare.bloodshareandroid.data.db.DbHelper;
import com.bloodshare.bloodshareandroid.data.network.ApiHelper;
import com.bloodshare.bloodshareandroid.data.prefs.PreferenceHelper;

/**
 * Created by sayem on 2/24/2017.
 */

public interface DataManager extends PreferenceHelper, DbHelper, ApiHelper {
}
