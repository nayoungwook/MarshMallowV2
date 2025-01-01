package com.coconut;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;

public class Sprite {

	private float[] vertexArray = {
			// position // color // UV Coordinates
			50f, -50f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1, 1, // Bottom right 0
			-50f, 50f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0, 0, // Top left 1
			50f, 50f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1, 0, // Top right 2
			-50f, -50f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0, 1 // Bottom left 3
	};

	// IMPORTANT: Must be in counter-clockwise order
	private int[] elementArray = { 2, 1, 0, // Top right triangle
			0, 1, 3 // bottom left triangle
	};

	private int vaoID, vboID, eboID;

	private String filepath;
	private int texID;

	public Sprite(String path) {
		initialize();
		this.filepath = path;
		// Generate texture on GPU
		texID = GL30.glGenTextures();

		GL30.glActiveTexture(GL30.GL_TEXTURE0);
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, texID);

		GL30.glPixelStorei(GL30.GL_UNPACK_ALIGNMENT, 1);
		
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_NEAREST);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_NEAREST);

		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_S, GL30.GL_REPEAT);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_T, GL30.GL_REPEAT);

		// Set texture parameters
		// Repeat image in both directions
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_S, GL30.GL_REPEAT);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_T, GL30.GL_REPEAT);
		// When stretching the image, pixelate
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_NEAREST);
		// When shrinking an image, pixelate
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_NEAREST);

		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer channels = BufferUtils.createIntBuffer(1);
		ByteBuffer image = STBImage.stbi_load(filepath, width, height, channels, 0);

		if (image != null) {
			if (channels.get(0) == 3) {
				GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL13.GL_RGB, width.get(0), height.get(0), 0, GL13.GL_RGB,
						GL30.GL_UNSIGNED_BYTE, image);
			} else if (channels.get(0) == 4) {
				GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL13.GL_RGBA, width.get(0), height.get(0), 0, GL13.GL_RGBA,
						GL30.GL_UNSIGNED_BYTE, image);
			}
		}
		GL30.glGenerateMipmap(GL30.GL_TEXTURE_2D);

		STBImage.stbi_image_free(image);
	}

	private void initialize() {
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

		// Add the vertex attribute pointers
		int positionsSize = 3;
		int colorSize = 4;
		int uvSize = 2;
		int vertexSizeBytes = (positionsSize + colorSize + uvSize) * 4;
		GL30.glVertexAttribPointer(0, positionsSize, GL30.GL_FLOAT, false, vertexSizeBytes, 0);
		GL30.glEnableVertexAttribArray(0);

		GL30.glVertexAttribPointer(1, colorSize, GL30.GL_FLOAT, false, vertexSizeBytes, positionsSize * 4);
		GL30.glEnableVertexAttribArray(1);

		GL30.glVertexAttribPointer(2, uvSize, GL30.GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize) * 4);
		GL30.glEnableVertexAttribArray(2);
	}

	public void bind() {
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, texID);
	}

	public void unbind() {
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, 0);
	}

	public void render() {
		// Upload texture to shader
		GL30.glActiveTexture(GL30.GL_TEXTURE0);
		bind();

		// Bind the VAO that we're using
		GL30.glBindVertexArray(vaoID);

		// Enable the vertex attribute pointers
		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
		 GL30.glEnableVertexAttribArray(2);

		GL30.glDrawElements(GL30.GL_TRIANGLES, elementArray.length, GL30.GL_UNSIGNED_INT, 0);

		// Unbind everything
		GL30.glDisableVertexAttribArray(0);
		GL30.glDisableVertexAttribArray(1);
		GL30.glDisableVertexAttribArray(2);

		GL30.glBindVertexArray(0);
	}

}
