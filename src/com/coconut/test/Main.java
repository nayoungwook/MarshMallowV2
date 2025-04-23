package com.coconut.test;

import java.awt.event.KeyEvent;

import com.coconut.marshmallow.Display;
import com.coconut.marshmallow.camera.Camera;
import com.coconut.marshmallow.font.TTFont;
import com.coconut.marshmallow.input.Input;
import com.coconut.marshmallow.math.Vector;
import com.coconut.marshmallow.object.FrameBuffer;
import com.coconut.marshmallow.renderer.Renderer;
import com.coconut.marshmallow.shader.Shader;
import com.coconut.marshmallow.shader.ShaderManager;
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

	private TTFont font = null;
	private TTFont fontBig = null;
	private Shader testShader = new Shader("msResources/shader/test/testVertex.glsl",
			"msResources/shader/test/testFragment.glsl");
	private Shader blurShader = new Shader("msResources/shader/test/testVertex.glsl",
			"msResources/shader/test/blur.glsl");

	private Sprite dungeon = null;
	private Sprite knight = null;
	private Sprite torch = null;

	private FrameBuffer frameBuffer = null;

	@Override
	public void init() {
		dungeon = new Sprite("msResources/img/dungeon.png");
		knight = new Sprite("msResources/img/knight.png");
		torch = new Sprite("msResources/img/torch.png");
		knight.cutImage(0, 0, 16, 16);

		font = new TTFont("font/font.ttf", 64f);
		fontBig = new TTFont("font/font.ttf", 64f * 2);

		frameBuffer = new FrameBuffer();
	}

	private boolean fs = false;
	private float timer = 0f;

	@Override
	public void update() {

		if (Input.keys[KeyEvent.VK_W])
			Camera.position.translate(0, 10f);
		if (Input.keys[KeyEvent.VK_S])
			Camera.position.translate(0, -10f);
		if (Input.keys[KeyEvent.VK_A])
			Camera.position.translate(-10f, 0);
		if (Input.keys[KeyEvent.VK_D])
			Camera.position.translate(10f, 0);

		if (Input.keys[KeyEvent.VK_Q])
			Camera.rotation -= 0.05f;
		if (Input.keys[KeyEvent.VK_E])
			Camera.rotation += 0.05f;

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

		timer += 0.02f;
	}

	@Override
	public void render() {
//		Renderer.setColor(new Color(30, 30, 30));
//		Renderer.renderUIRect(new Vector(0, 0, 0.5f), Display.width, Display.height);


		Display.uploadShader(ShaderManager.defaultShader);
		Renderer.renderImage(dungeon, new Vector(0, 720 / 2), 60 * 12, 60 * 12);

		frameBuffer.bind();
//		Display.uploadShader(blurShader);
		Renderer.renderImage(dungeon, new Vector(0, 0), 60 * 6, 60 * 6);
		frameBuffer.unbind();

		frameBuffer.render();

	}
}
