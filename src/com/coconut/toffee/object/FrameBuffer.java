package com.coconut.toffee.object;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.coconut.toffee.Display;
import com.coconut.toffee.camera.Camera;
import com.coconut.toffee.shader.Shader;
import com.coconut.toffee.shader.ShaderManager;
import com.coconut.toffee.sprite.Sprite;

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

		float[] vertexArray = new float[] { 1f, 1f, 0.0f, 1, 0 + 1, // Bottom
				-1f, -1f, 0.0f, 0, 0, // Top left 1
				1f, -1f, 0.0f, 1, 0, // Top right 2
				-1f, 1f, 0.0f, 0, 1// Bottom left 3
		};
		sprite = new Sprite(texture, vertexArray);
	}

	private int backupW = 0, backupH = 0;

	public void updateFramebufferTexture() {

		if (backupW == Display.width && backupH == Display.height)
			return;

		backupW = Display.width;
		backupH = Display.height;

		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);

		GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture);
		GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL13.GL_RGBA, Display.width, Display.height, 0, GL13.GL_RGBA,
				GL13.GL_UNSIGNED_BYTE, (ByteBuffer) null);

		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL30.GL_TEXTURE_2D, texture, 0);
	}

	@Override
	public void glRender() {
		if (sprite == null)
			return;

		this.updateFramebufferTexture();
		sprite.bind();
		
		Shader shaderBackup = ShaderManager.getCurrentShader();
		shaderBackup.unbind();
		ShaderManager.frameBufferShader.bind();

		GL30.glBindVertexArray(sprite.getVaoID());

		GL30.glDrawElements(GL30.GL_TRIANGLES, sprite.getElementArray().length, GL30.GL_UNSIGNED_INT, 0);
		shaderBackup.bind();
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
