package com.coconut.marshmallow.resource;

import java.awt.Font;

public class MSFont {

	public float size = 15f;
	public Font font;
	private String path = "";

	public MSFont(String path, float size) {
		this.path = path;
		this.size = size;
		makeFont();
	}

	public MSFont(String path) {
		this.path = path;
		makeFont();
	}

	public void makeFont() {
		MSResource res = new MSResource();
		this.font = res.getFont(path, size);
	}

}
