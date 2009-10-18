/**
 * Copyright 2007-2008 非也
 * All rights reserved. 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation。
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see http://www.gnu.org/licenses. *
 */
package org.fireflow.engine.impl;

// Generated Feb 23, 2008 12:04:21 AM by Hibernate Tools 3.2.0.b9
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.fireflow.engine.EngineException;
import org.fireflow.engine.IProcessInstance;
import org.fireflow.engine.IRuntimeContextAware;
import org.fireflow.engine.ITaskInstance;
import org.fireflow.engine.IWorkItem;
import org.fireflow.engine.IWorkflowSession;
import org.fireflow.engine.IWorkflowSessionAware;
import org.fireflow.engine.RuntimeContext;
import org.fireflow.engine.definition.IDefinitionService;
import org.fireflow.engine.definition.WorkflowDefinition;
import org.fireflow.engine.persistence.IPersistenceService;
import org.fireflow.engine.taskinstance.DynamicAssignmentHandler;
import org.fireflow.engine.taskinstance.IAssignable;
import org.fireflow.engine.taskinstance.ITaskInstanceManager;
import org.fireflow.kernel.IActivityInstance;
import org.fireflow.kernel.KernelException;
import org.fireflow.model.Task;
import org.fireflow.model.WorkflowProcess;
import org.fireflow.model.net.Activity;

/**
 * TaskInstance generated by hbm2java
 */
@SuppressWarnings("serial")
public class TaskInstance implements ITaskInstance, IAssignable, IRuntimeContextAware, IWorkflowSessionAware, java.io.Serializable {

    private String id = null;
    private String taskId = null;
    private String activityId = null;
    private String name = null;
    private String displayName = null;
    private Integer state = null;
    private Boolean suspended = null;
    private Date createdTime = null;
    private Date startedTime = null;
    private Date expiredTime = null;
    private Date endTime = null;
    private String assignmentStrategy = null;
    private String processInstanceId = null;
    private String processId = null;
    private Integer version = null;
//	private Set workItems = new HashSet(0);
    private String taskType = null;
    private String targetActivityId = null;
    private String fromActivityId = null;
//    private String tokenId = null;
    private Integer stepNumber = null;

    private Boolean canBeWithdrawn = true;

    protected transient RuntimeContext rtCtx = null;
    protected transient IWorkflowSession workflowSession = null;
    private transient IProcessInstance processInsatance = null;

    public void setRuntimeContext(RuntimeContext ctx) {
        this.rtCtx = ctx;
    }

