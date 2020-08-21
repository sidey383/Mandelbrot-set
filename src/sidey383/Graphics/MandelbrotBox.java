package sidey383.Graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import sidey383.math.Mandelbrot;

public class MandelbrotBox {

	private Mandelbrot mandelbrot;
	private ColorScheme colors;
	private Chunk mainChunk;
	private static MandelbrotBox instance;
	
	public MandelbrotBox(ColorScheme colors, int iterCount, int chunckMaxCount) 
	{
		this.colors = colors;
		mandelbrot = new Mandelbrot(iterCount);
		instance = this;
		mainChunk = new Chunk(-2, -2, 4, mandelbrot, colors);
		mainChunk.calculate();
	}
	
	/**
	*@param x Coordinate of center
	*@param y Coordinate of center
	*@param scale Size of pixel = 1/scale;
	**/
	public BufferedImage getFrame(double x, double y, int width, int height, int scale) 
	{
		BufferedImage frame = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics gr = frame.getGraphics();
		gr.setColor(new Color(colors.getColor((byte)0)));
		gr.drawRect(0, 0, width, height);
		System.out.print("");
		double x1 = (x - ( (double) width / 2 ) / scale );
		double x2 = (x + ( (double) width / 2 ) / scale );
		double y1 = (y - ( (double) height / 2 ) / scale );
		double y2 = (y + ( (double) height / 2 ) / scale );
		mainChunk.drawChuncks(frame, x1, y1, x2, y2);
		return frame;
	}
	
	public static MandelbrotBox getInstance() 
	{
		return instance;
	}
	
}
