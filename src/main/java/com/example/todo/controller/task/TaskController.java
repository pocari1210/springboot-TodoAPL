package com.example.todo.controller.task;

import com.example.todo.service.task.TaskSearchEntity;
import com.example.todo.service.task.TaskService;
import com.example.todo.service.task.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public String list(TaskSearchForm searchForm,Model model) {

        var statusEntityList = Optional.ofNullable(searchForm.status())
                .map(statusList -> statusList.stream().map(TaskStatus::valueOf).toList())
                .orElse(List.of());

        var searchEntity = new TaskSearchEntity(searchForm.summary(), statusEntityList);
        var taskList = taskService.find(searchEntity)
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

        var entity = form.toEntity(id);
        taskService.update(entity);

        return "redirect:/tasks/{id}";
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") long id) {
        taskService.delete(id);
        return "redirect:/tasks";
    }
}