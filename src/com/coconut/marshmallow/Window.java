package com.coconut.marshmallow;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import com.coconut.marshmallow.camera.Camera;
import com.coconut.marshmallow.input.KeyListener;
import com.coconut.marshmallow.input.MouseListener;
import com.coconut.marshmallow.shader.Shader;
import com.coconut.marshmallow.state.MSScene;
import com.coconut.marshmallow.state.SceneManager;

public class Window {

	private int width, height;
	String title;

	private long glfwWindow;

	private KeyListener keyListener;
	private MouseListener mouseListener;

	public Window(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;

		this.keyListener = new KeyListener();
		this.mouseListener = new MouseListener();

		inintializeOpenGL();
		init();

		Camera.updateScreenSize(width, height);
	}

	public void inintializeOpenGL() { // Setup an error callback
		GLFWErrorCallback.createPrint(System.err).set();

		if (!GLFW.glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW.");
		}

		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_FALSE);

		glfwWindow = GLFW.glfwCreateWindow(this.width, this.height, this.title, MemoryUtil.NULL, MemoryUtil.NULL);
		if (glfwWindow == MemoryUtil.NULL) {
			throw new IllegalStateException("Failed to create the GLFW window.");
		}

		GLFW.glfwMakeContextCurrent(glfwWindow);
		GLFW.glfwSwapInterval(1);

		GLFW.glfwShowWindow(glfwWindow);

		GLFW.glfwSetKeyCallback(glfwWindow, keyListener.getKeyboardCallback());
		GLFW.glfwSetCursorPosCallback(glfwWindow, mouseListener.getMouseMoveCallback());
		GLFW.glfwSetMouseButtonCallback(glfwWindow, mouseListener.getMouseButtonsCallback());

		GL.createCapabilities();

		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
		GL30.glEnableVertexAttribArray(2);

		GL13.glEnable(GL13.GL_BLEND);
		GL30.glBlendFuncSeparate(GL13.GL_SRC_ALPHA, GL13.GL_ONE_MINUS_SRC_ALPHA, GL13.GL_ONE, GL13.GL_ONE);
	}

	public void init() {
		shader = new Shader("res/shader/vertex.glsl", "res/shader/fragment.glsl");
	}

	public void update() {
		Camera.updateScreenSize(width, height);

		GLFW.glfwPollEvents();

		MSScene scene = SceneManager.getScene();
		if (scene != null)
			scene.update();
	}

	public static Shader shader;

	public void render() {
		GL30.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GL30.glClear(GL13.GL_COLOR_BUFFER_BIT);

		shader.bind();
		shader.uploadMat4f("uProjection", Camera.getInstance().getProjectionMatrix());
		shader.uploadMat4f("uView", Camera.getInstance().getViewMatrix());
		shader.uploadTexture("TEX_SAMPLER", 0);
		
		MSScene scene = SceneManager.getScene();
		if (scene != null)
			scene.render();

		GLFW.glfwSwapBuffers(glfwWindow);
	}

	public void run() {
		long initialTime = System.nanoTime();
		final double timeU = 1000000000 / 60;
		final double timeF = 1000000000 / 60;
		double deltaU = 0, deltaF = 0;
		int frames = 0, ticks = 0;
		long timer = System.currentTimeMillis();

		while (!GLFW.glfwWindowShouldClose(glfwWindow)) {

			long currentTime = System.nanoTime();
			deltaU += (currentTime - initialTime) / timeU;
			deltaF += (currentTime - initialTime) / timeF;
			initialTime = currentTime;

			if (deltaU >= 1) {
				update();
				ticks++;
				deltaU--;
			}

			if (deltaF >= 1) {
				render();
				frames++;
				deltaF--;
			}

			if (System.currentTimeMillis() - timer > 1000) {
				System.out.println(String.format("UPS : %s, FPS : %s", ticks, frames));
				frames = 0;
				ticks = 0;
				timer += 1000;
			}
		}

		// Free the memory
		Callbacks.glfwFreeCallbacks(glfwWindow);
		GLFW.glfwDestroyWindow(glfwWindow);

		// Terminate GLFW and the free the error callback
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();
	}
}
