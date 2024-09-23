package com.example.todo.service.task;

import java.util.List;

public class TaskService {

    public List<TaskEntity> find() {
        var task1 = new TaskEntity(
                1L,
                "Spring Boot を学ぶ",
                "TODO アプリケーションを作ってみる",
                TaskStatus.TODO
        );
        var task2 = new TaskEntity(
                2L,
                "Spring Security を学ぶ",
                "ログイン機能を作ってみる",
                TaskStatus.DOING
        );

        return List.of(task1, task2);
    }
}