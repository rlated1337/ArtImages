package sample;

import javafx.application.Platform;
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
import java.util.concurrent.TimeUnit;

import javafx.collections.*;
import javafx.scene.layout.*;
import javafx.event.*;
import javafx.scene.shape.Line;

import static java.lang.Thread.sleep;

public class Controller {
    // VIEWS
    @FXML private TextField startCurve;
    @FXML private Button drawButton;
    @FXML private Button clearButton;
    @FXML private Button pauseButton;
    @FXML private Button resetButton;
    @FXML private Button startButton;
    @FXML private Canvas drawArea;
    @FXML private ChoiceBox colorInput;
    @FXML private ChoiceBox choiceBox;
    @FXML private TextField startPos;
    @FXML private TextField layer;
    @FXML private TextField sizeOfSquare;

    // Size of the canvas for the Mandelbrot set
    private static final double CANVAS_WIDTH = 697;
    private static final double CANVAS_HEIGHT = 400;

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



    private static final String POINTALG = "Points (Logofatu Alg.)";
    private static final String TREE = "Tree";
    private static final String CIRCLE = "Endless Circle";
    private static final String SPONGE = "Sponge";
    private static final String ROTSQUARE = "Squares";
    private static final String CANTOR = "Cantor";
    private static final String KOCHCURVE = "Koch Curve";

    private static String[] colors = {"BLUE","BLACK","GREEN","PURPLE","RED","BROWN","CYAN","GREY","PINK","LIME"};


    public Main main;
    private ArrayList<Node> itemsDrawn;
    private Coordinates points;
    private ArrayList<Integer> angleList;


    private Figures figures = new Figures();


    public void setMain(Main main){
        this.main = main;
        initialize();
    }

    private Timer timer = new Timer();
    private Timer timer1 = new Timer();
    private Timer timer2 = new Timer();
    private Timer timer3 = new Timer();
    private Timer timer4 = new Timer();

    private int timer1Border = 20;
    private int timer2Border = 6;
    private int timer3Border = 7;
    private int timer4Border = 10;


    private int timer1Counter = 1;
    private int timer2Counter = 1;
    private int timer3Counter = 1;
    private int timer4Counter = 1;

    @FXML
    private void initialize(){
        drawArea.setHeight(CANVAS_HEIGHT);
        drawArea.setWidth(CANVAS_WIDTH);
        drawArea.setLayoutX(X_OFFSET);
        drawArea.setLayoutY(Y_OFFSET);
        startButton.setDisable(true);
        resetButton.setDisable(true);
        pauseButton.setDisable(true);
        ObservableList<String> list = FXCollections.observableArrayList(POINTALG,TREE,CIRCLE,SPONGE, ROTSQUARE, CANTOR,KOCHCURVE);
        choiceBox.setItems(list);

        ObservableList<String> list2 = FXCollections.observableArrayList("BLUE","BLACK","GREEN","PURPLE","RED","BROWN","CYAN","GREY","PINK","LIME");
        colorInput.setItems(list2);

        itemsDrawn = new ArrayList<Node>();
        angleList = new ArrayList<Integer>();
    }

