package noosc.project.otrs.bakingapp.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import noosc.project.otrs.bakingapp.Model.StepModel;
import noosc.project.otrs.bakingapp.R;
import noosc.project.otrs.bakingapp.UI.DetailRecipe;
import noosc.project.otrs.bakingapp.UI.DetailStep;

/**
 * Created by Fauziyyah Faturahma on 9/27/2017.
 */

public class AdapterSteps extends RecyclerView.Adapter<AdapterSteps.RecipeSteps> {


    private final List<StepModel> steps;

    public AdapterSteps(List<StepModel> steps) {
        this.steps = steps;
    }

    @Override
    public AdapterSteps.RecipeSteps onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_step, parent, false);
        return new AdapterSteps.RecipeSteps(view);
    }

    @Override
    public void onBindViewHolder(AdapterSteps.RecipeSteps holder, int position) {

        holder.textSteps.setText(steps.get(position).getShortDescription());

    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public class RecipeSteps extends RecyclerView.ViewHolder {
        @BindView(R.id.txtStep)
        TextView textSteps;


        public RecipeSteps(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.cardViewStep)
        public void klikStep(View v) {
            //Toast.makeText(v.getContext(), "oke cliked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(v.getContext(), DetailStep.class);
            intent.putExtra("step", new GsonBuilder().create().toJson(steps.get(getAdapterPosition())));
            v.getContext().startActivity(intent);
        }

    }
}
