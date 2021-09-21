package org.muntasir.lab;

import org.muntasir.lab.model.Task;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class TaskDAO {

    private final HashMap<Long, Task> taskMap = new HashMap<>();
    public AtomicLong counter;

    public TaskDAO() {
        init();
        counter = new AtomicLong(3);
    }

    private void init() {
        String[] description = {
                "Learn React",
                "Profit"
        };

        for (int i = 0; i < description.length; i++) {
            Task task = new Task(i, description[i], new Date());
            taskMap.put((long) i, task);
        }
    }

    public Collection<Task> getTaskList() {
        return taskMap.values()
                .stream()
                .filter(task -> !task.isCompleted())
                .collect(Collectors.toList());
    }

    public Task getTask(long taskId) throws Exception {
        if (!taskMap.containsKey(taskId)) throw new Exception("Task ID " + taskId + " is not found");
        return taskMap.get(taskId);
    }


    public Task addNewTask(Task task) {
        long taskId = counter.incrementAndGet();
        task.setId(taskId);
        taskMap.put(task.getId(), task);
        return task;
    }

    public Task completeTask(long taskId) throws Exception {
        if (!taskMap.containsKey(taskId)) throw new Exception("Task ID " + taskId + " is not found");
        Task task = taskMap.get(taskId);
        task.setCompleted(true);
        return task;
    }

    public Task deleteTask(long taskId) throws Exception {
        if (!taskMap.containsKey(taskId)) throw new Exception("Task ID " + taskId + " is not found");
        Task task = taskMap.get(taskId);
        taskMap.remove(taskId);
        return task;
    }

    public int getMapSize() {
        return this.taskMap.size();
    }
}
