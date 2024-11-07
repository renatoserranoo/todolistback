package com.project.todolistback.service;

import com.project.todolistback.entity.ToDo;
import com.project.todolistback.repository.ToDoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ToDoService {

    @Autowired
    private ToDoRepository todoRepository;

    public List<ToDo> create(ToDo todo){
        Optional<ToDo> existingTask = todoRepository.findByNome(todo.getNome());
        if (existingTask.isPresent()) {
            throw new RuntimeException("Erro ao salvar a tarefa. Verifique se o nome já existe.");
        }
        Integer maxOrder = todoRepository.findMaxOrder();
        todo.setOrdem(maxOrder + 1);
        todoRepository.save(todo);
        return list();
    }

    public List<ToDo> list(){
        return todoRepository.findAll();
    }

    public List<ToDo> update(ToDo todo){
        Optional<ToDo> existingTask = todoRepository.findByNome(todo.getNome());
        if (existingTask.isPresent() &&!existingTask.get().getId().equals(todo.getId())) {
            throw new RuntimeException("Erro ao salvar a tarefa. Verifique se o nome já existe.");
        }
        todoRepository.save(todo);
        return list();
    }

    public List<ToDo> delete(Long id){
        todoRepository.deleteById(id);
        return list();
    }

    @Transactional
    public List<ToDo> reorder(Long taskId, String direction) {
        ToDo taskToMove = todoRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        Integer currentOrder = taskToMove.getOrdem();
        Integer newOrder;

        if ("up".equals(direction) && currentOrder > 1) {
            newOrder = currentOrder - 1;
        } else if ("down".equals(direction)) {
            newOrder = currentOrder + 1;
        } else {
            return list();
        }

        List<ToDo> tasksToUpdate = todoRepository.findByOrdemBetween(
                Math.min(currentOrder, newOrder),
                Math.max(currentOrder, newOrder)
        );

        for (ToDo task : tasksToUpdate) {
            if (task.getId().equals(taskId)) {
                task.setOrdem(newOrder);
            } else {
                task.setOrdem("up".equals(direction) ?
                        task.getOrdem() + 1 : task.getOrdem() - 1);
            }
            todoRepository.save(task);
        }

        return list();
    }

    @Transactional
    public List<ToDo> reorderTo(Long taskId, Integer newPosition) {
        ToDo taskToMove = todoRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        Integer currentPosition = taskToMove.getOrdem();
        if (currentPosition.equals(newPosition)) {
            return list();
        }

        List<ToDo> tasksToUpdate = todoRepository.findByOrdemBetween(
                Math.min(currentPosition, newPosition),
                Math.max(currentPosition, newPosition)
        );

        if (currentPosition < newPosition) {
            for (ToDo task : tasksToUpdate) {
                if (task.getId().equals(taskId)) {
                    task.setOrdem(newPosition);
                } else if (task.getOrdem() <= newPosition) {
                    task.setOrdem(task.getOrdem() - 1);
                }
            }
        } else {
            for (ToDo task : tasksToUpdate) {
                if (task.getId().equals(taskId)) {
                    task.setOrdem(newPosition);
                } else if (task.getOrdem() >= newPosition) {
                    task.setOrdem(task.getOrdem() + 1);
                }
            }
        }

        tasksToUpdate.forEach(todoRepository::save);
        return list();
    }
}
