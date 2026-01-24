package bekezhan.io.taskmanagement.service;

import bekezhan.io.taskmanagement.dto.TaskRequestDTO;
import bekezhan.io.taskmanagement.entity.Task;
import bekezhan.io.taskmanagement.mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TaskService {

    private final Map<String, Task> taskStorage = new ConcurrentHashMap<>();

    @Autowired
    private TaskMapper taskMapper;

    public Task createTask(String title, String description) {
        Task task = new Task(title, description);
        taskStorage.put(task.getId(), task);
        return task;
    }

    /**
     * Создание задачи через DTO с использованием маппера
     */
    public Task createTaskFromDTO(TaskRequestDTO requestDTO) {
        Task task = taskMapper.toEntity(requestDTO);
        task.setId(java.util.UUID.randomUUID().toString());
        task.setCreatedAt(java.time.LocalDateTime.now());
        task.setUpdatedAt(java.time.LocalDateTime.now());
        if (task.getStatus() == null) {
            task.setStatus(Task.TaskStatus.PENDING);
        }
        taskStorage.put(task.getId(), task);
        return task;
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(taskStorage.values());
    }

    public Optional<Task> getTaskById(String id) {
        return Optional.ofNullable(taskStorage.get(id));
    }

    public Optional<Task> updateTaskFromDTO(String id, TaskRequestDTO requestDTO) {
        Task task = taskStorage.get(id);
        if (task != null) {
            taskMapper.updateEntityFromDTO(requestDTO, task);
            return Optional.of(task);
        }
        return Optional.empty();
    }

    public Optional<Task> updateTask(String id, String title, String description, Task.TaskStatus status) {
        Task task = taskStorage.get(id);
        if (task != null) {
            if (title != null) {
                task.setTitle(title);
            }
            if (description != null) {
                task.setDescription(description);
            }
            if (status != null) {
                task.setStatus(status);
            }
            return Optional.of(task);
        }
        return Optional.empty();
    }

    public boolean deleteTask(String id) {
        return taskStorage.remove(id) != null;
    }

    public long getTaskCount() {
        return taskStorage.size();
    }

    public Task processSlowTask(String title, String description) throws InterruptedException {
        Thread.sleep(3000); // 3 секунды задержки
        return createTask(title, description);
    }

    // Метод для генерации ошибки
    public Task processErrorTask(String title, String description) {
        throw new RuntimeException("Task processing failed: Internal error occurred");
    }
}