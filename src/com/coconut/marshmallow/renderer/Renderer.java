package com.coconut.marshmallow.renderer;

import java.awt.Color;

import com.coconut.marshmallow.Display;
import com.coconut.marshmallow.font.TTFont;
import com.coconut.marshmallow.math.Vector;
import com.coconut.marshmallow.object.GameObject;
import com.coconut.marshmallow.shader.ShaderManager;
import com.coconut.marshmallow.sprite.Sprite;

public class Renderer {

	public static Color color = new Color(0, 0, 0);

	public static void setColor(Color color) {
		Renderer.color = color;
	}

	public static void renderRect(Vector position, float width, float height) {
		RectRenderer rect = new RectRenderer(position, width, height, Renderer.color);

		Display.objects.add(rect);
	}

	public static void renderRect(Vector position, float width, float height, float rotation) {
		RectRenderer rect = new RectRenderer(position, width, height, Renderer.color);
		rect.rotation = rotation;
		Display.objects.add(rect);
	}

	public static void renderOval(Vector position, float width, float height) {
		OvalRenderer oval = new OvalRenderer(position, width, height, Renderer.color);

		Display.objects.add(oval);
	}

	public static void renderOval(Vector position, float width, float height, float rotation) {
		OvalRenderer oval = new OvalRenderer(position, width, height, Renderer.color);
		oval.rotation = rotation;
		Display.objects.add(oval);
	}

	public static void renderUIRect(Vector position, float width, float height) {
		UIRectRenderer rect = new UIRectRenderer(position, width, height, Renderer.color);

		Display.objects.add(rect);
	}

	public static void renderUIRect(Vector position, float width, float height, float rotation) {
		UIRectRenderer rect = new UIRectRenderer(position, width, height, Renderer.color);
		rect.rotation = rotation;
		Display.objects.add(rect);
	}

	public static void renderImage(Sprite sprite, Vector position, float width, float height) {
		if (sprite == null)
			return;

		GameObject object = new GameObject(0, 0, width, height);
		object.shader = Display.getShader();
		object.position = position;
		object.sprite = sprite;
		Display.objects.add(object);
	}

	public static void renderImage(Sprite sprite, Vector position, float width, float height, float rotation) {
		if (sprite == null)
			return;

		GameObject object = new GameObject(0, 0, width, height);
		object.shader = Display.getShader();
		object.position = position;
		object.sprite = sprite;
		object.rotation = rotation;
		Display.objects.add(object);
	}

	public static void renderFont(TTFont font, String text, Vector position) {
		if (font == null)
			return;

		FontRenderer object = new FontRenderer(new Vector(0, 0), 100, 100, color, text, font);
		object.shader = ShaderManager.defaultShader;
		object.text = text;
		object.position = position;
		Display.objects.add(object);
	}

	public static void renderFont(TTFont font, String text, Vector position, String align) {
		if (font == null)
			return;

		FontRenderer object = new FontRenderer(new Vector(0, 0), 100, 100, color, text, font);
		object.shader = ShaderManager.defaultShader;
		object.align = align;
		object.text = text;
		object.position = position;
		Display.objects.add(object);
	}

}
