package application;
	
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;


public class Main extends Application {
	
	int rowX,colY; //indicate the mine ares' columns and rows
	int mineNumbers; // indicate the mine numbers
	int labeledNumbers; //indicate the play's correctly label how many mines
	int testTimes; //indicate the testing times left
	String musicStatus,timerStatus;	//indicate the music/timer on/off status
	Scene homeScene, settingScene, gameScene;		//2 scenes to shift between each other 
	SettingPane settingPane;	//settingPane is the page for user to set
    GamePane gamePane;			//gamePane is where game runs
    HomePane homePane;			//homePane is the home page
    
	@Override
	public void start(Stage primaryStage) {
		
		homePane = new HomePane();
		settingPane = new SettingPane();
		gamePane = new GamePane(rowX, colY, mineNumbers);
        gameScene = new Scene(gamePane);
		settingScene = new Scene(settingPane);
		homeScene = new Scene(homePane);
		initHListener(primaryStage);	//initialize homePane's listener
		initSListener(primaryStage); 	//initialize settingPane's listener
		
		//game window start
        primaryStage.setScene(homeScene);
        primaryStage.setTitle("MineSweeper-V2-Yuan");
        primaryStage.show();
	}
	
	private void initHListener(Stage stage) {
		// TODO Auto-generated method stub
		homePane.newGameBtn.setOnMouseClicked(e->{
			musicStatus = (String) homePane.musicGroup.getSelectedToggle().getUserData();
			timerStatus = (String) homePane.timerGroup.getSelectedToggle().getUserData();
			System.out.println("music:" + musicStatus+ " count down timer:"+ musicStatus);
			stage.setScene(settingScene);
		});
	}

