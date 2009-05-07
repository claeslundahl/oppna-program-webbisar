package se.vgr.webbisar.svc;


public class ImageSize {
	private double width;
	private double height;
	
	public ImageSize(double width, double height) {
		this.height = height;
		this.width = width;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}
	
	@Override
	public String toString() {
		return new StringBuffer().append("width=").append(width).append(",").append("height=").append(height).toString();
	}

	public double max() {
		return width > height ? width : height;
	}
	
}
