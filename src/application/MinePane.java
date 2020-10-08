package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javafx.scene.paint.Color;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

class CellBtn extends Button{
	int labelStatus; 	//1:not labeled  -1:labeled
	int mineInfo;		//how many mines surrounds(>=9, indicate this is a mine)
	String nowStyle;	//what's the style of the button
	String clickedStyle = "-fx-background-color:#999999;";		//if clikced set the btn's bg to dark grey
	String labeledStyle = "-fx-background-color:#33FFFF;";		//if labeled set the btn's bg to blue
	String noStyle = "";	//defualt bg color
	public CellBtn(String init) {
		labelStatus = 1;
		mineInfo = 0;
		nowStyle = "";
		this.setText(init);
	}
}

public class MinePane extends GridPane{
	
	Random r;  //to control the process of placing mines
	int mines;	//the number of mines
	int rows,cols;   //the mine's layouts's width and height
	CellBtn[][] btnGraph;	//the mine's array
	List <String> mineList;		//indicate the mine's info
	ArrayList<CellBtn> mineBtnList;		//all the mine button in a list
	ArrayList<CellBtn> allBtnList;		//all the button in a list
	
	int[][] route = {{0,1},{1,0},{0,-1},{-1,0},{1,-1},{-1,1},{-1,-1},{1,1}};	//the neighbor of a button 
	
	
	
	public MinePane(int x, int y, int numbers) {
		
		r = new Random();
		rows = x;
		cols = y;
		mines = numbers;
		mineList = new ArrayList<String>();
		mineBtnList = new ArrayList<CellBtn>();
		allBtnList = new ArrayList<CellBtn>();
		btnGraph = new CellBtn[x][y];
		
		//initialize of all the btns
		for(int i=0; i<rows; i++) {
			for(int j=0; j<cols; j++) {
				btnGraph[i][j] = new CellBtn("  ");
				btnGraph[i][j].setMinSize(10, 10);
				btnGraph[i][j].setPrefSize(30, 30);
				allBtnList.add(btnGraph[i][j]);
			}
		}
		
		setVgap(2);
		setHgap(2);
		this.setAlignment(Pos.CENTER);
		
		// randomly generate the numbers's mines' positions
		while(mines!=0) {
			int mineX = r.nextInt(rows);
			int mineY = r.nextInt(cols);
			if(btnGraph[mineX][mineY].mineInfo != 9) {
				btnGraph[mineX][mineY].mineInfo = 9;
				mines--;
				mineList.add(String.valueOf(mineX)+","+String.valueOf(mineY));
				mineBtnList.add(btnGraph[mineX][mineY]);
			}
		}
		System.out.println(mineList.size());
		System.out.println(mineList);

		//calculate the mine's surrounding cell's info
		for(int i=0; i<rows; i++) {
			for(int j=0; j<cols; j++) {
				if (btnGraph[i][j].mineInfo>8) {
					for(int k=0; k<8; k++) {
						if(i+route[k][0]>=0 && i+route[k][0]<rows && j+route[k][1]>=0 && j+route[k][1]<cols) {
							btnGraph[i+route[k][0]][j+route[k][1]].mineInfo++;
						}
					}
				}
			}
		}
		
		//set the mine pane's btn
		for(int i=0;i<rows;i++) {
			for(int j = 0; j <cols; j++) {
				this.add(btnGraph[i][j], j, i);
			}
		}
	}
	
	
	//stack overflow error, open a 0 button's neighbor buttons
	public void neighborClear(int x, int y) {
		for(int i=0;i<8;i++) {
			if(x+route[i][0]>=0 && x+route[i][0]<rows && y+route[i][1]>=0 && y+route[i][1]<cols && btnGraph[x + route[i][0]][y + route[i][1]].mineInfo<9 && btnGraph[x + route[i][0]][y + route[i][1]].getText().equals("  ") ) {
				btnGraph[x + route[i][0]][y + route[i][1]].setText(String.valueOf(btnGraph[x + route[i][0]][y + route[i][1]].mineInfo));
				btnGraph[x + route[i][0]][y + route[i][1]].setStyle(btnGraph[x + route[i][0]][y + route[i][1]].clickedStyle);
				if(btnGraph[x + route[i][0]][y + route[i][1]].mineInfo == 0) {
					neighborClear(x + route[i][0], y + route[i][1]);
				}
			}
		}
	}
	
	
	//clear all the cell's button if lost
	public void lostClear() {
		// TODO Auto-generated method stub
		for(int i=0;i<this.rows;i++) {
			for(int j=0;j<this.cols;j++) {
				if(btnGraph[i][j].mineInfo>8) {
					this.btnGraph[i][j].setText("💣");
					this.btnGraph[i][j].setStyle("-fx-background-color: #ff0000;");  //become red
				}else {
					this.btnGraph[i][j].setText(Integer.toString(this.btnGraph[i][j].mineInfo));
				}
				this.btnGraph[i][j].setDisable(true);
			}
		}
	}

	//clear all the cell btn if win
	public void winClear() {
		// TODO Auto-generated method stub
		for(int i=0;i<this.rows;i++) {
			for(int j=0;j<this.cols;j++) {
				if(btnGraph[i][j].mineInfo>8) {
					this.btnGraph[i][j].setText("🚩");
					this.btnGraph[i][j].setStyle("-fx-background-color: #00ff00;");  //become green 
				}else {
					this.btnGraph[i][j].setText(Integer.toString(this.btnGraph[i][j].mineInfo));
				}
				this.btnGraph[i][j].setDisable(true);
			}
		}
	}	
}