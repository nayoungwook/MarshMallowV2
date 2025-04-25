package com.coconut.toffee.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import com.coconut.toffee.Display;
import com.coconut.toffee.math.Vector;

public class Input {
	public static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];
	public static Vector mousePointer = new Vector(0, 0);
	public static float scrollYOffset = 0.0f; // 스크롤 값 저장

	private GLFWKeyCallback keyboard;
	private GLFWCursorPosCallback mouseMove;
	private GLFWMouseButtonCallback mouseButtons;
	private GLFWScrollCallback mouseScroll;

	public Input() {
		keyboard = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				keys[key] = (action != GLFW.GLFW_RELEASE);
			}
		};

		mouseMove = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				mouseX = xpos - Display.width / 2;
				mouseY = -ypos + Display.height / 2;
				Input.mousePointer.setTransform((float) mouseX, (float) mouseY);
			}
		};

		mouseButtons = new GLFWMouseButtonCallback() {
			@Override
			public void invoke(long window, int button, int action, int mods) {
				buttons[button] = (action != GLFW.GLFW_RELEASE);
			}
		};

		mouseScroll = new GLFWScrollCallback() {
			@Override
			public void invoke(long window, double xoffset, double yoffset) {
				scrollYOffset = (float) yoffset; // y 방향 스크롤 값 저장
			}
		};
	}

	public void destroy() {
		keyboard.free();
		mouseMove.free();
		mouseButtons.free();
	}

	public GLFWKeyCallback getKeyboardCallback() {
		return keyboard;
	}

	public static boolean[] buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
	private static double mouseX, mouseY;

	public GLFWCursorPosCallback getMouseMoveCallback() {
		return mouseMove;
	}

	public GLFWMouseButtonCallback getMouseButtonsCallback() {
		return mouseButtons;
	}

	public GLFWScrollCallback getMouseScrollCallback() {
		return mouseScroll;
	}
}
