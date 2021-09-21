package org.muntasir.lab;

import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.muntasir.lab.model.Task;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Date;

import static javax.ws.rs.core.Response.Status.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(DropwizardExtensionsSupport.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TaskResourceTest {

    private static final DropwizardAppExtension<DefaultConfiguration> EXT = new DropwizardAppExtension<>(
        TaskManagerApp.class, "test-example.yaml"
    );

    static Client client;
    static String APP_URI;

    @BeforeAll
    public static void setupAll() {
        client = EXT.client();
        APP_URI = "http://localhost:" + EXT.getLocalPort() + "/api/v1";
    }

    Task newlyAddedTask;
    Task completedTask;

    @Test @Order(1)
    @DisplayName("List the existing tasks")
    void whenListAllTasks_ExpectItNotZero() {
        Response response = client.target(APP_URI +  "/tasks").request().get();
        assertEquals(OK.getStatusCode(), response.getStatus());
        Collection<Task> taskList = response.readEntity(Collection.class);

        int initialTaskListSize = taskList.size();
        assertNotEquals(0, initialTaskListSize);
    }

    @Test @Order(2)
    @DisplayName("Add a new task")
    public void whenNewTaskAdded_ExpectItsIdUpdated() {
        Task postParam = new Task();
        postParam.setDescription("New Task");
        postParam.setTargetDate(new Date());
        assertEquals(0L, postParam.getId());

        Response response = client.target(APP_URI +  "/task").request().post(Entity.json(postParam));
        newlyAddedTask = convertAndVerifyResponseNotNull(response);
        assertNotEquals(0L, newlyAddedTask.getId());
    }

    @Test @Order(3)
    @DisplayName("Find the newly added task")
    public void whenNewTaskIsAdded_ExpectItsDetailIsFound() {
        long taskId = newlyAddedTask == null ? -1 : newlyAddedTask.getId();
        Response response = client.target(APP_URI +  "/task/" + taskId).request().get();
        Task foundTask = convertAndVerifyResponseNotNull(response);
        assertEquals(newlyAddedTask, foundTask);
    }

    @Test @Order(4)
    @DisplayName("Update the task status to complete")
    public void whenTaskStatusIsUpdated_ExpectItIsChanged() {
        long taskId = newlyAddedTask == null ? -1 : newlyAddedTask.getId();
        Task emptyTask = new Task();
        Response response = client.target(APP_URI +  "/task/" + taskId).request().put(Entity.json(emptyTask));
        completedTask = convertAndVerifyResponseNotNull(response);
        assertTrue(completedTask.isCompleted());
    }

    @Test @Order(5)
    @DisplayName("Delete a task")
    public void whenATaskIsDeleted_ExpectItToNotFound() {
        long taskId = newlyAddedTask == null ? -1 : newlyAddedTask.getId();
        Response response = client.target(APP_URI +  "/task/" + taskId).request().delete();
        Task deletedTask = convertAndVerifyResponseNotNull(response);
        assertEquals(deletedTask.getId(), completedTask.getId());

        Response notFoundResponse = client.target(APP_URI +  "/task/" + taskId).request().get();
        assertEquals(NOT_FOUND.getStatusCode(), notFoundResponse.getStatus());
    }

    private Task convertAndVerifyResponseNotNull(Response response) {
        assertEquals(OK.getStatusCode(), response.getStatus());
        Task task = response.readEntity(Task.class);
        assertNotNull(task);
        return task;
    }
}
