package dacer.google.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;
import com.google.api.services.tasks.model.TaskLists;

/**
 * Author:dacer
 * Date  :Sep 10, 2013
 */
public class TaskUtils {
  public static final String LIST_NAME = "SimplePomodoro";
  private Tasks client;
  private Context mContext;
  private TaskRecorder recorder;
  
  public TaskUtils(Tasks c, Context cc) {
	client = c;
	mContext = cc;
	recorder = new TaskRecorder(mContext);
  }
  
  public void initGuideList() throws IOException{
	addTaskList(LIST_NAME);
	addTask(getListId(LIST_NAME), "Swipe to finish");
  }
  
//DB actions-------------
  public void updateDB(String listId) throws IOException{
	  List<String> titles = getTasksTitle(listId);
	  recorder.deleteAllTask();
	  for (String title : titles) {
		recorder.putTask(title, "");
	  }
  }
  
  public List<String> getTasksTitleFromDB(){
	  return recorder.getAllTasks();
  }
  

//API actions-------------
  public List<String> getTasksTitle(String TaskListId) throws IOException{
	List<String> result = new ArrayList<String>();
	List<Task> tasks =
	        client.tasks().list(TaskListId).setFields("items/title").execute().getItems();
	if (tasks != null) {
	  for (Task task : tasks) {
	    result.add(task.getTitle());
	  }
	} else {
	  result.add("No tasks.");
	}
	return result;
  }
  
  public void addTaskList(String listName) throws IOException{
	TaskList guideList = new TaskList();
	guideList.setTitle(listName);
	client.tasklists().insert(guideList).execute();
  }
  
  public void addTask(String listId, String title) throws IOException{
	Task task = new Task();
  	task.setTitle(title);	  	
  	client.tasks().insert(listId, task).execute();
  }
  
  public boolean listExist(String listName) throws IOException{
	  //Wait Change to load local file first!!!!!!!!!!!!!!!!!!!!
	TaskLists taskLists = client.tasklists().list().execute();
	boolean spListExist = false;
	for (TaskList taskList : taskLists.getItems()) {
		if(taskList.getTitle().equals(LIST_NAME)){
			spListExist = true;
		}
	}
	return spListExist;
  }
	  
  public String getListId(String listName) throws IOException{
	TaskLists taskLists = client.tasklists().list().execute();
	for (TaskList taskList : taskLists.getItems()) {
      if(taskList.getTitle().equals(listName)){
	    return taskList.getId();
	  }
	}
	return "";
  }	  
  
  
}
