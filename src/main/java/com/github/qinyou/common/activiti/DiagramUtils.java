package com.github.qinyou.common.activiti;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.bpmn.behavior.BoundaryEventActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.CallActivityBehavior;
import org.activiti.engine.impl.bpmn.parser.ErrorEventDefinition;
import org.activiti.engine.impl.bpmn.parser.EventSubscriptionDeclaration;
import org.activiti.engine.impl.jobexecutor.TimerDeclarationImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.*;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

@SuppressWarnings("Duplicates")
@Slf4j
public class DiagramUtils {

    public static JSONObject getDiagramNode(String processDefinitionId) {
        Map<String, JSONObject> subProcessInstanceMap = new HashMap<>();

        if (processDefinitionId == null) {
            throw new ActivitiObjectNotFoundException("processDefinitionId 参数不可为空");
        } else {
            ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ActivitiUtils.getRepositoryService().getProcessDefinition(processDefinitionId);
            if (processDefinition == null) {
                throw new ActivitiException("流程定义 ID: " + processDefinitionId + " 查询不到");
            } else {
                JSONObject responseJSON = new JSONObject();

                //流程定义信息
                responseJSON.put("processDefinition", getProcessDefinitionResponse(processDefinition));

                // TODO ??
                if (processDefinition.getParticipantProcess() != null) {
                    ParticipantProcess pProc = processDefinition.getParticipantProcess();
                    JSONObject participantProcessJSON = new JSONObject();
                    participantProcessJSON.fluentPut("id", pProc.getId())
                            .fluentPut("name", StringUtils.isNotEmpty(pProc.getName()) ? pProc.getName() : "")
                            .fluentPut("x", pProc.getX())
                            .fluentPut("y", pProc.getY())
                            .fluentPut("width", pProc.getWidth())
                            .fluentPut("height", pProc.getHeight());
                    responseJSON.put("participantProcess", participantProcessJSON);
                }

                JSONArray laneSetArray;
                JSONArray activityArray;
                if (processDefinition.getLaneSets() != null && !processDefinition.getLaneSets().isEmpty()) {
                    laneSetArray = new JSONArray();
                    for (LaneSet laneSet : processDefinition.getLaneSets()) {
                        JSONArray laneArray = new JSONArray();
                        if (laneSet.getLanes() != null && !laneSet.getLanes().isEmpty()) {
                            for (Lane lane : laneSet.getLanes()) {
                                JSONObject laneJSON = new JSONObject();
                                laneJSON.fluentPut("id", lane.getId())
                                        .fluentPut("name", StringUtils.isNotEmpty(lane.getName()) ? lane.getName() : "")
                                        .fluentPut("x", lane.getX())
                                        .fluentPut("y", lane.getY())
                                        .fluentPut("width", lane.getWidth())
                                        .fluentPut("height", lane.getHeight())
                                        .fluentPut("flowNodeIds", lane.getFlowNodeIds());
                                laneArray.add(laneJSON);
                            }
                        }
                        JSONObject laneSetJSON = new JSONObject();
                        laneSetJSON.fluentPut("id", laneSet.getId())
                                .fluentPut("name", StringUtils.isNotEmpty(laneSet.getName()) ? laneSet.getName() : "")
                                .fluentPut("lanes", laneArray);
                        laneSetArray.add(laneSetJSON);
                    }

                    if (laneSetArray.size() > 0) {
                        responseJSON.put("laneSets", laneSetArray);
                    }
                }

                laneSetArray = new JSONArray();
                activityArray = new JSONArray();
                for (ActivityImpl activity : processDefinition.getActivities()) {
                    getActivity(activity, activityArray, laneSetArray, subProcessInstanceMap);
                }
                responseJSON.put("activities", activityArray);
                responseJSON.put("sequenceFlows", laneSetArray);
                return responseJSON;
            }
        }
    }


