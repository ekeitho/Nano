package barqsoft.footballscores.widget;


import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by ekeitho on 12/15/15.
 */
public class ScoreWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {

        ScoreWidgetAdapter dataProvider = new ScoreWidgetAdapter(
                getApplicationContext(), intent);
        return dataProvider;
    }

}
