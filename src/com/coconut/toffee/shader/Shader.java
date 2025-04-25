package com.coconut.toffee.shader;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Shader {

	private String vertexShaderSrc;

	private String fragmentShaderSrc;

	private int vertexID, fragmentID, shaderProgram;

	private int shaderProgramBackup;

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
		if (ShaderManager.getCurrentShader() != this)
			ShaderManager.getCurrentShader().unbind();

		this.shaderProgramBackup = this.shaderProgram;
		ShaderManager.__setCurrentShader__(this);
		GL30.glUseProgram(shaderProgram);
	}

	public void unbind() {
		GL30.glUseProgram(0);
	}

	private int compileShader(int shaderID, String shaderSrc, int SHADER_TYPE) {
		shaderID = GL30.glCreateShader(SHADER_TYPE);
		GL30.glShaderSource(shaderID, shaderSrc);
		GL30.glCompileShader(shaderID);

		int success = GL30.glGetShaderi(shaderID, GL30.GL_COMPILE_STATUS);
		if (success == GL30.GL_FALSE) {
			int len = GL30.glGetShaderi(shaderID, GL30.GL_INFO_LOG_LENGTH);
			System.out.println(GL30.glGetShaderInfoLog(shaderID, len));
		}
		return shaderID;
	}

	private void initialize() {
		vertexID = compileShader(vertexID, vertexShaderSrc, GL30.GL_VERTEX_SHADER);
		fragmentID = compileShader(fragmentID, fragmentShaderSrc, GL30.GL_FRAGMENT_SHADER);

		shaderProgram = GL30.glCreateProgram();
		GL30.glAttachShader(shaderProgram, vertexID);
		GL30.glAttachShader(shaderProgram, fragmentID);
		GL30.glLinkProgram(shaderProgram);

		if (GL30.glGetProgrami(shaderProgram, GL30.GL_LINK_STATUS) == GL30.GL_FALSE) {
			int len = GL30.glGetProgrami(shaderProgram, GL30.GL_INFO_LOG_LENGTH);
			System.out.println(GL30.glGetProgramInfoLog(shaderProgram, len));
		}
	}

	public HashMap<String, Integer> uniformLocations = new HashMap<>();
	private static final FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);

	public void uploadMat4f(String varName, Matrix4f mat4) {
		if (shaderProgramBackup != shaderProgram)
			bind();
		int varLocation = 0;

		if (uniformLocations.containsKey(varName))
			varLocation = uniformLocations.get(varName);
		else {
			varLocation = GL30.glGetUniformLocation(shaderProgram, varName);
			uniformLocations.put(varName, varLocation);
		}
		if (varLocation == -1)
			return;

		mat4.get(matBuffer);
		GL30.glUniformMatrix4fv(varLocation, false, matBuffer);
	}

	public void uploadVec2fv(String varName, Vector2f[] vec) {
		if (shaderProgramBackup != shaderProgram)
			bind();

		int location = 0;
		if (uniformLocations.containsKey(varName))
			location = uniformLocations.get(varName);
		else {
			location = GL30.glGetUniformLocation(shaderProgram, varName);
			uniformLocations.put(varName, location);
		}

		float[] floatArray = new float[vec.length * 2];
		for (int i = 0; i < vec.length; i++) {
			if (vec[i] != null) {
				floatArray[i * 2] = vec[i].x;
				floatArray[i * 2 + 1] = vec[i].y;
			} else {
				floatArray[i * 2] = 0.0f;
				floatArray[i * 2 + 1] = 0.0f;
			}
		}
		if (location == -1)
			return;
		GL20.glUniform2fv(location, floatArray);
	}

	public void uploadVec3fv(String varName, Vector3f[] vec) {
		if (shaderProgramBackup != shaderProgram)
			bind();

		int location = 0;
		if (uniformLocations.containsKey(varName))
			location = uniformLocations.get(varName);
		else {
			location = GL30.glGetUniformLocation(shaderProgram, varName);
			uniformLocations.put(varName, location);
		}

		float[] floatArray = new float[vec.length * 3];
		for (int i = 0; i < vec.length; i++) {
			if (vec[i] != null) {
				floatArray[i * 3] = vec[i].x;
				floatArray[i * 3 + 1] = vec[i].y;
				floatArray[i * 3 + 2] = vec[i].z;
			} else {
				floatArray[i * 3] = 0.0f;
				floatArray[i * 3 + 1] = 0.0f;
				floatArray[i * 3 + 2] = 0.0f;
			}
		}
		if (location == -1)
			return;
		GL20.glUniform3fv(location, floatArray);
	}

	public void uploadMat3f(String varName, Matrix3f mat3) {
		if (shaderProgramBackup != shaderProgram)
			bind();
		int varLocation = 0;
		if (uniformLocations.containsKey(varName))
			varLocation = uniformLocations.get(varName);
		else {
			varLocation = GL30.glGetUniformLocation(shaderProgram, varName);
			uniformLocations.put(varName, varLocation);
		}

		mat3.get(matBuffer);
		GL30.glUniformMatrix3fv(varLocation, false, matBuffer);
	}

	public void uploadVec4f(String varName, Vector4f vec) {
		if (shaderProgramBackup != shaderProgram)
			bind();
		int varLocation = 0;
		if (uniformLocations.containsKey(varName))
			varLocation = uniformLocations.get(varName);
		else {
			varLocation = GL30.glGetUniformLocation(shaderProgram, varName);
			uniformLocations.put(varName, varLocation);
		}
		if (varLocation == -1)
			return;
		GL30.glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
	}

	public void uploadVec3f(String varName, Vector3f vec) {
		if (shaderProgramBackup != shaderProgram)
			bind();
		int varLocation = 0;
		if (uniformLocations.containsKey(varName))
			varLocation = uniformLocations.get(varName);
		else {
			varLocation = GL30.glGetUniformLocation(shaderProgram, varName);
			uniformLocations.put(varName, varLocation);
		}
		if (varLocation == -1)
			return;
		GL30.glUniform3f(varLocation, vec.x, vec.y, vec.z);
	}

	public void uploadVec2f(String varName, Vector2f vec) {
		if (shaderProgramBackup != shaderProgram)
			bind();
		int varLocation = 0;
		if (uniformLocations.containsKey(varName))
			varLocation = uniformLocations.get(varName);
		else {
			varLocation = GL30.glGetUniformLocation(shaderProgram, varName);
			uniformLocations.put(varName, varLocation);
		}

		if (varLocation == -1)
			return;

		GL30.glUniform2f(varLocation, vec.x, vec.y);
	}

	public void uploadFloat(String varName, float val) {
		if (shaderProgramBackup != shaderProgram)
			bind();
		int varLocation = 0;
		if (uniformLocations.containsKey(varName))
			varLocation = uniformLocations.get(varName);
		else {
			varLocation = GL30.glGetUniformLocation(shaderProgram, varName);
			uniformLocations.put(varName, varLocation);
		}
		if (varLocation == -1)
			return;
		GL30.glUniform1f(varLocation, val);
	}

	public void uploadInt(String varName, int val) {
		if (shaderProgramBackup != shaderProgram)
			bind();
		int varLocation = 0;
		if (uniformLocations.containsKey(varName))
			varLocation = uniformLocations.get(varName);
		else {
			varLocation = GL30.glGetUniformLocation(shaderProgram, varName);
			uniformLocations.put(varName, varLocation);
		}
		if (varLocation == -1)
			return;
		GL30.glUniform1i(varLocation, val);
	}

	public void uploadTexture(String varName, int slot) {
		if (shaderProgramBackup != shaderProgram)
			bind();
		int varLocation = 0;
		if (uniformLocations.containsKey(varName))
			varLocation = uniformLocations.get(varName);
		else {
			varLocation = GL30.glGetUniformLocation(shaderProgram, varName);
			uniformLocations.put(varName, varLocation);
		}
		if (varLocation == -1)
			return;
		GL30.glUniform1i(varLocation, slot);
	}
}
