package application;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class HomePane extends BorderPane {
	Label gameTitle,musicSwitch,timerSwitch;
	VBox switchBox;
	HBox musicBox, timerBox;
	Image[] homeImg;	// 0-title image, 1-music image, 2-timer image
	ImageView[] homeView;
	ToggleGroup musicGroup,timerGroup;
	RadioButton[] musicSwitches, timerSwitches;
	String[] switchStatus;	//indicate the 2 switches status,0-music,1-countdown timer
	Button newGameBtn;
	
	public HomePane() {
		homeImg = new Image[4];
		homeView = new ImageView[4];
		homeImgInit();
		
		gameTitle = new Label("MineSweeper by Renyi");
		gameTitle.setGraphic(homeView[0]);
		gameTitle.setFont(Font.font("Arial",20));
		musicSwitch = new Label("Music effect          ", homeView[1]);
		timerSwitch = new Label("CountDown mode", homeView[2]);
		switchBox = new VBox();
		musicBox = new HBox();
		timerBox = new HBox();
		musicSwitches = new RadioButton[2];
		timerSwitches = new RadioButton[2];
		musicGroup = new ToggleGroup();
		timerGroup = new ToggleGroup();
		newGameBtn = new Button("Go!", homeView[3]);
		
		newGameBtn.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
		switchStatus = new String[2];
		switchesInit();
		layoutInit();
	}

	private void layoutInit() {
		// TODO Auto-generated method stub
		this.setTop(gameTitle);
		this.setCenter(switchBox);
		this.setBottom(newGameBtn);
		this.setAlignment(newGameBtn, Pos.CENTER);
		this.setAlignment(gameTitle, Pos.CENTER);
		this.setAlignment(switchBox, Pos.CENTER);
		musicBox.setAlignment(Pos.CENTER);
		timerBox.setAlignment(Pos.CENTER);
	}

	private void switchesInit() {
		// TODO Auto-generated method stub
		musicBox.getChildren().add(musicSwitch);
		timerBox.getChildren().add(timerSwitch);
		musicSwitches[0] = new RadioButton("On");
		musicSwitches[0].setUserData("On");
		musicSwitches[0].setToggleGroup(musicGroup);
		musicSwitches[0].setSelected(true);
		
		musicSwitches[1] = new RadioButton("Off");
		musicSwitches[1].setUserData("Off");
		musicSwitches[1].setToggleGroup(musicGroup);
		
		timerSwitches[0] = new RadioButton("On");
		timerSwitches[0].setUserData("On");
		timerSwitches[0].setToggleGroup(timerGroup);
		timerSwitches[0].setSelected(true);
		
		timerSwitches[1] = new RadioButton("Off");
		timerSwitches[1].setUserData("Off");
		timerSwitches[1].setToggleGroup(timerGroup);
		for(int i=0;i<2;i++) {
			musicBox.getChildren().add(musicSwitches[i]);
			timerBox.getChildren().add(timerSwitches[i]);
		}
		switchBox.getChildren().addAll(musicBox,timerBox);
	}

	private void homeImgInit() {
		// TODO Auto-generated method stub
		for(int i=0; i<homeImg.length; i++) {
				homeImg[i] = new Image("file:img/homePaneImg/"+String.valueOf(i)+".png");
				homeView[i] = new ImageView(homeImg[i]);
				if(i==0) {
					homeView[i].setFitWidth(80);	//title img bigger
				}else {
					homeView[i].setFitWidth(30);
				}
				homeView[i].setPreserveRatio(true);
		}
	}
	
	
}
