package main;

import java.awt.Rectangle;

public class EventHandler {

	GamePanel gp;
	EventRect eventRect[][];
	
	int previousEventX, previousEventY;
	boolean canTouchEvent = true;
	
	public EventHandler(GamePanel gp) {
		this.gp = gp;
		
		eventRect = new EventRect[gp.maxWorldCol][gp.maxWorldRow];
		
		int col = 0;
		int row = 0;
		while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
			
			eventRect[col][row] = new EventRect();
			eventRect[col][row].x = 23;
			eventRect[col][row].y = 23;
			eventRect[col][row].width = 2;
			eventRect[col][row].height = 2;
			eventRect[col][row].eventRectDefaultX = eventRect[col][row].x;
			eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;
	
			col++;
			if(col == gp.maxWorldCol) {
				col = 0;
				row++;
			}
		}
		
	}
	
	public void checkEvent() {
		
		// Check if the player character is more than 1 tile away from the last event
		int xDistance = Math.abs(gp.player.worldX - previousEventX);
		int yDistance = Math.abs(gp.player.worldY - previousEventY);
		int distance = Math.max(xDistance, yDistance);
		if(distance > gp.tileSize) {
			canTouchEvent = true;
		}
			
		if(canTouchEvent == true) {
			if(hit(7, 9, "any") == true) {
				damagePit(7, 9, gp.dialogueState);
			}
			if(hit(8, 9, "right") == true) {
				healingPool(8, 9, gp.dialogueState);
			}
			if(hit(7, 16, "any") == true) {
				teleport(7, 16, gp.dialogueState);
			}
		}
		
	}
	
	public boolean hit(int col, int row, String reqDirection) {
		
		boolean hit = false;
		
		// GETTING PLAYER'S CURRENT SOLID AREA POSITIONS
		gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
		gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
		eventRect[col][row].x = col*gp.tileSize + eventRect[col][row].x;
		eventRect[col][row].y = row*gp.tileSize + eventRect[col][row].y;
		
		if(gp.player.solidArea.intersects(eventRect[col][row]) && eventRect[col][row].eventDone == false) {
			if(gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
				hit = true;
				
				previousEventX = gp.player.worldX;
				previousEventY = gp.player.worldY;
			}
		}
		
		gp.player.solidArea.x = gp.player.solidAreaDefaultX;
		gp.player.solidArea.y = gp.player.solidAreaDefaultY;
		eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
		eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;
		
		return hit;
	}
	
	public void damagePit(int col, int row, int gameState) {
		
		gp.gameState = gameState;
		gp.ui.currentDialogue = "You fall into a pit!";
		gp.player.life-=1;
//		eventRect[col][row].eventDone = true;
		canTouchEvent = false;
	}
	
	public void healingPool(int col, int row, int gameState) {
		if(gp.keyH.F_Pressed == true) {
			gp.gameState = gp.dialogueState;
			gp.ui.currentDialogue = "You drank the lake's blessed water.";
			gp.player.life = gp.player.maxLife;
		}
	}
	
	public void teleport(int col, int row, int gameState) {
		
		gp.gameState = gameState;
		gp.ui.currentDialogue = "You are teleported!";
		gp.player.worldX = gp.tileSize*34;
		gp.player.worldY = gp.tileSize*16;
	}
}
