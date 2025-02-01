package com.coconut.marshmallow.camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.coconut.marshmallow.math.Vector;

public class Camera {
	public static Matrix4f projectionMatrix = new Matrix4f(), viewMatrix = new Matrix4f();
	public static Vector position = new Vector(0f, 0f, 1);
	public static float relZ = 1f;
	public static float rotation = 0f;

	public Camera() {
		adjustProjection();
	}

	private static float screenW, screenH;

	public static void updateScreenSize(float screenW, float screenH) {
		Camera.screenW = screenW;
		Camera.screenH = screenH;
	}

	public static void adjustProjection() {
		projectionMatrix.identity();
		projectionMatrix.ortho(Camera.screenW / -2, Camera.screenW / 2, Camera.screenH / -2, Camera.screenH / 2, -1f,
				1f);
	}

	public static Matrix4f getViewMatrix() {
		Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
		Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
		viewMatrix.identity();

		viewMatrix.lookAt(new Vector3f(0, 0, 0), cameraFront.add(0, 0, 0.0f), cameraUp);

		return viewMatrix;
	}

	public static Matrix4f getProjectionMatrix() {
		adjustProjection();
		return projectionMatrix;
	}
}
