package sample.controllers;

import java.awt.*;
import java.net.URL;
import java.sql.Time;
import java.time.Duration;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.concurrent.Flow;

import com.sun.javafx.cursor.CursorType;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import sample.com.Task;

public class TasksWindow
{
    private LinkedList<LinkedList> tasks = new LinkedList<>();
    private String url = "jdbc:mysql://localhost:3306/app?useUnicode=true&serverTimezone=UTC";
    private String login = "root";
    private String password = "";
    public static int taskCount;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ScrollPane scroll;

    @FXML
    private TilePane tasksPlace;

    @FXML
    private Text tasksCountText;

    @FXML
    private Button addTask;

    @FXML
    void addTaskEvent(ActionEvent event) {
        addTask.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/addtask/addtask.fxml"));
        try {
            loader.load();
        } catch (Exception e){
            System.out.println("Ошибка открытия окна AddTask: " + e);
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.getIcons().add(new Image("/sample/assest/logo.png"));
        stage.showAndWait();
    }

    @FXML
    void initialize() {
        Task task = new Task(this.url);
        this.tasks = task.getTasks();

        addTask.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                addTask.setText("Новая задача");
            }
        });

        addTask.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                addTask.setText("Добавить задачу");
            }
        });

        this.taskCount = this.tasks.size();
        tasksCountText.setText("Всего задач: " + this.taskCount);

        tasksPlace.setHgap(10);
        tasksPlace.setVgap(10);
        tasksPlace.setAlignment(Pos.CENTER);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(10.0);
        shadow.setOffsetX(2);
        shadow.setOffsetY(2);
        shadow.setBlurType(BlurType.ONE_PASS_BOX);
        shadow.setColor(Color.web("#cccccc"));

        DropShadow shadowEntered = new DropShadow();
        shadowEntered.setRadius(0);
        shadowEntered.setOffsetX(0);
        shadowEntered.setOffsetY(0);
        shadowEntered.setBlurType(BlurType.ONE_PASS_BOX);
        shadowEntered.setColor(Color.web("#ffffff"));

        if (this.tasks.isEmpty()){
            Text noTask = new Text("У вас нет ни одной задачи");
            tasksPlace.getChildren().add(noTask);
        } else {
            for (int i = 0; i < this.tasks.size(); i++){

                FlowPane pane = new FlowPane();
                pane.setOrientation(Orientation.HORIZONTAL);
                pane.setAlignment(Pos.CENTER);
                pane.setStyle("-fx-background-color: #FFFFFF");
                pane.setPrefSize(210,240);

                LinkedList<String> tmp = this.tasks.get(i);

                TilePane task_body = new TilePane();
                task_body.setVgap(2);
                task_body.setAlignment(Pos.TOP_CENTER);
                task_body.setOrientation(Orientation.HORIZONTAL);
                task_body.setStyle("-fx-background-color: #FFFFFF");
                task_body.setPrefSize(210,160);

                Text name = new Text(tmp.get(0));
                name.setFont(new Font("Comic Sans", 15));
                name.setWrappingWidth(180);
                name.setTextAlignment(TextAlignment.CENTER);

                Text date = new Text(tmp.get(2));
                date.setFont(new Font("Comic Sans", 12));
                date.setWrappingWidth(180);
                date.setTextAlignment(TextAlignment.CENTER);

                Text about = new Text(tmp.get(1));
                about.setFont(new Font("Comic Sans", 12));
                about.setWrappingWidth(180);
                about.setTextAlignment(TextAlignment.CENTER);

                Button done = new Button("завершить");
                done.setStyle("-fx-background-color:  #23cba7");
                done.setTextFill(Paint.valueOf("#FFFFFF"));

                done.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {

                        task.deleteTask(tmp.get(4));
                        Alert taskDeleted = new Alert(Alert.AlertType.INFORMATION);
                        taskDeleted.setHeaderText("Задача успешно завершена");
                        taskDeleted.setContentText("Задача " + tmp.get(0) + " была завершена");
                        taskDeleted.showAndWait();
                        tasksPlace.getChildren().remove(pane);

                        TasksWindow taskW = new TasksWindow();
                        taskW.taskCount--;
                        tasksCountText.setText("Всего задач: " + taskW.taskCount);
                    }
                });

                task_body.getChildren().addAll(name, date, about);
                pane.getChildren().addAll(task_body, done);
                pane.setEffect(shadowEntered);
                pane.setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        pane.setEffect(shadow);
                    }
                });
                pane.setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        pane.setEffect(shadowEntered);
                    }
                });

                tasksPlace.getChildren().add(pane);
            }
        }

    }
}
