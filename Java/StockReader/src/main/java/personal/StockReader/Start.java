package personal.StockReader;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.animation.FillTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import personal.dataL.Stock;

public class Start extends Application {
	private Controller controller;
	private final Font font = new Font("Arial", 30);
	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("BET Components");
		controller = new Controller();
		controller.read();

		GridPane grid = createGrid();
		ArrayList<Label> stockLabels = createLabels();
		ArrayList<Label> varLabels = createLabels();
		ArrayList<Label> nameLabels = createLabels();


		addLabelsToGrid(nameLabels, stockLabels, varLabels, grid);

		double width = 400;
		double height = 700;

		StackPane root = new StackPane();

		Rectangle background = createBackgroundRectangleWithAnimation(width, height);

		root.getChildren().add(background);
		root.getChildren().add(grid);

		primaryStage.setScene(new Scene(root, width, height));

		PriceUpdater priceUpdater = new PriceUpdater(controller);

		AnimationTimer animationTimer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				// here we check if the lock is free
				if (controller.getLockObj().tryLock()) {
					try {

						int i = 0;
						for (Stock s : controller.getStocks()) {
							stockLabels.get(i).setText(i != 0 ? String.valueOf(Stock.getPr().format(s.getPrice()))
									: String.valueOf(s.getPrice()));
							varLabels.get(i).setText(String.valueOf(Stock.getVr().format(s.getVar())) + "%");

							if (s.getVar() > 0) {
								// nameLabels.get(i).setTextFill(Color.GREEN);
								varLabels.get(i).setTextFill(Color.GREEN);
								stockLabels.get(i).setTextFill(Color.GREEN);
							} else if (s.getVar() < 0) {
								// nameLabels.get(i).setTextFill(Color.RED);
								varLabels.get(i).setTextFill(Color.RED);
								stockLabels.get(i).setTextFill(Color.RED);
							}


							i++;
						}

					} finally {
						controller.getLockObj().unlock();
					}
				}
			}
		};

		addWindowResizeListener(primaryStage, background);

		animationTimer.start();

		priceUpdater.start();

		primaryStage.show();
	}

	private ArrayList<Label> createLabels() {
		ArrayList<Label> Labels = new ArrayList<Label>();
		for (Stock s : controller.getStocks()) {
			Label a = new Label("");
			a.setId(s.getName());
			a.setOnMousePressed(event -> a.setTextFill(Color.BLUE));
			a.setOnMouseReleased(event -> a.setTextFill(Color.BLACK));
			Labels.add(a);


		}
		Labels.get(0).setFont(font);
		return Labels;

	}



	private void addLabelsToGrid(ArrayList<Label> nameLabels, ArrayList<Label> stockLabels, ArrayList<Label> varLabels,
			GridPane grid) {
		int row = 0;
		int i = 0;
		for (Label name : nameLabels) {
			nameLabels.get(i).setText(name.getId());
			grid.add(nameLabels.get(i), 0, row);
			grid.add(stockLabels.get(i), 1, row);
			grid.add(varLabels.get(i), 2, row);

			row++;
			i++;
		}
	}

	/**
	 * Needed for the animation
	 * 
	 * @param stage
	 * @param background
	 */
	private void addWindowResizeListener(Stage stage, Rectangle background) {
		ChangeListener<Number> stageSizeListener = ((observable, oldValue, newValue) -> {
			background.setHeight(stage.getHeight());
			background.setWidth(stage.getWidth());
		});
		stage.widthProperty().addListener(stageSizeListener);
		stage.heightProperty().addListener(stageSizeListener);
	}

	private GridPane createGrid() {
		GridPane grid = new GridPane();
		grid.setHgap(20);
		grid.setVgap(20);
		grid.setAlignment(Pos.CENTER);
		return grid;
	}

	/**
	 * A small animation that runs in the background
	 * 
	 * @param width
	 * @param height
	 * @return
	 */
	private Rectangle createBackgroundRectangleWithAnimation(double width, double height) {
		Rectangle backround = new Rectangle(width, height);
		FillTransition fillTransition = new FillTransition(Duration.millis(2000), backround, Color.CORNSILK,
				Color.KHAKI);
		fillTransition.setCycleCount(Timeline.INDEFINITE);
		fillTransition.setAutoReverse(true);
		fillTransition.play();
		return backround;
	}

}
