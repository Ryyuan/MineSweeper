package application;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class SettingPane extends GridPane {
	
	Button easyBtn = new Button("Start"); 
	Button mediumBtn = new Button("Start");
	Button hardBtn = new Button("Start");
	Button customBtn = new Button("Start");
	TextField rowInput = new TextField();
	TextField colInput = new TextField();
	TextField mineInput = new TextField();
	
	public SettingPane() {
		this.setVgap(20);
		this.setHgap(20);
		this.setAlignment(Pos.CENTER);
		rowInput.setPrefWidth(40);
		colInput.setPrefWidth(40);
		mineInput.setPrefWidth(40);

		String[][] labels = new String[][] {
			{"Mode","RowX","ColY","Mines"},
			{"Easy","8","8","10"},
			{"Medium","16","16","40"},
			{"Hard","20","30","99"}};
		for(int i=0;i<labels.length;i++) {
			for(int j=0;j<labels[i].length;j++) {
				this.add(new Label(labels[i][j]), j, i);
			}
		}
		this.add(new Label("Check"),4 , 0);
		this.add(easyBtn, 4,1);
		this.add(mediumBtn, 4, 2);
		this.add(hardBtn, 4, 3);
		this.add(customBtn, 4, 4);
		this.add(new Label("Custom"), 0, 4);
		this.add(rowInput, 1, 4);
		this.add(colInput, 2, 4);
		this.add(mineInput,3, 4);
	}
}
