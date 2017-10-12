package noosc.project.otrs.bakingapp.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.GsonBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import noosc.project.otrs.bakingapp.Model.StepModel;
import noosc.project.otrs.bakingapp.R;

import static android.view.View.GONE;

/**
 * Created by Fauziyyah Faturahma on 10/6/2017.
 */

public class DetailRecipeFragment extends Fragment {

    @BindView(R.id.judulStep)
    TextView textJudul;
    @BindView(R.id.stepInstruction)
    TextView textStepIntroduction;
    @BindView(R.id.prevButton)
    Button buttonPrev;
    @BindView(R.id.nextButton)
    Button buttonNext;
    @BindView(R.id.video_view)
    SimpleExoPlayerView playerView;
    @BindView(R.id.thumbnailsView)
    ImageView imageView;

    private SimpleExoPlayer player;
    private boolean playWhenReady;
    private int currentWindow;
    private long playbackPosition;
    private String videoUrl;
    private int position;
    private StepModel[] stepList;

    private static final String TAG = "DetailRecipeFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_step, container, false);
        ButterKnife.bind(this, view);

        position = getArguments().getInt("position");

        String stepListJson = getArguments().getString("stepList");
        stepList = new GsonBuilder().create().fromJson(stepListJson, StepModel[].class);
        StepModel stepModel = stepList[position];

        stepModel.getShortDescription();
        getActivity().setTitle(stepModel.getShortDescription());

        textJudul.setText(stepModel.getShortDescription());
        textStepIntroduction.setText(stepModel.getDescription());
        videoUrl = stepModel.getVideoURL();

        buttonPrev.setVisibility(View.GONE);
        buttonNext.setVisibility(View.GONE);

        //for thumbnails

        if (!stepModel.getThumbnailURL().equals("")) {


            Glide.with(this)
                    .load(stepModel.getThumbnailURL())
                    .placeholder(R.drawable.ic_muffin)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .centerCrop()
                    .into(imageView);
        } else {

            imageView.setVisibility(GONE);
        }

        return view;
    }

    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(player);

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);

        if (videoUrl.equals("")) {

            playerView.setVisibility(View.GONE);
        } else {

            playerView.setVisibility(View.VISIBLE);
            Uri uri = Uri.parse(videoUrl);
            MediaSource mediaSource = buildMediaSource(uri);
            player.prepare(mediaSource, true, false);
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    @OnClick(R.id.nextButton)
    public void NextButton(View v) {

//        Toast.makeText(this, "Next Step", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(v.getContext(), DetailStep.class);
        intent.putExtra("position", position + 1);
        intent.putExtra("stepList", new GsonBuilder().create().toJson(stepList));
        v.getContext().startActivity(intent);

    }

    @OnClick(R.id.prevButton)
    public void PrevButton(View v) {

//        Toast.makeText(this, "Prev Step", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(v.getContext(), DetailStep.class);
        intent.putExtra("position", position - 1);
        intent.putExtra("stepList", new GsonBuilder().create().toJson(stepList));
        v.getContext().startActivity(intent);
    }

}
