/*
 * Copyright (c) 2012 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package dacer.google.task;

import java.io.IOException;
import java.util.List;

import dacer.utils.GlobalContext;

/**
 * Asynchronously load the tasks.
 * 
 * @author Yaniv Inbar
 */
class SyncDBTasks extends CommonAsyncTask {
  String TAG = "AsyncLoadTask--->";
  private TaskUtils tUtils;
  
  SyncDBTasks(TaskListFragment listFragment) {
    super(listFragment);
    tUtils = new TaskUtils(client,GlobalContext.getInstance());
  }

  @Override
  protected void doInBackground() throws IOException {
	  
    if(!tUtils.listExist(TaskUtils.LIST_NAME)){
    	tUtils.initGuideList();
    }
    tUtils.updateDB(tUtils.getListId(TaskUtils.LIST_NAME));
    List<String> result = tUtils.getTasksTitleFromDB();
    mFragment.tasksList = result;
  }

  static void run(TaskListFragment listFragment) {
    new SyncDBTasks(listFragment).execute();
  }
  
  
  

}
