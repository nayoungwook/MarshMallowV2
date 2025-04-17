package com.coconut.marshmallow.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL13;

import com.coconut.marshmallow.sprite.Sprite;

public class TTFont extends Sprite {

	private Font awtFont = null;
	public float size = 15f;
	public String textAlign = "center";

	public Font getFont(String path, float size) {
		InputStream stream;
		Font customFont = null;
		try {
			stream = ClassLoader.getSystemClassLoader().getResourceAsStream(path);
			customFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(size);
		} catch (Exception e) {
			System.out.println("FONT ERROR");
		}
		return customFont;
	}

	public TTFont(String fontPath, float fontSize) {
		awtFont = getFont(fontPath, fontSize);

		vertexArray = new float[] {
				// position // UV Coordinates
				0.5f, -0.5f, 0.0f, subXOffset + subWOffset, subYOffset + subHOffset, // Bottom
																						// right 0
				-0.5f, 0.5f, 0.0f, subXOffset, subYOffset, // Top left 1
				0.5f, 0.5f, 0.0f, subXOffset + subWOffset, subYOffset, // Top right 2
				-0.5f, -0.5f, 0.0f, subXOffset, subYOffset + subHOffset // Bottom left 3
		};

		initialize();
		initializeGlSettings();

		BufferedImage image = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) image.getGraphics();

		this.texID = loadTexture(image);
	}

	public BufferedImage createTextImage(String text, Font font, Color color) {
		BufferedImage tempImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = tempImage.createGraphics();

		g2d.setFont(font);
		FontMetrics fontMetrics = g2d.getFontMetrics();

		int textWidth = fontMetrics.stringWidth(text);
		int textHeight = fontMetrics.getHeight();
		this.width = textWidth;
		this.height = textHeight;

		textWidth = Math.max(1, textWidth);
		textHeight = Math.max(1, textHeight);

		BufferedImage image = new BufferedImage(textWidth, textHeight, BufferedImage.TYPE_INT_ARGB);
		g2d = image.createGraphics();

		g2d.setColor(color);
		g2d.setFont(font);
		g2d.drawString(text, 0, fontMetrics.getAscent());

		g2d.dispose();

		return image;
	}

	public void bakeFont(String text, Color color) {
		BufferedImage image = null;
		image = createTextImage(text, awtFont, color);

		updateTexture(image);
	}

	private int width = 0, height = 0;

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	private void updateTexture(BufferedImage image) {
		width = image.getWidth();
		height = image.getHeight();

		int[] pixels = new int[width * height];
		image.getRGB(0, 0, width, height, pixels, 0, width);

		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4); // RGBA 4바이트

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int pixel = pixels[y * width + x];
				buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red
				buffer.put((byte) ((pixel >> 8) & 0xFF)); // Green
				buffer.put((byte) (pixel & 0xFF)); // Blue
				buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha
			}
		}
		buffer.flip(); // OpenGL이 읽을 수 있도록 버퍼를 준비

		GL13.glBindTexture(GL13.GL_TEXTURE_2D, texID);

		// 텍스처 데이터 업로드
		GL13.glTexImage2D(GL13.GL_TEXTURE_2D, 0, GL13.GL_RGBA, width, height, 0, GL13.GL_RGBA, GL13.GL_UNSIGNED_BYTE,
				buffer);
	}

	private int loadTexture(BufferedImage image) {
		width = image.getWidth();
		height = image.getHeight();

		int[] pixels = new int[width * height];
		image.getRGB(0, 0, width, height, pixels, 0, width);

		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4); // RGBA 4바이트

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int pixel = pixels[y * width + x];
				buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red
				buffer.put((byte) ((pixel >> 8) & 0xFF)); // Green
				buffer.put((byte) (pixel & 0xFF)); // Blue
				buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha
			}
		}
		buffer.flip(); // OpenGL이 읽을 수 있도록 버퍼를 준비

		// OpenGL 텍스처 생성
		int textureID = GL13.glGenTextures();
		GL13.glBindTexture(GL13.GL_TEXTURE_2D, textureID);

		// 텍스처 설정
		GL13.glTexParameteri(GL13.GL_TEXTURE_2D, GL13.GL_TEXTURE_MIN_FILTER, GL13.GL_LINEAR);
		GL13.glTexParameteri(GL13.GL_TEXTURE_2D, GL13.GL_TEXTURE_MAG_FILTER, GL13.GL_LINEAR);

		// 텍스처 데이터 업로드
		GL13.glTexImage2D(GL13.GL_TEXTURE_2D, 0, GL13.GL_RGBA, width, height, 0, GL13.GL_RGBA, GL13.GL_UNSIGNED_BYTE,
				buffer);

		return textureID;
	}

}
