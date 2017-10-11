package noosc.project.otrs.bakingapp.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;

import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import noosc.project.otrs.bakingapp.Adapter.AdapterRecipe;
import noosc.project.otrs.bakingapp.Adapter.AdapterWidget;
import noosc.project.otrs.bakingapp.Model.RecipeModel;
import noosc.project.otrs.bakingapp.Network.Network;
import noosc.project.otrs.bakingapp.R;
import noosc.project.otrs.bakingapp.UI.RetrofitInterface;
import retrofit2.Call;

/**
 * The configuration screen for the {@link WidgetActivity WidgetActivity} AppWidget.
 */
public class WidgetActivityConfigureActivity extends Activity implements WidgetListener {

    private List<RecipeModel> recipes = new ArrayList<>();

    private static final String TAG = "WidgetActivityConfigure";
    private AdapterWidget adapterWidget;

    @BindView(R.id.lst_widget)RecyclerView recyclerWidget;

    private static final String PREFS_NAME = "noosc.project.otrs.bakingapp.widget.WidgetActivity";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;


    public WidgetActivityConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        Log.d(TAG, "loadTitlePref: "+titleValue);
        return titleValue;
        /*if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }*/
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);


        setContentView(R.layout.widget_activity_configure);
        ButterKnife.bind(this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false);
        recyclerWidget.setLayoutManager(layoutManager);

        adapterWidget = new AdapterWidget(recipes, this);
        recyclerWidget.setAdapter(adapterWidget);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        getAllMenu();
    }

    public void getAllMenu(){

        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                RetrofitInterface retrofitInterface = Network.getRetrofit().create(RetrofitInterface.class);

                //TODO - beginning checking for the existing movie is alrady favorited
                //for movie popular
                Call<List<RecipeModel>> recipeModel = retrofitInterface.getRecipe();
                try {
                    // recipes = recipeModel.execute().body();
                    recipes.addAll(recipeModel.execute().body());
                    for (RecipeModel data:recipes) {
                        Log.v("PRINTED 2", "" +data.getName());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                adapterWidget.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public void onSelectedRecipe(RecipeModel recipeModel) {

        RemoteViews views = new RemoteViews(getBaseContext().getPackageName(), R.layout.widget_activity);
        views.setTextViewText(R.id.titleIngredient, recipeModel.getName());
        String listIngredientStr = "";
        for (int i = 0; i < recipeModel.getIngredients().size(); i++) {
            listIngredientStr+="- "+recipeModel.getIngredients().get(i).getIngredient()+"\n";
        }
        views.setTextViewText(R.id.isiIngredient,listIngredientStr);


        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getBaseContext());
        appWidgetManager.updateAppWidget(mAppWidgetId, views);

        saveTitlePref(getBaseContext(), mAppWidgetId, new GsonBuilder().create().toJson(recipeModel));

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}

