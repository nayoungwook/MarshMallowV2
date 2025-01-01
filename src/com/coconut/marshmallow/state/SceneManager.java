package com.coconut.marshmallow.state;

public class SceneManager {

	private static MSScene scene;

	public static MSScene getScene() {
		return SceneManager.scene;
	}

	public static void setScene(MSScene scene) {
		SceneManager.scene = scene;
		SceneManager.scene.init();
	}
}
