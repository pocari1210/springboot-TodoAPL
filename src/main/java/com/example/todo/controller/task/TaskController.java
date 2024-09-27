package com.example.todo.controller.task;

import com.example.todo.service.task.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

// コントローラーとして認識させる
@Controller

@RequiredArgsConstructor

@RequestMapping("/tasks")

public class TaskController {

    //private final TaskService taskService = new TaskService();

    private final TaskService taskService;

//    public TaskController(TaskService taskService) {
//        this.taskService = taskService;
//    }

    // データを取得する
    // メソッドとGETの処理を行うURLを紐づける役割を担う
    @GetMapping
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

    @GetMapping("/{id}")
    public String showDetail(@PathVariable("id") long taskId, Model model) {
        var taskEntity = taskService.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: id = " + taskId));
        model.addAttribute("task", TaskDTO.toDTO(taskEntity));
        return "tasks/detail";
    }

    @GetMapping("/creationForm")
    public String showCreationForm(@ModelAttribute TaskForm form){
        return "tasks/form";
    }

    @PostMapping

        public String create(@Validated TaskForm form, BindingResult bindingResult) {
            if (bindingResult.hasErrors()) {
                return showCreationForm(form);
            }

        taskService.create(form.toEntity());

        return "redirect:/tasks";
    }

    @GetMapping("/{id}/editForm")
    public String showEditForm(@PathVariable("id") long id, Model model) {
        var form = new TaskForm("hoge", "hogehoge", "TODO");
        model.addAttribute("taskForm", form);
        return "tasks/form";
    }
}