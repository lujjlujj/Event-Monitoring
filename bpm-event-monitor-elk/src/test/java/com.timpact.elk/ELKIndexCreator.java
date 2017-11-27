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
package com.timpact.elk;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

/**
 * <b><code>ELKIndexCreator</code></b>
 * <p>
 * class_comment
 * <p>
 * <b>Creation Time:</b> 2017年1月7日 上午11:20:14
 */
public class ELKIndexCreator {

    private static String INDEX_NAME = "bpm-events";

    private static String PROCESS_TYPE_NAME = "ProcessSummary";

    private static String ACTIVITY_TYPE_NAME = "ActivitySummary";

    private static String URL = "http://115.28.47.100:9200/";

    private HttpClient httpClient = new HttpClient();

    @Test
    public void createIndex() throws JSONException, HttpException, IOException {
        PutMethod putMethod = new PutMethod(URL + INDEX_NAME);
        JSONObject masterObject = new JSONObject();
        JSONObject settingObject = new JSONObject();
        settingObject.put("number_of_shards", 3);
        settingObject.put("number_of_replicas", 1);
        masterObject.put("settings", settingObject);
        StringRequestEntity requestEntity = new StringRequestEntity(
                masterObject.toString(), "application/json", "UTF-8");
        putMethod.setRequestEntity(requestEntity);
        httpClient.executeMethod(putMethod);
        System.out.println(putMethod.getResponseBodyAsString());
    }


    @Test
    public void getIndex() throws HttpException, IOException {
        GetMethod getMethod = new GetMethod(URL + INDEX_NAME);
        httpClient.executeMethod(getMethod);
        System.out.println(getMethod.getResponseBodyAsString());
    }

    @Test
    public void deleteIndex() throws HttpException, IOException {
        DeleteMethod deleteMethod = new DeleteMethod(URL + INDEX_NAME);
        httpClient.executeMethod(deleteMethod);
        System.out.println(deleteMethod.getResponseBodyAsString());
    }
    @Test
    public void rebuildIndex() throws Exception {
        deleteIndex();
        createIndex();
        updateIndexMappingForProcess();
        updateIndexMappingForActivity();
    }

