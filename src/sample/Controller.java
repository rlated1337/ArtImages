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
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.collections.*;
import javafx.scene.layout.*;
import javafx.event.*;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;

import javax.swing.*;

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
    @FXML private TextField sizeOfSquare;

    // Size of the canvas for the Mandelbrot set
    private static final double CANVAS_WIDTH = 999;
    private static final double CANVAS_HEIGHT = 690;

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

    private Timer timer;
    private Timer timer1;
    private Timer timer2;
    private Timer timer3;
    private Timer timer4;

    private Boolean checkTimerOn = false;
    private Boolean checkTimer1On = false;
    private Boolean checkTimer2On = false;
    private Boolean checkTimer3On = false;
    private Boolean checkTimer4On = false;



    private int timer1Border = 20;
    private int timer2Border = 7;
    private int timer3Border = 7;
    private int timer4Border = 5;


/*
    private int timer1Counter = 1;
    private int timer2Counter = 1;
    private int timer3Counter = 1;
    private int timer4Counter = 1;
*/

    private static final AtomicInteger timer1Counter = new AtomicInteger();
    private static final AtomicInteger timer2Counter = new AtomicInteger();
    private static final AtomicInteger timer3Counter = new AtomicInteger();
    private static final AtomicInteger timer4Counter = new AtomicInteger();


    private String colorChoice;


    @FXML
    private void initialize(){
        timer1Counter.set(1);
        timer2Counter.set(1);
        timer3Counter.set(1);
        timer4Counter.set(1);

        drawArea.setHeight(CANVAS_HEIGHT);
        drawArea.setWidth(CANVAS_WIDTH);
        drawArea.setLayoutX(X_OFFSET);
        drawArea.setLayoutY(Y_OFFSET);
        startButton.setDisable(true);
        resetButton.setDisable(true);
        pauseButton.setDisable(true);
        ObservableList<String> list = FXCollections.observableArrayList(POINTALG,TREE,CIRCLE,SPONGE, ROTSQUARE, CANTOR,KOCHCURVE);
        choiceBox.setItems(list);

        ObservableList<String> list2 = FXCollections.observableArrayList("NO COLOR","BLUE","BLACK","GREEN","PURPLE","RED","BROWN","CYAN","GREY","PINK","LIME");
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



        // FALLBACK COLOR
        if(colorInput.getValue() != null){
            this.colorChoice = colorInput.getValue().toString();
        }


        if(!sizeOfSquare.getText().isEmpty()){
            timer1Border = Integer.parseInt(sizeOfSquare.getText());
            timer2Border = Integer.parseInt(sizeOfSquare.getText());
            timer3Border = Integer.parseInt(sizeOfSquare.getText());
            timer4Border = Integer.parseInt(sizeOfSquare.getText());
        }
        else{
            timer1Border = 20;
            timer2Border = 7;
            timer3Border = 7;
            timer4Border = 5;

        }

        

        Double startPosX = 0.0;
        Double startPosY = 0.0;

        if(!startPos.getText().isEmpty()){
            String[] coordinates = startPos.getText().split(",");

            startPosX = Double.parseDouble(coordinates[0]);
            startPosY = Double.parseDouble(coordinates[1]);
        }
        else{
            switch(userChoice){
                case POINTALG:
                    startPosX = 900.0;
                    startPosY = 500.0;
                    break;

                case TREE:
                    startPosX = 450.0;
                    startPosY = 370.0;
                    break;

                case CIRCLE:
                    startPosX = 475.0;
                    startPosY = 350.0;
                    break;

                case CANTOR:
                    startPosX = 10.0;
                    startPosY = 20.0;
                    break;

                case KOCHCURVE:
                    startPosX = 250.0;
                    startPosY = 350.0;
                    break;

                    default:
                        break;

            }
        }

        switch(userChoice){
            case POINTALG:
                calculatePoint(startPosX, startPosY, 3, 800);
                calculatePoint(startPosX+200, startPosY-200, 3, 800);
                final double xPosPointAlg = startPosX;
                final double yPosPointAlg = startPosY;
                timer = new Timer();
                this.checkTimerOn = true;
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

                                                      calculatePoint(xPosPointAlg, yPosPointAlg, type, 800);
                                                      calculatePoint(xPosPointAlg+200, yPosPointAlg-200, type, 800);

                                                  });
                                              }
                                          },  20,20
                );

                break;
            case TREE:
                // Oben
                drawTree(startPosX, startPosY, -90, 9, true,3.0f);

                // Unten
                drawTree(startPosX, startPosY, 90, 9, true,3.0f);

                //Links
                drawTree(startPosX, startPosY, 180, 9, true,3.0f);

                //Rechts
                drawTree(startPosX, startPosY, 0, 9, true,3.0f);



                final double xPosTree = startPosX;
                final double yPosTree = startPosY;
                timer1 = new Timer();
                this.checkTimer1On = true;
                timer1.scheduleAtFixedRate(new TimerTask() {
                                               //int i = 1;
                                               @Override
                                               public void run() {
                                                   Platform.runLater(() -> {
                                                       // your code here
                                                       timer1Counter.incrementAndGet();
                                                       System.out.println("COUNT: " + timer1Counter);

                                                       if(timer1Counter.get() >= timer1Border){
                                                           timer1.cancel();
                                                           timer1.purge();
                                                       }

                                                       drawTree(xPosTree, yPosTree, -90, 9, false,3.0f);
                                                       drawTree(xPosTree, yPosTree, 90, 9, false,3.0f);

                                                       drawTree(xPosTree, yPosTree, 180, 9, false,3.0f);
                                                       drawTree(xPosTree, yPosTree, 0, 9, false,3.0f);
        

                                                   });
                                               }
                                           },  1500,1500
                );
                break;
            case CIRCLE:
                drawCircle(startPosX,startPosY, 190.0f);
                break;
            case SPONGE:
                double size = CANVAS_WIDTH > CANVAS_HEIGHT ? (int) (CANVAS_HEIGHT * 0.8) : (int) (CANVAS_WIDTH * 0.8);
                //sponge(5,size, size, CANVAS_WIDTH / 2 - size / 2, CANVAS_HEIGHT / 2 - size / 2);
                sponge(5,size, size, CANVAS_WIDTH / 32 - size / 32, CANVAS_HEIGHT / 32 - size / 32);
                sponge(5,size, size, (CANVAS_WIDTH / 32 - size / 32) + 200, CANVAS_HEIGHT / 32 - size / 32);
                sponge(5,size, size, (CANVAS_WIDTH / 32 - size / 32) + 350, CANVAS_HEIGHT / 32 - size / 32);
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

                for (int j = 0; j < 5; j++) {
                    rotatedSquare(
                            384.0 - j * 20,
                            230.0 + j * 20,
                            sizeRotSquare + 150,
                            sizeRotSquare,
                            1);
                }

                for (int j = 0; j < 5; j++) {
                    rotatedSquare(
                            384.0 + j * 15,
                            230.0 - j * 15,
                            sizeRotSquare + 200,
                            sizeRotSquare,
                            1);
                }

                for (int j = 0; j < 5; j++) {
                    rotatedSquare(
                            384.0 - j * 20,
                            230.0 - j * 20,
                            sizeRotSquare + 200,
                            sizeRotSquare,
                            1);
                }

                /*
                        x: 384.5
                        y: 230.0
                 */

                timer2 = new Timer();
                checkTimer2On = true;

                timer2.scheduleAtFixedRate(new TimerTask() {
                                               int i = 1;
                                               @Override
                                               public void run() {
                                                   Platform.runLater(() -> {
                                                       // your code here
                                                       timer2Counter.incrementAndGet();
                                                       System.out.println("Count2: " + timer2Counter);

                                                       if(timer2Counter.get() > timer2Border){
                                                           timer2.cancel();
                                                           timer2.purge();
                                                       }

                                                       // TODO MAKE USE OF STARTX/STARTY FROM INPUT

                                                      rotatedSquare(
                                                              CANVAS_WIDTH / 2 - sizeRotSquare / 2,
                                                              CANVAS_HEIGHT / 2 - sizeRotSquare / 2 + 50,
                                                              sizeRotSquare + 150,
                                                              sizeRotSquare,
                                                              timer2Counter.get());

                                                       for (int j = 0; j < 5; j++) {
                                                           rotatedSquare(
                                                                   384.0 - j * 20,
                                                                   230.0 + j * 20,
                                                                   sizeRotSquare + 150,
                                                                   sizeRotSquare,
                                                                   timer2Counter.get());
                                                       }

                                                       for (int j = 0; j < 5; j++) {
                                                           rotatedSquare(
                                                                   384.0 + j * 15,
                                                                   230.0 - j * 15,
                                                                   sizeRotSquare + 200,
                                                                   sizeRotSquare,
                                                                   timer2Counter.get());
                                                       }

                                                       for (int j = 0; j < 5; j++) {
                                                           rotatedSquare(
                                                                   384.0 - j * 20 / 2.0,
                                                                   230.0 - j * 20 / 2.0,
                                                                   sizeRotSquare + 200,
                                                                   sizeRotSquare,
                                                                   timer2Counter.get());
                                                       }

                                                   });
                                              }
                                          },  2500,2500
                );



                break;
            case CANTOR:
                cantor(startPosX,startPosY, CANVAS_WIDTH-20, 190.0f);

                double yPosCantor = startPosY;
                final double xPosCantor = startPosX;
                timer3 = new Timer();
                this.checkTimer3On = true;
                timer3.scheduleAtFixedRate(new TimerTask() {
                                               int i = 1;
                                               int y = (int) yPosCantor;
                                               @Override
                                               public void run() {
                                                   Platform.runLater(() -> {
                                                       // your code here
                                                       timer3Counter.incrementAndGet();
                                                       System.out.println("Count3: " + timer3Counter);

                                                       if(timer3Counter.get() > timer3Border){
                                                           timer3.cancel();
                                                           timer3.purge();
                                                       }
                                                       y+= 50;

                                                       cantor(xPosCantor,y, CANVAS_WIDTH-20, 190.0f);

                                                   });
                                               }
                                           },  500,500
                );
                break;

            case KOCHCURVE:
                Coordinates c1 = new Coordinates(startPosX,startPosY);
                Coordinates c2 = new Coordinates(startPosX+400,startPosY);
                timer4 = new Timer();
                this.checkTimer4On = true;
                System.out.println("COUNTER 4 STATE: " + timer4Counter);
                timer4.scheduleAtFixedRate(new TimerTask() {
                                               int i = 1;
                                               int kochTimer = 4;
                                               @Override
                                               public void run() {
                                                   Platform.runLater(() -> {
                                                       // your code here
                                                       timer4Counter.incrementAndGet();
                                                       System.out.println("Count: " + timer4Counter);

                                                       if(timer4Counter.get() > timer4Border){
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
        // TODO FIX ERROR IN ALWAYS GOES INTO ELSE BRANCH
        System.out.println("Reset");

        if(checkTimerOn){
            timer.cancel();
            timer.purge();
            checkTimerOn = false;
        }
        else if(checkTimer1On){
            timer1.cancel();
            timer1.purge();

            checkTimer1On = false;
        }
        else if(checkTimer2On){
            timer2.cancel();
            timer2.purge();

            timer2Counter.set(1);

            checkTimer2On = false;
        }
        else if(checkTimer3On){
            timer3.cancel();
            timer3.purge();

            checkTimer3On = false;
        }
        else if(checkTimer4On){
            timer4.cancel();
            timer4.purge();
            checkTimer4On = false;
        }
        else{
            System.out.println("return");
            return;
        }

        timer1Counter.set(1);
        timer3Counter.set(1);
        timer4Counter.set(1);

        sizeOfSquare.clear();
        startPos.clear();




    }
    @FXML
    public void pauseTimer(){
        System.out.println("Pause");

        if(checkTimerOn){
            timer.cancel();
        }
        else if(checkTimer1On){
            timer1.cancel();
        }
        else if(checkTimer2On){
            timer2.cancel();
        }
        else if(checkTimer3On){
            timer3.cancel();
        }
        else if(checkTimer4On){
            timer4.cancel();
        }
        else{
            return;
        }

    }

    @FXML
    public void resumeTimer(){
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


            if(iNew >= 1000){
                iNew = Math.random() * 1000 + 20;
            }

            if(jNew >= 600){
                jNew = Math.random() * 600 + 1;
            }



            points = new Coordinates(iNew, jNew);

            retArray[k] = points;
        }




        // DRAWING
        GraphicsContext gc = drawArea.getGraphicsContext2D();

        ArrayList<Node> lines = new ArrayList<>();

        if(colorChoice.equals("NO COLOR")){
            lines = figures.init(1, gc, this.colors[(int) (Math.random() * 10) ], retArray[0].getX(),retArray[0].getY(), retArray[1].getX(), retArray[1].getY(),0, 0, 1);
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
        }
        else{
            lines = figures.init(1, gc, colorChoice, retArray[0].getX(),retArray[0].getY(), retArray[1].getX(), retArray[1].getY(),0, 0, 1);
            lines.addAll(figures.init(1,gc, colorChoice, retArray[2].getX(), retArray[2].getY(), retArray[3].getX(), retArray[3].getY(),0, 0, 1));

            lines.addAll(figures.init(1,gc, colorChoice, retArray[0].getX(), retArray[0].getY(), retArray[3].getX(), retArray[3].getY(), 0, 0, 1));
            lines.addAll(figures.init(1,gc, colorChoice, retArray[1].getX(), retArray[1].getY(), retArray[2].getX(), retArray[2].getY(), 0, 0, 1));


            gc.strokeLine(retArray[0].getX(),retArray[0].getY(), retArray[1].getX(), retArray[1].getY());
            gc.setStroke(Color.valueOf(colorChoice));

            gc.strokeLine(retArray[2].getX(), retArray[2].getY(), retArray[3].getX(), retArray[3].getY());
            gc.setStroke(Color.valueOf(colorChoice));

            gc.strokeLine(retArray[0].getX(), retArray[0].getY(), retArray[3].getX(), retArray[3].getY());
            gc.setStroke(Color.valueOf(colorChoice));

            gc.strokeLine(retArray[1].getX(), retArray[1].getY(), retArray[2].getX(), retArray[2].getY());
            gc.setStroke(Color.valueOf(colorChoice));
        }







        ArrayList<Node> ret = new ArrayList<>();

        for(Coordinates item : retArray){
            //  System.out.println(item.toString());

            double x = item.getX();
            double y = item.getY();


            if(colorChoice.equals("NO COLOR")){
                ret.addAll(figures.init(type, gc, this.colors[(int) (Math.random() * 10) ], x, y, 0.0, 0.0, 0, 0, 0));
                gc.strokeLine(x, y, 0.0, 0.0);
                gc.setStroke(Color.valueOf(this.colors[(int) (Math.random() * 10)]));
            }
            else{
                ret.addAll(figures.init(type, gc, colorChoice, x, y, 0.0, 0.0, 0, 0, 0));
                gc.strokeLine(x, y, 0.0, 0.0);
                gc.setStroke(Color.valueOf(colorChoice));
            }


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

    private void drawTree(double x1, double y1, double angle, int depth, Boolean firstCall, float widthModifier) {
        System.out.println("First CAll: " + firstCall);
        if (depth == 0) return;
        double x2 = x1 + (int) (Math.cos(Math.toRadians(angle)) * depth * 11.5);
        double y2 = y1 + (int) (Math.sin(Math.toRadians(angle)) * depth * 9.0);

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

            gc.strokeLine(x1, y1+50, x2, y2);
            gc.setStroke(Color.valueOf("BLACK"));
        }
        else{

            if(colorChoice.equals("NO COLOR")){
                lines = figures.init(1, gc, this.colors[(int) (Math.random() * 10) ], x1,y1, x2, y2, 0, 0, widthModifier);
                gc.strokeLine(x1, y1, x2, y2);
                gc.setStroke(Color.valueOf(this.colors[(int) (Math.random() * 10)]));
            }
            else{
                lines = figures.init(1, gc, colorChoice, x1,y1, x2, y2, 0, 0, widthModifier);
                gc.strokeLine(x1, y1, x2, y2);
                gc.setStroke(Color.valueOf(colorChoice));
            }

            widthModifier -= 0.5f;
            if(widthModifier < 0.5){
                widthModifier = 0.25f;
            }
        }


        for (Node itemm: lines) {
            //  System.out.println("ITEM: " +  itemm.toString());
            //    this.itemsDrawn.add(itemm);
            //     main.getRoot().getChildren().add(itemm);
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
        ArrayList<Node> lines = new ArrayList<>();


        if(colorChoice.equals("NO COLOR")){
            lines = figures.init(5, gc, this.colors[(int) (Math.random() * 10) ], x,y, 0, 0, radius, 0,0);
        }
        else{
            lines = figures.init(5, gc, colorChoice, x,y, 0, 0, radius, 0,0);
        }

        for (Node itemm: lines) {
            System.out.println("ITEM: " +  itemm.toString());
            // TODO FIX THIS SO IT LOOKS NICE AND DRAWING WITH CANVAS
            //gc.strokeOval(x, y, radius, radius);
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
            ArrayList<Node> lines = new ArrayList<>();
            if(colorChoice.equals("NO COLOR")){
                lines = figures.init(6, gc, this.colors[(int) (Math.random() * 10) ], x,y, 0, 0, 0, height, width);
            }
            else{
                lines = figures.init(6, gc, colorChoice, x,y, 0, 0, 0, height, width);
            }

            for (Node itemm: lines) {
                //  System.out.println("ITEM: " +  itemm.toString());
                //this.itemsDrawn.add(itemm);
                //main.getRoot().getChildren().add(itemm);
                gc.strokeRect(x,y,height,width);

                if(colorChoice.equals("NO COLOR")) {
                    gc.setStroke(Color.valueOf(this.colors[(int) (Math.random() * 10)]));
                }
                else{
                    gc.setStroke(Color.valueOf(colorChoice));
                }

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
        ArrayList<Node> rectangles = new ArrayList<>();


        if(colorChoice.equals("NO COLOR")){
            rectangles = figures.init(6, gc, this.colors[(int) (Math.random() * 10) ], x,y, 0, 0, 0, height, width);
        }
        else{
            rectangles = figures.init(6, gc, colorChoice, x,y, 0, 0, 0, height, width);
        }

        for (Node item: rectangles) {
            gc.strokeRect(x, y, width, height);
            gc.strokeRect(x-200,y-200,width,height);
            gc.strokeRect(x+200,y+200,width,height);

            if(colorChoice.equals("NO COLOR")){
                gc.setStroke(Color.valueOf(this.colors[(int) (Math.random() * 10)]));
            }
            else{
                gc.setStroke(Color.valueOf(colorChoice));
            }
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
            ArrayList<Node> rectangles = new ArrayList<>();

            if(colorChoice.equals("NO COLOR")){
                rectangles = figures.init(1, gc, this.colors[(int) (Math.random() * 10)], x, y, x + len, y, 0, 0, 1);
            }
            else{
                rectangles = figures.init(1, gc, colorChoice, x, y, x + len, y, 0, 0, 1);
            }

            for (Node item : rectangles) {
                gc.strokeLine(x, y, x+len, y);
                if(colorChoice.equals("NO COLOR")){
                    gc.setStroke(Color.valueOf(this.colors[(int) (Math.random() * 10)]));
                }
                else{
                    gc.setStroke(Color.valueOf(colorChoice));
                }
               // this.itemsDrawn.add(item);
               // this.main.getRoot().getChildren().add(item);
            }

            y += 20;
            radius *= 0.75f;

            cantor(x, y, len / 3, radius);
            cantor(x + len * 2 / 3, y, len / 3, radius);

            cantor(x + len * 2, y, len / 3, radius);
            cantor(x + len * 4, y +50, len / 3, radius);
            cantor(x + len * 6, y+100, len / 3, radius);
            cantor(x + len * 8, y+100, len / 3, radius);
            cantor(x + len * 10, y+75, len / 3, radius);




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
            ArrayList<Node> koch = new ArrayList<>();
            if(colorChoice.equals("NO COLOR")){
                koch = figures.init(1,gc,this.colors[(int) (Math.random() * 10)],c1.getX(),c1.getY(),c2.getX(),c2.getY(),0,0,1);
            }
            else{
                koch = figures.init(1,gc,colorChoice,c1.getX(),c1.getY(),c2.getX(),c2.getY(),0,0,1);
            }
            /*
            for (Node item : koch) {
                this.itemsDrawn.add(item);
                this.main.getRoot().getChildren().add(item);
            }
            */

            gc.setFill(Color.BLUE);

            gc.strokeLine(c1.getX(), c1.getY(), c2.getX(), c2.getY());

            if(colorChoice.equals("NO COLOR")){
                gc.setStroke(Color.valueOf(this.colors[(int) (Math.random() * 10)]));
            }
            else{
                gc.setStroke(Color.valueOf(colorChoice));
            }
        }

    }


}
