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

        var taskDTO = taskService.findById(taskId)
                .map(TaskDTO::toDTO)
                .orElseThrow(TaskNotFoundException::new);

        model.addAttribute("task", taskDTO);

        return "tasks/detail";
    }

    @GetMapping("/creationForm")
    public String showCreationForm(@ModelAttribute TaskForm form, Model model) {
        model.addAttribute("mode", "CREATE");
        return "tasks/form";
    }

    @PostMapping

        public String create(@Validated TaskForm form, BindingResult bindingResult, Model model) {
            if (bindingResult.hasErrors()) {
                return showCreationForm(form, model);
            }

        taskService.create(form.toEntity());

        return "redirect:/tasks";
    }

    @GetMapping("/{id}/editForm")
    public String showEditForm(@PathVariable("id") long id, Model model) {

        var form = taskService.findById(id)
                .map(TaskForm::fromEntity)
                .orElseThrow(TaskNotFoundException::new);

        model.addAttribute("taskForm", form);
        model.addAttribute("mode", "EDIT");

        return "tasks/form";
    }

    @PutMapping("{id}") // PUT /tasks/{id}
    public String update(
            @PathVariable("id") long id,
            @Validated @ModelAttribute TaskForm form,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "EDIT");
            return "tasks/form";
        }
        return "redirect:/tasks/{id}";
    }
}