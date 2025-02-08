package com.coconut.marshmallow.object;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;

import com.coconut.marshmallow.Display;
import com.coconut.marshmallow.camera.Camera;
import com.coconut.marshmallow.math.Mathf;
import com.coconut.marshmallow.math.Vector;
import com.coconut.marshmallow.shader.Shader;
import com.coconut.marshmallow.shader.ShaderManager;
import com.coconut.marshmallow.sprite.Sprite;

public class GameObject {

	public Sprite sprite;
	public Vector position = new Vector(0, 0);
	public Vector renderPosition = new Vector(0, 0);
	public float rotation = 0f;

	public float width, height;
	public float renderWidth, renderHeight;

	public Shader shader = ShaderManager.defaultShader;

	public Vector anchor = new Vector(0.5f, 0.5f);
	public boolean flipX = false, flipY = false;

	public GameObject(Vector position, float width, float height) {
		this.position = position;
		this.width = width;
		this.height = height;
	}

	public GameObject(float x, float y, float width, float height) {
		this.position = new Vector(x, y);
		this.width = width;
		this.height = height;
	}

	private Matrix4f modelMatrix;
	private Vector3f glmAnchor;

	public void render() {
		this.shader = Display.getShader();
		Display.objects.add(this);
	}

	public void update() {
	}

	public void MARSHMALLOW_RENDER() {
		if (sprite == null)
			return;

		if (Display.getShader() != shader)
			Display.uploadShader(shader);

		sprite.bind();

		modelMatrix = new Matrix4f();
		glmAnchor = new Vector3f(anchor.getX(), anchor.getY(), 0f);

		renderPosition = Mathf.toScreen(position);
		Vector renderSize = Mathf.toScreenSize(width, height, flipX, flipY);
		renderWidth = renderSize.getX();
		renderHeight = renderSize.getY();

		Display.getShader().uploadVec2f("uPosition", new Vector2f(this.position.getX(), this.position.getY()));
		Display.getShader().uploadVec2f("uSize", new Vector2f(this.width, this.height));

		modelMatrix.translate(new Vector3f(renderPosition.getX(), renderPosition.getY(), 0));

		modelMatrix.translate(new Vector3f((glmAnchor.x - 0.5f) * width, (glmAnchor.y - 0.5f) * height, 0f));
		modelMatrix.rotate(-this.rotation + Camera.rotation, new Vector3f(0.0f, 0.0f, 1.0f));
		modelMatrix.translate(new Vector3f((glmAnchor.x - 0.5f) * -1 * width, (glmAnchor.y - 0.5f) * -1 * height, 0f));

		modelMatrix.scale(renderWidth / 100, renderHeight / 100, 1);

		Display.getShader().uploadMat4f("uModel", modelMatrix);

		GL30.glBindVertexArray(sprite.getVaoID());

		GL30.glDrawElements(GL30.GL_TRIANGLES, sprite.getElementArray().length, GL30.GL_UNSIGNED_INT, 0);
	}

}