    private static void getActivity(ActivityImpl activity, JSONArray activityArray, JSONArray sequenceFlowArray, Map<String, JSONObject> subProcessInstanceMap) {
        JSONObject activityJSON = new JSONObject();
        String multiInstance = (String) activity.getProperty("multiInstance");
        if (multiInstance != null && !"sequential".equals(multiInstance)) {
            multiInstance = "parallel";
        }

        ActivityBehavior activityBehavior = activity.getActivityBehavior();
        boolean collapsed = activityBehavior instanceof CallActivityBehavior;
        Boolean expanded = (Boolean) activity.getProperty("isExpanded");
        if (expanded != null) {
            collapsed = !expanded;
        }

        Boolean isInterrupting = null;
        if (activityBehavior instanceof BoundaryEventActivityBehavior) {
            isInterrupting = ((BoundaryEventActivityBehavior) activityBehavior).isInterrupting();
        }

        JSONArray errorEventDefinitionsArray;
        for (PvmTransition sequenceFlow : activity.getOutgoingTransitions()) {
            String flowName = (String) sequenceFlow.getProperty("name");

            boolean isConditional = sequenceFlow.getProperty("condition") != null && !((String) activity.getProperty("type")).toLowerCase().contains("gateway");
            boolean isDefault = sequenceFlow.getId().equals(activity.getProperty("default")) && ((String) activity.getProperty("type")).toLowerCase().contains("gateway");
            List<Integer> waypoints = ((TransitionImpl) sequenceFlow).getWaypoints();
            errorEventDefinitionsArray = new JSONArray();
            JSONArray yPointArray = new JSONArray();

            for (int i = 0; i < waypoints.size(); i += 2) {
                errorEventDefinitionsArray.add((Integer) waypoints.get(i));
                yPointArray.add(waypoints.get(i + 1));
            }

            JSONObject flowJSON = new JSONObject();
            flowJSON.put("id", sequenceFlow.getId());
            flowJSON.put("name", flowName);
            flowJSON.put("flow", "(" + sequenceFlow.getSource().getId() + ")--" + sequenceFlow.getId() + "-->(" + sequenceFlow.getDestination().getId() + ")");
            if (isConditional) {
                flowJSON.put("isConditional", isConditional);
            }
            if (isDefault) {
                flowJSON.put("isDefault", isDefault);
            }
            flowJSON.put("xPointArray", errorEventDefinitionsArray);
            flowJSON.put("yPointArray", yPointArray);

            String conditionText = (String) sequenceFlow.getProperty("conditionText");
            if (conditionText != null) {
                flowJSON.put("conditionText", conditionText);
            }
            sequenceFlowArray.add(flowJSON);
        }

        JSONArray nestedActivityArray = new JSONArray();
        for (ActivityImpl nestedActivity : activity.getActivities()) {
            nestedActivityArray.add(nestedActivity.getId());
        }

        Map<String, Object> properties = activity.getProperties();
        JSONObject propertiesJSON = new JSONObject();

        while (true) {
            for (String key : activity.getProperties().keySet()) {
                Object prop = properties.get(key);
                if (prop instanceof String) {
                    propertiesJSON.put(key, prop);
                } else if (prop instanceof Integer) {
                    propertiesJSON.put(key, prop);
                } else if (prop instanceof Boolean) {
                    propertiesJSON.put(key, prop);
                } else if ("initial".equals(key)) {
                    ActivityImpl act = (ActivityImpl) prop;
                    propertiesJSON.put(key, act.getId());
                } else {
                    JSONObject errorEventDefinitionJSON;
                    if ("timerDeclarations".equals(key)) {
                        List<TimerDeclarationImpl> errorEventDefinitions = (List<TimerDeclarationImpl>) properties.get(key);
                        errorEventDefinitionsArray = new JSONArray();
                        if (errorEventDefinitions != null) {
                            for (TimerDeclarationImpl timerDeclaration : errorEventDefinitions) {
                                errorEventDefinitionJSON = new JSONObject();
                                errorEventDefinitionJSON.put("isExclusive", timerDeclaration.isExclusive());
                                if (timerDeclaration.getRepeat() != null) {
                                    errorEventDefinitionJSON.put("repeat", timerDeclaration.getRepeat());
                                }
                                errorEventDefinitionJSON.fluentPut("retries", String.valueOf(timerDeclaration.getRetries()))
                                        .fluentPut("type", timerDeclaration.getJobHandlerType())
                                        .fluentPut("configuration", timerDeclaration.getJobHandlerConfiguration());
                                errorEventDefinitionsArray.add(errorEventDefinitionJSON);
                            }
                        }
                        if (errorEventDefinitionsArray.size() > 0) {
                            propertiesJSON.put(key, errorEventDefinitionsArray);
                        }
                    } else if ("eventDefinitions".equals(key)) {
                        List<EventSubscriptionDeclaration> errorEventDefinitions = (List<EventSubscriptionDeclaration>) properties.get(key);
                        errorEventDefinitionsArray = new JSONArray();
                        if (errorEventDefinitions != null) {
                            for (EventSubscriptionDeclaration eventDefinition : errorEventDefinitions) {
                                errorEventDefinitionJSON = new JSONObject();
                                if (eventDefinition.getActivityId() != null) {
                                    errorEventDefinitionJSON.put("activityId", eventDefinition.getActivityId());
                                }
                                errorEventDefinitionJSON.put("eventName", eventDefinition.getEventName());
                                errorEventDefinitionJSON.put("eventType", eventDefinition.getEventType());
                                errorEventDefinitionJSON.put("isAsync", eventDefinition.isAsync());
                                errorEventDefinitionJSON.put("isStartEvent", eventDefinition.isStartEvent());
                                errorEventDefinitionsArray.add(errorEventDefinitionJSON);
                            }
                        }
                        if (errorEventDefinitionsArray.size() > 0) {
                            propertiesJSON.put(key, errorEventDefinitionsArray);
                        }
                    } else if ("errorEventDefinitions".equals(key)) {
                        List<ErrorEventDefinition> errorEventDefinitions = (List<ErrorEventDefinition>) properties.get(key);
                        errorEventDefinitionsArray = new JSONArray();
                        if (errorEventDefinitions != null) {

                            for (ErrorEventDefinition errorEventDefinition : errorEventDefinitions) {
                                errorEventDefinitionJSON = new JSONObject();
                                // 此处 原  判断为null使用 objectNode.putNull("errorCode"), putNull
                                errorEventDefinitionJSON.put("errorCode", errorEventDefinition.getErrorCode());

                                errorEventDefinitionJSON.put("handlerActivityId", errorEventDefinition.getHandlerActivityId());
                                errorEventDefinitionsArray.add(errorEventDefinitionJSON);
                            }
                        }

                        if (errorEventDefinitionsArray.size() > 0) {
                            propertiesJSON.put(key, errorEventDefinitionsArray);
                        }
                    }
                }
            }


            if ("callActivity".equals(properties.get("type"))) {
                CallActivityBehavior callActivityBehavior = null;
                if (activityBehavior instanceof CallActivityBehavior) {
                    callActivityBehavior = (CallActivityBehavior) activityBehavior;
                }

                if (callActivityBehavior != null) {
                    propertiesJSON.put("processDefinitonKey", callActivityBehavior.getProcessDefinitonKey());
                    JSONArray processInstanceArray = new JSONArray();

                    if (StringUtils.isNotEmpty(callActivityBehavior.getProcessDefinitonKey())) {
                        ProcessDefinition lastProcessDefinition = (ProcessDefinition) ActivitiUtils.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey(callActivityBehavior.getProcessDefinitonKey()).latestVersion().singleResult();
                        if (lastProcessDefinition != null) {
                            JSONObject processInstanceJSON = new JSONObject()
                                    .fluentPut("processDefinitionId", lastProcessDefinition.getId())
                                    .fluentPut("processDefinitionKey", lastProcessDefinition.getKey())
                                    .fluentPut("processDefinitionName", lastProcessDefinition.getName());
                            processInstanceArray.add(processInstanceJSON);
                        }
                    }

                    if (processInstanceArray.size() > 0) {
                        propertiesJSON.put("processDefinitons", processInstanceArray);
                    }
                }
            }

            if ("userTask".equals(properties.get("type"))) {
                // 放入用户任务 办理人、候选组、候选人信息
                TaskDefinition definition = (TaskDefinition) activity.getProperty("taskDefinition");
                if (definition.getAssigneeExpression() != null) {
                    //log.info("assignee Value: {}",definition.getAssigneeExpression().getValue());
                    propertiesJSON.put("assignee", definition.getAssigneeExpression().getExpressionText());
                }
                if (definition.getCandidateGroupIdExpressions().size() > 0) {
                    Set<String> texts = new HashSet<>();
                    definition.getCandidateGroupIdExpressions().forEach(expression -> {
                        texts.add(expression.getExpressionText());
                    });
                    propertiesJSON.put("candidateGroups", texts);
                }
                if (definition.getCandidateUserIdExpressions().size() > 0) {
                    Set<String> texts = new HashSet<>();
                    definition.getCandidateUserIdExpressions().forEach(expression -> {
                        texts.add(expression.getExpressionText());
                    });
                    propertiesJSON.put("candidateUsers", texts);
                }
            }

            activityJSON.put("activityId", activity.getId());
            activityJSON.put("properties", propertiesJSON);
            if (multiInstance != null) {
                activityJSON.put("multiInstance", multiInstance);
            }

            if (collapsed) {
                activityJSON.put("collapsed", collapsed);
            }

            if (nestedActivityArray.size() > 0) {
                activityJSON.put("nestedActivities", nestedActivityArray);
            }

            if (isInterrupting != null) {
                activityJSON.put("isInterrupting", isInterrupting);
            }

            activityJSON.fluentPut("x", activity.getX())
                    .fluentPut("y", activity.getY())
                    .fluentPut("width", activity.getWidth())
                    .fluentPut("height", activity.getHeight());
            activityArray.add(activityJSON);
            for (ActivityImpl nestedActivity : activity.getActivities()) {
                getActivity(nestedActivity, activityArray, sequenceFlowArray, subProcessInstanceMap);
            }
            return;
        }
    }

