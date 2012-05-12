/**
 * Copyright 2007-2010 非也
 * All rights reserved. 
 * 
 * This library is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License v3 as published by the Free Software
 * Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along
 * with this library; if not, see http://www.gnu.org/licenses/lgpl.html.
 *
 */
package org.fireflow.pdl.fpdl20.diagram.impl;

import java.util.ArrayList;
import java.util.List;

import org.fireflow.pdl.fpdl20.diagram.AssociationShape;
import org.fireflow.pdl.fpdl20.diagram.CommentShape;
import org.fireflow.pdl.fpdl20.diagram.Diagram;
import org.fireflow.pdl.fpdl20.diagram.DiagramElement;
import org.fireflow.pdl.fpdl20.diagram.GroupShape;
import org.fireflow.pdl.fpdl20.diagram.MessageFlowShape;
import org.fireflow.pdl.fpdl20.diagram.PoolShape;
import org.fireflow.pdl.fpdl20.diagram.TransitionShape;
import org.fireflow.pdl.fpdl20.diagram.WorkflowNodeShape;

/**
 *
 * @author 非也 nychen2000@163.com
 * Fire Workflow 官方网站：www.firesoa.com 或者 www.fireflow.org
 *
 */
public class DiagramImpl extends AbsDiagramElement implements Diagram {
	private String direction = Diagram.HORIZONAL;
	
	private List<TransitionShape> transitions = new ArrayList<TransitionShape>();
	private List<WorkflowNodeShape> workflowNodes = new ArrayList<WorkflowNodeShape>();

	private List<PoolShape> pools = new ArrayList<PoolShape>();
	private List<MessageFlowShape> messageFlows = new ArrayList<MessageFlowShape>();
	private List<CommentShape> comments = new ArrayList<CommentShape>();
	private List<AssociationShape> associations = new ArrayList<AssociationShape>();

	public DiagramImpl(String id,String subflowId){
		this.id = id;
		this.workflowElementId = subflowId;
	}
	
	public DiagramElement findChild(String diagramElementId){
		if (diagramElementId.equals(this.id)){
			return this;
		}
		for (DiagramElement diagramElm : transitions){
			if (diagramElm.getId().equals(id)){
				return diagramElm;
			}
		}
		
		for (DiagramElement diagramElm : workflowNodes){
			if (diagramElm.getId().equals(id)){
				return diagramElm;
			}
			if (diagramElm instanceof GroupShape){
				DiagramElement tmp = diagramElm.findChild(id);
				if (tmp!=null){
					return tmp;
				}
			}
		}
		for (DiagramElement diagramElm : associations){
			if (diagramElm.getId().equals(diagramElementId)){
				return diagramElm;
			}
		}
		for (DiagramElement diagramElm : comments){
			if (diagramElm.getId().equals(diagramElementId)){
				return diagramElm;
			}
		}
		
		for (DiagramElement diagramElm : messageFlows){
			if (diagramElm.getId().equals(diagramElementId)){
				return diagramElm;
			}
		}
		
		for (DiagramElement diagramElm : pools){
			if (diagramElm.getId().equals(diagramElementId)){
				return diagramElm;
			}
			if (diagramElm instanceof PoolShape){
				DiagramElement tmp = diagramElm.findChild(diagramElementId);
				if (tmp!=null){
					return tmp;
				}
			}
		}
		return null;
	}

	
	public DiagramElement findChildByWorkflowElementId(String workflowElementId){
		if (workflowElementId.equals(this.workflowElementId)){
			return this;
		}
		for (DiagramElement diagramElm : transitions){
			if (diagramElm.getWorkflowElementRef().equals(workflowElementId)){
				return diagramElm;
			}
		}
		
		for (DiagramElement diagramElm : workflowNodes){
			if (diagramElm.getWorkflowElementRef().equals(workflowElementId)){
				return diagramElm;
			}
			if (diagramElm instanceof GroupShape){
				DiagramElement tmp = diagramElm.findChild(workflowElementId);
				if (tmp!=null){
					return tmp;
				}
			}
		}
		return null;
	}	

	public List<TransitionShape> getTransitions() {
		return transitions;
	}


	public void addTransition(TransitionShape transitionShape) {
		transitions.add(transitionShape);

	}


	public List<WorkflowNodeShape> getWorkflowNodeShapes() {		
		return workflowNodes;
	}


	public void addWorkflowNodeShape(WorkflowNodeShape shape) {
		workflowNodes.add(shape);		
	}

	
	/* (non-Javadoc)
	 * @see org.fireflow.pdl.fpdl20.diagram.Diagram#getDirection()
	 */
	public String getDirection() {
		return this.direction;
	}
	
	public void setDirection(String d){
		this.direction = d;
	}

	/* (non-Javadoc)
	 * @see org.fireflow.pdl.fpdl20.diagram.Diagram#getPools()
	 */
	public List<PoolShape> getPools() {
		return pools;
	}

	/* (non-Javadoc)
	 * @see org.fireflow.pdl.fpdl20.diagram.Diagram#addPool(org.fireflow.pdl.fpdl20.diagram.Pool)
	 */
	public void addPool(PoolShape pool) {
		pools.add(pool);
	}

	/* (non-Javadoc)
	 * @see org.fireflow.pdl.fpdl20.diagram.Diagram#gegMessageFlows()
	 */
	public List<MessageFlowShape> getMessageFlows() {
		return messageFlows;
	}

	/* (non-Javadoc)
	 * @see org.fireflow.pdl.fpdl20.diagram.Diagram#addMessageFlow(org.fireflow.pdl.fpdl20.diagram.MessageFlow)
	 */
	public void addMessageFlow(MessageFlowShape msgFlow) {
		messageFlows.add(msgFlow);
	}

	/* (non-Javadoc)
	 * @see org.fireflow.pdl.fpdl20.diagram.Diagram#getComments()
	 */
	public List<CommentShape> getComments() {

		return comments;
	}

	/* (non-Javadoc)
	 * @see org.fireflow.pdl.fpdl20.diagram.Diagram#addComment(org.fireflow.pdl.fpdl20.diagram.Comment)
	 */
	public void addComment(CommentShape cmmt) {
		comments.add(cmmt);

	}

	/* (non-Javadoc)
	 * @see org.fireflow.pdl.fpdl20.diagram.Diagram#getAssociations()
	 */
	public List<AssociationShape> getAssociations() {
		return associations;
	}

	/* (non-Javadoc)
	 * @see org.fireflow.pdl.fpdl20.diagram.Diagram#addAssociation(org.fireflow.pdl.fpdl20.diagram.Association)
	 */
	public void addAssociation(AssociationShape association) {
		associations.add(association);
	}

}