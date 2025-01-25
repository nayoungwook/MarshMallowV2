package com.coconut.test;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.coconut.marshmallow.Window;
import com.coconut.marshmallow.camera.Camera;
import com.coconut.marshmallow.input.KeyListener;
import com.coconut.marshmallow.object.GameObject;
import com.coconut.marshmallow.sprite.Sprite;
import com.coconut.marshmallow.state.MSScene;
import com.coconut.marshmallow.state.SceneManager;

public class Main {

	public static Window window;

	public static void main(String[] args) {
		window = new Window("Workspace", 1280, 720);
		SceneManager.setScene(new Workspace());
		window.run();
	}

}

class Workspace implements MSScene {

	private ArrayList<Sprite> sprites = new ArrayList<>();
	private ArrayList<GameObject> objects = new ArrayList<>();

	@Override
	public void init() {
		sprites.add(new Sprite("res/img/aru.png"));

		int w = 100, h = 100, s = 100;

		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				GameObject obj = new GameObject((-w / 2 + i) * s, (-h / 2 + j) * s, s, s);
				obj.sprite = sprites.get(0);
				objects.add(obj);
			}
		}
	}

	@Override
	public void update() {
		if (KeyListener.isKeyDown(KeyEvent.VK_W))
			Camera.getInstance().position.translate(0, 4);
		if (KeyListener.isKeyDown(KeyEvent.VK_S))
			Camera.getInstance().position.translate(0, -4);
		if (KeyListener.isKeyDown(KeyEvent.VK_A))
			Camera.getInstance().position.translate(-4, 0);
		if (KeyListener.isKeyDown(KeyEvent.VK_D))
			Camera.getInstance().position.translate(4, 0);
	}

	@Override
	public void render() {
		for (int i = 0; i < objects.size(); i++) {
			objects.get(i).render();
		}
	}
}
