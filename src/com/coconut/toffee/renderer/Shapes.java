package com.coconut.toffee.renderer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

public class Shapes {

	protected float[] vertexArray;

	private int[] elementArray = { 2, 1, 0, // Top right triangle
			0, 1, 3 // bottom left triangle
	};

	public int[] getElementArray() {
		return elementArray;
	}

	protected int vaoID, vboID, eboID;

	public int getVaoID() {
		return vaoID;
	}

	public static Shapes oval = new Shapes().initializeOval();
	public static Shapes rect = new Shapes().initializeRect();

	public Shapes() {
	}

	public Shapes initializeOval() {
		float x = 0, y = 0;
		int segments = 300;
		float radius = 0.5f;

		// 정점 배열 (x, y, z 좌표를 저장)
		vertexArray = new float[(segments + 2) * 3]; // (segments + 1)개의 점 + 중심점

		// 원의 중심점 추가 (x, y, z)
		vertexArray[0] = x;
		vertexArray[1] = y;
		vertexArray[2] = 0.0f;

		// 나머지 원의 점 추가
		for (int i = 1; i <= segments + 1; i++) {
			double angle = 2.0 * Math.PI / segments * (i - 1);
			float dx = (float) Math.cos(angle) * radius;
			float dy = (float) Math.sin(angle) * radius;

			vertexArray[i * 3] = x + dx;
			vertexArray[i * 3 + 1] = y + dy;
			vertexArray[i * 3 + 2] = 0.0f;
		}

		initialize();

		GL30.glVertexAttribPointer(0, 3, GL30.GL_FLOAT, false, 3 * 4, 0);
		GL30.glEnableVertexAttribArray(0);

		return this;
	}

	public Shapes initializeRect() {
		vertexArray = new float[] { 0.5f, -0.5f, 0.0f, // Bottom
				-0.5f, 0.5f, 0.0f, // Top left 1
				0.5f, 0.5f, 0.0f, // Top right 2
				-0.5f, -0.5f, 0.0f, // Bottom left 3
		};

		initialize();

		GL30.glVertexAttribPointer(0, 3, GL30.GL_FLOAT, false, 12, 0);
		GL30.glEnableVertexAttribArray(0);

		return this;
	}

	protected void initialize() {
		vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);

		// Create a float buffer of vertices
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
		vertexBuffer.put(vertexArray).flip();

		// Create VBO upload the vertex buffer
		vboID = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboID);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, vertexBuffer, GL30.GL_STATIC_DRAW);

		// Create the indices and upload
		IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
		elementBuffer.put(elementArray).flip();

		eboID = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, eboID);
		GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL30.GL_STATIC_DRAW);
	}

}
