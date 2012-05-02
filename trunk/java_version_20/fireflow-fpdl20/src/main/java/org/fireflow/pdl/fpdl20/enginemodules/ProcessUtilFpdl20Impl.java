/**
 * Copyright 2007-2010 非也
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
package org.fireflow.pdl.fpdl20.enginemodules;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.fireflow.engine.context.AbsEngineModule;
import org.fireflow.engine.context.RuntimeContext;
import org.fireflow.engine.entity.repository.ProcessKey;
import org.fireflow.engine.entity.repository.ProcessRepository;
import org.fireflow.engine.entity.repository.ResourceRepository;
import org.fireflow.engine.entity.repository.ServiceRepository;
import org.fireflow.engine.entity.repository.impl.ProcessRepositoryImpl;
import org.fireflow.engine.modules.persistence.PersistenceService;
import org.fireflow.engine.modules.persistence.ProcessPersister;
import org.fireflow.engine.modules.persistence.ResourcePersister;
import org.fireflow.engine.modules.persistence.ServicePersister;
import org.fireflow.engine.modules.process.ProcessUtil;
import org.fireflow.model.InvalidModelException;
import org.fireflow.model.binding.ResourceBinding;
import org.fireflow.model.binding.ServiceBinding;
import org.fireflow.model.data.Property;
import org.fireflow.model.io.DeserializerException;
import org.fireflow.model.io.SerializerException;
import org.fireflow.model.process.WorkflowElement;
import org.fireflow.model.resourcedef.ResourceDef;
import org.fireflow.model.servicedef.ServiceDef;
import org.fireflow.pdl.fpdl20.io.FPDLDeserializer;
import org.fireflow.pdl.fpdl20.io.FPDLSerializer;
import org.fireflow.pdl.fpdl20.io.ImportLoader;
import org.fireflow.pdl.fpdl20.misc.FpdlConstants;
import org.fireflow.pdl.fpdl20.process.Activity;
import org.fireflow.pdl.fpdl20.process.StartNode;
import org.fireflow.pdl.fpdl20.process.Subflow;
import org.fireflow.pdl.fpdl20.process.WorkflowProcess;
import org.fireflow.pdl.fpdl20.process.features.Feature;
import org.fireflow.pdl.fpdl20.process.features.startnode.TimerStartFeature;
import org.fireflow.pdl.fpdl20.process.features.startnode.WebserviceStartFeature;
import org.fireflow.service.callback.CallbackService;

/**
 * TODO 等fire2.0 流程xsd出来后，此处需要用FPDLSerializer和FPDLDeserializer改造。
 * @author 非也
 * @version 2.0
 */
