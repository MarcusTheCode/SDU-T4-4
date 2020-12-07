package worldOfZuulGui.rooms;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import worldOfZuulGui.Item;
import worldOfZuulGui.Room;
import worldOfZuulGui.StatusScore;
import worldOfZuulGui.TimeClass;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLOutput;


public class Controller {

    //Singleton
    StatusScore depressionBar = StatusScore.getInstance();
    TimeClass time = TimeClass.getInstance();


    //Game information
    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label timeLabel;


    //Map
    @FXML
    private ImageView map;

    @FXML
    void showMap(KeyEvent event) {
        if(event.getCode().equals(KeyCode.M)){
            map.setVisible(true);
        }
    }

    @FXML
    void hideMap(KeyEvent event) {
        if(event.getCode().equals(KeyCode.M)){
            map.setVisible(false);
        }
    }

    //Item information text box
    @FXML
    private AnchorPane textBox;

    @FXML
    private TextArea itemInformation;

    @FXML
    void hideTextBox(MouseEvent event) throws IOException {
        textBox.setVisible(false);
        checkIfGameIsOver(event);
    }

    public void itemTextBoxShow(String itemInfo){
        textBox.setVisible(true);
        itemInformation.setText(itemInfo);
    }


    //Change room methods
    @FXML
    void goNextRoom(MouseEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();

        switch (thisStage.getTitle()) {
            case "Bedroom":
                changeRoom(Room.LivingRoom.name(), event);
                break;
            case "LivingRoom":
                changeRoom(Room.Kitchen.name(), event);
                break;
            case "Kitchen":
            case "Fridge":
                changeRoom(Room.Entry.name(), event);
                break;

        }
    }

    @FXML
    void goPrevRoom(MouseEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();

        switch (thisStage.getTitle()) {
            case "Bedroom":
                changeRoom(Room.BathRoom.name(), event);
                break;
            case "LivingRoom":
            case "BathRoom":
            case "Computer":
                changeRoom(Room.Bedroom.name(), event);
                break;
            case "Kitchen":
                changeRoom(Room.LivingRoom.name(), event);
                break;
            case "Fridge":
            case "Entry":
                changeRoom(Room.Kitchen.name(), event);
                break;

        }
    }


    //Computer room button
    @FXML
    void goToComputerRoom(MouseEvent event) throws IOException {
        changeRoom(Room.Computer.name(), event);
    }

    //Fridge interactions

    @FXML
    private Button scoreCheck;

    @FXML
    private Button interactFriesNeutral;

    @FXML
    private Button interactFriesNegative;

    @FXML
    private Button interactFriesPositive;

    @FXML
    private Text hungry;

    @FXML
    void goToFrigheRoom(MouseEvent event) throws IOException{
        changeRoom(Room.Fridge.name(),event);
    }

    @FXML
    void checkScore(MouseEvent event) throws IOException{
        scoreCheck.setDisable(true);
        scoreCheck.setVisible(false);
        hungry.setVisible(false);
    }





    //Bedroom items:
    Item bed = new Item("You lay in your bed",0.05);
    Item computer = new Item("You finished a voice chat with your friends, you feel good",-0.05);

    //Livingroom items:
    Item tv = new Item("You watch some tv",-0.05);
    Item book = new Item("You read you for a while",0.05);
    Item window = new Item("You stare out the window, sad that you can't go outside",-0.05);

    //Bathroom items:
    Item toilet = new Item("You use the toilet",0.05);
    Item bath = new Item("You take a nice long bath", 0.05);

    //Entrance items:
    Item door = new Item("You look at the door, and wonder what is outside",0.05);



    //Bedroom items
    @FXML
    void layInBed(MouseEvent event) throws IOException {
        simpleItemInteraction(bed,event);
    }


    //Computer room
    @FXML
    private TextArea textAreaComputer;

    @FXML
    void showComputerInfo(MouseEvent event) throws IOException {
        textAreaComputer.setText(computer.getItemInteractionMessage());
        if(textAreaComputer.isVisible()){
            textAreaComputer.setVisible(false);
        } else{
            textAreaComputer.setVisible(true);
        }
        if(textAreaComputer.isVisible()){
            computer.changeScore();
        }
        updateScoreText();
        checkIfGameIsOver(event);
    }


    //Bathroom items
    @FXML
    void useToilet(MouseEvent event) throws IOException {
        simpleItemInteraction(toilet,event);

    }

    @FXML
    void useBath(MouseEvent event) throws IOException {
        simpleItemInteraction(bath,event);
    }


    //Living room
    @FXML
    void useTV(MouseEvent event) throws IOException {
        simpleItemInteraction(tv,event);
    }

    @FXML
    void readBook(MouseEvent event) throws IOException {
        simpleItemInteraction(book,event);
    }

    @FXML
    void lookOutWindow(MouseEvent event) throws IOException {
        simpleItemInteraction(window,event);
    }


    //Entry
    @FXML
    void exitDoor(MouseEvent event) throws IOException {
        simpleItemInteraction(door,event);
    }


    //Initializing the scoreText
    @FXML
    void initialize() {
    progressBar.setProgress(depressionBar.getScore());
    timeLabel.setText(time.getFullTime());
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
    }


    //Change room
    public void changeRoom(String roomName, MouseEvent event) throws IOException, NullPointerException {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage)node.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource(roomName +".fxml"));
        thisStage.setTitle(roomName);
        thisStage.setScene(new Scene(root));
        timeline.stop();
        thisStage.show();
    }

    public void updateScoreText(){
        progressBar.setProgress(depressionBar.getScore());
    }

    //Time
    Timeline timeline = new Timeline(
            new KeyFrame( Duration.seconds(3),
                    event -> {
                        timeLabel.setText(time.getFullTime());
                        time.addTime();
                    }
            )
    );

    //Method to check if game is over. Java use floating points for doubles and will not hit 0 as it should
    //therefore 0.001 and not 0.
    public void checkIfGameIsOver(MouseEvent event) throws IOException {
        if(depressionBar.getScore() >= 1){
            changeRoom(Room.gameWon.name(), event);
            depressionBar.setScore(0.5); //Resets the point-system after end game
        }else if(depressionBar.getScore() <= 0.001){
            changeRoom(Room.gameLost.name(), event);
            depressionBar.setScore(0.5);
        }
    }


    public void simpleItemInteraction(Item item, MouseEvent event) throws IOException {
        if(item.getCanBeInteractedWith()) {
            item.changeScore();
            System.out.println(depressionBar.getScore());
            updateScoreText();
            itemTextBoxShow(item.getItemInteractionMessage());
        }
    }
}