    @FXML
    public void testMethod(){
        //drawShapes(gc);
        startButton.setDisable(false);
        resetButton.setDisable(false);
        pauseButton.setDisable(false);
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

        Integer curveStart;

        if(startCurve.getText().isEmpty()){
            curveStart = 5;
        }
        else{
            curveStart = Integer.parseInt(startCurve.getText().toString());
        }
        System.out.println("Curve Start: " + curveStart);



        switch(userChoice){
            case POINTALG:
                calculatePoint(800, 500, 3, 800);
                timer.scheduleAtFixedRate(new TimerTask() {
                                              int i = 0;
                                              @Override
                                              public void run() {
                                                  Platform.runLater(() -> {
                                                      // your code here
                                                      i++;
                                                      System.out.println("Count: " + i);
                                                      int type;

                                                      if(i % 2 == 0){
                                                          type = 3;
                                                      }
                                                      else{
                                                          type = 0;
                                                      }

                                                      calculatePoint(800, 500, type, 800);

                                                  });
                                              }
                                          },  20,20
                );

                break;
            case TREE:
                drawTree(380, 400, -90, 9, true,3.0f);
                timer1.scheduleAtFixedRate(new TimerTask() {
                                               //int i = 1;
                                               @Override
                                               public void run() {
                                                   Platform.runLater(() -> {
                                                       // your code here
                                                       timer1Counter++;
                                                       System.out.println("COUNT: " + timer1Counter);

                                                       if(timer1Counter >= timer1Border){
                                                           timer1.cancel();
                                                           timer1.purge();
                                                       }

                                                       drawTree(380, 400, -90, 9, false,3.0f);


                                                   });
                                               }
                                           },  1500,1500
                );
                break;
            case CIRCLE:
                drawCircle(380,260, 190.0f);
                break;
            case SPONGE:
                double size = CANVAS_WIDTH > CANVAS_HEIGHT ? (int) (CANVAS_HEIGHT * 0.8) : (int) (CANVAS_WIDTH * 0.8);
                sponge(5,size, size, CANVAS_WIDTH / 2 - size / 2, CANVAS_HEIGHT / 2 - size / 2);
                break;
            case ROTSQUARE:
                double sizeRotSquare = CANVAS_WIDTH > CANVAS_HEIGHT ? CANVAS_HEIGHT / 3 : CANVAS_WIDTH / 3;
                System.out.println(CANVAS_WIDTH / 2 - sizeRotSquare / 2);
                System.out.println(CANVAS_HEIGHT / 2 - sizeRotSquare / 2);

                rotatedSquare(
                        CANVAS_WIDTH / 2 - sizeRotSquare / 2,
                        CANVAS_HEIGHT / 2 - sizeRotSquare / 2 + 50,
                        sizeRotSquare + 150,
                        sizeRotSquare,
                        1);
                timer2.scheduleAtFixedRate(new TimerTask() {
                    int i = 1;
                                              @Override
                                              public void run() {
                                                  Platform.runLater(() -> {
                                                      // your code here
                                                      timer2Counter++;
                                                      System.out.println("Count2: " + timer2Counter);

                                                      if(timer2Counter > timer2Border){
                                                          timer2.cancel();
                                                          timer2.purge();
                                                      }

                                                      rotatedSquare(
                                                              CANVAS_WIDTH / 2 - sizeRotSquare / 2,
                                                              CANVAS_HEIGHT / 2 - sizeRotSquare / 2 + 50,
                                                              sizeRotSquare + 150,
                                                              sizeRotSquare,
                                                              i);

                                                  });
                                              }
                                          },  2500,2500
                );
                break;
            case CANTOR:
                cantor(10,20, CANVAS_WIDTH-20, 190.0f);
                timer3.scheduleAtFixedRate(new TimerTask() {
                                               int i = 1;
                                               int y = 20;
                                               @Override
                                               public void run() {
                                                   Platform.runLater(() -> {
                                                       // your code here
                                                       timer3Counter++;
                                                       System.out.println("Count3: " + timer3Counter);

                                                       if(timer3Counter > timer3Border){
                                                           timer3.cancel();
                                                           timer3.purge();
                                                       }
                                                       y+= 50;

                                                       cantor(10,y, CANVAS_WIDTH-20, 190.0f);

                                                   });
                                               }
                                           },  500,500
                );
                break;
            case KOCHCURVE:
                Coordinates c1 = new Coordinates(100,200);
                Coordinates c2 = new Coordinates(500,200);
                Coordinates c3 = new Coordinates(100,400);
                Coordinates c4 = new Coordinates(500,400);
                timer4.scheduleAtFixedRate(new TimerTask() {
                                               int i = 1;
                                               int kochTimer = 4;
                                               @Override
                                               public void run() {
                                                   Platform.runLater(() -> {
                                                       // your code here
                                                       timer4Counter++;
                                                       System.out.println("Count: " + timer4Counter);

                                                       if(timer4Counter > timer4Border){
                                                           timer4.cancel();
                                                           timer4.purge();
                                                       }

                                                       kochCurve(c1,c2,kochTimer);
                                                       kochTimer++;


                                                   });
                                               }
                                           },  1500,1500
                );
                break;

            default:
                break;

        }



     //   ArrayList<Node> ret = figures.init(Integer.parseInt(userChoice), gc, colorChoice, startPosX, startPosY, 0.0, 0.0, 0, 0 ,0); // TODO HEIGHT WIDTH
/*
        for (Node item: ret) {
            System.out.println(item);
            this.itemsDrawn.add(item);
            System.out.println(itemsDrawn.toString());
            main.getRoot().getChildren().add(item);
        }
*/



    }

