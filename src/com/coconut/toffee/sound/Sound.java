package com.coconut.toffee.sound;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {

	private URL url;
	private AudioInputStream audioIn;
	private Clip clip;
	private String path;
	public int startPosition = 0;
	public FloatControl control;

	public Clip getClip() {
		return clip;
	}

	private void getSound() {
		try {
			url = this.getClass().getClassLoader().getResource(path);
			audioIn = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip();
			clip.open(audioIn);

			control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Sound(String path) {
		this.path = path;
		getSound();
	}

	public float getVolume() {
		return control.getValue();
	}

	public void setVolume(float vol) {
		if (vol >= 36 || vol <= -51)
			return;
		control.setValue(vol - 30);
	}

	public void pause() {
		clip.stop();
	}

	public void resume() {
		clip.start();
	}

	public void restart() {
		clip.setFramePosition(0);
	}

	public void setFramePos(int pos) {
		clip.setFramePosition(pos);
	}

	public void play() {

		Thread thread = new Thread() {

			@Override
			public void run() {
				try {
					getSound();
					clip.setFramePosition(startPosition);
					clip.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		thread.setDaemon(false);
		thread.start();
	}

}
