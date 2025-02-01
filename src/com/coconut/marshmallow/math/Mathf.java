package com.coconut.marshmallow.math;

import com.coconut.marshmallow.Display;
import com.coconut.marshmallow.camera.Camera;

public class Mathf {

	public static float getDistance(Vector position, Vector position2) {
		return (float) Math.abs(Math.sqrt(((position.getX() - position2.getX()) * (position.getX() - position2.getX())
				+ (position.getY() - position2.getY()) * (position.getY() - position2.getY()))));
	}

	public static float getAngle(Vector position, Vector position2) {
		return (float) -Math.atan2(position2.getY() - position.getY(), position2.getX() - position.getX());
	}

	public static float getXv(float moveSpeed, Vector position, Vector position2) {
		return (float) Math.cos(getAngle(position, position2)) * moveSpeed;
	}

	public static float getYv(float moveSpeed, Vector position, Vector position2) {
		return (float) Math.sin(getAngle(position, position2)) * moveSpeed;
	}

	public static Vector toScreenSize(float width, float height, boolean flipX, boolean flipY) {
		int fx = 1, fy = 1;

		float renderWidth, renderHeight;

		renderWidth = width * (Camera.position.getZ() * Camera.relZ);
		renderHeight = height * (Camera.position.getZ() * Camera.relZ);

		if (flipX) {
			fx = -1;
		}

		if (flipY) {
			fy = -1;
		}

		renderWidth *= (fx);
		renderHeight *= (fy);

		return new Vector(renderWidth, renderHeight);
	}

	public static Vector toScreen(Vector position) {
		float width = Display.width, height = Display.height;

		float camX = Camera.position.getX();
		float camY = Camera.position.getY();
		float camZ = Camera.position.getZ();
		float camRot = Camera.rotation;

		float relX = position.getX() - camX;
		float relY = position.getY() - camY;

		float dist = Mathf.getDistance(new Vector(0, 0), new Vector(relX, relY));
		float angle = (float) Math.atan2(relY, relX) + camRot;

		float depthScale = camZ * Camera.relZ;
		float screenX = (float) (Math.cos(angle) * dist * depthScale);
		float screenY = (float) (Math.sin(angle) * dist * depthScale);

		return new Vector(screenX, screenY);
	}

	public static Vector toWorld(Vector screenPosition) {
		float width = Display.width, height = Display.height;

		float camX = Camera.position.getX();
		float camY = Camera.position.getY();
		float camZ = Camera.position.getZ();
		float camRot = Camera.rotation;

		float relX = screenPosition.getX();
		float relY = screenPosition.getY();

		float depthScale = camZ == 0 ? 1 : 1 / camZ / Camera.relZ;
		float worldX = relX * depthScale;
		float worldY = relY * depthScale;

		float angle = (float) Math.atan2(worldY, worldX) - camRot;
		float dist = (float) Math.sqrt(worldX * worldX + worldY * worldY);

		float finalX = camX + (float) Math.cos(angle) * dist;
		float finalY = camY + (float) Math.sin(angle) * dist;

		return new Vector(finalX, finalY);
	}
}