    @FXML
    public void clearCanvas() {

        System.out.println("CLEARING...");
/*
        CustomThread t1 = new CustomThread("ABC THREAD");

        t1.start();
*/


   //
        this.drawArea.getGraphicsContext2D().clearRect(0,0, CANVAS_WIDTH, CANVAS_HEIGHT);

        for (Node item : itemsDrawn) {
            main.getRoot().getChildren().remove(item);
        }
    }


    @FXML
    public void resetTimer(){
        System.out.println("Reset");


        timer.cancel();
        timer.purge();

        timer1.cancel();
        timer1.purge();
        timer1Counter = 0;

        timer2.cancel();
        timer2.purge();
        timer2Counter = 0;


        timer3.cancel();
        timer3.purge();
        timer3Counter = 0;

        timer4.cancel();
        timer4.purge();
        timer4Counter = 0;


    }
    @FXML
    public void pauseTimer(){
        System.out.println("Pause");

        timer.cancel();


        timer1.cancel();


        timer2.cancel();


        timer3.cancel();


        timer4.cancel();

    }

    @FXML
    public void resumeTimer(){
        timer = new Timer();
        timer1 = new Timer();
        timer2 = new Timer();
        timer3 = new Timer();
        timer4 = new Timer();
        testMethod();
    }





    private void calculatePoint(double i, double j, int type, double calc){

        double iNew = 0.0;
        double jNew = 0.0;
        Coordinates[] retArray = new Coordinates[4];


        // CALCULATE 4 POINTS
        for(int k = 0; k < 4; k++){
            iNew = calc * (a[k] * i / calc + b[k] * j/calc + e[k]);
            jNew = calc * (c[k] * i / calc + d[k] * j/calc + f[k]);


            iNew += (double) (Math.cos(Math.toRadians(-90)) * 6 * 5.0);
            jNew += (double) (Math.cos(Math.toRadians(-90)) * 5 * 5.0);


            if(iNew >= 770){
                iNew = Math.random() * 770 + 20;
            }

            if(jNew >= 420){
                jNew = Math.random() * 420 + 1;
            }



            points = new Coordinates(iNew, jNew);

            retArray[k] = points;
        }




        // DRAWING
        GraphicsContext gc = drawArea.getGraphicsContext2D();

        ArrayList<Node> lines = figures.init(1, gc, this.colors[(int) (Math.random() * 10) ], retArray[0].getX(),retArray[0].getY(), retArray[1].getX(), retArray[1].getY(),0, 0, 1);
        lines.addAll(figures.init(1,gc, this.colors[(int) (Math.random() * 10) ], retArray[2].getX(), retArray[2].getY(), retArray[3].getX(), retArray[3].getY(),0, 0, 1));

        lines.addAll(figures.init(1,gc, this.colors[(int) (Math.random() * 10) ], retArray[0].getX(), retArray[0].getY(), retArray[3].getX(), retArray[3].getY(), 0, 0, 1));
        lines.addAll(figures.init(1,gc, this.colors[(int) (Math.random() * 10) ], retArray[1].getX(), retArray[1].getY(), retArray[2].getX(), retArray[2].getY(), 0, 0, 1));


        gc.strokeLine(retArray[0].getX(),retArray[0].getY(), retArray[1].getX(), retArray[1].getY());
        gc.setStroke(Color.valueOf(this.colors[(int) (Math.random() * 10)]));

        gc.strokeLine(retArray[2].getX(), retArray[2].getY(), retArray[3].getX(), retArray[3].getY());
        gc.setStroke(Color.valueOf(this.colors[(int) (Math.random() * 10)]));

        gc.strokeLine(retArray[0].getX(), retArray[0].getY(), retArray[3].getX(), retArray[3].getY());
        gc.setStroke(Color.valueOf(this.colors[(int) (Math.random() * 10)]));

        gc.strokeLine(retArray[1].getX(), retArray[1].getY(), retArray[2].getX(), retArray[2].getY());
        gc.setStroke(Color.valueOf(this.colors[(int) (Math.random() * 10)]));





        ArrayList<Node> ret = new ArrayList<>();

        for(Coordinates item : retArray){
          //  System.out.println(item.toString());

            double x = item.getX();
            double y = item.getY();

           ret.addAll(figures.init(type, gc, this.colors[(int) (Math.random() * 10) ], x, y, 0.0, 0.0, 0, 0, 0));


            gc.strokeLine(x, y, 0.0, 0.0);
            gc.setStroke(Color.valueOf(this.colors[(int) (Math.random() * 10)]));
        }

        // MERGE 2 ARRAYLIST'S
        // ret.addAll(lines);

        for (Node line: lines){
            if (!ret.contains(line))
                ret.add(line);
        }

        for (Node itemm: ret) {
            System.out.println("ITEM: " +  itemm.toString());
            // this.itemsDrawn.add(itemm);
           // main.getRoot().getChildren().add(itemm);
        }


    }

