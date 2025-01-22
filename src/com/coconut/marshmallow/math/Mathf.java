package com.coconut.marshmallow.math;

public class Mathf {

	public static float getDistance(Vector position, Vector position2) {
		return (float) Math.abs(Math.sqrt(((position.getX() - position2.getX()) * (position.getX() - position2.getX())
				+ (position.getY() - position2.getY()) * (position.getY() - position2.getY()))));
	}

	public static float getAngle(Vector position, Vector position2) {
		return (float) Math.atan2(position2.getY() - position.getY(), position2.getX() - position.getX());
	}

	public static float getXv(float moveSpeed, Vector position, Vector position2) {
		return (float) Math.cos(getAngle(position, position2)) * moveSpeed;
	}

	public static float getYv(float moveSpeed, Vector position, Vector position2) {
		return (float) Math.sin(getAngle(position, position2)) * moveSpeed;
	}

}
