package noosc.project.otrs.bakingapp.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Created by Fauziyyah Faturahma on 10/2/2017.
 */

public class DetailStep extends AppCompatActivity {

    @BindView(R.id.judulStep) TextView textJudul;
    @BindView(R.id.stepInstruction) TextView textStepIntroduction;
    @BindView(R.id.prevButton) Button buttonPrev;
    @BindView(R.id.nextButton) Button buttonNext;
    @BindView(R.id.video_view) SimpleExoPlayerView playerView;


    private SimpleExoPlayer player;
    private boolean playWhenReady;
    private int currentWindow;
    private long playbackPosition;
    private String videoUrl;
    private int position;
    private StepModel[] stepList;


    private static final String TAG = "DetailStep";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_step);
        ButterKnife.bind(this);

//        //untuk ngambil banyaknya step
//        String stepJson = getIntent().getExtras().getString("step");
//        StepModel stepModel = new GsonBuilder().create().fromJson(stepJson, StepModel.class);

        //untuk ngambil banyaknya position
        position = getIntent().getExtras().getInt("position");

        String stepListJson =  getIntent().getExtras().getString("stepList");
        stepList = new GsonBuilder().create().fromJson(stepListJson, StepModel[].class);
        StepModel stepModel = stepList[position];

        stepModel.getShortDescription();
        setTitle(stepModel.getShortDescription());
        Log.v("STEP MODEL", "" + stepModel.getShortDescription());

        textJudul.setText(stepModel.getShortDescription());
        textStepIntroduction.setText(stepModel.getDescription());
        videoUrl = stepModel.getVideoURL();
        Log.d(TAG, "TES ID: "+stepModel.getId());

        if (position==0){
            buttonPrev.setVisibility(View.GONE);
        }

        if(position==stepList.length-1){
            buttonNext.setVisibility(View.GONE);
        }

    }

    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(player);

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);

        if (videoUrl.equals("")){

            playerView.setVisibility(View.GONE);
            Log.d(TAG, "Player: NOT visible");
        }
        else {
            Log.d(TAG, "Player: VISIBLE");

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
        intent.putExtra("position", position+1);
        intent.putExtra("stepList", new GsonBuilder().create().toJson(stepList));
        v.getContext().startActivity(intent);
        finish();
    }

    @OnClick(R.id.prevButton)
    public void PrevButton(View v) {

//        Toast.makeText(this, "Prev Step", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(v.getContext(), DetailStep.class);
        intent.putExtra("position", position-1);
        intent.putExtra("stepList", new GsonBuilder().create().toJson(stepList));
        v.getContext().startActivity(intent);
        finish();
    }
}