    private void drawTree(int x1, int y1, double angle, int depth, Boolean firstCall, float widthModifier) {
        if (depth == 0) return;
        int x2 = x1 + (int) (Math.cos(Math.toRadians(angle)) * depth * 11.5);
        int y2 = y1 + (int) (Math.sin(Math.toRadians(angle)) * depth * 9.0);

        int angleModifier;
        int angleCount = 20;
        do {
            angleModifier = (int) (Math.random() * 45) + 20;
            if(!angleList.contains(angleModifier)){
                angleList.add(angleModifier);
            }
        }while(angleList.size()<angleCount);

        GraphicsContext gc = drawArea.getGraphicsContext2D();

        ArrayList<Node> lines;


        if(firstCall){
             lines = figures.init(1, gc, "BLACK", x1,y1 + 50, x2, y2, 0, 0, 5);
        }
        else{
            lines = figures.init(1, gc, this.colors[(int) (Math.random() * 10) ], x1,y1, x2, y2, 0, 0, widthModifier);
            widthModifier -= 0.5f;
            if(widthModifier < 0.5){
                widthModifier = 0.25f;
            }
        }


        for (Node itemm: lines) {
          //  System.out.println("ITEM: " +  itemm.toString());
            this.itemsDrawn.add(itemm);
            main.getRoot().getChildren().add(itemm);
        }
     //   System.out.println(angleList.toString());
       // System.out.println(angleList.get((int) Math.random() * angleList.size()));
        int randomAngle = angleList.get((int) (Math.random() * angleList.size()));
        angleList.remove(angleList.indexOf(randomAngle));
        drawTree(x2, y2, angle - randomAngle, depth - 1, false, widthModifier);
        drawTree(x2, y2, angle + randomAngle, depth - 1, false, widthModifier);
    }

    private void drawCircle(double x, double y, float radius){
        GraphicsContext gc = drawArea.getGraphicsContext2D();


        ArrayList<Node> lines = figures.init(5, gc, this.colors[(int) (Math.random() * 10) ], x,y, 0, 0, radius, 0,0);

        for (Node itemm: lines) {
            //  System.out.println("ITEM: " +  itemm.toString());
            this.itemsDrawn.add(itemm);
            main.getRoot().getChildren().add(itemm);
        }

        if(radius > 2) {
            radius *= 0.75f;
           // drawCircle(x, y, radius);

            /*
            drawCircle(x + radius/2, y, radius/2);
            drawCircle(x - radius/2, y, radius/2);
            */

            drawCircle(x + radius/2, y, radius/2);
            drawCircle(x - radius/2, y, radius/2);
            drawCircle(x, y + radius/2, radius/2);
            drawCircle(x, y - radius/2, radius/2);

            //x 380 - y 260
            // + 150  - 150
            // 530     110
/*
            drawCircle(x + radius/2 + 150, y, radius/2);
            drawCircle(x - radius/2 + 150, y, radius/2);
            drawCircle(x, y + radius/2 - 150, radius/2);
            drawCircle(x, y - radius/2 - 150, radius/2);


            drawCircle(x + radius/2 - 150, y, radius/2);
            drawCircle(x - radius/2 - 150, y, radius/2);
            drawCircle(x, y + radius/2 + 50, radius/2);
            drawCircle(x, y - radius/2 + 50, radius/2);
*/

        }

    }

