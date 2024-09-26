package com.example.todo.controller.task;

import com.example.todo.service.task.TaskEntity;
import com.example.todo.service.task.TaskService;
import com.example.todo.service.task.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

// コントローラーとして認識させる
@Controller

@RequiredArgsConstructor

public class TaskController {

    //private final TaskService taskService = new TaskService();

    private final TaskService taskService;

//    public TaskController(TaskService taskService) {
//        this.taskService = taskService;
//    }

    // データを取得する
    // メソッドとGETの処理を行うURLを紐づける役割を担う
    @GetMapping("/tasks")
    public String list(Model model) {

        var taskList = taskService.find()

                // リストの中身を変換
                .stream()

                // TaskDTOからtoDTOメソッド参照している
                .map(TaskDTO::toDTO)

                // List型に変換
                .toList();

        model.addAttribute("taskList", taskList);
        return "tasks/list";
    }

    @GetMapping("/tasks/{id}")
    public String showDetail(@PathVariable("id") long taskId, Model model) {
        var taskEntity = taskService.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: id = " + taskId));
        model.addAttribute("task", TaskDTO.toDTO(taskEntity));
        return "tasks/detail";
    }

    @GetMapping("/tasks/creationForm")
    public String showCreationForm() {
        return "tasks/form";
    }

    @PostMapping("/tasks")
    public String create(TaskForm form) {

        var newEntity = new TaskEntity(null, form.summary(), form.description(),
                TaskStatus.valueOf(form.status()));

        taskService.create(newEntity);

        return "redirect:/tasks";
    }
}