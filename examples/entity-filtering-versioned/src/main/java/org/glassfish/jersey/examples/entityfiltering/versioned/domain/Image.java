package org.glassfish.jersey.examples.entityfiltering.versioned.domain;

import java.io.Serializable;

public class Image implements Serializable {

	private static final long serialVersionUID = 1L;

	@Versioned(since = "1.0")
	private int id;

	@Versioned(since = "2.0")
	private float aspectRatio;

	@Deprecated
	@Versioned(since = "4.0.0", until = "5.0.0")
	private int width;

	@Versioned(since = "5")
	private int height;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getAspectRatio() {
		return aspectRatio;
	}

	public void setAspectRatio(float aspectRatio) {
		this.aspectRatio = aspectRatio;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
