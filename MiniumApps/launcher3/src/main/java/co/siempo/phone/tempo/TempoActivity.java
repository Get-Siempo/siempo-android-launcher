package co.siempo.phone.tempo;


import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;

import co.siempo.phone.MainActivity;
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
import minium.co.core.util.UIUtils;

@EActivity(R.layout.activity_tempo)
public class TempoActivity extends CoreActivity {
    private String TAG = "TempoActivity";

    @Subscribe
    public void appInstalledEvent(AppInstalledEvent event) {
        if (event.isRunning()) {
            ((Launcher3App) CoreApplication.getInstance()).setAllDefaultMenusApplication();
        }
    }


    @AfterViews
    void afterViews() {
        // To check the notification service is enable or not.
        if (!MainActivity.isEnabled(this)) {
            UIUtils.confirmWithCancel(this, null, getString(R.string.msg_noti_service_dialog), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivityForResult(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"), 100);
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        }


        loadFragment(TempoFragment_.builder().build(), R.id.tempoView, "main");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && !MainActivity.isEnabled(this)) {
            UIUtils.confirmWithCancel(this, null, getString(R.string.msg_noti_service_dialog), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivityForResult(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"), 100);
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        PackageUtil.checkPermission(this);
    }



}