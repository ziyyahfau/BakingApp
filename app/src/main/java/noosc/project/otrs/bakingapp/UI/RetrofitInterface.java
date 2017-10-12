package noosc.project.otrs.bakingapp.UI;

import java.util.List;

import noosc.project.otrs.bakingapp.Model.RecipeModel;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Fauziyyah Faturahma on 10/2/2017.
 */

public interface RetrofitInterface {

    @GET("baking.json")
    Call<List<RecipeModel>> getRecipe();

}

