package sidey383.Graphics;

import java.util.Random;

public class BlurRandomColorScheme implements ColorScheme {

	private int[] colors = new int[256];
	private int colorCount;
	private long seed;
	
	/**
	 * @param colorCount Count of colors. Other colors will be created as the average of the specified ones.
	 * @param seed Seed for generate colors.
	 */
	public BlurRandomColorScheme(long seed, int colorCount) {
		this.colorCount = colorCount;
		this.seed = seed;
		Random rand = new Random(seed);
		for(int i = 0; i < colorCount; i++)
			colors[i] = rand.nextInt(16777216);
	}
	
	/**
	 * @param colorCount > 1. Count of colors. Other colors will be created as the average of the specified ones.
	 */
	public BlurRandomColorScheme(int colorCount) {
		this.colorCount = colorCount;
		this.seed = System.currentTimeMillis();
		Random rand = new Random(seed);
		for(int i = 0; i < colorCount; i++) {
			colors[ (int) ( 256 * ( (double) i / (colorCount - 1) ) ) ] = rand.nextInt(16777216);	
			if(i>0) 
			{
				int a = (int) ( 256 * ( (double) (i-1) / (colorCount - 1) ) );
				int b =  (int) ( 256 * ( (double) i / (colorCount - 1) ) );
				for(int j = a+1; j < b; j++ ) 
				{
					colors[j] = (colors[a]&0xff)*j+(colors[b]&0xff)*(b-a-j)/(b-a) + 0xff * (colors[a]>>8&0xff)*j+(colors[b]>>8&0xff)*(b-a-j)/(b-a) + 0xffff * (colors[a]>>16&0xff)*j+(colors[b]>>16&0xff)*(b-a-j)/(b-a);
				}
			}
		}
	}
	
	@Override
	public int getColor(byte number) {
		int num = number<0?256+number:number;
		return colors[num];
	}
	
	public long getSeed() 
	{
		return seed;
	}
	
	public int getColorCount() 
	{
		return colorCount;
	}

}
