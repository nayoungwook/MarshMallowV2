package com.coconut.test;

import com.coconut.marshmallow.Window;
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

	private Sprite sprite = null;
	private GameObject object = null;

	@Override
	public void init() {
		sprite = new Sprite("res/img/bigPinkCookie.png", 0, 0, 700, 900);
		object = new GameObject(0, 0, 50, 50);
		object.sprite = sprite;
	}

	@Override
	public void update() {
		object.rotation += 0.04f;
	}

	@Override
	public void render() {
		object.render();
	}
}