    private void sponge(int count, double width, double height, double x, double y) {
        System.out.println(x + " , " + y);
        GraphicsContext gc = drawArea.getGraphicsContext2D();
        if(count < 1) {
           // g.drawRect(r.x, r.y, r.width, r.height);

            ArrayList<Node> lines = figures.init(6, gc, this.colors[(int) (Math.random() * 10) ], x,y, 0, 0, 0, height, width);

            for (Node itemm: lines) {
                //  System.out.println("ITEM: " +  itemm.toString());
                this.itemsDrawn.add(itemm);
                main.getRoot().getChildren().add(itemm);
            }

            return;
        }

        double width2 = width / 3;
        double height2 = height / 3;

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(!(i == 1 && j == 1)) {
                   // fractal(new Rectangle(r.x + (int) (i * width), r.y + (int) (j * height), (int) width, (int) height), count - 1);

                    sponge(count - 1, width2, height2, x + (int) (i * width2), y + (int) (j * height2));
                }
            }
        }
    }


    private void rotatedSquare(double x, double y, double width, double height, int count){
        GraphicsContext gc = drawArea.getGraphicsContext2D();


        ArrayList<Node> rectangles = figures.init(6, gc, this.colors[(int) (Math.random() * 10) ], x,y, 0, 0, 0, height, width);

        for (Node item: rectangles) {
            gc.strokeRect(x, y-50, width, height);
            gc.setStroke(Color.valueOf(this.colors[(int) (Math.random() * 10)]));
            //this.itemsDrawn.add(item);
            //this.main.getRoot().getChildren().add(item);
        }

        if(count < 1)
            return;

        double newSize = width / 2;

        rotatedSquare(x - newSize / 2, y, newSize, newSize, count - 1);
        rotatedSquare(x + width - newSize, y - newSize / 2, newSize, newSize, count - 1);
        rotatedSquare(x + width - newSize / 2, y + height - newSize, newSize, newSize, count - 1);
        rotatedSquare(x, y + height - newSize / 2, newSize, newSize, count - 1);
    }


    private void cantor(double x, double y, double len, float radius) {
        GraphicsContext gc = drawArea.getGraphicsContext2D();

        if (len >= 1 && radius > 2) {
            ArrayList<Node> rectangles = figures.init(1, gc, this.colors[(int) (Math.random() * 10)], x, y, x + len, y, 0, 0, 1);

            for (Node item : rectangles) {
                gc.strokeLine(x, y, x+len, y);
                gc.setStroke(Color.valueOf(this.colors[(int) (Math.random() * 10)]));
               // this.itemsDrawn.add(item);
               // this.main.getRoot().getChildren().add(item);
            }



            y += 20;
            radius *= 0.75f;

            cantor(x, y, len / 3, radius);
            cantor(x + len * 2 / 3, y, len / 3, radius);

            cantor(x + len * 2 / 6, y, len / 6, radius);


        }
    }
    private void kochCurve(Coordinates c1, Coordinates c2, int times){
        GraphicsContext gc = drawArea.getGraphicsContext2D();
        Coordinates c3,c4,c5;
        double theta = Math.PI/3;

        if(times>0){

            c3 = new Coordinates((2*c1.getX()+c2.getX())/3,(2*c1.getY()+c2.getY())/3);
            c5 = new Coordinates((2*c2.getX()+c1.getX())/3,(2*c2.getY()+c1.getY())/3);
            c4 = new Coordinates(c3.getX() + (c5.getX() - c3.getX())*Math.cos(theta) + (c5.getY() - c3.getY())*Math.sin(theta),c3.getY() - (c5.getX() - c3.getX())*Math.sin(theta) + (c5.getY() - c3.getY())*Math.cos(theta));

            kochCurve(c1,c3,times-1);
            kochCurve(c3,c4,times-1);
            kochCurve(c4,c5,times-1);
            kochCurve(c5,c2,times-1);
        }
        else{
            ArrayList<Node> koch = figures.init(1,gc,"BLACK",c1.getX(),c1.getY(),c2.getX(),c2.getY(),0,0,1);
/*
            for (Node item : koch) {
                this.itemsDrawn.add(item);
                this.main.getRoot().getChildren().add(item);
            }
            */

            gc.strokeLine(c1.getX(), c1.getY(), c2.getX(), c2.getY());
            gc.setStroke(Color.valueOf(this.colors[(int) (Math.random() * 10)]));
        }

    }


}
