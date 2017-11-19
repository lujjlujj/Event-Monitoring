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

package com.timpact.mdb;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import com.timpact.mdb.Event.IBMBPM857JSONEventConverter;
import com.timpact.mdb.Event.IBMBPMEventConstants;
import com.timpact.mdb.Event.JSONEventConverter;
import com.timpact.mdb.config.MDBConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.InputStream;
import java.util.Iterator;

/**
 * Created by Terry on 17-9-21.
 */
@MessageDriven(mappedName = "esi/eventMonitorActivationSpec", activationConfig = {
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/monQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class MonitorEventMessageDrivenBean implements MessageListener {

    private Log log = LogFactory.getLog(MonitorEventMessageDrivenBean.class);

    private MDBConfiguration config;

    private HttpClient httpClient = new HttpClient();

    private JSONEventConverter converter;

    /**
     * Construct <code>MonitorEventMessageDrivenBean</code>
     *
     * @throws Throwable if any error for initial setup
     */
    public MonitorEventMessageDrivenBean() throws Throwable {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("EventMonitorMDB.yml");
            YAMLFactory yamlFactory = new YAMLFactory();
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            YAMLParser yamlParser = yamlFactory.createParser(inputStream);
            final JsonNode node = mapper.readTree(yamlParser);
            TreeTraversingParser treeTraversingParser = new TreeTraversingParser(node);
            config = mapper.readValue(treeTraversingParser, MDBConfiguration.class);
            converter = new IBMBPM857JSONEventConverter(config.getJsonVersion(), config.getBpmCellName());
        } catch (Exception e) {
            log.error("Failed to initiate MDBConfiguration from EventMonitorMDB.yml.", e);
            throw new Throwable();
        }
    }

    /**
     * @param message The message from the queue
     */
    public void onMessage(Message message) {
        //log.info("Received message:" + message.toString());
        String messageId = "";
        if (message instanceof TextMessage) {
            try {
                messageId = message.getJMSMessageID().toString();
                String inputStr = ((TextMessage) message).getText();
                if (config.isConvertXml2JsonRequired()) {
                    Xml2JsonConverter converter = new Xml2JsonConverter();
                    inputStr = converter.convert(((TextMessage) message).getText());
                }
                // DO Conversion
                JSONObject jsonObject = converter.convert(new JSONObject(inputStr));
                if (jsonObject == null) {
                    return;
                }
                // Log the event into log file
                if (config.isGenerateLogRequred()) {
                    log.info("BPM Monitoring Event: " + jsonObject.toString());
                }
                // Send the event to elastic search
                if (config.getEsConfiguration() != null & config.getEsConfiguration().isEnabled()) {
                    String eventType = jsonObject.getString("eventType");
                    String action = jsonObject.getString("action");
                    if (config.getEsConfiguration().isMergeEventRequired()) {
                        if (action.equals(IBMBPMEventConstants.STATUS_ACTIVITY_READY) || action.equals(IBMBPMEventConstants.STATUS_PROCESS_STARTED)) {
                            jsonObject.put("startTime", jsonObject.getString("eventTime"));
                            insertDocument(jsonObject, getIndexType(eventType));
                        }
                        if (action.equals(IBMBPMEventConstants.STATUS_ACTIVITY_ACTIVE)) {
                            processActiveEvent(eventType, "activityFullId:" + jsonObject.getString("activityFullId"), jsonObject);
                        }
                        if (action.equals(IBMBPMEventConstants.STATUS_ACTIVITY_COMPLETED)) {
                            processCompletionEvent(eventType, "activityFullId:" + jsonObject.getString("activityFullId"), jsonObject);
                        }
                        if (action.equals(IBMBPMEventConstants.STATUS_PROCESS_COMPLETED)) {
                            processCompletionEvent(eventType, "processInstanceUUID:" + jsonObject.getString("processInstanceUUID"), jsonObject);

                        }
                    } else {
                        insertDocument(jsonObject, getIndexType(eventType));
                    }
                }
            } catch (Exception e) {
                log.error(String.format("Failed to process message %s .", messageId), e);
            }
        } else {
            log.warn(String.format("Unspported message type %s .", message.getClass().toString()));
        }
    }

    private void updateDocument(String indexType, String indexId, JSONObject jsonObject) throws Exception {
        PostMethod postMethod = new PostMethod("http://" + config.getEsConfiguration().getHosts() + "/"
                + config.getEsConfiguration().getIndex() + "/"
                + indexType + "/" + indexId + "/_update?pretty");
        JSONObject data = new JSONObject();
        data.put("doc", jsonObject);
        StringRequestEntity requestEntity = new StringRequestEntity(
                data.toString(), "application/json", "UTF-8");
        postMethod.setRequestEntity(requestEntity);
        httpClient.executeMethod(postMethod);
        handlerResult(postMethod);
    }

    private JSONObject searchEvent(String eventType, String queryCondition) throws Exception {
        GetMethod getMethod = new GetMethod("http://" + config.getEsConfiguration().getHosts() + "/"
                + config.getEsConfiguration().getIndex() + "/"
                + getIndexType(eventType) + "/_search?q=" + queryCondition + "&pretty");
        httpClient.executeMethod(getMethod);
        handlerResult(getMethod);
        JSONObject result = new JSONObject(getMethod.getResponseBodyAsString());
        if (result.has("hits") && result.getJSONObject("hits").getInt("total") > 0) {
            return result.getJSONObject("hits").getJSONArray("hits").getJSONObject(0);
        } else if (result.has("error")) {
            log.error(String.format("Failed to search %s event with condition (%s), error: %s.", eventType, queryCondition,
                    result.getJSONObject("error").getJSONObject("root_cause").getString("reason")));
            return null;
        } else {
            log.warn(String.format("%s is not found with condition (%s).", eventType, queryCondition));
            return null;
        }
    }



    private void processCompletionEvent(String eventType, String queryCondition, JSONObject jsonObject) throws Exception {
        JSONObject result = searchEvent(eventType, queryCondition);
        if (result != null) {
            jsonObject.put("completionTime", jsonObject.get("eventTime"));
            updateDocument(getIndexType(eventType), result.getString("_id"), jsonObject);
        } else {
            insertDocument(jsonObject, getIndexType(eventType));
        }
    }

    private void processActiveEvent(String eventType, String queryCondition, JSONObject jsonObject) throws Exception {
        JSONObject result = searchEvent(eventType, queryCondition);
        if (result != null) {
            jsonObject.put("startTime", jsonObject.get("eventTime"));
            updateDocument(getIndexType(eventType), result.getString("_id"), jsonObject);
        } else {
            insertDocument(jsonObject, getIndexType(eventType));
        }
    }

    /**
     * Send the document to elastic
     *
     * @param jsonObject
     * @throws Exception if any exception occurs
     */
    private void insertDocument(JSONObject jsonObject, String type) throws Exception {
        PostMethod postMethod = new PostMethod("http://" + config.getEsConfiguration().getHosts() + "/"
                + config.getEsConfiguration().getIndex() + "/"
                + type + "?pretty");
        StringRequestEntity requestEntity = new StringRequestEntity(
                jsonObject.toString(), "application/json", "UTF-8");
        postMethod.setRequestEntity(requestEntity);

        // Set the message timeout
        if (config.getEsConfiguration().getTimeout() != 0) {
            httpClient.getHttpConnectionManager().
                    getParams().setConnectionTimeout(config.getEsConfiguration().getTimeout());
            httpClient.getHttpConnectionManager().
                    getParams().setSoTimeout(config.getEsConfiguration().getTimeout());
        }
        httpClient.executeMethod(postMethod);
        handlerResult(postMethod);
    }

    /**
     * Gets event type
     *
     * @param type of the event
     */
    private String getIndexType(String type) throws Exception {
        if (type.equalsIgnoreCase(IBMBPMEventConstants.PROCESS_TYPE))
            return config.getEsConfiguration().getProcessType();

        if (type.equalsIgnoreCase(IBMBPMEventConstants.ACTIVITY_TYPE))
            return config.getEsConfiguration().getActivityType();

        throw new Exception("Unsupported Event getIndexType");

    }

    private void handlerResult(HttpMethodBase method) throws Exception {
        JSONObject result = new JSONObject(method.getResponseBodyAsString());
        log.info(result.toString());
    }
}