    public RuntimeContext getRuntimeContext() {
        return this.rtCtx;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public TaskInstance() {
        this.state = ITaskInstance.INITIALIZED;
        this.suspended = false;
    }

    public TaskInstance(ProcessInstance workflowProcessInsatnce) {
        this.state = ITaskInstance.INITIALIZED;
        this.suspended = false;  
        this.processInsatance = workflowProcessInsatnce;
    }

//	public TaskInstance(String taskId, String activityId, String name,
//			String displayName, Integer state, Date createdTime, Date startedTime,
//			Date expiredTime, Date endTime, Boolean asignToEveryone,
//			ProcessInstance workflowProcessInsatnceId, Set workItems) {
//		this.taskId = taskId;
//		this.activityId = activityId;
//		this.name = name;
//		this.label = displayName;
//		this.state = state;
//		this.createdTime = createdTime;
//		this.startedTime = startedTime;
//		this.expiredTime = expiredTime;
//		this.endTime = endTime;
//		this.asignToEveryone = asignToEveryone;
//		this.processInsatnce = workflowProcessInsatnceId;
//		this.workItems = workItems;
//	}
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskId() {
        return this.taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getActivityId() {
        return this.activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String label) {
        this.displayName = label;
    }

    public Integer getState() {
        return this.state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreatedTime() {
        return this.createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getStartedTime() {
        return this.startedTime;
    }

    public void setStartedTime(Date startedTime) {
        this.startedTime = startedTime;
    }

    public Date getExpiredTime() {
        return this.expiredTime;
    }

    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
    }

    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getAssignmentStrategy() {
        return assignmentStrategy;
    }

    public void setAssignmentStrategy(String completionStrategy) {
        this.assignmentStrategy = completionStrategy;
    }

    public Boolean isSuspended() {
        return suspended;
    }

    public Boolean getSuspended(){
    	return suspended;
    }
    public void setSuspended(Boolean suspended) {
        this.suspended = suspended;
    }


    

    public IProcessInstance getAliveProcessInstance() {
    	if (this.processInsatance==null){
    		if (this.rtCtx!=null){
    			IPersistenceService persistenceService = rtCtx.getPersistenceService();
    			this.processInsatance = persistenceService.findAliveProcessInstanceById(this.processInstanceId);
    		}
    	}
        if (this.processInsatance != null) {
        	if (this.workflowSession!=null){
        		((IWorkflowSessionAware) this.processInsatance).setCurrentWorkflowSession(this.workflowSession);
        	}
        	if (this.rtCtx!=null){
        		((IRuntimeContextAware) this.processInsatance).setRuntimeContext(this.rtCtx);
        	}
            
        } 
        return this.processInsatance;
    }

    /*
    public void setProcessInstance(
    IProcessInstance processInsatnce) {
    this.processInsatnce = processInsatnce;
    if (this.processInsatnce!=null ){
    this.processInstanceId = this.processInsatnce.getId();
    }else {
    this.processInstanceId = null;
    }
    }
     */
//	public Set getWorkItems() {
//		return this.workItems;
//	}
//
//	public void setWorkItems(Set workItems) {
//		this.workItems = workItems;
//	}
    /**
     * "asign"这个单词写错了，将会被废除。（20090830）
     * @deprecated
     */
    public IWorkItem asignToActor(String id) throws EngineException, KernelException {
        return assignToActor(id);
    }    
    
    public IWorkItem assignToActor(String id) throws EngineException, KernelException {
        ITaskInstanceManager taskInstanceMgr = this.rtCtx.getTaskInstanceManager();
        WorkItem wi = taskInstanceMgr.createWorkItem(this.workflowSession,this.getAliveProcessInstance(),this, id);    	
        return wi;
    }

    /**
     * "asign"这个单词写错了，将会被废除。（20090830）
     * @deprecated
     */    
    public List<IWorkItem> asignToActors(List<String> ids) throws EngineException, KernelException {
       return assignToActors(ids);
    }
    
    public List<IWorkItem> assignToActors(List<String> ids) throws EngineException, KernelException {
        //task应该有一个标志(asignToEveryone)，表明asign的规则 (?)
        List<IWorkItem> workItemList = new ArrayList<IWorkItem>();
        for (int i = 0; ids != null && i < ids.size(); i++) {
            ITaskInstanceManager taskInstanceMgr = this.rtCtx.getTaskInstanceManager();
            WorkItem wi = taskInstanceMgr.createWorkItem(this.workflowSession,this.getAliveProcessInstance(),this, ids.get(i));
            wi.setCurrentWorkflowSession(workflowSession);
            workItemList.add(wi);
        }
        return workItemList;
    }

    public Task getTask() throws EngineException {
    	if (rtCtx==null)System.out.println("====Inside taskInstance rtCtx is null");
    	IDefinitionService definitionService = rtCtx.getDefinitionService();
    	if (definitionService==null)System.out.println("====Inside taskInstance definitionService is null");
        WorkflowDefinition workflowDef = definitionService.getWorkflowDefinitionByProcessIdAndVersionNumber(this.getProcessId(), this.getVersion());
        if (workflowDef == null) {
            return null;
        }

        return (Task) workflowDef.getWorkflowProcess().findWFElementById(this.getTaskId());

    }

    public Activity getActivity() throws EngineException {
        WorkflowDefinition workflowDef = rtCtx.getDefinitionService().getWorkflowDefinitionByProcessIdAndVersionNumber(this.getProcessId(), this.getVersion());
        if (workflowDef == null) {
            return null;
        }
        return (Activity) workflowDef.getWorkflowProcess().findWFElementById(this.getActivityId());
    }

    public WorkflowProcess getWorkflowProcess() throws EngineException {
        WorkflowDefinition workflowDef = rtCtx.getDefinitionService().getWorkflowDefinitionByProcessIdAndVersionNumber(this.getProcessId(), this.getVersion());
        if (workflowDef == null) {
            return null;
        }

        return workflowDef.getWorkflowProcess();
    }

    public final void start() throws EngineException, KernelException {
        ITaskInstanceManager taskInstanceMgr = this.rtCtx.getTaskInstanceManager();
        taskInstanceMgr.startTaskInstance(workflowSession, this.getAliveProcessInstance(), this);
//        taskInstanceMgr.startTaskInstance(this);
    }

    public void complete(IActivityInstance targetActivityInstance) throws EngineException, KernelException {
        ITaskInstanceManager taskInstanceMgr = this.rtCtx.getTaskInstanceManager();
        taskInstanceMgr.completeTaskInstance(workflowSession,  this.getAliveProcessInstance(), this, targetActivityInstance);
//        taskInstanceMgr.completeTaskInstance(this, targetActivityInstance);
    }

/*
    public IWorkItem asignToActor(String id, boolean needClaim) throws EngineException, KernelException {
        ITaskInstanceManager taskInstanceMgr = this.rtCtx.getTaskInstanceManager();
        WorkItem wi = taskInstanceMgr.createWorkItem(this, id);
        if (!needClaim) {
            wi.claim();
        }
        return wi;
    }
*/
    public IWorkflowSession getCurrentWorkflowSession() {
        return this.workflowSession;
    }

    public void setCurrentWorkflowSession(IWorkflowSession session) {
        this.workflowSession = session;
    }

    public String getProcessInstanceId() {
        return this.processInstanceId;
    }

    public void setProcessInstanceId(String s) {
        this.processInstanceId = s;
    }

    public String getProcessId() {
        return this.processId;
    }

    public void setProcessId(String s) {
        this.processId = s;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer v) {
        this.version = v;
    }

    public String getTargetActivityId() {
        return this.targetActivityId;
    }

    public void setTargetActivityId(String s) {
        this.targetActivityId = s;
    }

    public Integer getStepNumber(){
        return this.stepNumber;
    }

    public void setStepNumber(Integer i){
        this.stepNumber = i;
    }

    public Boolean getCanBeWithdrawn() {
        return canBeWithdrawn;
    }

    public void setCanBeWithdrawn(Boolean canBeWithdrawn) {
        this.canBeWithdrawn = canBeWithdrawn;
    }

    public String getFromActivityId() {
        return fromActivityId;
    }

    public void setFromActivityId(String fromActivityId) {
        this.fromActivityId = fromActivityId;
    }

    public void suspend() throws EngineException {
        if (this.state==ITaskInstance.COMPLETED || this.state==ITaskInstance.CANCELED){
            throw new EngineException(this.getAliveProcessInstance(),this.getTask(),"The task instance can not be suspended,the state of this task instance is "+this.state);
        }
        if (this.isSuspended()){
            return;
        }
        this.setSuspended(Boolean.TRUE);
        IPersistenceService persistenceService = this.rtCtx.getPersistenceService();
        persistenceService.saveOrUpdateTaskInstance(this);
    }

    public void restore() throws EngineException {
        if (this.state==ITaskInstance.COMPLETED || this.state==ITaskInstance.CANCELED){
            throw new EngineException(this.getAliveProcessInstance(),this.getTask(),"The task instance can not be restored,the state of this task instance is "+this.state);
        }
        if (!this.isSuspended()){
            return;
        }
        this.setSuspended(Boolean.FALSE);
        IPersistenceService persistenceService = this.rtCtx.getPersistenceService();
        persistenceService.saveOrUpdateTaskInstance(this);
    }

	public void abort() throws EngineException, KernelException {
		abort(null);
		
	}

	public void abort(String targetActivityId)
			throws EngineException, KernelException {
		abort(targetActivityId,null);
		
	}

	public void abort(String targetActivityId,
			DynamicAssignmentHandler dynamicAssignmentHandler)
			throws EngineException, KernelException {
		
    	if (this.workflowSession==null){
    		new EngineException(this.getProcessInstanceId(),
    				this.getWorkflowProcess(),this.getTaskId(),
    				"The current workflow session is null.");
    	}
    	if (this.rtCtx==null){
    		new EngineException(this.getProcessInstanceId(),
    				this.getWorkflowProcess(),this.getTaskId(),
    				"The current runtime context is null.");    		
    	}
    	
        if ((this.getState().intValue() == ITaskInstance.COMPLETED ) ||
        		(this.getState().intValue()==ITaskInstance.CANCELED)) {
            throw new EngineException(this.getProcessInstanceId(), this.getWorkflowProcess(),
                    this.getTaskId(),
                    "Abort task instance failed . The state of the task instance [id=" + this.getId() + "] is " + this.getState());
        }    	
    	
    	if (dynamicAssignmentHandler!=null){
    		this.workflowSession.setDynamicAssignmentHandler(dynamicAssignmentHandler);
    	}		

    	
        ITaskInstanceManager taskInstanceMgr = this.rtCtx.getTaskInstanceManager();
        taskInstanceMgr.abortTaskInstance(this.workflowSession, this.getAliveProcessInstance(), this, targetActivityId);
    	
	}

	public void abortEx(String targetActivityId,DynamicAssignmentHandler dynamicAssignmentHandler) throws EngineException,KernelException{
		
    	if (this.workflowSession==null){
    		new EngineException(this.getProcessInstanceId(),
    				this.getWorkflowProcess(),this.getTaskId(),
    				"The current workflow session is null.");
    	}
    	if (this.rtCtx==null){
    		new EngineException(this.getProcessInstanceId(),
    				this.getWorkflowProcess(),this.getTaskId(),
    				"The current runtime context is null.");    		
    	}
    	
        if ((this.getState().intValue() == ITaskInstance.COMPLETED ) ||
        		(this.getState().intValue()==ITaskInstance.CANCELED)) {
            throw new EngineException(this.getProcessInstanceId(), this.getWorkflowProcess(),
                    this.getTaskId(),
                    "Abort task instance failed . The state of the task instance [id=" + this.getId() + "] is " + this.getState());
        }    	
    	
    	if (dynamicAssignmentHandler!=null){
    		this.workflowSession.setDynamicAssignmentHandler(dynamicAssignmentHandler);
    	}		

    	
        ITaskInstanceManager taskInstanceMgr = this.rtCtx.getTaskInstanceManager();
        taskInstanceMgr.abortTaskInstanceEx(this.workflowSession, this.getAliveProcessInstance(), this, targetActivityId);
    			
	}
  
//    public String getTokenId() {
//        return tokenId;
//    }
//
//    public void setTokenId(String tokenId) {
//        this.tokenId = tokenId;
//    }
}
