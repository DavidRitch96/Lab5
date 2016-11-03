package ev3SearchObject;

import lejos.robotics.SampleProvider;

public class ObjectDistance {
	public static double BLOCK_DIST = 3, NOISE_MARGIN = 3;
	private SampleProvider usSensor;
	private float[] usData;
	
	public ObjectDistance(SampleProvider usSensor, float[] usData){
		this.usSensor = usSensor;
		this.usData = usData;
	}
	
	public boolean isClose(){
		boolean isClose = false;
		int count = 0;
		
		for (int i=0; i<10; i++){
			if (getFilteredData() < BLOCK_DIST + NOISE_MARGIN){
				count++;
			}
			if (count == 5){
				isClose = true;
			}
		}
		return isClose;
	}
	
	
	public boolean isFar(){
		boolean isFar = false;
		int count = 0;
		
		for (int i=0; i<10; i++){
			if (getFilteredData() < BLOCK_DIST+50 + NOISE_MARGIN){
				count++;
			}
			if (count == 5){
				isFar = true;
			}
		}
		return isFar;
	}
	
	public float getFilteredData() {
		usSensor.fetchSample(usData, 0);
		return usData[0]*100;
	}
	
	
}
