package com.example.gamehub;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

// this class was created  for the use of coin grab game only
class SoundManager {

    private MediaPlayer mp;        //var for the background music
    private SoundPool soundPool;
    private int soundID;           //var for coin grab sound
    private float maxVolume;
    boolean loaded = false;
    Context context;


    void startMusic(Context context) {
        if (mp != null) {
            stopMusic();
        }

        mp = MediaPlayer.create(context, R.raw.music);

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });
        mp.start();
    }

    void stopMusic() {
        mp.stop();
        mp.reset();
        mp.release();
        mp = null;
    }

    void stopGrab() {

        loaded=false;
    }
    void initSoundPool(Context context, float maxVolume) {
        this.context = context;
        this.maxVolume = maxVolume;

        // Load the sounds
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {

            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });

        reloadSound();
    }

    private void reloadSound() {
        loaded = false;
        soundID = soundPool.load(context, R.raw.coin, 1);
    }

    void performSound() {
        // Is the sound loaded
        if (loaded) {
            soundPool.play(soundID, maxVolume, maxVolume, 1, 0, 1.0f);
            reloadSound();
        }
    }
}