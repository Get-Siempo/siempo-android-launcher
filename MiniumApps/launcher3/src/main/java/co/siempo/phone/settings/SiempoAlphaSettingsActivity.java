package co.siempo.phone.settings;

import android.app.Fragment;
import android.content.Context;
import android.util.Log;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;

import co.siempo.phone.R;
import co.siempo.phone.app.Launcher3App;
import co.siempo.phone.notification.NotificationFragment;
import co.siempo.phone.notification.NotificationRetreat_;
import co.siempo.phone.ui.TopFragment_;
import co.siempo.phone.util.PackageUtil;
import de.greenrobot.event.Subscribe;
import minium.co.core.app.CoreApplication;
import minium.co.core.event.AppInstalledEvent;
import minium.co.core.event.HomePressEvent;
import minium.co.core.ui.CoreActivity;

/**
 * Created by hardik on 17/8/17.
 */


@EActivity(R.layout.activity_siempo_alpha_settings)
public class SiempoAlphaSettingsActivity extends CoreActivity {

    private Context context;

    @Subscribe
    public void appInstalledEvent(AppInstalledEvent event) {
        if (event.isRunning()) {
            ((Launcher3App) CoreApplication.getInstance()).setAllDefaultMenusApplication();
        }
    }

    private final String TAG = "SiempoAlphaSetting";


    @AfterViews
    void afterViews() {
        initView();
    }


    public void initView() {
        context = SiempoAlphaSettingsActivity.this;
    }


    @Override
    protected void onResume() {
        super.onResume();
        PackageUtil.checkPermission(this);
    }




}
