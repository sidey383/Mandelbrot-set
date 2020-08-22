package sidey383.graphics;

import java.util.Random;

public class BlurRandomColorScheme implements ColorScheme {

	private int[] colors;
	private int colorCount;
	private long seed;
	
	/**
	 * @param colorCount Count of colors. Other colors will be created as the average of the specified ones.
	 * @param seed Seed for generate colors.
	 */
	public BlurRandomColorScheme(long seed, int colorCount, int arraySize) {
		colors = new int[arraySize];
		this.colorCount = colorCount;
		this.seed = seed;
		Random rand = new Random(seed);
		for(int i = 0; i < colorCount; i++) {
			colors[ (int) ( (arraySize-1) * ( (double) i / (colorCount - 1) ) ) ] = rand.nextInt(16777216);	
			if(i>0) 
			{
				int a = (int) ( (arraySize-1) * ( (double) (i-1) / (colorCount - 1) ) );
				int b =  (int) ((arraySize-1) * ( (double) i / (colorCount - 1) ) );
				for(int j = a+1; j < b; j++ ) 
				{
					colors[j] = (colors[a]&0xff)*j+(colors[b]&0xff)*(b-a-j)/(b-a) + 0xff * (colors[a]>>8&0xff)*j+(colors[b]>>8&0xff)*(b-a-j)/(b-a) + 0xffff * (colors[a]>>16&0xff)*j+(colors[b]>>16&0xff)*(b-a-j)/(b-a);
				}
			}
		}
	}
	
	@Override
	public int getColor(int num) {
		if(num < colors.length && num >= 0)
			return colors[num];
		return 0;
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
