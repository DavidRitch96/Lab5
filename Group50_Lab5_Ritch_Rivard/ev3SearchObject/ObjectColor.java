package ev3SearchObject;

import ev3SearchObject.Navigation;
import lejos.hardware.Audio;
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class ObjectColor {
	private SampleProvider colorValue;
	private float[] colorData;
	private TextLCD t;
	private Odometer odo;
	private ObjectDistance objectDistance;
	private EV3LargeRegulatedMotor rotMotorLeft;
	private EV3LargeRegulatedMotor rotMotorRight;
	public static float ROTATION_SPEED = 100;
	
	
	public ObjectColor(SampleProvider colorValue, float[] colorData, TextLCD t, Odometer odo, ObjectDistance objectDistance, EV3LargeRegulatedMotor rotMotorLeft, EV3LargeRegulatedMotor  rotMotorRight){ //TextLCD t){
		this.colorValue = colorValue;
		this.colorData = colorData;
		this.t = t;
		this.odo = odo;
		this.objectDistance=objectDistance;
		this.rotMotorLeft=rotMotorLeft;
		this.rotMotorRight=rotMotorRight;
	}

	//This is the code for part 1
	public void isBlock(){

		//Get the color	
		getColor();
		
		//If the color is blue, print Block
		if (colorData[1] > colorData[0] && colorData[1] > colorData[2]){
			System.out.println("Block");
		}
		else{
			System.out.println("Not Block");
		}
		
	}
	
	/*This is for part 2. The robot is currently able to detect the styrofoam block and
	* the block of wood, and if able to capture the styrofoam block and to bring it to the final
	* square. However, we had trouble setting up the scan to navigate the robot and have run out
	* of  time to work on this lab.
	*/

	
	//this is the function used for part 2
	public void isBlock2(){
		double dist;
		Navigation nav= new Navigation(odo);
		//This scans for objects with the ultrasonic sensor and if it's really close then it will determine if it is a blue block or not then bring the blue block to the correct place.
		while(true){
				
			dist = objectDistance.getFilteredData();
			
			//if the object is close enough to detect if it is a color or not
			if(objectDistance.isClose()) {
				getColor();
				System.out.println(dist);
					if (colorData[1] > colorData[0] && colorData[1] > colorData[2]){
						Sound.beep();
						nav.setSpeeds(0, 0);
						lowerArm();
						nav.travelTo(60.96, 60.96);
						nav.turnTo(45,true);
						Sound.beep();
						Sound.beep();
						Sound.beep();
						break;
					}
					else{
						
						nav.setSpeeds(0, 0);
						Sound.beep();
						Sound.beep();
						nav.turnTo(odo.getAng()+90, true);
						nav.goForward(30);
						nav.turnTo(odo.getAng()-90, true);
						nav.goForward(30);
					}
				
			}
			//if the object is located but it is too far to read the color of it
			else if(dist<80) {
				
				nav.setSpeeds(200, 200);
			}
			//if no object is located so it needs to turn in place to find an object
			else {
				nav.setSpeeds(-100,100);
			}
		}
	}

	//this gets the color reading from the light sensor
	private void getColor(){
		colorValue.fetchSample(colorData, 0);
	}
	
	//this lowers the hoop so it can capture the blue block
	private void lowerArm(){
		rotMotorLeft.rotate(90);
		rotMotorRight.rotate(90);	
		
	}	

}
