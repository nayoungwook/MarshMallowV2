package com.coconut.marshmallow.object;

import java.nio.ByteBuffer;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.coconut.marshmallow.Display;
import com.coconut.marshmallow.camera.Camera;
import com.coconut.marshmallow.sprite.Sprite;

public class FrameBuffer extends GameObject {

	private int fbo;
	private int texture;

	public FrameBuffer() {
		super(0, 0, Camera.getResolutionX(), Camera.getResolutionY());
		initialize();
	}

	private void initialize() {
		fbo = GL30.glGenFramebuffers();

		texture = GL30.glGenTextures();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture);
		GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL13.GL_RGBA, (int) Camera.getResolutionX(),
				(int) Camera.getResolutionY(), 0, GL13.GL_RGBA, GL13.GL_UNSIGNED_BYTE, (ByteBuffer) null);
		GL20.glDrawBuffers(GL30.GL_COLOR_ATTACHMENT0);

		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL30.GL_TEXTURE_2D, texture, 0);

		float[] vertexArray = new float[] {
				1f, 1f, 0.0f, 1, 0 + 1, // Bottom
				-1f, -1f, 0.0f, 0, 0, // Top left 1
				1f, -1f, 0.0f, 1, 0, // Top right 2
				-1f, 1f, 0.0f, 0, 1// Bottom left 3
		};
		sprite = new Sprite(texture, vertexArray);
	}

	@Override
	public void MARSHMALLOW_RENDER() {
		if (sprite == null)
			return;

		sprite.bind();

		Display.getShader().uploadMat4f("uProjection", new Matrix4f().identity());
		Display.getShader().uploadMat4f("uView", new Matrix4f().identity());
		Display.getShader().uploadMat4f("uModel", new Matrix4f().identity());
		
		GL30.glBindVertexArray(sprite.getVaoID());

		GL30.glDrawElements(GL30.GL_TRIANGLES, sprite.getElementArray().length, GL30.GL_UNSIGNED_INT, 0);
	}

	public void bind() {
		Display.frameBuffer = fbo;
	}

	public void unbind() {
		Display.frameBuffer = 0;
	}

	public int getTexture() {
		return texture;
	}

	public int getFBO() {
		return fbo;
	}

}