public class ProcessUtilFpdl20Impl  extends AbsEngineModule implements
		ProcessUtil {
	RuntimeContext ctx = null;

	public String getProcessEntryId(String workflowProcessId, int version,String processType){
		return workflowProcessId+"."+WorkflowProcess.MAIN_FLOW_NAME;
	}
	
    public ServiceBinding getServiceBinding(ProcessKey processKey,String subflowId, String activityId)throws InvalidModelException{
    	WorkflowProcess process = (WorkflowProcess)this.getWorkflowProcess(processKey);
    	if (process==null) return null;
    	Subflow subflow = process.getLocalSubflow(subflowId);
    	Activity activity = subflow.getActivity(activityId);
    	if (activity==null){
    		return null;
    	}else{
    		return activity.getServiceBinding();
    	}
    }
    
    public ResourceBinding getResourceBinding(ProcessKey processKey,String subflowId, String activityId)throws InvalidModelException{
    	WorkflowProcess process = (WorkflowProcess)this.getWorkflowProcess(processKey);
    	if (process==null) return null;
    	Subflow subflow = process.getLocalSubflow(subflowId);
    	Activity activity = subflow.getActivity(activityId);
    	if (activity==null){
    		return null;
    	}else{
    		return activity.getResourceBinding();
    	}
    }
    
    public Object getActivity(ProcessKey processKey,String subflowId, String activityId)throws InvalidModelException{
    	WorkflowProcess process = (WorkflowProcess)this.getWorkflowProcess(processKey);
    	if (process==null) return null;
    	Subflow subflow = process.getLocalSubflow(subflowId);
    	Activity activity = subflow.getActivity(activityId);
    	return activity;
    }
	/* (non-Javadoc)
	 * @see org.fireflow.engine.context.RuntimeContextAware#getRuntimeContext()
	 */
	public RuntimeContext getRuntimeContext() {
		
		return ctx;
	}

	/* (non-Javadoc)
	 * @see org.fireflow.engine.context.RuntimeContextAware#setRuntimeContext(org.fireflow.engine.context.RuntimeContext)
	 */
	public void setRuntimeContext(RuntimeContext ctx) {
		this.ctx = ctx;

	}

	/* (non-Javadoc)
	 * @see org.fireflow.engine.repository.ProcessRepositoryService#getWorkflowProcess(org.fireflow.engine.entity.repository.ProcessKey)
	 */
	protected Object getWorkflowProcess(ProcessKey processKey) throws InvalidModelException{
		PersistenceService persistenceService = ctx.getEngineModule(PersistenceService.class, FpdlConstants.PROCESS_TYPE);
		ProcessPersister processPersister = persistenceService.getProcessPersister();
		ProcessRepository repository = processPersister.findProcessRepositoryByProcessKey(processKey);
		return repository.getProcessObject();
	}

	/* (non-Javadoc)
	 * @see org.fireflow.engine.modules.process.ProcessUtil#deserializeXml2Process(java.lang.String)
	 */
	public Object deserializeXml2Process(InputStream inStream) throws InvalidModelException{
		if (inStream==null) return null;
		FPDLDeserializer parser = new FPDLDeserializer();
		
		//2012-02-02
		InnerImportLoader importLoader = new InnerImportLoader();
		parser.setImportLoader(importLoader);
		
		try {
			WorkflowProcess process;
			process = parser.deserialize(inStream);
			
			return process;
		} catch(IOException e){
			throw new InvalidModelException(e);
		} catch (DeserializerException e) {
			throw new InvalidModelException(e);
		}

	}

	/* (non-Javadoc)
	 * @see org.fireflow.engine.modules.process.ProcessUtil#serializeProcess2Xml(java.lang.Object)
	 */
	public String serializeProcess2Xml(Object process) throws InvalidModelException{
		
		WorkflowProcess wfProcess = (WorkflowProcess)process;
		FPDLSerializer ser = new FPDLSerializer();
		try {
			return ser.serializeToXmlString(wfProcess);
		} catch (IOException e) {
			
			throw new InvalidModelException(e);
		} catch (SerializerException e) {
			throw new InvalidModelException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.fireflow.engine.modules.process.ProcessUtil#serializeProcess2ProcessRepository(java.lang.Object)
	 */
	public ProcessRepository serializeProcess2ProcessRepository(Object process) throws InvalidModelException{
		WorkflowProcess wfProcess = (WorkflowProcess)process;
		ProcessRepositoryImpl repository = new ProcessRepositoryImpl();

		repository.setProcessId(wfProcess.getId());
		repository.setTimerStart(isTimerStart(wfProcess));
		repository.setHasCallbackService(hasCallbackService(wfProcess));
		repository.setProcessType(FpdlConstants.PROCESS_TYPE);
		repository.setName(wfProcess.getName());
		String displayName = wfProcess.getDisplayName();
		repository.setDisplayName((displayName==null || displayName.trim().equals(""))?wfProcess.getName():displayName);
		repository.setDescription(wfProcess.getDescription());
		
		//设置缺省的FileName 为 <processId>.f20.xml
		repository.setFileName(wfProcess.getId()+"."+FpdlConstants.PROCESS_FILE_SUFFIX);
		
		repository.setProcessObject(process);
		repository.setProcessContent(this.serializeProcess2Xml(process));	

		return repository;
	}
	
	private boolean hasCallbackService(WorkflowProcess workflowProcess){
		List<Subflow> subflowList = workflowProcess.getLocalSubflows();
		if (subflowList == null || subflowList.size() == 0)
			return false;
		for (Subflow subflow : subflowList) {
			// 首先检查Activity是否绑定了Callback service
			List<Activity> activityList = subflow.getActivities();
			if (activityList != null) {
				for (Activity activity : activityList) {
					ServiceBinding svcBinding = activity.getServiceBinding();
					if (svcBinding != null) {
						ServiceDef svcDef = svcBinding.getService();
						if (svcDef != null && svcDef instanceof CallbackService) {
							return true;
						}
					}
				}
			}
			// 然后检查Main subflow 的 StartNode是否绑定了CallbackService
			if (WorkflowProcess.MAIN_FLOW_NAME.equals(subflow.getName())){
				List<StartNode> startNodeList = subflow.getStartNodes();
				if (startNodeList != null) {
					for (StartNode startNode : startNodeList) {
						Feature ft = startNode.getFeature();
						if (ft != null && ft instanceof WebserviceStartFeature) {
							WebserviceStartFeature wsFt = (WebserviceStartFeature) ft;
							ServiceBinding svcBinding = wsFt.getServiceBinding();
							if (svcBinding != null) {
								ServiceDef svcDef = svcBinding.getService();
								if (svcDef != null && svcDef instanceof CallbackService) {
									return true;
								}
							}
						}
					}
				}
			}

		}
		return false;
	}
	
	private boolean isTimerStart(WorkflowProcess wfProcess){
		Subflow subflow = wfProcess.getMainflow();
		if (subflow==null) return false;
		StartNode startNode = (StartNode)subflow.getEntry();
		if (startNode==null) return false;
		
		Feature startNodeFeature = startNode.getFeature();
		if (startNodeFeature!=null && startNodeFeature instanceof TimerStartFeature){
			return true;
		}
		return false;
	}
	
	private class InnerImportLoader implements ImportLoader{

		/* (non-Javadoc)
		 * @see org.fireflow.pdl.fpdl20.io.ImportLoader#loadResources(java.lang.String)
		 */
		public List<ResourceDef> loadResources(String resourceLocation)
				throws DeserializerException, IOException {
			PersistenceService persistenceService = ctx.getEngineModule(PersistenceService.class, FpdlConstants.PROCESS_TYPE);
			ResourcePersister resourcePersister = persistenceService.getResourcePersister();
			
			ResourceRepository repository = resourcePersister.findResourceRepositoryByFileName(resourceLocation);
			return repository.getResources();
		}

		/* (non-Javadoc)
		 * @see org.fireflow.pdl.fpdl20.io.ImportLoader#loadServices(java.lang.String)
		 */
		public List<ServiceDef> loadServices(String serviceLocation)
				throws DeserializerException, IOException {
			PersistenceService persistenceService = ctx.getEngineModule(PersistenceService.class, FpdlConstants.PROCESS_TYPE);
			ServicePersister servicePersister = persistenceService.getServicePersister();
			
			ServiceRepository repository = servicePersister.findServiceRepositoryByFileName(serviceLocation);
			return repository.getServices();
		}

		/* (non-Javadoc)
		 * @see org.fireflow.pdl.fpdl20.io.ImportLoader#loadProcess(java.lang.String)
		 */
		public WorkflowProcess loadProcess(String processLocation)
				throws InvalidModelException, DeserializerException,
				IOException {
			// TODO Auto-generated method stub
			return null;
		}
		
	}


	/* (non-Javadoc)
	 * @see org.fireflow.engine.modules.process.ProcessUtil#getProperty(org.fireflow.engine.entity.repository.ProcessKey, java.lang.String, java.lang.String)
	 */
	public Property getProperty(ProcessKey processKey, String processElementId,
			String propertyName)throws InvalidModelException {
    	WorkflowProcess process = (WorkflowProcess)this.getWorkflowProcess(processKey);
    	if (process==null) return null;
    	WorkflowElement  workflowElement = process.findWorkflowElementById(processElementId);
    	if (workflowElement instanceof Subflow){
    		return ((Subflow)workflowElement).getProperty(propertyName);
    	}else if (workflowElement instanceof Activity){
    		return ((Activity)workflowElement).getProperty(propertyName);
    	}
		return null;
	}
}
