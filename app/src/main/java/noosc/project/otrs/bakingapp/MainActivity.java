package noosc.project.otrs.bakingapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import noosc.project.otrs.bakingapp.Adapter.AdapterRecipe;
import noosc.project.otrs.bakingapp.Model.RecipeModel;
import noosc.project.otrs.bakingapp.Network.Network;
import noosc.project.otrs.bakingapp.UI.RetrofitInterface;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.lst_main)
    RecyclerView recyclerView;
    private List<RecipeModel> recipes = new ArrayList<>();

    private static final String TAG = "MainActivity";
    private AdapterRecipe adapterRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapterRecipe = new AdapterRecipe(recipes);
        recyclerView.setAdapter(adapterRecipe);

        //onRestore
        if (savedInstanceState != null) {
            recipes.addAll(Arrays.asList(new GsonBuilder().create().fromJson(savedInstanceState.getString("recipedata"), RecipeModel[].class)));
            adapterRecipe.notifyDataSetChanged();
            recyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable("recyclerState"));

            return;
        }
        getAllMenu();

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putParcelable("recyclerState", recyclerView.getLayoutManager().onSaveInstanceState());
        outState.putString("recipedata", new GsonBuilder().create().toJson(recipes));

        super.onSaveInstanceState(outState);
    }

    public void getAllMenu() {

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
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                adapterRecipe.notifyDataSetChanged();
            }
        }.execute();


    }


}
