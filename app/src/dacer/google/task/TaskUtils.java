package dacer.google.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.Context;

import com.dacer.simplepomodoro.R;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.Task;

import dacer.settinghelper.SettingUtility;

/**
 * Author:dacer
 * Date  :Sep 13, 2013
 * Used to deal with data between local and web
 */
public class TaskUtils {
	public static final String LIST_NAME = "SimplePomodoro";

	public static void initWebList(Context c,Tasks tasks) throws IOException{
		TaskWebUtils tWebUtils;
		tWebUtils = new TaskWebUtils(tasks, c);
		TaskLocalUtils tLocalUtils = new TaskLocalUtils(c);
		if(tWebUtils.listExistOnWeb(LIST_NAME)){
			tLocalUtils.deleteList();
			String listId = tWebUtils.getListIdFromWeb(LIST_NAME);
			SettingUtility.setTaskListId(listId);
		}else{
			tLocalUtils.deleteList();
			tWebUtils.addTaskListToWeb(LIST_NAME);
			String listId = tWebUtils.getListIdFromWeb(LIST_NAME);
			SettingUtility.setTaskListId(listId);
			addTaskByTitle(c.getString(R.string.i_from_web), tWebUtils, listId);
		}
	  }
	
	public static void initLocalList(Context c){
		TaskLocalUtils tLocalUtils = new TaskLocalUtils(c);
		tLocalUtils.addNewTask(c.getString(R.string.swipe_right_to_finish));
		tLocalUtils.addNewTask(c.getString(R.string.click_plus_to_add_task));
		tLocalUtils.addNewTask(c.getString(R.string.hold_plus_to_change_account));
		tLocalUtils.addNewTask(c.getString(R.string.pull_to_sync));
		SettingUtility.setFirstStart(false);
	}
	
	public static void addTaskByTitle(String title,TaskWebUtils tWebUtils,String listId) throws IOException{
		Task t = new Task();
		t.setTitle(title);
		tWebUtils.addTaskToWeb(listId, t);
	}
	public static void updateDB(String listId,Context c,Tasks tasks) throws IOException{
		//Read http://idacer.tk/?p=42
		  
		TaskLocalUtils tLocalUtils;
		TaskWebUtils tWebUtils;
		tLocalUtils = new TaskLocalUtils(c);
		tWebUtils = new TaskWebUtils(tasks, c);
		
		//FIRST Push and delete new tasks(identifrer == 0) in local and delete in DB
		HashMap<Integer, Task> localNewTaskMap =  tLocalUtils.getLocalNewTaskMap();
		Iterator<Integer> keySetIterator = localNewTaskMap.keySet().iterator();
	    while(keySetIterator.hasNext()){
	    	Integer db_id = keySetIterator.next();
	    	Task t = localNewTaskMap.get(db_id);
	    	t.setId(null);
	    	tWebUtils.addTaskToWeb(listId, t);//may receive invalid listId
	    	tLocalUtils.deleteTaskInDBTrue(db_id);
	    	listId = SettingUtility.getTaskListId();//add it because listid maybe updated in this loop
	    }
	    
	    //SECOND
	    List<Task> localTaskList = tLocalUtils.getTasksFromDB();
	    List<Task> webTaskList = tWebUtils.getTasksFromWeb(listId);//may receive invalid listId
	    List<Task> waitAddToLocalDB = new ArrayList<Task>();
	    if(!webTaskList.isEmpty()){
	    	for(Task t : webTaskList){
		    	boolean exist = false;
		    	for(Task tLocal : localTaskList){
		    		if(tLocal.getId().equals(t.getId())){
		    			exist = true;
		    		}
		    	}
		    	if(!exist){
		    		waitAddToLocalDB.add(t);
		    	}
		    }
	    } 
	    if(!waitAddToLocalDB.isEmpty()){
		    tLocalUtils.putTasksToDB(waitAddToLocalDB);
		    }
	    
	    //THIRD
	    localTaskList = tLocalUtils.getTasksFromDB();
	    for(Task tLocal : localTaskList){
	    	for(Task tWeb : webTaskList){
	    		if(tLocal.getId().equals(tWeb.getId())){
	    			long tLocalUpdateTime = tLocal.getUpdated().getValue();
	    			long tWebUpdateTime = tWeb.getUpdated().getValue();
	    			if(tLocalUpdateTime > tWebUpdateTime){
	    				//update tLocal to web
	    				tWebUtils.updateTasktoWeb(listId, tLocal);
	    			}else if(tLocalUpdateTime < tWebUpdateTime){
	    				//update tWeb to local db
	    				tLocalUtils.updateTaskLocal(tWeb);
	    			}
	    		}
	    	}
	    }
	  }
}
