package com.hijamaplanet.drawer;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hijamaplanet.BaseActivity;
import com.hijamaplanet.R;
import com.hijamaplanet.player.EqualizerUtils;
import com.hijamaplanet.player.MusicNotificationManager;
import com.hijamaplanet.player.MusicService;
import com.hijamaplanet.player.PlaybackInfoListener;
import com.hijamaplanet.player.PlayerAdapter;
import com.hijamaplanet.player.Song;
import com.hijamaplanet.player.SongProvider;
import com.hijamaplanet.player.SongsAdapter;
import com.hijamaplanet.player.Utils;
import com.hijamaplanet.service.network.model.Dua;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.inflate;
import static com.hijamaplanet.utils.NetworkUtil.haveNetworkConnection;

public class DuaActivity extends BaseActivity implements SongsAdapter.SongSelectedListener{

    public static List<String> duaListShow;
    public static List<String> duaPathShow;
    private List<Dua> duaList;

    Boolean isSDPresent, isSDSupportedDevice;
    File mydir;
    public static int sizeOfdatabaseSongs;

    private LinearLayoutManager mSongsLayoutManager;
    private int mAccent;
    private boolean sThemeInverted;
    private RecyclerView mSongsRecyclerView;
    public static SongsAdapter mSongsAdapter;
    private TextView mPlayingAlbum, mPlayingSong, mDuration, mSongPosition;

