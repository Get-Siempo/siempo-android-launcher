package co.siempo.phone.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import io.focuslauncher.R;
import co.siempo.phone.fragments.HelpFragment;
import co.siempo.phone.helper.FirebaseHelper;


public class HelpActivity extends CoreActivity {
    private String TAG = "HelpActivity";
    private long startTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        loadFragment(new HelpFragment(), R.id.helpView, "main");
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseHelper.getInstance().logScreenUsageTime(this.getClass().getSimpleName(), startTime);
    }

}
