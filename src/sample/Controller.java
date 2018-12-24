package sample;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import java.util.*;
import javafx.collections.*;
import javafx.scene.layout.*;
import javafx.event.*;
import javafx.scene.shape.Line;

public class Controller {
    // VIEWS
    @FXML private TextField startCurve;
    @FXML private Button drawButton;
    @FXML private Button clearButton;
    @FXML private Canvas drawArea;
    @FXML private ChoiceBox colorInput;
    @FXML private ChoiceBox choiceBox;
    @FXML private TextField startPos;
    @FXML private TextField layer;
    @FXML private TextField sizeOfSquare;

    // Size of the canvas for the Mandelbrot set
    private static final int CANVAS_WIDTH = 697;
    private static final int CANVAS_HEIGHT = 400;

    // Left and right border
    private static final int X_OFFSET = 25;

    // Top and Bottom border
    private static final int Y_OFFSET = 25;


    // VALUES FOR CURVE POINTS
    private static final double[] a = {0, 0.5, 0.5, 0},
                                  b = {0.5, 0, 0, -0.5},
                                  c = {0.5, 0, 0, -0.5},
                                  d = {0, 0.5, 0.5, 0},
                                  e = {0, 0, 0.5, 1},
                                  f = {0, 0.5, 0.5, 0.5};



    public Main main;
    private ArrayList<Node> itemsDrawn;
    private Coordinates points;


    private Figures figures = new Figures();


    public void setMain(Main main){
        this.main = main;
        initialize();
    }

    @FXML
    private void initialize(){
        drawArea.setHeight(CANVAS_HEIGHT);
        drawArea.setWidth(CANVAS_WIDTH);
        drawArea.setLayoutX(X_OFFSET);
        drawArea.setLayoutY(Y_OFFSET);

        ObservableList<String> list = FXCollections.observableArrayList("1","2","3","4","5","6","7","8","9","10");
        choiceBox.setItems(list);

        ObservableList<String> list2 = FXCollections.observableArrayList("BLUE","BLACK","GREEN","PURPLE","RED","BROWN","CYAN","GREY","PINK","LIME");
        colorInput.setItems(list2);

        itemsDrawn = new ArrayList<Node>();
    }

    @FXML
    public void testMethod(){
        //drawShapes(gc);
        GraphicsContext gc = drawArea.getGraphicsContext2D();
        String userChoice = choiceBox.getValue().toString();
        String colorChoice;

        // FALLBACK COLOR
        if(colorInput.getValue() != null){
            colorChoice = colorInput.getValue().toString();
        }
        else{
            colorChoice = "BLACK";
        }

        Double startPosX;
        Double startPosY;

        if(!startPos.getText().isEmpty()){
            String[] coordinates = startPos.getText().split(",");

            startPosX = Double.parseDouble(coordinates[0]);
            startPosY = Double.parseDouble(coordinates[1]);
        }
        else{
            startPosX = 0.0;
            startPosY = 0.0;
            System.out.println("No starting point provided");
        }



        ArrayList<Node> ret = figures.init(Integer.parseInt(userChoice), gc, colorChoice, startPosX, startPosY);

        for (Node item: ret) {
            System.out.println(item);
            this.itemsDrawn.add(item);
            System.out.println(itemsDrawn.toString());
            main.getRoot().getChildren().add(item);
        }



      /*   Example calling one draw function often */
/*
        double x = 0;
        double y  = 0;

        for (int i = 0; i < 100 ; i++) {


            if(i > 50){
                x = 50 + i * 10;
                y = 50 + i * 15;
            }
            else{
                x = 500 - i * 10;
                y = 600 - i * 15;
            }

            if(y > 280){
                y = 250;
            }

            Node ret2 = figures.init(4,gc,colorChoice,x,y);
            main.getRoot().getChildren().add(ret2);

            this.itemsDrawn.add(ret2);
        }
        */

    }

    @FXML
    public void clearCanvas() {

        System.out.println("pressed");

        for (Node item : itemsDrawn) {
            main.getRoot().getChildren().remove(item);
        }

    }


    private void calculatePoint(double i, double j){
        double iNew = 0.0;
        double jNew = 0.0;
        Coordinates[] retArray = new Coordinates[4];


        // CALCULATE 4 POINTS

        for(int k = 0; k < 4; k++){
            points = new Coordinates();

            iNew = 800 * (a[k] * i / 800.0 + b[k] * j/800.0 + e[k]);
            jNew = 800 * (c[k] * i / 800.0 + d[k] * j/800.0 + f[k]);

            points.setX(iNew);
            points.setY(jNew);

            retArray[k] = points;
        }

        GraphicsContext gc = drawArea.getGraphicsContext2D();
        for(Coordinates item : retArray){
            System.out.println(item.toString());

            ArrayList<Node> ret = figures.init(3, gc, "BLACK", item.getX(), item.getY());

            for (Node itemm: ret) {
                this.itemsDrawn.add(itemm);
                main.getRoot().getChildren().add(itemm);
            }
        }






    }


}