    public static SeekBar mSeekBarAudio;
    private LinearLayout mControlsContainer;
    private View mPlayerInfoView;
    private ImageView mPlayPauseButton, mSkipPrevButton;
    public static PlayerAdapter mPlayerAdapter;
    private boolean mUserIsSeeking = false;
    private boolean sPlayerInfoLongPressed = false;
    private MusicService mMusicService;
    private PlaybackListener mPlaybackListener;
    private MusicNotificationManager mMusicNotificationManager;
    public static List<Song> songs;
    private boolean mIsBound;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.nav_activity_dua_list, null, false);
        drawer.addView(contentView,1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        duaList = getIntent().getParcelableArrayListExtra("Dua");
        
        isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        isSDSupportedDevice = Environment.isExternalStorageRemovable();

        if (isSDSupportedDevice && isSDPresent) {
            File root = Environment.getExternalStorageDirectory();
            File dir = new File(root + "/HijamaDua/");
            if (dir.exists() == false) dir.mkdirs();
        } else {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                mydir = new File(Environment.getExternalStorageDirectory(), "HijamaDua");
                if (!mydir.exists()) mydir.mkdirs();
            } else checkPermission();
        }

        getViews();
        initializeSeekBar();
        doBindService();
    }

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(@NonNull final ComponentName componentName, @NonNull final IBinder iBinder) {
            mMusicService = ((MusicService.LocalBinder) iBinder).getInstance();
            mPlayerAdapter = mMusicService.getMediaPlayerHolder();
            mMusicNotificationManager = mMusicService.getMusicNotificationManager();
            mMusicNotificationManager.setAccentColor(mAccent);

            if (mPlaybackListener == null) {
                mPlaybackListener = new PlaybackListener();
                mPlayerAdapter.setPlaybackInfoListener(mPlaybackListener);
            }
            checkReadStoragePermissions();
        }

        @Override
        public void onServiceDisconnected(@NonNull final ComponentName componentName) {
            mMusicService = null;
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        if (mPlayerAdapter != null && mPlayerAdapter.isMediaPlayer()) mPlayerAdapter.onPauseActivity();
    }

    private void checkReadStoragePermissions() {
        if (Utils.isMarshmallow()) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                showPermissionRationale();
            } else onPermissionGranted();

        } else onPermissionGranted();
    }

    @TargetApi(23)
    private void showPermissionRationale() {
        final AlertDialog builder = new AlertDialog.Builder(getApplicationContext()).create();
        View view = inflate(getApplicationContext(), R.layout.dialog_one_button, null);
        builder.setIcon(R.drawable.ic_folder);
        builder.setView(view);
        if (builder.getWindow() != null) {
            builder.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        TextView title, message;
        title = findViewById(R.id.title);
        message = findViewById(R.id.message);
        title.setText(getString(R.string.app_name));
        message.setText(getString(R.string.perm_rationale));

        Button positiveButton = findViewById(R.id.dlg_one_button_btn_ok);
        positiveButton.setOnClickListener((View v) -> {
            builder.dismiss();
            final int READ_FILES_CODE = 2588;
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                    , READ_FILES_CODE);
        });
        builder.setCanceledOnTouchOutside(false);
        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getViews() {
        mControlsContainer = findViewById(R.id.controls_container);
        mPlayerInfoView = findViewById(R.id.player_info);
        mPlayingSong = findViewById(R.id.playing_song);
        mPlayingAlbum = findViewById(R.id.playing_album);

        setupPlayerInfoTouchBehaviour();

        mPlayPauseButton = findViewById(R.id.play_pause);

        mSkipPrevButton = findViewById(R.id.skip_prev);
        mSkipPrevButton.setOnLongClickListener(v -> {
            setRepeat();
            return false;
        });
        mSeekBarAudio = findViewById(R.id.seekTo);

        mDuration = findViewById(R.id.duration);
        mSongPosition = findViewById(R.id.song_position);
        mSongsRecyclerView = findViewById(R.id.songs_rv);
    }

    private void setupPlayerInfoTouchBehaviour() {
        mPlayerInfoView.setOnLongClickListener(v -> {
            if (!sPlayerInfoLongPressed) {
                mPlayingSong.setSelected(true);
                mPlayingAlbum.setSelected(true);
                sPlayerInfoLongPressed = true;
            }
            return true;
        });
        mPlayerInfoView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_OUTSIDE || event.getAction() == MotionEvent.ACTION_MOVE) {
                if (sPlayerInfoLongPressed) {
                    mPlayingSong.setSelected(false);
                    mPlayingAlbum.setSelected(false);
                    sPlayerInfoLongPressed = false;
                }
            }
            return false;
        });
    }

    private static String getSongLoaderSortOrder() {
        return MediaStore.Audio.Artists.DEFAULT_SORT_ORDER + ", " + MediaStore.Audio.Albums.DEFAULT_SORT_ORDER + ", " + MediaStore.Audio.Media.DEFAULT_SORT_ORDER;
    }

    public void setSongsRecyclerView() {
        songs = SongProvider.getSongs();
        try {
            if (songs.size() != 0) setUpList();
            else setOnlyDatabase();
        } catch (Exception e) {
            checkPermission();
        }

        mSongsLayoutManager = new LinearLayoutManager(this);
        mSongsRecyclerView.setLayoutManager(mSongsLayoutManager);
        mSongsAdapter = new SongsAdapter(DuaActivity.this, duaListShow);
        mSongsRecyclerView.setAdapter(mSongsAdapter);
    }

    public void setOnlyDatabase() {
        duaListShow = new ArrayList<>();
        duaPathShow = new ArrayList<>();
        if (haveNetworkConnection(this)) {
            for (int c = 0; c < duaList.size(); c++) {
                duaListShow.add(duaList.get(c).getDuas_name());
                duaPathShow.add(duaList.get(c).getDuas_link());
            }
            sizeOfdatabaseSongs = duaListShow.size();
        } else if (!haveNetworkConnection(this)) sizeOfdatabaseSongs = 0;
    }

    public void setUpList() {
        duaListShow = new ArrayList<>();
        duaPathShow = new ArrayList<>();

        if (haveNetworkConnection(this)) {
            for (int c = 0; c < duaList.size(); c++) {
                for (int i = 0; i < songs.size(); i++) {
                    final Song song = songs.get(i);
                    final String songTitle = song.title;
                    if (songTitle.equalsIgnoreCase(duaList.get(c).getDuas_name())) break;
                    else if (i == songs.size() - 1 && !songTitle.equalsIgnoreCase(duaList.get(c).getDuas_name())) {
                        duaListShow.add(duaList.get(c).getDuas_name());
                        duaPathShow.add(duaList.get(c).getDuas_link());
                    }
                }
            }
            sizeOfdatabaseSongs = duaListShow.size();
        } else if (!haveNetworkConnection(this)) sizeOfdatabaseSongs = 0;
    }

    private void initializeSeekBar() {
        mSeekBarAudio.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    final int currentPositionColor = mSongPosition.getCurrentTextColor();
                    int userSelectedPosition = 0;

                    @Override
                    public void onStartTrackingTouch(@NonNull final SeekBar seekBar) {
                        mUserIsSeeking = true;
                    }

                    @Override
                    public void onProgressChanged(@NonNull final SeekBar seekBar, final int progress, final boolean fromUser) {
                        if (fromUser) {
                            userSelectedPosition = progress;
                            mSongPosition.setTextColor(Utils.getColorFromResource(DuaActivity.this, mAccent, R.color.blue));
                        }
                        mSongPosition.setText(Song.formatDuration(progress));
                    }

                    @Override
                    public void onStopTrackingTouch(@NonNull final SeekBar seekBar) {
                        if (mUserIsSeeking) {
                            mSongPosition.setTextColor(currentPositionColor);
                        }
                        mUserIsSeeking = false;
                        mPlayerAdapter.seekTo(userSelectedPosition);
                    }
                });
    }

    private void setRepeat() {
        if (checkIsPlayer()) {
            mPlayerAdapter.reset();
            updateResetStatus(false);
        }
    }

    public void skipPrev(@NonNull final View v) {
        if (checkIsPlayer()) {
            mPlayerAdapter.instantReset();
            if (mPlayerAdapter.isReset()) {
                mPlayerAdapter.reset();
                updateResetStatus(false);
            }
        }
    }

    public void resumeOrPause(@NonNull final View v) {
        if (checkIsPlayer()) mPlayerAdapter.resumeOrPause();
    }

    public void skipNext(@NonNull final View v) {
        if (checkIsPlayer()) mPlayerAdapter.skip(true);
    }

    private boolean checkIsPlayer() {
        boolean isPlayer = mPlayerAdapter.isMediaPlayer();
        if (!isPlayer) EqualizerUtils.notifyNoSessionId(this);
        return isPlayer;
    }

    private void onPermissionGranted() {
        setSongsRecyclerView();
        restorePlayerStatus();
    }

    private void updateResetStatus(final boolean onPlaybackCompletion) {
        final int themeColor = sThemeInverted ? R.color.white : R.color.black;
        final int color = onPlaybackCompletion ? themeColor : mPlayerAdapter.isReset() ? mAccent : themeColor;
        mSkipPrevButton.post(() -> mSkipPrevButton.setColorFilter(Utils.getColorFromResource(this, color, onPlaybackCompletion ? themeColor : mPlayerAdapter.isReset() ? R.color.blue : themeColor), PorterDuff.Mode.SRC_IN));
    }

    private void updatePlayingStatus() {
        final int drawable = mPlayerAdapter.getState() != PlaybackInfoListener.State.PAUSED ? R.drawable.ic_pause : R.drawable.ic_play;
        mPlayPauseButton.post(() -> mPlayPauseButton.setImageResource(drawable));
    }

    private void updatePlayingInfo(final boolean restore, final boolean startPlay) {

        if (startPlay) {
            mPlayerAdapter.getMediaPlayer().start();
            new Handler().postDelayed(() -> mMusicService.startForeground(MusicNotificationManager.NOTIFICATION_ID, mMusicNotificationManager.createNotification()), 250);
        }

        final Song selectedSong = mPlayerAdapter.getCurrentSong();

        final int duration = mPlayerAdapter.getMediaPlayer().getDuration();
        mSeekBarAudio.setMax(duration);
        Utils.updateTextView(mDuration, Song.formatDuration(duration));

        final Spanned spanned = Utils.buildSpanned(getString(R.string.playing_song, "", selectedSong.title));
        mPlayingSong.post(() -> mPlayingSong.setText(spanned));
        Utils.updateTextView(mPlayingAlbum, selectedSong.albumName);

        if (restore) {
            mSeekBarAudio.setProgress(mPlayerAdapter.getPlayerPosition());
            updatePlayingStatus();
            updateResetStatus(false);

            new Handler().postDelayed(() -> {
                if (mMusicService.isRestoredFromPause()) {
                    mMusicService.stopForeground(false);
                    mMusicService.getMusicNotificationManager().getNotificationManager().notify(MusicNotificationManager.NOTIFICATION_ID, mMusicService.getMusicNotificationManager().getNotificationBuilder().build());
                    mMusicService.setRestoredFromPause(false);
                }
            }, 250);
        }
    }

    private void restorePlayerStatus() {
        mSeekBarAudio.setEnabled(mPlayerAdapter.isMediaPlayer());
        if (mPlayerAdapter != null && mPlayerAdapter.isMediaPlayer()) {
            mPlayerAdapter.onResumeActivity();
            updatePlayingInfo(true, false);
        }
    }

    private void doBindService() {
        bindService(new Intent(DuaActivity.this, MusicService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
        final Intent startNotStickyIntent = new Intent(this, MusicService.class);
        startService(startNotStickyIntent);
    }

    public void doUnbindService() {
        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlaybackListener = null;
        doUnbindService();
    }

    @Override
    public void onSongSelected(@NonNull final Song playedSong, @NonNull final List<Song> songsForPlayedArtist) {
        if (!mSeekBarAudio.isEnabled()) mSeekBarAudio.setEnabled(true);
        mPlayerAdapter.setCurrentSong(playedSong, songsForPlayedArtist);
        mPlayerAdapter.initMediaPlayer(playedSong);
    }
    
    class PlaybackListener extends PlaybackInfoListener {
        @Override
        public void onPositionChanged(int position) {
            if (!mUserIsSeeking) mSeekBarAudio.setProgress(position);
        }

        @Override
        public void onStateChanged(@State int state) {
            updatePlayingStatus();
            if (mPlayerAdapter.getState() != State.RESUMED && mPlayerAdapter.getState() != State.PAUSED)
                updatePlayingInfo(false, true);
        }

        @Override
        public void onPlaybackCompleted() {
            updateResetStatus(true);
        }
    }
}



