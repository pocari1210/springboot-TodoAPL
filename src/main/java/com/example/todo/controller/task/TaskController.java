package com.example.todo.controller.task;

import com.example.todo.service.task.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// コントローラーとして認識させる
@Controller
public class TaskController {

    private final TaskService taskService = new TaskService();

    // データを取得する
    // メソッドとGETの処理を行うURLを紐づける役割を担う
    @GetMapping("/tasks")
    public String list(Model model) {

        model.addAttribute("taskList", taskService.find()); //Todo
        return "tasks/list";
    }
}