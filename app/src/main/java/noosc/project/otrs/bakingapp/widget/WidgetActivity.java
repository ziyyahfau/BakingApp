package noosc.project.otrs.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.gson.GsonBuilder;

import noosc.project.otrs.bakingapp.Model.RecipeModel;
import noosc.project.otrs.bakingapp.R;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link WidgetActivityConfigureActivity WidgetActivityConfigureActivity}
 */
public class WidgetActivity extends AppWidgetProvider {
    private static final String TAG = "WidgetActivity";
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        String widgetText = WidgetActivityConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        Log.d(TAG, "updateAppWidget: "+widgetText);
        if(widgetText !=null) {
            RecipeModel recipeModel = new GsonBuilder().create().fromJson(widgetText, RecipeModel.class);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_activity);
            views.setTextViewText(R.id.titleIngredient, recipeModel.getName());
            String listIngredientStr = "";
            for (int i = 0; i < recipeModel.getIngredients().size(); i++) {
                listIngredientStr+="- "+recipeModel.getIngredients().get(i).getIngredient()+"\n";
            }
            views.setTextViewText(R.id.isiIngredient,listIngredientStr);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            WidgetActivityConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

