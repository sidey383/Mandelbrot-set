package sidey383.graphics;

public class RGBBlurColor implements ColorScheme {
	
	private int max = 0;
	
	public RGBBlurColor(int max) {
		this.max = max;
	}

	@Override
	public int getColor(int number) {
		if(number >= max || number < 0)
			return 0;
		if((max-number) < max/3) return (max-number);
		return (max - number) * 0xffffff / max;
	}

}
