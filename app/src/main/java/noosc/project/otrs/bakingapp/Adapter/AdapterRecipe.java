package noosc.project.otrs.bakingapp.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.GsonBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import noosc.project.otrs.bakingapp.Model.RecipeModel;
import noosc.project.otrs.bakingapp.R;
import noosc.project.otrs.bakingapp.UI.DetailRecipe;

/**
 * Created by Fauziyyah Faturahma on 9/27/2017.
 */

public class AdapterRecipe extends RecyclerView.Adapter<AdapterRecipe.RecipeMenu> {

    private final List<RecipeModel> recipes;

    public AdapterRecipe(List<RecipeModel> recipes) {
        this.recipes = recipes;
    }

    @Override
    public RecipeMenu onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list, parent, false);
        return new RecipeMenu(view);
    }

    @Override
    public void onBindViewHolder(RecipeMenu holder, int position) {

        holder.textRecipe.setText(recipes.get(position).getName());
        holder.textPortion.setText(recipes.get(position).getServings() + "");

        Glide.with(holder.itemView.getContext())
                .load(recipes.get(position).getImage())
                .placeholder(R.mipmap.ic_food)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .into(holder.cardImage);

    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class RecipeMenu extends RecyclerView.ViewHolder {
        @BindView(R.id.cardImg)
        ImageView cardImage;
        @BindView(R.id.txtRecipe)
        TextView textRecipe;
        @BindView(R.id.txtPortion)
        TextView textPortion;

        public RecipeMenu(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.cardVieMenu)
        public void klikMenu(View v) {
            Intent intent = new Intent(v.getContext(), DetailRecipe.class);
            intent.putExtra("recipe", new GsonBuilder().create().toJson(recipes.get(getAdapterPosition())));
            v.getContext().startActivity(intent);
        }

    }
}
