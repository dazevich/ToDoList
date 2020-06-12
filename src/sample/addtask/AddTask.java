package sample.addtask;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

import com.sun.glass.ui.EventLoop;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.Main;
import sample.com.Task;
import sample.controllers.TasksWindow;

public class AddTask {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea taskAbout;

    @FXML
    private TextField taskName;

    @FXML
    private Button addTaskBtn;

    @FXML
    private DatePicker taskDate;

    @FXML
    private Pane errorPlace;

    @FXML
    void backToTasks(MouseEvent event) {
        addTaskBtn.getScene().getWindow().hide();
        Main main = new Main();
        Stage primaryStage = new Stage();
        try {
            main.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {


        addTaskBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                String name = taskName.getText();
                String about = taskAbout.getText();
                LocalDate taskEnd = taskDate.getValue();

                if (name.isEmpty() || about.isEmpty() || taskEnd == null){
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Не все поля заполнены!");
                    error.setHeaderText("Ошибка заполнения");
                    error.setContentText("Проверьте, указали ли вы название, описание и дату завершения задачи!");
                    error.showAndWait();
                } else {
                    String error = "";
                    int nameSize = name.length();
                    if (nameSize > 30){
                        error += "Название слишком длинное. Используйте максимум 30 символов! \n";
                        Alert errorMsg = new Alert(Alert.AlertType.ERROR);
                        errorMsg.setTitle("Слишком длинное название!");
                        errorMsg.setHeaderText("Ошибка");
                        errorMsg.setContentText(error);
                        errorMsg.showAndWait();
                    } else {
                        Task task = new Task("jdbc:mysql://localhost:3306/app?useUnicode=true&serverTimezone=UTC");
                        task.addTask(name, about, taskEnd);
                        System.out.println(taskEnd);
                    }
                }



            }
        });

    }
}
