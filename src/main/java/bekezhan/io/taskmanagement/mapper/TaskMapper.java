package bekezhan.io.taskmanagement.mapper;

import bekezhan.io.taskmanagement.dto.TaskRequestDTO;
import bekezhan.io.taskmanagement.dto.TaskResponseDTO;
import bekezhan.io.taskmanagement.entity.Task;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskResponseDTO toResponseDTO(Task task);

    List<TaskResponseDTO> toResponseDTOList(List<Task> tasks);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Task toEntity(TaskRequestDTO requestDTO);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    void updateEntityFromDTO(TaskRequestDTO requestDTO, @MappingTarget Task task);
}