package noosc.project.otrs.bakingapp.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.GsonBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import noosc.project.otrs.bakingapp.Adapter.AdapterIngredients;
import noosc.project.otrs.bakingapp.Adapter.AdapterSteps;
import noosc.project.otrs.bakingapp.Model.IngredientModel;
import noosc.project.otrs.bakingapp.Model.RecipeModel;
import noosc.project.otrs.bakingapp.Model.StepModel;
import noosc.project.otrs.bakingapp.R;
import noosc.project.otrs.bakingapp.StepListener;

/**
 * Created by Fauziyyah Faturahma on 9/27/2017.
 */
public class DetailRecipe extends AppCompatActivity implements StepListener {

    @BindView(R.id.lstIngredients) RecyclerView recyclerViewIngredients;
    @BindView(R.id.lstSteps) RecyclerView recyclerViewSteps;
    private boolean twoPanel;

    private static final String TAG = "DetailRecipe";
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe);
        ButterKnife.bind(this);

        String recipesJson = getIntent().getExtras().getString("recipe");
        RecipeModel recipeModel = new GsonBuilder().create().fromJson(recipesJson, RecipeModel.class);
        recipeModel.getName();
        setTitle(recipeModel.getName());
        Log.v("RECIPE MODEL", "" + recipeModel.getName());

        List<IngredientModel> ingredients = recipeModel.getIngredients();
        List<StepModel> steps = recipeModel.getSteps();

        RecyclerView.LayoutManager layoutManagerIng = new LinearLayoutManager(this);
        recyclerViewIngredients.setLayoutManager(layoutManagerIng);

        RecyclerView.LayoutManager layoutManagerSteps = new LinearLayoutManager(this);
        recyclerViewSteps.setLayoutManager(layoutManagerSteps);

        AdapterIngredients adapterIngredients = new AdapterIngredients(ingredients);
        recyclerViewIngredients.setAdapter(adapterIngredients);

        AdapterSteps adapterSteps = new AdapterSteps(steps, this);
        recyclerViewSteps.setAdapter(adapterSteps);

        ViewCompat.setNestedScrollingEnabled(recyclerViewIngredients, false);
        ViewCompat.setNestedScrollingEnabled(recyclerViewSteps, false);

        if (findViewById(R.id.detailStepContent) != null) {

            twoPanel = true;

            //for connect recipe activity and recipe fragments
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.detailStepContent, new Fragment()).commit();
        }

        Log.d(TAG, "onCreate: Print two panel = " + twoPanel);
    }

    @Override
    public void onStepSelected(int position, String stepListJson) {
        if (twoPanel) {
            DetailRecipeFragment fragment = new DetailRecipeFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            bundle.putString("stepList", stepListJson);
            fragment.setArguments(bundle);
            //for connect recipe activity and recipe fragments
            fragmentManager.beginTransaction().replace(R.id.detailStepContent, fragment).commit();
        }
        else {
            Intent intent = new Intent(this, DetailStep.class);
            intent.putExtra("position", position);
            intent.putExtra("stepList", stepListJson);
            startActivity(intent);
        }
    }
}
