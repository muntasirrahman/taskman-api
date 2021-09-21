package org.muntasir.lab;

import io.swagger.v3.oas.annotations.Operation;
import org.muntasir.lab.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

import static javax.ws.rs.core.Response.Status.*;

@Path("/api/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskResource.class);

    private TaskDAO taskDAO;

    public TaskResource() {
        this.taskDAO = new TaskDAO();
    }

    @GET
    @Operation(description = "List of Task")
    @Path("/tasks")
    public Response viewTaskList() {
        Collection<Task> result = taskDAO.getTaskList();
        if (result.size() == 0) LOGGER.error("Empty task list");
        return Response.ok(result).build();
    }

    @GET
    @Operation(description = "Detailed of a particular task")
    @Path("/task/{taskId}")
    public Response viewTaskDetail(@PathParam("taskId") long taskId) {
        LOGGER.info("View detailed task " + taskId);
        try {
            Task task = taskDAO.getTask(taskId);
            return Response.ok(task).build();
        } catch (Exception e) {
            LOGGER.error("View task detail failed " + e.getMessage());
            return Response.status(NOT_FOUND.getStatusCode(), e.getMessage()).build();
        }
    }

    @POST
    @Operation(description = "Create a new task")
    @Path("/task")
    public Response createNewTask(Task task) {
        Task newlyAddedTask = taskDAO.addNewTask(task);
        return Response.ok(newlyAddedTask).build();
    }

    @PUT
    @Operation(description = "Modify task's description, date, or completion status")
    @Path("/task/{taskId}")
    public Response updateTask(@PathParam("taskId") long taskId, Task task) {
        try {
            Task newlyUpdatedTask = taskDAO.completeTask(taskId);
            return Response.ok(newlyUpdatedTask).build();
        } catch (Exception e) {
            LOGGER.error("Update failed " + e.getMessage());
            return Response.status(NOT_FOUND.getStatusCode(), e.getMessage()).build();
        }
    }

    @DELETE
    @Operation(description = "Delete a task")
    @Path("/task/{taskId}")
    public Response deleteTask(@PathParam("taskId") long taskId) {
        try {
            Task newlyDeletedTask = taskDAO.deleteTask(taskId);
            LOGGER.info("Task " + taskId + " is deleted.");
            return Response.ok(newlyDeletedTask).build();
        } catch (Exception e) {
            LOGGER.error("Delete failed " + e.getMessage());
            return Response.status(NOT_FOUND.getStatusCode(), e.getMessage()).build();
        }
    }

    public TaskDAO getTaskResource() {
        return taskDAO;
    }
}
