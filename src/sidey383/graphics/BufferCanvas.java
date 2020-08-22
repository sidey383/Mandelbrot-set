package sidey383.graphics;

import java.awt.image.BufferedImage;

import sidey383.frame.Frame;

public class BufferCanvas {
	
	private BufferedImage image = new BufferedImage(BUFFER_SIZE, BUFFER_SIZE, BufferedImage.TYPE_3BYTE_BGR);
	/**
	 * Must be a divisor of BUFFER_SIZE.
	 */
	public static final int THREAD_COUNT = 8;
	/**
	 * Width and height of image.
	 */
	public static final int BUFFER_SIZE = 6000;
	private int perThr = BUFFER_SIZE/THREAD_COUNT; 
	private double x;
	private double y;
	private double width;
	private int counter = 0;
	
	public BufferCanvas(double x, double y, double width) 
	{
		Frame.canvas.add(this);
		this.x = x;
		this.y = y;
		this.width = width;
		calculate();
	}
	
	private void calculate() 
	{
		for(int i = 0; i < THREAD_COUNT; i++) 
		{
			int number = i;
			new Thread(()->
			{
				for(int j = number*perThr; j< (number+1)*perThr; j++) 
					for(int k = 0; k < BUFFER_SIZE;k++) 
						image.setRGB(j, k, Frame.colors.getColor( Frame.mandelbrot.isBelongs( x + ( j * width / BUFFER_SIZE ) , y + ( k * width / BUFFER_SIZE ))) );
				counter++;
				if(counter == THREAD_COUNT) 
				{ 
					Frame.calculated = false;
				}
			}).start();
		}
	}
	
	public BufferedImage getImage() 
	{
		return image;
	}
	
	public double getX() 
	{
		return x;
	}
	
	public double getY() 
	{
		return y;
	}
	
	public double getWidth() 
	{
		return width;
	}
	/**
	 * @param x Coordinate of dot.
	 * @return Corresponding coordinate of a point in the image.
	 **/
	public int getPixelX(double x) 
	{
		return (int) (BUFFER_SIZE*(x-this.x)/width);
	}
	/**
	 * @param y Coordinate of dot
	 * @return Corresponding coordinate of a point in the image.
	 **/
	public int getPixelY(double y) 
	{
		return (int) (BUFFER_SIZE*(y-this.y)/width);
	}

}
