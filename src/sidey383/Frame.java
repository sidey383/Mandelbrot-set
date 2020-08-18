package sidey383;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import sidey383.math.Mandelbrot;

public class Frame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public static Frame instance;
	public double x = 0;
	public double y = 0;
	public int width = 0;
	public int height = 0;
	public double scale = 400;
	public int colorCount = 0;
	public int iterCount = 0;
	private Mandelbrot mandelbrot;
	Color[] colors = new Color[11];
	JLabel image;
	
	public Frame(double x, double y, double scale) {
		instance = this;
		this.x = x;
		this.y = y;
		this.scale = scale;
		iterCount = 5000;
		mandelbrot = new Mandelbrot(iterCount);
		colorCount = 100;
		colors = new Color[colorCount+1];
		Random rand = new Random(383);
		colors[0] = new Color(rand.nextInt(200),rand.nextInt(200),rand.nextInt(200));
		for(int i = 1; i<10;i++) 
		{
			colors[i*10] = new Color(rand.nextInt(200),rand.nextInt(200),rand.nextInt(200));
			for(int j = 0; j < 10;j++)
				colors[i*10-10+j] = new Color((colors[i*10].getRGB()*(10-j)+colors[i*10-10].getRGB()*(j))/10);
		}
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("mandelbrot");
		this.setSize(1920, 1080);
		image = new JLabel(new ImageIcon(new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_3BYTE_BGR)));
		image.setLocation(0, 0);
		repaint();
		add(image);
		this.setVisible(true);
		setExtendedState(MAXIMIZED_BOTH);
		addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == 68)
					getInstance().x += (height/4)/getInstance().scale;
				if(e.getKeyCode() == 65)
					 getInstance().x -= (height/4)/getInstance().scale;
				if(e.getKeyCode() == 87)
					getInstance().y += (width/4)/getInstance().scale;
				if(e.getKeyCode() == 83)
					getInstance().y -= (width/4)/getInstance().scale;
				if(e.getKeyCode() == 222)
					getInstance().scale = getInstance().scale*1.5;
				if(e.getKeyCode() == 47)
					getInstance().scale = getInstance().scale/1.5;
				if(e.getKeyCode() == 82) {
					repaint();
					System.out.println("x = " +getInstance().x+" y= "+getInstance().y+" scale = "+getInstance().scale);
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
	}
	
	@Override
	public void repaint() {
		try {
			width = getWidth();
			height = getHeight();
			BufferedImage im =  new BufferedImage(width,height, BufferedImage.TYPE_3BYTE_BGR);
			Graphics g = im.getGraphics();
			int[][] xbuf = new int[colorCount][width*height];
			int[][] ybuf = new int[colorCount][width*height];
			int[] count = new int[colorCount];
			for(int i = 0;i<11;i++)
				count[i] = 0;
			for(int w = 0;w<width;w++)
				for(int h = 0; h< height; h++) 
				{
					int dot = mandelbrot.isBelongs((w-width/2)/scale+x, (height/2-h)/scale+y);
					dot = (int)(((double) dot)/iterCount*(colorCount-1));
					xbuf[dot][count[dot]] = w;
					ybuf[dot][count[dot]] = h;
					count[dot]++;
				}
			for(int i = 0; i< colorCount;i++) {
				g.setColor(colors[i]);
				for(int j = 0; j<count[i];j++) 
				{
					g.drawOval(xbuf[i][j], ybuf[i][j], 0, 0);
				}
			}
			image.setIcon(new ImageIcon(im));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) 
	{
		@SuppressWarnings("unused")
		Frame mainFrame = new Frame(0,0, 600);
	}
	
	public static Frame getInstance() 
	{
		return instance;
	}
	
}
