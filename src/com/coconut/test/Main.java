package com.coconut.test;

import java.awt.Color;
import java.awt.event.KeyEvent;

import com.coconut.marshmallow.Display;
import com.coconut.marshmallow.camera.Camera;
import com.coconut.marshmallow.font.TTFont;
import com.coconut.marshmallow.input.Input;
import com.coconut.marshmallow.math.Mathf;
import com.coconut.marshmallow.math.Vector;
import com.coconut.marshmallow.object.GameObject;
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
	private GameObject obj = null;

	@Override
	public void init() {
		sprite = new Sprite("msResources/img/aru.png");
		font = new TTFont("font/font.ttf", 64f);
		fontBig = new TTFont("font/font.ttf", 64f * 2);
		obj = new GameObject(0, 0, 100, 100);
		obj.sprite = sprite;
	}

	private boolean fs = false;
	private Vector position = new Vector(0, 0);

	@Override
	public void update() {

		obj.rotation += 0.01f;

		if (Input.keys[KeyEvent.VK_W])
			Camera.position.translate(0, 10f);
		if (Input.keys[KeyEvent.VK_S])
			Camera.position.translate(0, -10f);
		if (Input.keys[KeyEvent.VK_A])
			Camera.position.translate(-10f, 0);
		if (Input.keys[KeyEvent.VK_D])
			Camera.position.translate(10f, 0);

		if (Input.keys[KeyEvent.VK_Q])
			Camera.rotation -= 0.01f;
		if (Input.keys[KeyEvent.VK_E])
			Camera.rotation += 0.01f;

		if (Input.keys[KeyEvent.VK_F]) {
			if (!fs)
				Main.window.setFullScreen();
			else
				Main.window.setWindowScreen(1280, 720);
			fs = !fs;
			Input.keys[KeyEvent.VK_F] = false;
		}

		Camera.position.translate(0, 0, Input.scrollYOffset * 0.05f);
		Input.scrollYOffset = 0;

		Vector wp = Mathf.screenToWorld(Input.mousePointer);
		obj.position = wp;
	}

	@Override
	public void render() {
		obj.render();
		Renderer.setColor(new Color(255, 100, 200));
		Renderer.renderFont(font, "asdf", new Vector(100, 0, 0));
	}
}
