package com.coconut.marshmallow;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

public class Shader {

	private String vertexShaderSrc;

	private String fragmentShaderSrc;

	private int vertexID, fragmentID, shaderProgram;

	private String vertexPath, fragmentPath;

	public Shader(String vertexPath, String fragmentPath) {
		this.vertexPath = vertexPath;
		this.fragmentPath = fragmentPath;

		try {
			vertexShaderSrc = new String(Files.readAllBytes(Paths.get(vertexPath)));
			fragmentShaderSrc = new String(Files.readAllBytes(Paths.get(fragmentPath)));
		} catch (IOException e) {
			e.printStackTrace();
		}

		initialize();
	}

	public String getVertexPath() {
		return vertexPath;
	}

	public String getFragmentPath() {
		return fragmentPath;
	}

	public void bind() {
		GL30.glUseProgram(shaderProgram);
	}

	public void unbind() {
		GL30.glUseProgram(0);
	}

	private void initialize() {
		// ============================================================
		// Compile and link shaders
		// ============================================================

		// First load and compile the vertex shader
		vertexID = GL30.glCreateShader(GL30.GL_VERTEX_SHADER);
		// Pass the shader source to the GPU
		GL30.glShaderSource(vertexID, vertexShaderSrc);
		GL30.glCompileShader(vertexID);

		// Check for errors in compilation
		int success = GL30.glGetShaderi(vertexID, GL30.GL_COMPILE_STATUS);
		if (success == GL30.GL_FALSE) {
			int len = GL30.glGetShaderi(vertexID, GL30.GL_INFO_LOG_LENGTH);
			System.out.println("ERROR: 'defaultShader.glsl'\n\tVertex shader compilation failed.");
			System.out.println(GL30.glGetShaderInfoLog(vertexID, len));
		}

		// First load and compile the vertex shader
		fragmentID = GL30.glCreateShader(GL30.GL_FRAGMENT_SHADER);
		// Pass the shader source to the GPU
		GL30.glShaderSource(fragmentID, fragmentShaderSrc);
		GL30.glCompileShader(fragmentID);

		// Check for errors in compilation
		success = GL30.glGetShaderi(fragmentID, GL30.GL_COMPILE_STATUS);
		if (success == GL30.GL_FALSE) {
			int len = GL30.glGetShaderi(fragmentID, GL30.GL_INFO_LOG_LENGTH);
			System.out.println("ERROR: 'defaultShader.glsl'\n\tFragment shader compilation failed.");
			System.out.println(GL30.glGetShaderInfoLog(fragmentID, len));
		}

		// Link shaders and check for errors
		shaderProgram = GL30.glCreateProgram();
		GL30.glAttachShader(shaderProgram, vertexID);
		GL30.glAttachShader(shaderProgram, fragmentID);
		GL30.glLinkProgram(shaderProgram);

		// Check for linking errors
		success = GL30.glGetProgrami(shaderProgram, GL30.GL_LINK_STATUS);
		if (success == GL30.GL_FALSE) {
			int len = GL30.glGetProgrami(shaderProgram, GL30.GL_INFO_LOG_LENGTH);
			System.out.println("ERROR: 'defaultShader.glsl'\n\tLinking of shaders failed.");
			System.out.println(GL30.glGetProgramInfoLog(shaderProgram, len));
		}
	}

	public void uploadMat4f(String varName, Matrix4f mat4) {
		int varLocation = GL30.glGetUniformLocation(shaderProgram, varName);
		bind();
		FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
		mat4.get(matBuffer);
		GL30.glUniformMatrix4fv(varLocation, false, matBuffer);
	}

	public void uploadMat3f(String varName, Matrix3f mat3) {
		int varLocation = GL30.glGetUniformLocation(shaderProgram, varName);
		bind();
		FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
		mat3.get(matBuffer);
		GL30.glUniformMatrix3fv(varLocation, false, matBuffer);
	}

	public void uploadVec4f(String varName, Vector4f vec) {
		int varLocation = GL30.glGetUniformLocation(shaderProgram, varName);
		bind();
		GL30.glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
	}

	public void uploadVec3f(String varName, Vector3f vec) {
		int varLocation = GL30.glGetUniformLocation(shaderProgram, varName);
		bind();
		GL30.glUniform3f(varLocation, vec.x, vec.y, vec.z);
	}

	public void uploadVec2f(String varName, Vector2f vec) {
		int varLocation = GL30.glGetUniformLocation(shaderProgram, varName);
		bind();
		GL30.glUniform2f(varLocation, vec.x, vec.y);
	}

	public void uploadFloat(String varName, float val) {
		int varLocation = GL30.glGetUniformLocation(shaderProgram, varName);
		bind();
		GL30.glUniform1f(varLocation, val);
	}

	public void uploadInt(String varName, int val) {
		int varLocation = GL30.glGetUniformLocation(shaderProgram, varName);
		bind();
		GL30.glUniform1i(varLocation, val);
	}

	public void uploadTexture(String varName, int slot) {
		int varLocation = GL30.glGetUniformLocation(shaderProgram, varName);
		bind();
		GL30.glUniform1i(varLocation, slot);
	}
}
