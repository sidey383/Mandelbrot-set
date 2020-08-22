package sidey383.frame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;

import sidey383.graphics.*;
import sidey383.math.Mandelbrot;

public class Frame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public static final int ITERATION_COUNT = 1000;
	public static final double MEMORY_OVERFLOW = 0.95;
	
	public static Mandelbrot mandelbrot;
	public static ColorScheme colors;
	
	public static Frame instance;
	public double x = 0;
	public double y = 0;
	public long scale = 400;
	public static ArrayList<BufferCanvas> canvas = new ArrayList<BufferCanvas>();
	private BufferCanvas lastCanvas;
	public static boolean calculated = false;
	private long updateTime = System.currentTimeMillis();
	private File lastDir = new File(".");
	JLabel image;
	
	public Frame(double x, double y, int scale) {
		instance = this;
		this.x = x;
		this.y = y;
		this.scale = scale;
		//colors = new BlurRandomColorScheme(383, 20, ITERATION_COUNT);
		colors = new RGBBlurColor(ITERATION_COUNT);
		mandelbrot = new Mandelbrot(ITERATION_COUNT);
		this.setSize(1920, 1080);
		
		image = new JLabel(new ImageIcon(new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_3BYTE_BGR)));
		image.setLocation(0, 0);
		
		add(image);
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("mandelbrot");
		this.setVisible(true);
		setExtendedState(MAXIMIZED_BOTH);
		
		MListener listener = new MListener();
		addMouseListener(listener);
		addMouseWheelListener(listener); 
		addKeyListener(listener);
		
		startUpdates(100);
	}
	/**
	 * to start recurring updates
	 * @param ms the minimum update time in milliseconds
	 */
	public void startUpdates(long ms) 
	{
		new Thread(()->
		{
			while(true) 
			{
				if(ms < System.currentTimeMillis() - updateTime)
					update();
				try {
					Thread.sleep(ms/2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	/**
	 * Update update sreen image
	 */
	public void update() 
	{
		updateTime = System.currentTimeMillis();
		double x1 = x - (double)getWidth()/2/scale;
		double x2 = x + (double)getWidth()/2/scale;
		double y1 = y - (double)getHeight()/2/scale;
		double y2 = y + (double)getHeight()/2/scale;
		double length = Math.max((double)getWidth()/scale, (double)getHeight()/scale);
		BufferCanvas can = lastCanvas;
		
		if(memoryOverflow())
			if(canvas.size() > 0)
				canvas.remove(0); 
			
		if(lastCanvas == null 
			|| x1 < lastCanvas.getX() 
			|| y1 < lastCanvas.getY() 
			|| lastCanvas.getY()+lastCanvas.getWidth() < y2
			|| lastCanvas.getX()+lastCanvas.getWidth() < x2
			|| BufferCanvas.BUFFER_SIZE / lastCanvas.getWidth() < scale ) 
		{
			lastCanvas = null;
			for(BufferCanvas canv: canvas)
				if(conatin(x1, y1, x2, y2, canv.getX(), canv.getY(), canv.getWidth()))
					if((lastCanvas==null && BufferCanvas.BUFFER_SIZE / canv.getWidth() > scale) || (lastCanvas!=null && BufferCanvas.BUFFER_SIZE / lastCanvas.getWidth() < BufferCanvas.BUFFER_SIZE / canv.getWidth())) 
					{
						lastCanvas = canv;
					}
			if(lastCanvas == null && !calculated) 
			{
				Frame.calculated = true;
				new BufferCanvas(x-length, y-length, length*2);
			}
		}
		if(lastCanvas == null && can!=null)
			lastCanvas =can;
		if(lastCanvas !=null)
			draw(lastCanvas);
	}
	
	/**
	 * @param canvas Canvas for drawing on the screen
	 */
	public void draw(BufferCanvas canvas) 
	{
		BufferedImage bimage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		String text = "x = " +this.x+" y = "+this.y+" width = "+(double)getWidth()/scale;
		Graphics gr = bimage.getGraphics();
		gr.drawImage(canvas.getImage(), 0, 0, bimage.getWidth(), bimage.getHeight(),canvas.getPixelX(this.x - ((double)getWidth())/2/scale) , canvas.getPixelY(this.y + ((double)getHeight())/2/scale), canvas.getPixelX(this.x + ((double)getWidth())/2/scale) , canvas.getPixelY(this.y - ((double)getHeight())/2/scale), null);
		gr.setColor(new Color(255*255*255));
		bimage.getGraphics().drawString(text, 20, 40);
		image.setIcon(new ImageIcon(bimage));
	}
	
	public void screenshot() 
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Сохранение файла");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.png",".");
        fileChooser.setFileFilter(filter);
        fileChooser.setSelectedFile(new File(lastDir, "x=" + x+" y=" +y+" width=" + (double)getWidth()/scale));
        fileChooser.showOpenDialog(this);
        File f = fileChooser.getSelectedFile();
        if(lastCanvas != null)
			try {
				BufferedImage rotated = new BufferedImage(lastCanvas.getImage().getWidth(),lastCanvas.getImage().getHeight() , lastCanvas.getImage().getType());
				rotated.getGraphics().drawImage(lastCanvas.getImage(), 0, 0, lastCanvas.getImage().getWidth(), lastCanvas.getImage().getHeight(), 0, lastCanvas.getImage().getHeight(), lastCanvas.getImage().getWidth(), 0, null);
				ImageIO.write(rotated, "png", new File(f.getAbsolutePath()+".png"));
				lastDir = f.getParentFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public static boolean conatin(double x1, double y1, double x2, double y2, double x1_, double y1_, double width) 
	{
		return !(x1_ > x1 || y1_ > y1 || x1_ + width < x2 || y1_ + width < y2) ;
	}
	
	public boolean memoryOverflow() 
	{
		return (double)(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/Runtime.getRuntime().maxMemory() > MEMORY_OVERFLOW;
	}
	
	public static void main(String[] args) 
	{
		@SuppressWarnings("unused")
		Frame mainFrame = new Frame(0 , 0, 500);
	}
	
	public static Frame getInstance() 
	{
		return instance;
	}
}
