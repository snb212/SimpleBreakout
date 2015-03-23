package org.game.objects;

import java.util.ArrayList;
import org.newdawn.slick.geom.Rectangle;

public class BreakoutMouseBar extends Rectangle{

	//avg speed that the bar is moving 
	private float velocity;
	private float lastLoc;
	private ArrayList<Float> speeds;
	
	public BreakoutMouseBar(float x, float y, float width, float height ){
		super(x, y, width, height);
		
		speeds = new ArrayList<Float>();
		lastLoc = this.getCenterX();
	}

	public float getVelocity() {
		if (velocity > 10){
			return 10;
		} else if (velocity < -10){
			return -10;
		}
		
		return velocity;
	}

	public ArrayList<Float> getSpeed() {
		return speeds;
	}

	public void updateLocation(float location, int delta) {
		float speed = (location - lastLoc) / delta;
		System.out.println("loc: " + location + " lastLoc: " + lastLoc);
		
		speeds.add(speed);
		if(speeds.size() > 15){
			speeds.remove(0);
		}
		
		float total = 0;
		for(Float dataPoint : speeds){
			total += dataPoint;
		}
		
		velocity = total / (float) speeds.size();
		
		for(Float printSpeed : speeds){
			System.out.print(printSpeed + " , ");	
		}
		System.out.println();
		
		System.out.println("Mouse Bar Velocity: " + velocity);
		
		lastLoc = location;
	}
	
	
}
