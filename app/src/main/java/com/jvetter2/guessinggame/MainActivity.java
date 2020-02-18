package com.jvetter2.guessinggame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    boolean soundOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent settingsIntent = getIntent();
        soundOn = settingsIntent.getBooleanExtra(getString(R.string.sound_variable), true);
    }

    public void playGame(View view) {
        MediaPlayer playSound = MediaPlayer.create(getApplicationContext(), R.raw.sonic);

        if (!soundOn) {
            playSound.setVolume(0, 0);
        }

        playSound.start();

        Intent intent = new Intent(getApplicationContext(), GameActivity.class);
        intent.putExtra(getString(R.string.sound_variable), soundOn);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.settings, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (Integer.valueOf(item.getItemId()).equals(R.id.action_settings)) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        } else {
            return false;
        }
    }
}
