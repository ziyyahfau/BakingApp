package noosc.project.otrs.bakingapp.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import noosc.project.otrs.bakingapp.Model.IngredientModel;
import noosc.project.otrs.bakingapp.R;

/**
 * Created by Fauziyyah Faturahma on 9/27/2017.
 */

public class AdapterIngredients extends RecyclerView.Adapter<AdapterIngredients.RecipeIngredients> {


    private final List<IngredientModel> ingredients;

    public AdapterIngredients(List<IngredientModel> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public AdapterIngredients.RecipeIngredients onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ingredients, parent, false);
        return new AdapterIngredients.RecipeIngredients(view);
    }

    @Override
    public void onBindViewHolder(AdapterIngredients.RecipeIngredients holder, int position) {

        holder.textIngredient.setText(ingredients.get(position).getIngredient());


    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class RecipeIngredients extends RecyclerView.ViewHolder {
        @BindView(R.id.txtIngredient)
        TextView textIngredient;


        public RecipeIngredients(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
