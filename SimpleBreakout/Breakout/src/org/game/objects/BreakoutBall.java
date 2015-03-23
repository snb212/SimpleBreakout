package org.game.objects;

import org.newdawn.slick.geom.Circle;

public class BreakoutBall extends Circle{

	private float vectorX;
	private float vectorY;
	private float positionX;
	private float positionY;
	private int id;
	private boolean falling;
	private int maxSpeed = 2;
	
	public BreakoutBall(float positionX, float positionY, float vectorX, float vectorY, int id, boolean falling){
		super(positionX, positionY, 10);
		this.vectorX = vectorX;
		this.vectorY = vectorY;
		this.positionX = positionX;
		this.positionY = positionY;
		this.id = id;
		this.falling = falling;
	}

	public float getVectorX() {
		if (vectorX > maxSpeed){
			return maxSpeed;
		} else if (vectorX < -maxSpeed){
			return -maxSpeed;
		}
		return vectorX;
	}

	public void setVectorX(float vectorX) {
		this.vectorX = vectorX;
	}

	public float getVectorY() {
		if (vectorY > maxSpeed){
			return maxSpeed;
		} else if (vectorY < -maxSpeed){
			return -maxSpeed;
		}
		return vectorY;
	}

	public void setVectorY(float vectorY) {
		this.vectorY = vectorY;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isFalling() {
		return falling;
	}

	public void setFalling(boolean falling) {
		this.falling = falling;
	}

	public float getPositionX() {
		return positionX;
	}

	public void setPositionX(float positionX) {
		this.positionX = positionX;
		this.setX(positionX);
	}

	public float getPositionY() {
		return positionY;
	}

	public void setPositionY(float positionY) {
		this.positionY = positionY;
		this.setY(positionY);
	}
	
}
