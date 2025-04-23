package com.coconut.marshmallow.camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.coconut.marshmallow.math.Vector;

public class Camera {
	private static Matrix4f projectionMatrix = new Matrix4f(), viewMatrix = new Matrix4f();
	public static Vector position = new Vector(0f, 0f, 1f);
	public static float rotation = 0f;

	public Camera() {
	}

	private static float resolutionX = 0, resolutionY = 0;

	public static float getResolutionX() {
		return resolutionX;
	}
	
	public static float getResolutionY() {
		return resolutionY;
	}
	
	public static void adjustProjection(float resolutionX, float resolutionY) {
		Camera.resolutionX = resolutionX;
		Camera.resolutionY = resolutionY;

		projectionMatrix = new Matrix4f();
		projectionMatrix.identity().ortho(resolutionX / -2, resolutionX / 2, resolutionY / -2, resolutionY / 2, -100,
				100);
	}

	public static Matrix4f getViewMatrix() {
		Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
		Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);

		Vector3f cameraPos = new Vector3f(-position.getX(), -position.getY(), -1);

		viewMatrix.identity();
		viewMatrix.scale(position.getZ());
		viewMatrix.rotate(rotation, 0, 0, 1);
		viewMatrix.translate(cameraPos);
		viewMatrix.lookAt(new Vector3f(0, 0, 0), cameraFront, cameraUp);

		return viewMatrix;
	}

	public static Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
}
