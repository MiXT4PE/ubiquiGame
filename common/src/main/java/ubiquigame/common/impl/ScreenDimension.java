package ubiquigame.common.impl;

import java.util.Objects;

public class ScreenDimension {

	private int width = 0;
	private int height = 0;

	public ScreenDimension() {

	}

	public ScreenDimension(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof ScreenDimension)) {
			return false;
		}
		ScreenDimension castOther = (ScreenDimension) other;
		return width == castOther.width && height == castOther.height;
	}

	@Override
	public int hashCode() {
		return Objects.hash(width, height);
	}

	@Override
	public String toString() {
		return width + " x " + height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
