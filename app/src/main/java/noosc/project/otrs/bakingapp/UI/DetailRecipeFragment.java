package noosc.project.otrs.bakingapp.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import noosc.project.otrs.bakingapp.R;

/**
 * Created by Fauziyyah Faturahma on 10/6/2017.
 */

public class DetailRecipeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.detai_recipe_fragment, container, false);
        return view;

    }

}
