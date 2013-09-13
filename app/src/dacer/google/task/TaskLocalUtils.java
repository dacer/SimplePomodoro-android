package dacer.google.task;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import android.content.Context;
import android.database.Cursor;

import com.google.api.client.util.DateTime;
import com.google.api.services.tasks.model.Task;

/**
 * Author:dacer
 * Date  :Sep 13, 2013
 */
public class TaskLocalUtils {

	private TaskRecorder recorder;

	public TaskLocalUtils(Context c) {
		// TODO Auto-generated constructor stub
		recorder = new TaskRecorder(c);
	}
	
	
//  public ArrayList<Task> getTasksFromDB(){
//	  ArrayList<Task> tList = new ArrayList<Task>();
//	  Task t = new Task();
//	  
//  }
  public void addNewTask(String title) throws ParseException{
	  DateTime dt = new DateTime(nowUTCDate());
	  recorder.putTask(title, "", dt, "0",false,false);
  }
  
  public void updateTaskLocal(Task t){
	  if(recorder.identifierExist(t.getId())){
		  boolean deleted;
		  boolean completed =!(t.getCompleted() == null);
		  String updateTime = t.getUpdated().toStringRfc3339();
		  if(t.getDeleted() == null){
			  deleted = false;
		  }else{
			  deleted = t.getDeleted();
		  }
		  String identifier = t.getId();
		  recorder.setTaskCompleted(completed, identifier);
		  recorder.setTaskDeletedFlag(deleted, identifier);
		  recorder.setTaskTitle(t.getTitle(), identifier);
		  recorder.setUpdateTime(updateTime, identifier);
	  }else{
		  throw new IllegalArgumentException("The Task Not Exist!");
	  }
  }
  
  public void putTasksToDB(List<Task> tList){
	  for (Task t : tList){
		  boolean deleted;
		  boolean completed =!(t.getCompleted() == null);
		  if(t.getDeleted() == null){
			  deleted = false;
		  }else{
			  deleted = t.getDeleted();
		  }
		  recorder.putTask(t.getTitle(), 
				  t.getNotes(), 
				  t.getUpdated(), 
				  t.getId(), 
				  deleted,
				  completed);
	  }
  }
  
  public List<Task> getTasksFromDB(){
	  ArrayList<Integer> allId = recorder.getAllId();
	  List<Task> listTask = new ArrayList<Task>();
	  for (Integer id : allId){
		  Task t = new Task();
		  t.setDeleted(recorder.getTaskDeleteByID(id));
		  t.setTitle(recorder.getTaskTitleByID(id));
		  if(recorder.getTaskCompletedByID(id)){
			  t.setStatus("completed");// Fuck!!! Why MUST use it instead of setCompleted!!!!
		  }
		  t.setUpdated(DateTime.parseRfc3339(recorder.getUpdateTimeByID(id)));
		  t.setId(recorder.getTaskIdentifierByID(id));
		  listTask.add(t);
	  }
	  return listTask;
  }
  
  public void refreshUpdateTime(Task task) throws ParseException{
	  DateTime dt = new DateTime(nowUTCDate());
	  task.setUpdated(dt);
  }
  
  public void deleteTaskInDBFlag(Task task){
	  recorder.setTaskDeletedFlag(true, task.getId());
  }
  
  public void deleteTaskInDBTrue(Task task){
	  recorder.deleteTaskTrue(task.getId());
  }
  
  public boolean getTaskCompleted(Task task){
	  return (task.getCompleted()==null? false:true);
  }

  public String getTitleById(int id){
	  return recorder.getTaskTitleByID(id);
  }
  public void setTitleById(int id, String title){
	  recorder.setTaskTitleByID(title, id);
	  try {
		recorder.setUpdateTimeByID(new DateTime(nowUTCDate()).toStringRfc3339(), id);
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  public void deleteList(){
	  recorder.deleteList();
  }
  public Cursor getAllCursor(){
	  return recorder.getCursor();
  }
  public List<String> getTasksTitleFromDB() throws IOException{
		List<String> result = new ArrayList<String>();
		List<Task> tasks = getTasksFromDB();
		if (tasks != null) {
		  for (Task task : tasks) {
		    result.add(task.getTitle());
		  }
		} else {
		  result.add("No tasks.");
		}
		return result;
	  }
  public void closeDB(){
	  recorder.closeDB();
  }
  private static Date nowUTCDate() throws ParseException{
		SimpleDateFormat dateFormatUTC = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
		dateFormatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
		SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
		dateFormatLocal.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormatLocal.parse( dateFormatUTC.format(new Date()) );
	}
}
