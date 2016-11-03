package ev3SearchObject;
import lejos.hardware.Audio;
import lejos.hardware.Sound;

import lejos.robotics.SampleProvider;

public class USLocalizer {
	public enum LocalizationType { FALLING_EDGE, RISING_EDGE };
	public static float ROTATION_SPEED = 100;
	public Sound audio;
	private Odometer odo;
	private SampleProvider usSensor;
	private float[] usData;
	private LocalizationType locType;
	
	public USLocalizer(Odometer odo,  SampleProvider usSensor, float[] usData, LocalizationType locType) {
		this.odo = odo;
		this.usSensor = usSensor;
		this.usData = usData;
		this.locType = locType;
	}
	
	public void doLocalization() {
		double [] pos = new double [3];
		double angleA = 0;
		double angleB = 0;
		double angle = 0;
		Navigation nav= new Navigation(odo);

		

			if (locType == LocalizationType.FALLING_EDGE) {
				
				// rotate the robot until it sees no wall
				while(getFilteredData()<=30){
					nav.setSpeeds(ROTATION_SPEED, -ROTATION_SPEED);
			
				}

				
				// keep rotating until the robot sees a wall, then latch the angle
				while(getFilteredData()> 30){
							
							
					nav.setSpeeds(ROTATION_SPEED, -ROTATION_SPEED);	
				}
				
				
				angleA = odo.getAng();
					
						
						
				// switch direction and wait until it sees no wall	
				while(getFilteredData()<=30){
					nav.setSpeeds(-ROTATION_SPEED, ROTATION_SPEED);	
				}
						
				// keep rotating until the robot sees a wall, then latch the angle

				while(getFilteredData()> 30){
					nav.setSpeeds(-ROTATION_SPEED, ROTATION_SPEED);	
				}
				
				angleB = odo.getAng();

				//Setting the north angle
				if(angleA<angleB) {
					angle = 45 - (angleA + angleB)/2.0;
				}
				else {
					angle = 225 - (angleA + angleB)/2.0;
				}
				
				// update the odometer position
				odo.setPosition(new double[] { 0.0, 0.0,
						(odo.getAng() + angle) }, new boolean[] { false,
						false, true });

				nav.turnTo(0, true);

					
					
				
			} 
			else {
					/*
					 * The robot should turn until it sees the wall, then look for the
					 * "rising edges:" the points where it no longer sees the wall.
					 * This is very similar to the FALLING_EDGE routine, but the robot
					 * will face toward the wall for most of it.
					 */
					
				// rotate the robot until it sees a wall
				while(getFilteredData()>=30){
					nav.setSpeeds(ROTATION_SPEED, -ROTATION_SPEED);
			
				}

				
				// keep rotating until the robot sees no wall, then latch the angle
				while(getFilteredData()< 30){
							
							
					nav.setSpeeds(ROTATION_SPEED, -ROTATION_SPEED);	
				}
				
				nav.setSpeeds(0, 0);
				angleA = odo.getAng();
					
						
						
				// switch direction and wait until it sees no wall	
				while(getFilteredData()>=30){
					nav.setSpeeds(-ROTATION_SPEED, ROTATION_SPEED);	
				}
						
				// keep rotating until the robot sees a wall, then latch the angle

				while(getFilteredData()< 30){
					nav.setSpeeds(-ROTATION_SPEED, ROTATION_SPEED);	
				}
				
				
				angleB = odo.getAng();

				//setting the north angle
				if(angleA<angleB) {
					angle = 45 - (angleA + angleB)/2.0;
				}
				else {
					angle = 225 - (angleA + angleB)/2.0;
				}
				angle -= 180;
				
				//updating the odometer position
				odo.setPosition(new double[] { 0.0, 0.0,
						(odo.getAng() + angle) }, new boolean[] { false,
						false, true });
				System.out.println("Turn to 270");
				nav.turnTo(0, true);
			}
			
			//Determine current location
			
			//get y
			nav.turnTo(270, true);
			odo.setPosition(new double[] { 0.0, getFilteredData()+11.5-30.48,
					0 }, new boolean[] { false,
					true, false });
			
			//get x
			nav.turnTo(180, true);
			odo.setPosition(new double[] { getFilteredData()+11.5-30.48, 0.0,
					0 }, new boolean[] { true,
					false, false });
			
			//Travel to 0,0 and turn to angle 0
			nav.travelTo(0, 0);
			nav.turnTo(0, true);

		}
		
		


	//getFilteredData() method
	private float getFilteredData() {
		usSensor.fetchSample(usData, 0);
		float distance = usData[0];
		
		if (100*distance > 50){
			distance = 50;
			
		}
		else {
			distance*=100;
		}
		return distance;
	}

}
