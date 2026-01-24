package bekezhan.io.taskmanagement.dto;

import bekezhan.io.taskmanagement.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequestDTO {
    private String title;
    private String description;
    private Task.TaskStatus status;

    public TaskRequestDTO(String title, String description) {
        this.title = title;
        this.description = description;
    }
}