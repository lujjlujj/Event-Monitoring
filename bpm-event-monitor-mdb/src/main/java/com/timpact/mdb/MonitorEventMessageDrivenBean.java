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
import com.timpact.mdb.Event.JSONEventConverter;
import com.timpact.mdb.config.MDBConfiguration;
import org.apache.commons.httpclient.HttpClient;
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
            //  Class clazz = MonitorEventMessageDrivenBean.class;
            //InputStream inputStream = clazz.getResourceAsStream("/EventMonitorMDB.yml");
//
//
//            // Load the configuration
//            ClassLoader classLoader = getClass().getClassLoader();
//            URL url = classLoader.getResource("EventMonitorMDB.yml");
//            InputStream input = new FileInputStream(url.getFile());
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
                    insertDocument(jsonObject, getEventType(jsonObject.toString()));
                }
            } catch (Exception e) {
                log.error(String.format("Failed to process message %s .", messageId), e);
            }
        } else {
            log.warn(String.format("Unspported message type %s .", message.getClass().toString()));
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
        JSONObject result = new JSONObject(postMethod.getResponseBodyAsString());
        log.info(result.toString());
    }

    /**
     * Gets event type
     *
     * @param jsonStr json of string
     */
    private String getEventType(String jsonStr) throws Exception {
        JSONObject jsonObject = new JSONObject(jsonStr);
        // TODO Get the type of event
        String type = "Process";//jsonObject.getString("TODO");
        if (type.equalsIgnoreCase("Process"))
            return config.getEsConfiguration().getProcessType();

        if (type.equalsIgnoreCase("activity"))
            return config.getEsConfiguration().getActivityType();

        throw new Exception("Unsupported Event Type");

    }
}