    private static JSONObject getProcessDefinitionResponse(ProcessDefinitionEntity processDefinition) {
        JSONObject pdrJSON = new JSONObject();
        pdrJSON.put("id", processDefinition.getId());
        pdrJSON.put("name", processDefinition.getName());
        pdrJSON.put("key", processDefinition.getKey());
        pdrJSON.put("version", processDefinition.getVersion());
        pdrJSON.put("deploymentId", processDefinition.getDeploymentId());
        pdrJSON.put("isGraphicNotationDefined", isGraphicNotationDefined(processDefinition));
        return pdrJSON;
    }

    private static boolean isGraphicNotationDefined(ProcessDefinitionEntity processDefinition) {
        return ((ProcessDefinitionEntity) ActivitiUtils.getRepositoryService()
                .getProcessDefinition(processDefinition.getId()))
                .isGraphicalNotationDefined();
    }


    /// ------------- 下面为 高亮流程图使用 ------------------
    public static List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinition, String processInstanceId) {
        List<String> highLightedFlows = new ArrayList<>();
        List<HistoricActivityInstance> historicActivityInstances = ActivitiUtils.getHistoryService().createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();
        LinkedList<HistoricActivityInstance> hisActInstList = new LinkedList<>(historicActivityInstances);
        getHighlightedFlows(processDefinition.getActivities(), hisActInstList, highLightedFlows);
        return highLightedFlows;
    }

    private static void getHighlightedFlows(List<ActivityImpl> activityList, LinkedList<HistoricActivityInstance> hisActInstList, List<String> highLightedFlows) {
        List<ActivityImpl> startEventActList = new ArrayList<>();
        Map<String, ActivityImpl> activityMap = new HashMap<>(activityList.size());

        for (ActivityImpl activity : activityList) {
            activityMap.put(activity.getId(), activity);
            String actType = (String) activity.getProperty("type");
            if (actType != null && actType.toLowerCase().contains("startevent")) {
                startEventActList.add(activity);
            }
        }

        HistoricActivityInstance firstHistActInst = (HistoricActivityInstance) hisActInstList.getFirst();
        String firstActType = firstHistActInst.getActivityType();
        if (firstActType != null && !firstActType.toLowerCase().contains("startevent")) {
            PvmTransition startTrans = getStartTransaction(startEventActList, firstHistActInst);
            if (startTrans != null) {
                highLightedFlows.add(startTrans.getId());
            }
        }

        while (true) {
            ActivityImpl activity;
            HistoricActivityInstance histActInst;
            do {
                if (hisActInstList.isEmpty()) {
                    return;
                }
                histActInst = (HistoricActivityInstance) hisActInstList.removeFirst();
                activity = (ActivityImpl) activityMap.get(histActInst.getActivityId());
            } while (activity == null);

            boolean isParallel = false;
            String type = histActInst.getActivityType();
            if (!"parallelGateway".equals(type) && !"inclusiveGateway".equals(type)) {
                if ("subProcess".equals(histActInst.getActivityType())) {
                    getHighlightedFlows(activity.getActivities(), hisActInstList, highLightedFlows);
                }
            } else {
                isParallel = true;
            }
            List<PvmTransition> allOutgoingTrans = new ArrayList<>();
            allOutgoingTrans.addAll(activity.getOutgoingTransitions());
            allOutgoingTrans.addAll(getBoundaryEventOutgoingTransitions(activity));
            List<String> activityHighLightedFlowIds = getHighlightedFlows(allOutgoingTrans, hisActInstList, isParallel);
            highLightedFlows.addAll(activityHighLightedFlowIds);
        }
    }

    private static PvmTransition getStartTransaction(List<ActivityImpl> startEventActList, HistoricActivityInstance firstActInst) {
        for (ActivityImpl startEventAct : startEventActList) {
            for (PvmTransition trans : startEventAct.getOutgoingTransitions()) {
                if (trans.getDestination().getId().equals(firstActInst.getActivityId())) {
                    return trans;
                }
            }
        }
        return null;
    }

    private static List<PvmTransition> getBoundaryEventOutgoingTransitions(ActivityImpl activity) {
        List<PvmTransition> boundaryTrans = new ArrayList<>();
        for (ActivityImpl subActivity : activity.getActivities()) {
            String type = (String) subActivity.getProperty("type");
            if (type != null && type.toLowerCase().contains("boundary")) {
                boundaryTrans.addAll(subActivity.getOutgoingTransitions());
            }
        }
        return boundaryTrans;
    }

    private static List<String> getHighlightedFlows(List<PvmTransition> pvmTransitionList, LinkedList<HistoricActivityInstance> hisActInstList, boolean isParallel) {
        List<String> highLightedFlowIds = new ArrayList<>();
        PvmTransition earliestTrans = null;
        HistoricActivityInstance earliestHisActInst = null;
        Iterator it = pvmTransitionList.iterator();

        while (true) {
            while (true) {
                PvmTransition pvmTransition;
                HistoricActivityInstance destHisActInst;
                do {
                    if (!it.hasNext()) {
                        if (!isParallel && earliestTrans != null) {
                            highLightedFlowIds.add(earliestTrans.getId());
                        }
                        return highLightedFlowIds;
                    }
                    pvmTransition = (PvmTransition) it.next();
                    String destActId = pvmTransition.getDestination().getId();
                    destHisActInst = findHisActInst(hisActInstList, destActId);
                } while (destHisActInst == null);

                if (isParallel) {
                    highLightedFlowIds.add(pvmTransition.getId());
                } else if (earliestHisActInst == null || earliestHisActInst.getId().compareTo(destHisActInst.getId()) > 0) {
                    earliestTrans = pvmTransition;
                    earliestHisActInst = destHisActInst;
                }
            }
        }
    }

    private static HistoricActivityInstance findHisActInst(LinkedList<HistoricActivityInstance> hisActInstList, String actId) {
        Iterator it = hisActInstList.iterator();
        HistoricActivityInstance hisActInst;
        do {
            if (!it.hasNext()) {
                return null;
            }
            hisActInst = (HistoricActivityInstance) it.next();
        } while (!hisActInst.getActivityId().equals(actId));
        return hisActInst;
    }
}

