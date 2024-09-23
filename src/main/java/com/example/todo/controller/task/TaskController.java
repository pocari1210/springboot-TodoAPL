package com.example.todo.controller.task;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

// コントローラーとして認識させる
@Controller
public class TaskController {

    // データを取得する
    // メソッドとGETの処理を行うURLを紐づける役割を担う
    @GetMapping("/tasks")
    public String list(Model model) {
        var task1 = new TaskDTO(
                1L,
                "Spring Boot を学ぶ",
                "TODO アプリケーションを作ってみる",
                "TODO"
        );
        var task2 = new TaskDTO(
                2L,
                "Spring Security を学ぶ",
                "ログイン機能を作ってみる",
                "TODO"
        );

        var taskList = List.of(task1, task2);

        model.addAttribute("taskList", taskList);
        return "tasks/list";
    }
}