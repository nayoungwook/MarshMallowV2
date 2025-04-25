package com.coconut.toffee.shader;

public class ShaderManager {

	public static DefaultShader defaultShader = new DefaultShader();
	public static ShapeShader shapeShader = new ShapeShader();
	public static FrameBufferShader frameBufferShader = new FrameBufferShader();
	private static Shader currentShader = ShaderManager.defaultShader;

	public static Shader getCurrentShader() {
		return currentShader;
	}

	public static void __setCurrentShader__(Shader shader) {
		ShaderManager.currentShader = shader;
	}

}
