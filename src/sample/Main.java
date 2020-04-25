package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

enum Rotation{
    UP,
    RIGHT,
    LEFT,
    DOWN
}

public class Main extends Application {
    int width = 1000;
    int height = 1000;
    int n = 100;
    int w = width/n;
    int h = height/n;
    boolean[][] grid = new boolean[n][n];
    Rectangle[][] rect = new Rectangle[n][n];
    int antPosX, antPosY;
    Rotation antRotation;

    void initializeGrid(){
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                grid[i][j] = false;
            }
        }
    }

    void paintGrid(){
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(grid[i][j]) rect[i][j].setFill(Color.BLACK);
                else rect[i][j].setFill(Color.WHITE);
            }
        }
    }

    @Override
    public void start(Stage stage){
        Pane rectPane = new Pane();
        VBox leftNav = new VBox();
        VBox rightNav = new VBox();
        Button btn = new Button("Button");
        Label label = new Label("Step count: ");
        leftNav.setPrefWidth(100);
        rightNav.setPrefWidth(100);
        leftNav.getChildren().add(btn);
        rightNav.getChildren().add(label);

        initializeGrid();
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                rect[i][j] = new Rectangle(w,h);
                rect[i][j].setX(i*w);
                rect[i][j].setY(j*h);
                rect[i][j].setStroke(Color.BLACK);
                rectPane.getChildren().add(rect[i][j]);
            }
        }
        paintGrid();

        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(leftNav);
        borderPane.setCenter(rectPane);
        borderPane.setRight(rightNav);

        Scene scene = new Scene(borderPane);
        stage.setTitle("Langton's Ant");
        stage.setScene(scene);
        stage.show();

        stage.setMinWidth(800);
        stage.setMinHeight(510);

        antPosX = n/2;
        antPosY = n/2;
        paintAnt();
        antRotation = Rotation.UP;

        rectPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                double mouseX = mouseEvent.getX();
                double mouseY = mouseEvent.getY();
                int col = (int)(mouseX/w);
                int row = (int)(mouseY/h);
                Color color = grid[col][row] ? Color.WHITE : Color.BLACK;
                rect[col][row].setFill(color);
                grid[col][row] = !grid[col][row];
            }
        });

        beginTimer();
    }

    void paintAnt(){
        rect[antPosX][antPosY].setFill(Color.DEEPPINK);
    }

    void beginTimer(){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                if(!(antPosX > 0 && antPosX < n-1 && antPosY > 0 && antPosY < n-1)){
                    timer.cancel();
                    timer.purge();
                    return;
                }

                switch(antRotation){
                    case UP:
                        if(grid[antPosX][antPosY]) { antRotation = Rotation.LEFT; antPosX--; }
                        else { antRotation = Rotation.RIGHT; antPosX++; }
                        break;
                    case DOWN:
                        if(grid[antPosX][antPosY]) { antRotation = Rotation.RIGHT; antPosX++; }
                        else { antRotation = Rotation.LEFT; antPosX--; }
                        break;
                    case LEFT:
                        if(grid[antPosX][antPosY]) { antRotation = Rotation.DOWN; antPosY--; }
                        else { antRotation = Rotation.UP; antPosY++; }
                        break;
                    case RIGHT:
                        if(grid[antPosX][antPosY]) { antRotation = Rotation.UP; antPosY++; }
                        else { antRotation = Rotation.DOWN; antPosY--; }
                        break;
                }
                grid[antPosX][antPosY] = !grid[antPosX][antPosY];
                paintGrid();
                paintAnt();
            }

        }, 0, 10);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
