package com.coconut.marshmallow.object;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL13;
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
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture);
		GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL13.GL_RGBA, (int) Camera.getResolutionX(),
				(int) Camera.getResolutionY(), 0, GL13.GL_RGBA, GL13.GL_UNSIGNED_BYTE, (ByteBuffer) null);

		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL30.GL_TEXTURE_2D, texture, 0);

		sprite = new Sprite(texture);
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
