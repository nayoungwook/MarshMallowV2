package com.coconut.test;

import java.awt.Color;
import java.awt.event.KeyEvent;

import com.coconut.marshmallow.Display;
import com.coconut.marshmallow.camera.Camera;
import com.coconut.marshmallow.font.TTFont;
import com.coconut.marshmallow.input.Input;
import com.coconut.marshmallow.math.Vector;
import com.coconut.marshmallow.renderer.Renderer;
import com.coconut.marshmallow.sprite.Sprite;
import com.coconut.marshmallow.state.Scene;
import com.coconut.marshmallow.state.SceneManager;

public class Main {

	public static Display window;

	public static void main(String[] args) {
		window = new Display("Workspace", 1280, 720);
		SceneManager.setScene(new Workspace());
		window.run();
	}

}

class Workspace implements Scene {

	private Sprite sprite = null;
	private TTFont font = null;
	private TTFont fontBig = null;

	@Override
	public void init() {
		sprite = new Sprite("msResources/img/aru.png");
		font = new TTFont("font/font.ttf", 64f);
		fontBig = new TTFont("font/font.ttf", 64f * 2);
	}

	@Override
	public void update() {
		if (Input.keys[KeyEvent.VK_W])
			Camera.position.translate(0, 10);
		if (Input.keys[KeyEvent.VK_S])
			Camera.position.translate(0, -10);
		if (Input.keys[KeyEvent.VK_A])
			Camera.position.translate(-10, 0);
		if (Input.keys[KeyEvent.VK_D])
			Camera.position.translate(10, 0);
		timer+=4;
	}

	private int timer = 0;

	@Override
	public void render() {
		Renderer.renderImage(sprite, new Vector(0, 0), 100, 100);

		sprite.cutImage(timer, 40, 100, 100);
		Renderer.setColor(new Color(255, 150, 100));
		Renderer.renderFont(font, "qwer", new Vector(0, 0));
	}
}