    @Test
    public void updateIndexMappingForProcess() throws JSONException, HttpException,
            IOException {
        PutMethod putMethod = new PutMethod(URL + INDEX_NAME + "/_mapping/"
                + PROCESS_TYPE_NAME + "?pretty");
        JSONObject propertyObject = new JSONObject();
        JSONObject fieldObject = new JSONObject();
        fieldObject.put("bpmCellName", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        fieldObject.put("jsonVersion", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        fieldObject.put("action", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        fieldObject.put("eventType", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        fieldObject.put("startTime", createFieldPropertyJSONObject("date", "not_analyzed", "yyyy-MM-dd'T'HH:mm:ss.SSSZZ"));
        fieldObject.put("completionTime", createFieldPropertyJSONObject("date", "not_analyzed", "yyyy-MM-dd'T'HH:mm:ss.SSSZZ"));
        fieldObject.put("eventTime", createFieldPropertyJSONObject("date", "not_analyzed", "yyyy-MM-dd'T'HH:mm:ss.SSSZZ"));
        fieldObject.put("processInstanceId", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        fieldObject.put("processName", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        fieldObject.put("processVersion", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        fieldObject.put("processFullId", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        fieldObject.put("processInstanceFullId", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        fieldObject.put("processInstanceUUID", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        fieldObject.put("processStatus", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        fieldObject.put("applicationName", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        propertyObject.put("properties", fieldObject);
        StringRequestEntity requestEntity = new StringRequestEntity(
                propertyObject.toString(), "application/json", "UTF-8");
        putMethod.setRequestEntity(requestEntity);
        httpClient.executeMethod(putMethod);
        System.out.println(putMethod.getResponseBodyAsString());
    }

    @Test
    public void updateIndexMappingForActivity() throws JSONException, HttpException,
            IOException {
        PutMethod putMethod = new PutMethod(URL + INDEX_NAME + "/_mapping/"
                + ACTIVITY_TYPE_NAME + "?pretty");
        JSONObject propertyObject = new JSONObject();
        JSONObject fieldObject = new JSONObject();
        fieldObject.put("bpmCellName", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        fieldObject.put("jsonVersion", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        fieldObject.put("action", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        fieldObject.put("startTime", createFieldPropertyJSONObject("date", "not_analyzed", "yyyy-MM-dd'T'HH:mm:ss.SSSZZ"));
        fieldObject.put("completionTime", createFieldPropertyJSONObject("date", "not_analyzed", "yyyy-MM-dd'T'HH:mm:ss.SSSZZ"));
        fieldObject.put("eventTime", createFieldPropertyJSONObject("date", "not_analyzed", "yyyy-MM-dd'T'HH:mm:ss.SSSZZ"));
        fieldObject.put("processInstanceId", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        fieldObject.put("processName", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        fieldObject.put("processVersion", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        fieldObject.put("processFullId", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        fieldObject.put("processInstanceFullId", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        fieldObject.put("processInstanceUUID", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        fieldObject.put("applicationName", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        fieldObject.put("activityVersion", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        fieldObject.put("activityFullId", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        fieldObject.put("activityName", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        fieldObject.put("activityType", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        fieldObject.put("assigneeType", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        fieldObject.put("assigneeId", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        fieldObject.put("assigneeName", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        propertyObject.put("properties", fieldObject);
        StringRequestEntity requestEntity = new StringRequestEntity(
                propertyObject.toString(), "application/json", "UTF-8");
        putMethod.setRequestEntity(requestEntity);
        httpClient.executeMethod(putMethod);
        System.out.println(putMethod.getResponseBodyAsString());
    }

    @Test
    public void getIndexMappingForProcess() throws HttpException, Exception {


        GetMethod getMethod = new GetMethod(URL + INDEX_NAME + "/_mapping/"
                + PROCESS_TYPE_NAME + "?size=60&pretty");
        httpClient.executeMethod(getMethod);
        System.out.println(getMethod.getResponseBodyAsString());

    }

    @Test
    public void searchDataForProcess() throws HttpException, Exception {
        JSONObject condition = new JSONObject();
        //condition.put("_source.processInstanceUUID", "bpmCell01-c904b3b1-afc1-4698-bf5a-a20892c20275.2064.f5fa8035-29e3-4ee9-9dfb-cda7ae1f249d.705");
        condition.put("_id","AV_AhpMG0_Pl3klLvTRT");
        JSONObject term = new JSONObject();
        term.put("term", condition);
        JSONObject query = new JSONObject();
        query.put("query", term);

//        GetMethod postMethod = new GetMethod(URL + INDEX_NAME + "/"
//                + PROCESS_TYPE_NAME + "/_search?q=processInstanceUUID:bpmCell01-c904b3b1-afc1-4698-bf5a-a20892c20275.2064.f5fa8035-29e3-4ee9-9dfb-cda7ae1f249d.705&pretty");
        GetMethod postMethod = new GetMethod(URL + INDEX_NAME + "/"
                + PROCESS_TYPE_NAME + "/_search?pretty");


        httpClient.executeMethod(postMethod);
        System.out.println(postMethod.getResponseBodyAsString());
    }

    @Test
    public void searchDataForActivity() throws HttpException, IOException {
        PostMethod potMethod = new PostMethod(URL + INDEX_NAME + "/"
                + ACTIVITY_TYPE_NAME + "/_search?size=70&pretty");
        System.out.print(potMethod.getURI().toString());
        httpClient.executeMethod(potMethod);
        System.out.println(potMethod.getResponseBodyAsString());
    }


    private JSONObject createFieldPropertyJSONObject(String type, String index,
                                                     String format) throws JSONException {
        JSONObject property = new JSONObject();
        property.put("type", type);
        if (!StringUtils.isEmpty(index)) {
            property.put("index", index);
            property.put("store", true);
        }
        if (!StringUtils.isEmpty(format)) {
            property.put("format", format);
        }
        return property;
    }
}
