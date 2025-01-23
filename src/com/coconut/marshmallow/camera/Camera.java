package com.coconut.marshmallow.camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.coconut.marshmallow.math.Vector;

public class Camera {
	private Matrix4f projectionMatrix, viewMatrix;
	public Vector position;
	public float rotation = 0f;

	private static Camera instance;

	public static Camera getInstance() {
		if (instance == null) {
			synchronized (Camera.class) {
				if (instance == null) {
					instance = new Camera();
				}
			}
		}
		return instance;
	}

	public Camera() {
		position = new Vector(0f, 0f);
		projectionMatrix = new org.joml.Matrix4f();
		viewMatrix = new org.joml.Matrix4f();
		adjustProjection();
	}

	private static float screenW, screenH;

	public static void updateScreenSize(float screenW, float screenH) {
		Camera.screenW = screenW;
		Camera.screenH = screenH;
	}

	public void adjustProjection() {
		projectionMatrix.identity();
		projectionMatrix.ortho(-Camera.screenW / 2, Camera.screenW / 2, -Camera.screenH / 2, Camera.screenH / 2, 0,
				100);
	}

	public Matrix4f getViewMatrix() {
		Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
		Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
		this.viewMatrix.identity();

		cameraUp.rotateAxis(rotation, 0f, 0f, 1f);

		viewMatrix.lookAt(new Vector3f(position.getX(), position.getY(), 20.0f),
				cameraFront.add(position.getX(), position.getY(), 0.0f), cameraUp);

		return this.viewMatrix;
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
}
