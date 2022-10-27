package com.majorproject.view;

import java.net.URL;

import com.majorproject.model.Model;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * The class that initialises and manages the app's theme song.
 * The audio begins on app start, is played in a loop and only stops when paused.
 */
public class ThemeSong
{
    Model model;
    MediaPlayer mediaPlayer;

    public ThemeSong(Model model, URL songFilePath)
    {
        this.model = model;

        Media song = new Media(songFilePath.toString());
        mediaPlayer = new MediaPlayer(song);

        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
    }

    /** Alternates between playing and pausing the media. */
    protected void playAudio()
    {
        if(model.validateAudioStatus(mediaPlayer.getStatus()))
            mediaPlayer.pause();
        else
            mediaPlayer.play();
    }
}
