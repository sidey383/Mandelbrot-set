package sidey383;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import sidey383.Graphics.BlurRandomColorScheme;
import sidey383.Graphics.MandelbrotBox;

public class Frame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public static Frame instance;
	public static int imagenumber;
	public double x = 0;
	public double y = 0;
	public int width = 0;
	public int height = 0;
	public int scale = 400;
	MandelbrotBox box;
	Color[] colors = new Color[11];
	JLabel image;
	
	public Frame(double x, double y, int scale) {
		BlurRandomColorScheme colors = new BlurRandomColorScheme(383, 10);
		box = new MandelbrotBox(colors, 1000, 200);
		instance = this;
		this.x = x;
		this.y = y;
		this.scale = scale;
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
					getInstance().x += (height/4)/(double)getInstance().scale;
				if(e.getKeyCode() == 65)
					 getInstance().x -= (height/4)/(double)getInstance().scale;
				if(e.getKeyCode() == 87)
					getInstance().y += (width/4)/(double)getInstance().scale;
				if(e.getKeyCode() == 83)
					getInstance().y -= (width/4)/(double)getInstance().scale;
				if(e.getKeyCode() == 222)
					getInstance().scale = (int) ( getInstance().scale*1.5+1);
				if(e.getKeyCode() == 47)
					getInstance().scale = (int) (getInstance().scale/1.5-1);
					long time = System.currentTimeMillis();
					repaint();
					//System.out.println("x = " +getInstance().x+" y= "+getInstance().y+" scale = "+getInstance().scale+" "+(System.currentTimeMillis() - time)+" ms");
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
	}
	
	@Override
	public void repaint() {
		width = getWidth();
		height = getHeight();
		image.setIcon(new ImageIcon(box.getFrame(x, y, width, height, scale)));
		//super.repaint();
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
