package sidey383.math;

public class Methods {

	public static int twoPow(int p) 
	{
		int t = 1;
		for(int i = 0; i < p;i++)
			t = t*2;
		return t;
	}
	
	public static int upLog2(int p) 
	{
		for(int i = 0; true;i++) {
			if(p == 0) 
				return i;
			p=p>>1;
		}
	}
	
	public static int getChunckY(double y, int depth) 
	{
		return 0;
	}
	
}
