package dacer.google.task;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import android.content.Context;
import android.util.Log;

import com.google.api.client.util.DateTime;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;
import com.google.api.services.tasks.model.TaskLists;

import dacer.settinghelper.SettingUtility;

/**
 * Author:dacer
 * Date  :Sep 10, 2013
 */
public class TaskWebUtils {
  private Tasks client;
  private Context mContext;
  
  public TaskWebUtils(Tasks task, Context cc) {
	client = task;
	mContext = cc;
  }
  
  


//API actions-------------
//  public List<String> getTasksTitleFromWeb(String TaskListId) throws IOException{
//	List<String> result = new ArrayList<String>();
//	List<Task> tasks =
//	        client.tasks().list(TaskListId).setFields("items/title").execute().getItems();
//	if (tasks != null) {
//	  for (Task task : tasks) {
//	    result.add(task.getTitle());
//	  }
//	} else {
//	  result.add("No tasks.");
//	}
//	return result;
//  }
  
  public void addTaskToWeb(String listId, Task task) throws IOException{	
	  if(SettingUtility.getTaskListId().equals("0")){ //Get web list id and save to local
		  TaskUtils.initWebList(mContext, client);
		  listId = SettingUtility.getTaskListId();
	  }
	  try {
		client.tasks().insert(listId, task).execute();
	  } catch (IOException e) {
			// TODO Auto-generated catch block
		e.printStackTrace();
		Log.e("addTaskError--->", e.getMessage());
		if(is404error(e.getMessage())){
			TaskUtils.initWebList(mContext,client);  //[webListId error]Update web list id 
			addTaskToWeb(SettingUtility.getTaskListId(), task);
		}else {
			throw e;
		}
	}
  }
  
  public void updateTasktoWeb(String listId,Task t) throws IOException{
//	  Log.e("updateTask----->",".");
//	  Log.e("ID-->",t.getId());
//	  Log.e("listid-->",listId);
//	  Log.e("completed-->",t.getCompleted().toStringRfc3339());
//	  Log.e("deleted-->",String.valueOf(t.getDeleted()));
//	  Log.e("UpdateTime-->",t.getUpdated().toStringRfc3339());
	  client.tasks().update(listId, t.getId(),t).execute();
  }
  
  public List<Task> getTasksFromWeb(String listId) throws IOException{
	if(SettingUtility.getTaskListId().equals("0")){ //Get web list id and save to local
	  TaskUtils.initWebList(mContext, client);
	  listId = SettingUtility.getTaskListId();
	}
	com.google.api.services.tasks.model.Tasks tasks = new 
			com.google.api.services.tasks.model.Tasks();
	try {
		tasks = client.tasks().list(listId)
		.setShowDeleted(true)
		.setShowCompleted(true)
		.setShowHidden(false).execute();
	} catch (IOException e) {  // Draw snake and add feet(chinese proverbs XD)
		// TODO Auto-generated catch block
		e.printStackTrace();
		Log.e("getTask----->", e.getMessage());
		if(is404error(e.getMessage())){
			Log.e("getTasksError--->", e.getMessage());
			TaskUtils.initWebList(mContext,client);
			return getTasksFromWeb(SettingUtility.getTaskListId());
		}else{
			throw e;
		}
	}
    return tasks.getItems();
  }
	  
  public void addTaskListToWeb(String listName) throws IOException{
	TaskList guideList = new TaskList();
	guideList.setTitle(listName);
	client.tasklists().insert(guideList).execute();
  }
  
  
  
  //It seems google task api only can upload single task once,F*CK!!!
//  public void addTasksToWeb(String listId, List<Task> taskList) throws IOException{	
//	for(Task t : taskList){
//		client.tasks().insert(listId, t).execute();
//	}
//  }
  
  public boolean listExistOnWeb(String listName) throws IOException{
	TaskLists taskLists = client.tasklists().list().execute();
	boolean spListExist = false;
	for (TaskList taskList : taskLists.getItems()) {
		if(taskList.getTitle().equals(TaskUtils.LIST_NAME)){
			spListExist = true;
		}
	}
	return spListExist;
  }
	  
  public String getListIdFromWeb(String listName) throws IOException{
	TaskLists taskLists = client.tasklists().list().execute();
	for (TaskList taskList : taskLists.getItems()) {
      if(taskList.getTitle().equals(listName)){
	    return taskList.getId();
	  }
	}
	return "";
  }	  

  
  
  private static boolean is404error(String str){ 
	    return str.contains("404"); 
	} 
}
