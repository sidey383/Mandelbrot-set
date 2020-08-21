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
