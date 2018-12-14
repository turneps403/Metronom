package com.example.rico.soundpool

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Build

import android.widget.Button


class MainActivity : AppCompatActivity() {
    protected lateinit var soundPool: SoundPool

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

        soundPool = SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build()

        val sound1 = soundPool.load(this, R.raw.sound1, 1)
        val sound2 = soundPool.load(this, R.raw.sound2, 1)

        val button1 = findViewById<Button>(R.id.button1)
        button1.setOnClickListener {
            soundPool.autoPause()
            soundPool.play(sound1,1f, 1f, 0, 0, 1f)
        }

        val button2 = findViewById<Button>(R.id.button2)
        button2.setOnClickListener {
            soundPool.autoPause()
            soundPool.play(sound2, 1f, 1f, 0, 0, 1f)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
        // soundPool = null
    }

}
