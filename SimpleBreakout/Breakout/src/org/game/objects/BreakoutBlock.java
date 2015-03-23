package org.game.objects;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;

public class BreakoutBlock extends Rectangle {

	float positionX;
	float positionY;
	int blockId;
	Color color;
	
	public BreakoutBlock(float positionX, float positionY, float width, float height, int blockId){
		super(positionX, positionY, width, height);
		this.positionX = positionX;
		this.positionY = positionY;
		this.blockId = blockId;
		this.color = Color.gray;
		
		System.out.println("Building bar at x: " + positionX + " y:" + positionY + " w:" + width + " h:" + height);
	}
	
	public BreakoutBlock(float positionX, float positionY, float width, float height, int blockId, Color color){
		super(positionX, positionY, width, height);
		this.positionX = positionX;
		this.positionY = positionY;
		this.blockId = blockId;
		this.color = color;
		
		System.out.println("Building bar at x: " + positionX + " y:" + positionY + " w:" + width + " h:" + height);
	}

	public int getBlockId() {
		return blockId;
	}

	public void setBlockId(int blockId) {
		this.blockId = blockId;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
}
