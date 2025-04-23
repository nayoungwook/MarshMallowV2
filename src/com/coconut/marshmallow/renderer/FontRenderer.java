package com.coconut.marshmallow.renderer;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;

import com.coconut.marshmallow.Display;
import com.coconut.marshmallow.font.TTFont;
import com.coconut.marshmallow.math.Vector;
import com.coconut.marshmallow.object.GameObject;

public class FontRenderer extends GameObject {

	protected Matrix4f modelMatrix;
	protected Vector3f glmAnchor;
	protected Color color = null;

	public TTFont font = null;
	public String text = "";
	public String align = "center";

	@Override
	public void MARSHMALLOW_RENDER() {
		font.bakeFont(text, color);
		sprite = font;
		width = font.getWidth();
		height = font.getHeight();

		if (sprite == null)
			return;

		sprite.bind();

		if (this.align == "left") {
			position.translate(width / 2, 0);
		} else if (this.align == "right") {
			position.translate(width / -2, 0);
		}

		modelMatrix = makeModelMatrix();

		Display.getShader().uploadMat4f("uModel", modelMatrix);

		GL30.glBindVertexArray(sprite.getVaoID());

		GL30.glDrawElements(GL30.GL_TRIANGLES, sprite.getElementArray().length, GL30.GL_UNSIGNED_INT, 0);
	}

	public FontRenderer(Vector position, float width, float height, Color color, String text, TTFont font) {
		super(position, width, height);
		this.color = color;
		this.font = font;
	}

}
