package br.ce.wcaquino.taskbackend.controller;

import br.ce.wcaquino.taskbackend.model.Task;
import br.ce.wcaquino.taskbackend.repo.TaskRepo;
import br.ce.wcaquino.taskbackend.utils.ValidationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @Mock
    private TaskRepo taskRepo;

    @InjectMocks
    private TaskController controller;

    @Test
    void naoDeveSalvarTarefaSemDescrciao() throws ValidationException {
        Task task = new Task();
        task.setDueDate(LocalDate.now());
        try {
            controller.save(task);
        }
        catch (ValidationException e) {
            assertEquals("Fill the task description", e.getMessage());
        }
    }

    @Test
    void naoDeveSalvarTarefaSemData() throws ValidationException {
        Task task = new Task();
        task.setTask("Description");
        try {
            controller.save(task);
        }
        catch (ValidationException e) {
            assertEquals("Fill the due date", e.getMessage());
        }
    }

    @Test
    void naoDeveSalvarTarefaComDataPassada() throws ValidationException {
        Task task = new Task();
        task.setTask("Description");
        task.setDueDate(LocalDate.now().minusDays(1));
        try {
            controller.save(task);
        }
        catch (ValidationException e) {
            assertEquals("Due date must not be in past", e.getMessage());
        }
    }

    @Test
    void deveSalvarTarefaComSucesso() throws ValidationException {
        Task task = new Task();
        task.setTask("Description");
        task.setDueDate(LocalDate.now().plusDays(1));
        Mockito.when(taskRepo.save(task)).thenReturn(task);
        ResponseEntity<Task> responseEntity = controller.save(task);

        assertEquals(responseEntity.getBody(), task);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
    }
}