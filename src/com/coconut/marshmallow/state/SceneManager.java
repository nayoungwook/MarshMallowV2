package com.coconut.marshmallow.state;

public class SceneManager {

	private static Scene scene;

	public static Scene getScene() {
		return SceneManager.scene;
	}

	public static void setScene(Scene scene) {
		SceneManager.scene = scene;
		SceneManager.scene.init();
	}
}
