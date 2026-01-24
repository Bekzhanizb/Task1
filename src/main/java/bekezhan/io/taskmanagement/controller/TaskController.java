package bekezhan.io.taskmanagement.controller;

import bekezhan.io.taskmanagement.dto.ApiResponseDTO;
import bekezhan.io.taskmanagement.dto.TaskRequestDTO;
import bekezhan.io.taskmanagement.dto.TaskResponseDTO;
import bekezhan.io.taskmanagement.entity.Task;
import bekezhan.io.taskmanagement.mapper.TaskMapper;
import bekezhan.io.taskmanagement.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskMapper taskMapper;


    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskRequestDTO request) {
        Task task = taskService.createTaskFromDTO(request);
        TaskResponseDTO response = taskMapper.toResponseDTO(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        List<TaskResponseDTO> responseDTOs = taskMapper.toResponseDTOList(tasks);
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable String id) {
        return taskService.getTaskById(id)
                .map(taskMapper::toResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(
            @PathVariable String id,
            @RequestBody TaskRequestDTO request) {
        return taskService.updateTaskFromDTO(id, request)
                .map(taskMapper::toResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        boolean deleted = taskService.deleteTask(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/ok")
    public ResponseEntity<ApiResponseDTO> ok() {
        ApiResponseDTO response = ApiResponseDTO.builder()
                .status("OK")
                .message("Request processed successfully")
                .data(null)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/slow")
    public ResponseEntity<ApiResponseDTO> slow() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        Thread.sleep(3000);
        long endTime = System.currentTimeMillis();

        ApiResponseDTO response = ApiResponseDTO.builder()
                .status("OK")
                .message("Slow request processed")
                .processingTime(endTime - startTime)
                .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/error")
    public ResponseEntity<ApiResponseDTO> error() {
        throw new RuntimeException("Intentional error for testing purposes");
    }
}