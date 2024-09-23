package com.example.todo.controller.todo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// コントローラーとして認識させる
@Controller
public class TaskController {

    // データを取得する
    // メソッドとGETの処理を行うURLを紐づける役割を担う
    @GetMapping("/tasks")
    public String list() {
        return "tasks/list";
    }
}