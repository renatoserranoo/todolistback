package com.project.todolistback.controller;

import com.project.todolistback.entity.ToDo;
import com.project.todolistback.service.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
@CrossOrigin("*")
public class ToDoController {

    @Autowired
    private ToDoService toDoService;

    @PostMapping
    public List<ToDo> create(@RequestBody  ToDo todo){
        return toDoService.create(todo);
    }

    @GetMapping
    public List<ToDo> list(){
        return toDoService.list();
    }

    @PutMapping
    public List<ToDo> update(@RequestBody ToDo todo){
        return toDoService.update(todo);
    }

    @DeleteMapping("/{id}")
    public List<ToDo> delete(@PathVariable Long id){
        return toDoService.delete(id);
    }

    @PutMapping("/{id}/reorder/{direction}")
    public List<ToDo> reorder(@PathVariable Long id, @PathVariable String direction) {
        return toDoService.reorder(id, direction);
    }

    @PutMapping("/{id}/reorder-to/{position}")
    public List<ToDo> reorderTo(@PathVariable Long id, @PathVariable Integer position) {
        return toDoService.reorderTo(id, position);
    }
}
