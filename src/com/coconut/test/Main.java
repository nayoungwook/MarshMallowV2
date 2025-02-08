package com.coconut.test;

import java.awt.event.KeyEvent;

import com.coconut.marshmallow.Display;
import com.coconut.marshmallow.camera.Camera;
import com.coconut.marshmallow.font.TTFont;
import com.coconut.marshmallow.input.Input;
import com.coconut.marshmallow.math.Vector;
import com.coconut.marshmallow.renderer.Renderer;
import com.coconut.marshmallow.shader.Shader;
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
	private Shader shader = null;

	@Override
	public void init() {
		sprite = new Sprite("msResources/img/aru.png");
		font = new TTFont("font/font.ttf", 64f);
		fontBig = new TTFont("font/font.ttf", 64f * 2);
		shader = new Shader("msResources/shader/vertex.glsl", "msResources/shader/fragment.glsl");
	}

	private boolean fs = false;

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
		timer += 0.1;

		if (Input.keys[KeyEvent.VK_F]) {
			if (!fs)
				Main.window.setFullScreen();
			else
				Main.window.setWindowScreen(1280, 720);
			fs = !fs;
			Input.keys[KeyEvent.VK_F] = false;
		}
	}

	private float timer = 0;

	@Override
	public void render() {
		shader.uploadFloat("uTimer", timer);
		Display.uploadShader(shader);
		Renderer.renderImage(sprite, new Vector(0, 0), 200, 200);
	}
}
