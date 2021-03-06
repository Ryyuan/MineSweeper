package application;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GamePane extends BorderPane {
	
	HBox topBar,btmBar;
	MinePane minePane;
	Button restartBtn, scanBox, settingBtn;
	Label Mines,Chances,Status,Time;
	Label minesShow, chancesShow, statusShow,timeShow;
	String[] statusLabel= {"Going","Lost","Win"};
	Image imgRestart,imgSettings;
	ImageView viewRestart,viewSettings;
	Image[] topBarImg,scanImg;
	ImageView[] topBarView,scanView;
	Media bgMedia,btnMedia;
	MediaPlayer bgMediaPlayer, btnMediaPlayer;
	Timer mTimer;
	TimerTask mTimerTsk;
	long startTime;
	SimpleDateFormat format = new SimpleDateFormat("mm:ss");
	

	public GamePane(int x, int y, int numbers) {
		// all the layouts in the pane
		topBarImg = new Image[4];
		topBarView = new ImageView[4];
		scanImg = new Image[4];
		scanView = new ImageView[4];
		ImageInit();
		
		minePane = new MinePane(x, y, numbers);
		topBar = new HBox();
		btmBar = new HBox();
		restartBtn = new Button("Restart",viewRestart);
		settingBtn = new Button("Setting",viewSettings);
		scanBox = new Button("",scanView[0]);
		scanBox.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
		Mines = new Label("Mines:", topBarView[0]);
		Chances = new Label("Chances:", topBarView[1]);
		Status = new Label("Status:", topBarView[2]);
		Time = new Label("Time:", topBarView[3]);
		minesShow = new Label();
		chancesShow =new Label();
		statusShow = new Label();
		timeShow = new Label();
		
		
		// whole layout
		this.setTop(topBar);
		this.setCenter(minePane);
		this.setBottom(btmBar);
		this.setAlignment(topBar, Pos.CENTER);
		
		//topBar layout
		timeShow.setPrefWidth(40);
		minesShow.setPrefWidth(20);
		chancesShow.setPrefWidth(20);
		statusShow.setPrefWidth(40);
		topBar.setPadding(new Insets(10,30,10,30));
		topBar.setSpacing(10);
		topBar.setAlignment(Pos.CENTER);
		topBar.getChildren().addAll(Mines, minesShow,Chances, chancesShow, Status, statusShow, Time, timeShow);

		// btmBar layout

		btmBar.setAlignment(Pos.CENTER);
		btmBar.setPadding(new Insets(10));
		btmBar.setSpacing(20);
		btmBar.getChildren().addAll(restartBtn, scanBox, settingBtn);

	}
	
	private void ImageInit() {
		// TODO Auto-generated method stub
		for(int i=0; i<4; i++) {
			topBarImg[i] = new Image("file:img/topBarImg/topBarIcon"+String.valueOf(i)+".png");
			topBarView[i] = new ImageView(topBarImg[i]);
			topBarView[i].setFitWidth(30);
			topBarView[i].setPreserveRatio(true);
			scanImg[i] = new Image("file:img/scanImg/scanIcon"+String.valueOf(i)+".png");
			scanView[i] = new ImageView(scanImg[i]);
			scanView[i].setFitWidth(50);
			scanView[i].setPreserveRatio(true);
		}
		imgRestart = new Image("file:img/btmBarImg/Restart.png");
		viewRestart = new ImageView(imgRestart);
		viewRestart.setFitWidth(30);
		viewRestart.setPreserveRatio(true);
		imgSettings = new Image("file:img/btmBarImg/Settings.png");
		viewSettings = new ImageView(imgSettings);
		viewSettings.setFitWidth(30);
		viewSettings.setPreserveRatio(true);
	}

	//refresh time
	public void timeStart(String timerStatus, long timeCountDown) {
		//if time starts form 0, which means timer is off
		if(timerStatus == "Off") {
			startTime = System.currentTimeMillis();
			mTimer = new Timer();
		    mTimer.scheduleAtFixedRate(new TimerTask() {
		        public void run() {
		        		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
		                Platform.runLater(() -> timeShow.setText(simpleDateFormat.format(System.currentTimeMillis()-startTime)));
		                System.out.println(simpleDateFormat.format(System.currentTimeMillis()-startTime));
		        }
		    }, 0,1000);
		}else {
			startTime = timeCountDown;
			mTimer = new Timer();
		    mTimer.scheduleAtFixedRate(new TimerTask() {
		        public void run() {
		        	long timeNow = startTime--;
	        		if(timeNow < 0) {
	        			timeStop();
	        		}else {
	        			long timeMin = timeNow/60;
	        			long timeSec = timeNow%60;
	        			Platform.runLater(() -> timeShow.setText(String.valueOf(timeMin)+":"+String.valueOf(timeSec)));
	        		}
		        }
		    }, 0,1000);
		}
		
	}
	
	//freeze time
	public void timeStop() {
		mTimer.cancel();
		mTimer.purge();
	}
	
	//refresh mines label
	public void mineShowChange(int lable) {
		int beforeLabel = Integer.parseInt(minesShow.getText());
		int afertLabel = beforeLabel + lable;
		minesShow.setText(String.valueOf(afertLabel));
	}
	
	//refresh status 
	public void statusChange(int i) {
		statusShow.setText(statusLabel[i]);
	}
	
	//play the bg music only if musicStatus == On
	public void bgMusicPlay(String musicStatus) {
		//bg music player
		if(musicStatus.equals("On")) {
			bgMedia = new Media(new File("music/backgroud.mp3").toURI().toString());
			bgMediaPlayer = new MediaPlayer(bgMedia);
			bgMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); //indefinitely play bg music
			bgMediaPlayer.play();
		}

	}
	
	//stop the bg music
	public void bgMusicStop(String musicStatus) {
		if(musicStatus.equals("On")) {
			bgMediaPlayer.stop();
		}
	}
	
	//btn music effect
	public void btnMusicPlayOnce(String musicStatus) {
		//btn music effect player
		if(musicStatus.equals("On")) {
			btnMedia = new Media(new File("music/button.mp3").toURI().toString());
			btnMediaPlayer = new MediaPlayer(btnMedia);
			btnMediaPlayer.setVolume(0.5);
			btnMediaPlayer.play();
		}
	}
	
	//win music effect
	public void winMusicPlayOnce(String musicStatus) {
		if(musicStatus.equals("On")) {
			bgMediaPlayer.stop();
			bgMedia = new Media(new File("music/gamewin.mp3").toURI().toString());
			bgMediaPlayer = new MediaPlayer(bgMedia);
			bgMediaPlayer.play();
		}
	}
	
	//lost music effect
	public void lostMusicPlayOnce(String musicStatus) {
		if(musicStatus.equals("On")) {
			bgMediaPlayer.stop();
			bgMedia = new Media(new File("music/gameover.mp3").toURI().toString());
			bgMediaPlayer = new MediaPlayer(bgMedia);
			bgMediaPlayer.play();
		}
	}
}
