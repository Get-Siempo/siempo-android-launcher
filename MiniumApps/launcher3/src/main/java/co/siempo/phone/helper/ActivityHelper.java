package co.siempo.phone.helper;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.List;

import co.siempo.phone.BuildConfig;
import co.siempo.phone.R;
import co.siempo.phone.activities.AlphaSettingsActivity_;
import co.siempo.phone.activities.NoteListActivity;
import co.siempo.phone.activities.SuppressNotificationActivity;
import co.siempo.phone.app.CoreApplication;
import co.siempo.phone.db.DBClient;
import co.siempo.phone.launcher.FakeLauncherActivity;
import co.siempo.phone.log.Tracer;
import co.siempo.phone.utils.UIUtils;


public class ActivityHelper {

    private Context context;

    public ActivityHelper(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void openNotesApp(boolean openLast) {
        try {
            Intent intent = new Intent(getContext(), NoteListActivity.class);
            intent.putExtra(NoteListActivity.EXTRA_OPEN_LATEST, openLast);
            getContext().startActivity(intent);
        } catch (Exception e) {
            CoreApplication.getInstance().logException(e);
            Tracer.e(e, e.getMessage());
        }
    }


    public void handleDefaultLauncher(Activity activity) {
        if (activity != null) {
            if (UIUtils.isMyLauncherDefault(activity)) {
                Tracer.i("Launcher3 is the default launcher");
                activity.getPackageManager().clearPackagePreferredActivities(activity.getPackageName());
                openChooser(activity);
            } else {
                Tracer.i("Launcher3 is not the default launcher: " + UIUtils.getLauncherPackageName(activity));
                if (UIUtils.getLauncherPackageName(activity).equals("android")) {
                    openChooser(activity);
                } else
                    resetPreferredLauncherAndOpenChooser(activity);
            }
        }
    }

    private void resetPreferredLauncherAndOpenChooser(Activity activity) {
        if (activity != null) {
            PackageManager packageManager = activity.getPackageManager();
            ComponentName componentName = new ComponentName(activity, FakeLauncherActivity.class);
            packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            activity.startActivity(startMain);
            packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);
        }
    }

    private void openChooser(Activity activity) {
        if (activity != null) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(startMain);
        }
    }


    public void openBecomeATester() {
        final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) {
            CoreApplication.getInstance().logException(e);
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    /**
     * Open the application with predefine package name.
     */
    public boolean openAppWithPackageName(String packageName) {
        if (packageName != null && !packageName.equalsIgnoreCase("")) {
            try {
                new DBClient().deleteMsgByPackageName(packageName);
                Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
                context.startActivity(intent);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                CoreApplication.getInstance().logException(e);
                UIUtils.alert(context, context.getString(R.string.app_not_found));
                return false;
            }
        } else {
            UIUtils.alert(context, context.getString(R.string.app_not_found));
            return false;
        }
    }


    public void openSiempoSuppressNotificationsSettings() {
        try {
            Intent i = new Intent(context, SuppressNotificationActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(i);
        } catch (Exception e) {
            Tracer.e(e, e.getMessage());
            CoreApplication.getInstance().logException(e);
        }
    }


    public void openSiempoAlphaSettingsApp() {
        try {
            AlphaSettingsActivity_.intent(getContext()).start();
        } catch (Exception e) {
            CoreApplication.getInstance().logException(e);
            Tracer.e(e, e.getMessage());
        }
    }
}
