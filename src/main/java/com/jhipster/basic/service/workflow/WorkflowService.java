package com.jhipster.basic.service.workflow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jhipster.basic.domain.User;
import com.jhipster.basic.repository.UserRepository;
import com.jhipster.basic.security.SecurityUtils;

@Service
@Transactional
public class WorkflowService {
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private UserRepository userRepository;
	
	public String startProcess(){
		User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
		
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("user", user);
		
		runtimeService.startProcessInstanceByKey("simpleProcess", variables);

		return processInfo();
		
	}
	
	private String processInfo() {
		List<Task> tasks = taskService.createTaskQuery().orderByTaskCreateTime().asc().list();
		
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("Number of process definitions : "
				+ repositoryService.createProcessDefinitionQuery().count() + "--> Tasks >> ");

		for (Task task : tasks) {
			stringBuilder
					.append(task + " | Assignee: " + task.getAssignee() + " | Description: " + task.getDescription());
		}

		return stringBuilder.toString();
	}
	
	public List<Task> getTasks() {
		User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
		
		return taskService.createTaskQuery().taskAssignee(user.getFirstName()).list();
	}
	
	public void completeTask(String taskId) {
		taskService.complete(taskId);
	}

}
