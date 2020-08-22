package sidey383.frame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MListener implements MouseListener, MouseWheelListener, KeyListener {
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		double multipler = 1;
		if(e.getWheelRotation()<0)
			for(int i = 0; i<-e.getWheelRotation();i++) 
				multipler*=1.1;
		else
			for(int i = 0; i<e.getWheelRotation();i++) 
				multipler/=1.1;
		changeScale(multipler, e.getX(), e.getY());
	}
	
	public void changeScale(double multipler, int mouseX, int mouseY) 
	{
		long scale = Frame.getInstance().scale;
		if(scale*multipler < 20) return;
		double x = Frame.getInstance().x;
		double y = Frame.getInstance().y;
		int width = Frame.getInstance().getWidth();
		int height = Frame.getInstance().getHeight();
		mouseX = mouseX - width/2;
		mouseY = height/2 - mouseY;
		x = x - (double)mouseX/scale*(1-multipler);
		y = y - (double)mouseY/scale*(1-multipler);
		scale = (long) (scale*multipler);
		Frame.getInstance().scale = scale;
		Frame.getInstance().x = x;
		Frame.getInstance().y = y;
		Frame.getInstance().update();
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == 69) // key e Realesed
			Frame.getInstance().screenshot();
		}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == 68) // key d Pressed
			Frame.getInstance().x += (Frame.getInstance().getWidth()/80)/(double)Frame.getInstance().scale;
		if(e.getKeyCode() == 65) // key a Pressed
			Frame.getInstance().x -= (Frame.getInstance().getWidth()/80)/(double)Frame.getInstance().scale;
		if(e.getKeyCode() == 87) // key w Pressed
			Frame.getInstance().y += (Frame.getInstance().getHeight()/80)/(double)Frame.getInstance().scale;
		if(e.getKeyCode() == 83) // key s Pressed
			Frame.getInstance().y -= (Frame.getInstance().getHeight()/80)/(double)Frame.getInstance().scale; 
		if(e.getKeyCode() == 222) // key ' Pressed
			Frame.getInstance().scale = (long) ( Frame.getInstance().scale*1.5);
		if(e.getKeyCode() == 47) // key / Pressed
			Frame.getInstance().scale = (long) ( Frame.getInstance().scale/1.5);
		Frame.getInstance().update();
	}

}
