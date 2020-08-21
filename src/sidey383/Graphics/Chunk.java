package sidey383.Graphics;

import java.awt.image.BufferedImage;

import sidey383.math.Mandelbrot;

public class Chunk {
	
	private int depth;
	private double width;
	private double yMin;
	private double yMax;
	private double xMin;
	private double xMax;
	//private double scale; //size of pixel
	private Chunk[][] chunks = new Chunk[2][2];
	private Mandelbrot mand;
	private boolean ready = false;
	private boolean startCalculate = false;
	private BufferedImage cImage;
	ColorScheme colors;
	private int end = 0;
	
	
	/**
	*@param x coordinate of lower left corner
	*@param y coordinate of lower left corner
	*@param width width of chunk
	**/
	public Chunk(double x, double y, double width, Mandelbrot mand, ColorScheme colors) 
	{
		this.colors = colors;
		xMin = x;
		yMin = y;
		xMax = x + width;
		yMax = y + width;
		//scale = width/2000;
		this.width = width;
		this.mand = mand;
		System.out.println("creat new chunk xMin = " +xMin+" yMin = "+yMin+ " xMax =  "+xMax+" yMax = "+yMax );
	}
	
	public void drawChuncks(BufferedImage image, double x1, double y1, double x2, double y2)
	{
		//System.out.println("x1 = "+x1+" x2 = "+x2+" y1 = "+y2+" y2 = "+ y2);
		if(containedIn(x1, y1, x2, y2)) {
			double iScale = (x2-x1)/image.getWidth();
			if(isCalculate()) { 
				int x = (int)( (double)(xMin-x1)/(x2-x1)*image.getWidth());
				int y = (int)( (double)(yMax-y2)/(x2-x1)*image.getWidth());
				int width = (int)( this.width/(x2-x1)*image.getWidth());
				System.out.println(" x = "+x+" y= "+ y+" width= "+width);
				image.getGraphics().drawImage(cImage, x, y, width, width, null);
			}else 
			{
				calculate();
			}
			if(iScale<width/2000) 
			{
				for(int i = 0; i < 2; i++) 
					for(int j = 0; j < 2; j++) 
					{
						if(chunks[i][j] == null) 
						{
							chunks[i][j] = new Chunk(xMin + (i*width/2), yMin + (j*width/2), width/2, mand, colors);
						}
						if(chunks[i][j].containedIn(x1, y1, x2, y2)) 
						{
							chunks[i][j].drawChuncks(image, x1, y1, x2, y2);
						}
 					}
			}
		}
	}
	
	public boolean contain(double x, double y) 
	{
		return xMin <= x && x <= xMax  && yMin <= y && y <= yMax;
	}
	
	public boolean containedIn(double x1, double y1, double x2, double y2) 
	{
		if(x1 <= xMin && xMin <= x2 && y1 <= yMin && yMin <= y2 ) return true;
		if(x1 <= xMax && xMax <= x2 && y1 <= yMin && yMin <= y2 ) return true;
		if(x1 <= xMin && xMin <= x2 && y1 <= yMax && yMax <= y2 ) return true;
		if(x1 <= xMax && xMax <= x2 && y1 <= yMax && yMax <= y2 ) return true;
		if(contain(x1, y2)||contain(x1, y1)||contain(x2, y2)||contain(x2, y1)) return true;
		return false;
	}
	
	public void calculate() 
	{
		if(startCalculate == true) return;
		startCalculate = true;
		cImage =  new BufferedImage(2000, 2000, BufferedImage.TYPE_3BYTE_BGR);
		int thread = 4; //must be a divisor of 2000
		int[][] threadData = new int[thread][2];
		int batch = 2000/thread;
		for(int i = 0, k = 0; i<thread; i++) 
		{
			threadData[i][0] = k;
			k+=batch;
			threadData[i][1] = k;
		}
		for(int i = 0; i < thread; i++) {
			int number = i;
			new Thread(()->
			{
				long time = System.currentTimeMillis();
				for(int x_ = threadData[number][0]; x_< threadData[number][1]; x_++) 
					for(int y_ = 0; y_ < 2000; y_++)
						cImage.setRGB(x_, y_, colors.getColor((byte) ( 255 *  ( (double) mand.isBelongs( xMin + ((double)x_*width)/2000, yMin + ((double)y_*width)/2000 ) / mand.getItetCount()))));
				end++;
				if(end == thread) 
					{
					ready = true;
					/*try {
						System.out.println("write "+sidey383.Frame.imagenumber);
						ImageIO.write(cImage, "png", new File("D:/mandelbrot/image_"+(sidey383.Frame.imagenumber++)+".png"));
					} catch (IOException e) {
						e.printStackTrace();x
					}*/
					}
				//System.out.println("end trhead number "+ number+" in "+ (System.currentTimeMillis() - time)+"ms" );
			}).start();
		}
	}
	
	public boolean isCalculate() 
	{
		return ready;
	}
	
	public int getDepth() 
	{
		return depth;
	}
	

}
