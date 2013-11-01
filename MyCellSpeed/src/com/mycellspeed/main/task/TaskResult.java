package com.mycellspeed.main.task;

public class TaskResult {
	
	private Boolean taskSuccess;
	private String feedback;
	
	public TaskResult() {}
	
	public Boolean isTaskSuccess() { return taskSuccess; }
	public void taskSuccess() { taskSuccess = true; }
	public void taskFail() { taskSuccess = false; }
	
	public String getFeedback() { return feedback; }
	public void setFeedback(String value) { feedback = value; }
}
