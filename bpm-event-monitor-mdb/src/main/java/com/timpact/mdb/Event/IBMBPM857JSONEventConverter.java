/*
 * Copyright (c) 2017 Sprinter Development Team. All rights reserved.
 *
 *  This software is only to be used for the purpose for which it has been
 *  provided. No part of it is to be reproduced, disassembled, transmitted,
 *  stored in a retrieval system, nor translated in any human or computer
 *  language in any way for any purposes whatsoever without the prior written
 *  consent of the Sprinter Development Team.
 *  Infringement of copyright is a serious civil and criminal offence, which can
 *  result in heavy fines and payment of substantial damages.
 */
package com.timpact.mdb.Event;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Terry on 17-9-26.
 */
public class IBMBPM857JSONEventConverter implements JSONEventConverter {

    private String jsonVersion;

    private String bpmCellName;

    private List<String> activityTypelist = new ArrayList<String>();

    private List<String> processTypelist = new ArrayList<String>();


    public IBMBPM857JSONEventConverter(String jsonVersion, String bpmCellName) {
        this.jsonVersion = jsonVersion;
        this.bpmCellName = bpmCellName;
        activityTypelist.add(IBMBPMEventConstants.STATUS_ACTIVITY_READY);
        activityTypelist.add(IBMBPMEventConstants.STATUS_ACTIVITY_ACTIVE);
        activityTypelist.add(IBMBPMEventConstants.STATUS_ACTIVITY_COMPLETED);

        processTypelist.add(IBMBPMEventConstants.STATUS_PROCESS_STARTED);
        processTypelist.add(IBMBPMEventConstants.STATUS_PROCESS_COMPLETED);
    }

    public JSONObject convert(JSONObject root) throws Exception {
        JSONObject targetObject = new JSONObject();
        targetObject.put("bpmCellName", bpmCellName);
        targetObject.put("jsonVersion", jsonVersion);
        JSONObject eventPointData = root.getJSONObject("mon:monitorEvent").getJSONObject("mon:eventPointData");
        // Event Type
        String action = eventPointData.getJSONObject("mon:kind").getString("content");
        if (action.contains(":")) {
            action = action.substring(action.indexOf(":") + 1, action.length());
        }
        String eventType = null;
        if (activityTypelist.contains(action)) {
            eventType = IBMBPMEventConstants.ACTIVITY_TYPE;
        }
        if (processTypelist.contains(action)) {
            eventType = IBMBPMEventConstants.PROCESS_TYPE;
        }
        if (eventType == null) {
            return null;
        }
        targetObject.put("action", action);
        targetObject.put("eventType", eventType);
        // Time
        String eventTime = eventPointData.getJSONObject("mon:time").getString("content");
        targetObject.put("eventTime", eventTime);

        // Model
        JSONArray models = eventPointData.getJSONArray("mon:model");

        for (int i = 0; i < models.length(); i++) {
            JSONObject model = models.getJSONObject(i);
            String modelType = model.getString("mon:type");
            if (modelType.equalsIgnoreCase("bpmn:process")) {
                String processInstanceId = model.getJSONObject("mon:instance").getString("mon:id");
                targetObject.put("processInstanceId", processInstanceId);
                String processName = model.getString("mon:name");
                targetObject.put("processName", processName);
                String processVersion = model.getString("mon:version");
                targetObject.put("processVersion", processVersion);
                String modelId = model.getString("mon:id");
                String processFullId = modelId + "." + processVersion;
                targetObject.put("processFullId", processFullId);
                String processInstanceFullId = processFullId + "." + processInstanceId;
                targetObject.put("processInstanceFullId", processInstanceFullId);
                String processInstanceUUID = bpmCellName + "-" + processInstanceFullId;
                targetObject.put("processInstanceUUID", processInstanceUUID);
                if (eventType.equals(IBMBPMEventConstants.PROCESS_TYPE)) {
                    String processStatus = model.getJSONObject("mon:instance").getString("mon:state");
                    targetObject.put("processStatus", processStatus);
                }

            }
            if (modelType.equalsIgnoreCase("wle:processApplication")) {
                String applicationName = model.getString("mon:name");
                targetObject.put("applicationName", applicationName);
            }
            if (modelType.equalsIgnoreCase("bpmn:userTask") || modelType.equalsIgnoreCase("bpmn:scriptTask")) {

                String activityVersion = model.getString("mon:version");
                targetObject.put("activityVersion", activityVersion);
                String modelId = model.getString("mon:id");
                String activityFullId = modelId + "." + activityVersion;
                targetObject.put("activityFullId", activityFullId);
                String activityName = model.getString("mon:name");
                targetObject.put("activityName", activityName);
                String activityType = model.getString("mon:type").replaceAll("bpmn:", "");
                targetObject.put("activityType", activityType);
                JSONObject taskInstance = model.getJSONObject("mon:instance");
                String activityShortId = taskInstance.getString("mon:id");
                targetObject.put("activityShortId", activityShortId);
            }
        }
        if (targetObject.has("activityShortId")) {
            String activityFullId = targetObject.getString("processInstanceFullId") + "." + targetObject.getString("activityShortId");
            targetObject.put("activityFullId", activityFullId);
            targetObject.remove("activityShortId");
        }
        if (root.getJSONObject("mon:monitorEvent").has("mon:applicationData")) {
            JSONObject applicationData = root.getJSONObject("mon:monitorEvent").getJSONObject("mon:applicationData");
            if (applicationData.has("wle:tracking-point") && applicationData.getJSONObject("wle:tracking-point").has("wle:tracked-field")) {
                JSONArray trackedFields = applicationData.getJSONObject("wle:tracking-point").getJSONArray("wle:tracked-field");
                JSONArray targetTrackedFields = new JSONArray();
                for (int i = 0; i < trackedFields.length(); i++) {
                    JSONObject trackedField = trackedFields.getJSONObject(i);
                    JSONObject targetTrackedField = new JSONObject();
                    String fieldName = trackedField.getString("wle:name");
                    String fieldType = trackedField.getString("wle:type").replaceAll("xs:", "");
                    fieldName = fieldName + "_" + fieldType;
                    String fieldValue = trackedField.getString("content");
                    targetTrackedField.put(fieldName, fieldValue);
                    targetTrackedFields.put(targetTrackedField);
                }
                targetObject.put("businessFields", targetTrackedFields);
            }
        }
        return targetObject;
    }
}
