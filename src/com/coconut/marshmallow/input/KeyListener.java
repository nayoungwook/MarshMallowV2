package com.coconut.marshmallow.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class KeyListener {
	private static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];

	private GLFWKeyCallback keyboard;

	public KeyListener() {
		keyboard = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				keys[key] = (action != GLFW.GLFW_RELEASE);
			}
		};
	}

	public static boolean isKeyDown(int key) {
		return keys[key];
	}

	public void destroy() {
		keyboard.free();
	}

	public GLFWKeyCallback getKeyboardCallback() {
		return keyboard;
	}

}