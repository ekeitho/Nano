package barqsoft.footballscores.widget;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;

/**
 * Created by ekeitho on 12/15/15.
 */
public class ScoreWidgetProvider extends AppWidgetProvider {

    //incorporates Looper, Thread and MessageQueue together
    private static HandlerThread sWorkerThread;
    //allows you to send and process Message and Runnable objects associated with a thread's MessageQueue.
    private static Handler sWorkerQueue;
    private static ScoreObserver sDataObserver;
    public static final String TOAST_ACTION = "com.ekeitho.TOAST_ACTION";
    public static final String ITEM_POSITION = "com.ekeitho.ITEM_POSITION";


    public ScoreWidgetProvider() {
        sWorkerThread = new HandlerThread("AppWidgetProvider-worker");
        sWorkerThread.start();
        sWorkerQueue = new Handler(sWorkerThread.getLooper());
    }

    @Override
    public void onEnabled(Context context) {
        final ContentResolver resolver = context.getContentResolver();
        if (sDataObserver == null) {
            final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            final ComponentName cn = new ComponentName(context, ScoreWidgetProvider.class);
            sDataObserver = new ScoreObserver(mgr, cn, sWorkerQueue);
            resolver.registerContentObserver(DatabaseContract.ScoresTable.buildScoreWithDate(), true, sDataObserver);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(TOAST_ACTION)) {
            int widgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            int index = intent.getIntExtra(ScoreWidgetProvider.ITEM_POSITION, 0);
            Toast.makeText(context, "Touched position " + index, Toast.LENGTH_SHORT).show();
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        for (int widgetId : appWidgetIds) {
            Intent intent = new Intent(context, ScoreWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            // set view the widget
            RemoteViews mView = new RemoteViews(context.getPackageName(), R.layout.score_widget_layout);
            //no longer using deprecated setRemoteAdapter
            mView.setRemoteAdapter(R.id.widgetListView, intent);
            mView.setEmptyView(R.id.widgetListView, R.id.empty_view);

            Intent toastIntent = new Intent(context, ScoreWidgetProvider.class);
            toastIntent.setAction(ScoreWidgetProvider.TOAST_ACTION);
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

            PendingIntent pendingIntent = PendingIntent
                    .getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mView.setPendingIntentTemplate(R.id.widgetListView, pendingIntent);


            appWidgetManager.updateAppWidget(widgetId, mView);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}

class ScoreObserver extends ContentObserver {

    private AppWidgetManager manager;
    private ComponentName name;

    ScoreObserver(AppWidgetManager mgr, ComponentName nm, Handler h) {
        super(h);
        manager = mgr;
        name = nm;
    }

    @Override
    public void onChange(boolean selfChange) {
        manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(name), R.id.widgetListView);
    }
}

