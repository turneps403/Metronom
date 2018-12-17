package com.example.rico.soundpool

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.media.MediaMetadataRetriever
import android.content.res.AssetFileDescriptor

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Build

import android.widget.Button

import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.schedule
import android.util.Log


class MainActivity : AppCompatActivity() {
    protected lateinit var soundPool: SoundPool
    var timer: Timer = Timer()

    internal inner class audioTask(val sounds_list: MutableList<List<Int>>) : TimerTask() {
        override fun run() {
            var sound = sounds_list.removeAt(0)
            Log.w("SOUND", "curent: " + sound)
            soundPool.autoPause()
            soundPool.play(sound[0],1f, 1f, 0, 0, 1f)
            this.cancel()
            if (sounds_list.size > 0) {
                timer.schedule(audioTask(sounds_list), sound[1].toLong());
                //soundPool.autoPause()
                //soundPool.play(this.next_sound_num, 1f, 1f, 0, 0, 1f)
            } else {
                //timer.cancel();
            }
        }
    };

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

        var mySoundList: MutableList<List<Int>> = mutableListOf<List<Int>>()
        val items = listOf(R.raw.sound1, R.raw.sound2, R.raw.sound3)
        // https://stackoverflow.com/questions/24030756/mediaextractor-mediametadataretriever-with-raw-asset-file
        var mmr = MediaMetadataRetriever()
        for (sound in items) {
            val afd = getResources().openRawResourceFd(sound);
            mmr.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            // mmr.setDataSource(R.raw.sound1)
            val durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val millSecond = Integer.parseInt(durationStr)

            val sound_int = soundPool.load(this, sound, 1)

            mySoundList.add(listOf(sound_int, millSecond))
        }
        Log.w("DURATION", "millSecond " + mySoundList)


        val button1 = findViewById<Button>(R.id.button1)
        button1.setOnClickListener {
            timer.purge();
            timer.cancel();
            timer = Timer()
            val copyOfMySoundList = mySoundList.toMutableList()
            timer.schedule(audioTask(copyOfMySoundList),0);
            //soundPool.autoPause()
            //soundPool.play(sound1,1f, 1f, 0, 0, 1f)
        }

        val button2 = findViewById<Button>(R.id.button2)
        button2.setOnClickListener {
            soundPool.autoPause()
            soundPool.play(sound2, 1f, 1f, 0, 0, 1f)
        }

    }

    public override fun onPause() {
        super.onPause()
        // timer.cancel();
        /*
        if (soundPool != null) {
            soundPool.cancel()
            soundPool.purge()
        }
        */
    }

    override fun onDestroy() {
        super.onDestroy()
        //timer.cancel();  //Terminates this timer,discarding any currently scheduled tasks.
        //timer.purge();   // Removes all cancelled tasks from this timer's task queue.
        //soundPool.cancel()
        //soundPool.purge()
        //soundPool.release()
        // soundPool = null
    }

}
