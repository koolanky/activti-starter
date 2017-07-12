package com.jhipster.basic.web.rest;

import java.util.List;

import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jhipster.basic.service.workflow.WorkflowService;

@RestController
@RequestMapping("/api")
public class WorkflowResource {

	@Autowired
	private WorkflowService workflowService;
	
	@RequestMapping("/process")
	public String startProcessInstance(){
		return workflowService.startProcess();
	}
	
	@RequestMapping("/tasks/")
	public String getMyTasks(){
		List<Task> tasks=workflowService.getTasks();
		return tasks.toString();
	}
	
	@RequestMapping(value = "/completetask")
	public String completeTask(@RequestParam String id) {
		workflowService.completeTask(id);
		return "Task with id " + id + " has been completed!";
	}
	
}
