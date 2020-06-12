package sample.com;

import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.util.LinkedList;

public class Task
{

    private String login = "root";
    private String password = "";
    private String url;
    private Connection connection;
    private LinkedList<LinkedList> tasks = new LinkedList<>();

    public Task(String url)
    {
        try {
            this.connection = DriverManager.getConnection(url, login, password);
            System.out.println("Подключение установлено");

            this.url = url;
            this.login = login;
            this.password = password;

        } catch (Exception e){
            System.out.println("Подключение не выполнено, ошибка: " + e.getMessage());
        }
    }

    public LinkedList<LinkedList> getTasks()
    {
        LinkedList<LinkedList> task = this.tasks;

        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM tasks;");
            while (resultSet.next()){

                LinkedList<String> data = new LinkedList<>();

                String name = resultSet.getString("name");
                String about = resultSet.getString("about");
                String time = resultSet.getDate("data").toString();
                String status = resultSet.getString("status");
                String id = resultSet.getString("id");

                data.add(name);
                data.add(about);
                data.add(time);
                data.add(status);
                data.add(id);

                this.tasks.add(data);
            }
        } catch (SQLException e){
            System.out.println("Не удалось создать объект statment. Причина: " + e);
        }
        return task;

    }

    public void addTask(String name, String about, LocalDate date)
    {
        Date newDate = Date.valueOf(date);
        try {
            String query = "INSERT INTO tasks(name, about, data) VALUES(?,?,?)";
            PreparedStatement statement = this.connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2,about);
            statement.setDate(3,newDate);
            int result = statement.executeUpdate();

            Alert done = new Alert(Alert.AlertType.CONFIRMATION);
            done.setTitle("Отлично");
            done.setHeaderText("Задача успешно добавлена. Выполните её до " + newDate);
            done.setContentText(about);
            done.showAndWait();

        } catch (SQLException e){
            System.out.println("Не удалось добавить задачу, ошибка " + e);
        }
    }

    public void deleteTask(String id)
    {
        String query = "DELETE FROM tasks WHERE id=?";
        try {
            PreparedStatement statement = this.connection.prepareStatement(query);
            statement.setString(1, id);
            int result = statement.executeUpdate();

        } catch (SQLException throwables) {
            System.out.println("Не удалось удалить задачу из-за " + throwables);
        }
    }

    private void stopConnection()
    {
        try {
            this.connection.close();
        } catch (SQLException e){
            System.out.println("Подключение не остановлено. Ошибка " + e);
        }
    }




}
