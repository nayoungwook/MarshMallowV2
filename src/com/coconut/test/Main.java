package com.coconut.test;

import com.coconut.Sprite;
import com.coconut.marshmallow.Window;
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

	@Override
	public void init() {
		sprite = new Sprite("res/img/bigPinkCookie.png");
	}

	@Override
	public void update() {
	}

	@Override
	public void render() {
		sprite.render();
	}
}
