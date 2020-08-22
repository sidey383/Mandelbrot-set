package sidey383.math;

public class Mandelbrot {

	private int iterCount = 0;
	
	
	public Mandelbrot(int iterCount) 
	{
		this.iterCount = iterCount;
	}
	
	/**
	  @return step at which it became clear that the number does not belong to the set or iterCount if number belong to the set.
	 **/
	public int isBelongs(double x, double y) 
	{
		if(x>2||y>2) return 0;
		if(((x-0.25)*(x-0.25)+y*y+0.5*(x-0.25))*((x-0.25)*(x-0.25)+y*y+0.5*(x-0.25)) - 0.25*((x-0.25)*(x-0.25)+y*y) < 0) return iterCount;
		double zx = x;
		double zy = y;
		for(int i = 0; i<iterCount; i++) {
			if(zx*zx+zy*zy>4) return i;
			double x_ = zx*zx - zy*zy + x;
			zy = 2*zx*zy+y;
			zx = x_;
		}
		return iterCount;
	}
	
	public int getItetCount() 
	{
		return iterCount;
	}
	
}
