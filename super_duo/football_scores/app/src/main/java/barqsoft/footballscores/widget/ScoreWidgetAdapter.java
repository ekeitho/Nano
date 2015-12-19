package barqsoft.footballscores.widget;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;

/**
 * Created by ekeitho on 12/15/15.
 */

@SuppressLint("NewApi")
public class ScoreWidgetAdapter implements RemoteViewsService.RemoteViewsFactory {

    private Cursor mCursor;
    private Context mContext;
    private int mAppWidgetId;

    public static final int COL_DATE = 1;
    public static final int COL_MATCHTIME = 2;
    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_LEAGUE = 5;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_ID = 8;
    public static final int COL_MATCHDAY = 9;

    public ScoreWidgetAdapter(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                                          AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        // do nothing here, since we reload the cursor in onDataSetChanged
    }

    @Override
    public void onDataSetChanged() {
        if (mCursor != null) {
            mCursor.close();
        }

        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
        String[] date = {mformat.format(new Date(System.currentTimeMillis()))};

        final long token = Binder.clearCallingIdentity();
        try {
            mCursor = mContext.getContentResolver().query(DatabaseContract.ScoresTable.buildScoreWithDate(), null, null, date, null);
        } finally {
            Binder.restoreCallingIdentity(token);
        }

    }


    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

        if(mCursor.moveToPosition(position)) {
            rv.setTextViewText(R.id.home_name, mCursor.getString(COL_HOME));
            rv.setTextViewText(R.id.away_name, mCursor.getString(COL_AWAY));

            if(Integer.parseInt(mCursor.getString(COL_HOME_GOALS)) < 0) {
                rv.setTextViewText(R.id.score_textview, "0 - 0");
            } else {
                rv.setTextViewText(R.id.score_textview, mCursor.getString(COL_HOME_GOALS) + " - " +
                        mCursor.getString(COL_AWAY_GOALS));
            }

            rv.setTextViewText(R.id.date_textview, mCursor.getString(COL_DATE));


            Bundle extra = new Bundle();
            extra.putInt(ScoreWidgetProvider.ITEM_POSITION, position);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extra);
            // now it is possible to distinguish the individual on click action of an item
            rv.setOnClickFillInIntent(R.id.widget_item, fillInIntent);
        }

        return rv;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

}