	private void initGlistener(Stage stage) {
		// btmBar button's click listener
        gamePane.restartBtn.setOnMouseClicked(e->{
        	StartGame(rowX, colY, mineNumbers, stage);
        	initCellBtnListenr();
        });
        
        gamePane.settingBtn.setOnMouseClicked(e->{
        	stage.setScene(homeScene);
        	gamePane.bgMusicStop(musicStatus);
        });
        
        //btmBar testRec's listener (drag and drop to test a cell)
        gamePane.scanBox.setOnDragEntered(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag-and-drop gesture entered the target */
                System.out.println("start testing");
                /* show to the user that it is an actual gesture target */
                if (event.getGestureSource() != gamePane.scanBox &&
                        event.getDragboard().hasString()) {
                	String testInfo = event.getDragboard().getString();
                	// if test chances is used up , it will become no change
                	if(testTimes>0) {
                    	if(Integer.parseInt(testInfo)<9) {
                    		gamePane.scanBox.setGraphic(gamePane.scanView[1]);

                    	}else {
                    		gamePane.scanBox.setGraphic(gamePane.scanView[2]);
                    	}
                	}else {
                		gamePane.scanBox.setGraphic(gamePane.scanView[3]);
                	}
                }
                event.consume();
            }
        });
       
        gamePane.scanBox.setOnDragExited(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* mouse moved away, remove the graphical cues */

            	gamePane.scanBox.setGraphic(gamePane.scanView[0]);
            	System.out.println("onDragExited");
                event.consume();
                testTimes--;
                if(testTimes>=0) {
                	gamePane.chancesShow.setText(String.valueOf(testTimes));
                }
                System.out.println("left test times:" + testTimes);
            }
        });
        
        
        //minePane's btn listener
        initCellBtnListenr();

	}

	private void initCellBtnListenr() {
		// TODO Auto-generated method stub
        for(int i=0; i<gamePane.minePane.btnGraph.length; i++) {
        	for(int j=0; j<gamePane.minePane.btnGraph[i].length; j++) {
        		CellBtn btnPick = gamePane.minePane.btnGraph[i][j];
        		ArrayList<CellBtn> neighborBtnList = new ArrayList<CellBtn>();
        		String focus = "-fx-border-color: #0066CC; -fx-border-width: 1px;";
        		for(int k=0; k<8; k++) {
        			if(i+gamePane.minePane.route[k][0]>=0 && i+gamePane.minePane.route[k][0]< rowX&& j+gamePane.minePane.route[k][1]>=0 &&j+gamePane.minePane.route[k][1]<colY )
        			{
        				neighborBtnList.add(gamePane.minePane.btnGraph[i+gamePane.minePane.route[k][0]][j+gamePane.minePane.route[k][1]]);
        			}

        		}
        		int pickX = i;
        		int pickY = j;
        		//drag event
        		btnPick.setOnDragDetected(new EventHandler<MouseEvent>() {
					public void handle(MouseEvent event) {
						/* drag was detected, start drag-and-drop gesture */
						System.out.println("onDragDetected");

						/* allow any transfer mode */
						Dragboard db = btnPick.startDragAndDrop(TransferMode.ANY);

						/* put a string on drag-board */
						ClipboardContent content = new ClipboardContent();
						content.putString(String.valueOf(btnPick.mineInfo));
						db.setContent(content);
						event.consume();
					}
				});
				//mouse hover event
				btnPick.setOnMouseEntered(e->{
					for(CellBtn neighborBtn : neighborBtnList) {
						neighborBtn.nowStyle = neighborBtn.getStyle();	//save their original style
						String plus = neighborBtn.getStyle() + focus;
						neighborBtn.setStyle(plus);
					}
				});
				btnPick.setOnMouseExited(e->{
					for(CellBtn neighborBtn : neighborBtnList) {
						neighborBtn.setStyle(neighborBtn.nowStyle);
					}
				});
				
				//mouse click event (left key and right key)
				btnPick.setOnMouseClicked(e->{
					if(e.getButton() == MouseButton.PRIMARY){
						if(btnPick.mineInfo < 9) {
							btnPick.setText(String.valueOf(btnPick.mineInfo));
							btnPick.setStyle(btnPick.clickedStyle);
							btnPick.nowStyle = btnPick.clickedStyle;
							if(btnPick.mineInfo == 0) {
								gamePane.minePane.neighborClear(pickX,pickY);
							}
						}else {
							System.out.println("Game Over");
							gamePane.statusChange(1);
							gamePane.minePane.lostClear();
							gamePane.timeStop();
							gamePane.lostMusicPlayOnce(musicStatus);	//play lost music
						}
						
					}
					else if(e.getButton() == MouseButton.SECONDARY) {
						if(btnPick.labelStatus == 1) {
							btnPick.setStyle(btnPick.labeledStyle);
							btnPick.nowStyle = btnPick.labeledStyle;
							btnPick.setText("🚩");
							gamePane.mineShowChange(-1);
							if(btnPick.mineInfo > 8) {
								labeledNumbers++;
							}
						}else {
							btnPick.setStyle(btnPick.noStyle);
							btnPick.nowStyle = btnPick.noStyle;
							btnPick.setText("  ");
							gamePane.mineShowChange(1);
							if(btnPick.mineInfo > 8) {
								labeledNumbers--;
							}
						}
						btnPick.labelStatus = -btnPick.labelStatus;
						checkWin();  //check if the player labels all the mine btns
					}
					gamePane.btnMusicPlayOnce(musicStatus);
				});	
        	}
        }
	}

	private void checkWin() {
		// TODO Auto-generated method stub
		if(labeledNumbers == mineNumbers && Integer.valueOf(gamePane.minesShow.getText()) == 0) {
			gamePane.statusChange(2);   //change the label show
			gamePane.timeStop();    //freeze time
			gamePane.winMusicPlayOnce(musicStatus);	//play win music 
			gamePane.minePane.winClear();
		}
	}

	private void initSListener(Stage stage) {
		
		settingPane.easyBtn.setOnMouseClicked(e->{
        	StartGame(8,8,10,stage);
        	initGlistener(stage);
        });
        settingPane.mediumBtn.setOnMouseClicked(e->{
        	StartGame(16,16,40,stage);
        	initGlistener(stage);
        });
        settingPane.hardBtn.setOnMouseClicked(e->{
        	StartGame(20,30,90,stage);
        	initGlistener(stage);
        });
        settingPane.customBtn.setOnMouseClicked(e->{
        	StartGame(
        			Integer.parseInt(settingPane.rowInput.getText()),
        			Integer.parseInt(settingPane.colInput.getText()),
        			Integer.parseInt(settingPane.mineInput.getText()),
        			stage
        			);
        	initGlistener(stage);
        });
	}

	private void StartGame(int x, int y, int mines, Stage nowStage) {
		// TODO Auto-generated method stub
		rowX = x;
		colY = y;
		mineNumbers = mines;
		labeledNumbers = 0;
		testTimes = mines/2; 
    	gamePane.statusChange(0);	//change game status
		gamePane.minesShow.setText(Integer.toString(mineNumbers));  //change mine's number's status
		gamePane.chancesShow.setText(String.valueOf(testTimes));
		gamePane.timeStart();		//change time status
		gamePane.bgMusicPlay(musicStatus);		//play background music
    	gamePane.getChildren().remove(gamePane.minePane);			//remove an old mine's pane and add a new one
    	gamePane.minePane = new MinePane(rowX, colY, mineNumbers);
    	gamePane.setCenter(gamePane.minePane);
    	nowStage.setScene(gameScene);
    	System.out.println("game start" + " row:"+ rowX+" col:"+ colY+" mines:"+ mineNumbers);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
