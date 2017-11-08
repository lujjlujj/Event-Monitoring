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
 *
 */
public class ELKIndexCreator {

    private static String INDEX_NAME = "process-event-log";

    private static String TYPE_NAME = "process";

    private static String URL = "http://115.28.47.100:9200/";

    private HttpClient httpClient = new HttpClient();

    @Test
    public void checkListType() {
        List list = new ArrayList();
        if (list instanceof Object) {
            System.out.println("Hi");
        }
    }

    //@Test
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

    //@Test
    public void getIndex() throws HttpException, IOException {
        GetMethod getMethod = new GetMethod(URL + INDEX_NAME);
        httpClient.executeMethod(getMethod);
        System.out.println(getMethod.getResponseBodyAsString());
    }

    //@Test
    public void deleteIndex() throws HttpException, IOException {
        DeleteMethod deleteMethod = new DeleteMethod(URL + INDEX_NAME);
        httpClient.executeMethod(deleteMethod);
        System.out.println(deleteMethod.getResponseBodyAsString());
    }

    //@Test
    public void updateIndexMapping() throws JSONException, HttpException,
            IOException {
        PutMethod putMethod = new PutMethod(URL + INDEX_NAME + "/_mapping/"
                + TYPE_NAME + "?pretty");
        JSONObject propertyObject = new JSONObject();
        JSONObject fieldObject = new JSONObject();
        fieldObject.put("taskId", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        fieldObject.put("taskName", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        fieldObject.put("priority", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        fieldObject.put("startDate", createFieldPropertyJSONObject("date", "not_analyzed", "yyyy-MM-dd'T'HH:mm:ss.SSSZZ"));
        fieldObject.put("completionDate", createFieldPropertyJSONObject("date", "not_analyzed", "yyyy-MM-dd'T'HH:mm:ss.SSSZZ"));
        fieldObject.put("actionBy", createFieldPropertyJSONObject("string", "not_analyzed", ""));
        propertyObject.put("properties", fieldObject);
        StringRequestEntity requestEntity = new StringRequestEntity(
                propertyObject.toString(), "application/json", "UTF-8");
        putMethod.setRequestEntity(requestEntity);
        httpClient.executeMethod(putMethod);
        System.out.println(putMethod.getResponseBodyAsString());
    }

    //@Test
    public void getIndexMapping() throws HttpException, IOException {
        GetMethod getMethod = new GetMethod(URL + INDEX_NAME + "/_mapping/"
                + TYPE_NAME + "?pretty");
        httpClient.executeMethod(getMethod);
        System.out.println(getMethod.getResponseBodyAsString());

    }

    //Test
    public void insertDocument() throws Exception{
        PostMethod postMethod = new PostMethod(URL + INDEX_NAME + "/"
                + TYPE_NAME + "?pretty");
        JSONObject propertyObject = new JSONObject();
        propertyObject.put("taskId", "12345");
        propertyObject.put("taskName", "Data Capture");
        propertyObject.put("priority", "High");
        propertyObject.put("startDate", "2017-04-02T16:00:00.000Z");
        if (!StringUtils.isEmpty("2017-04-02T16:00:00.000Z")) {
           propertyObject.put("completionDate", "2017-04-02T16:00:00.000Z");
        }
        propertyObject.put("actionBy", "Terry J J LU");
        StringRequestEntity requestEntity = new StringRequestEntity(
                propertyObject.toString(), "application/json", "UTF-8");
        postMethod.setRequestEntity(requestEntity);
        httpClient.executeMethod(postMethod);
        JSONObject result = new JSONObject(postMethod.getResponseBodyAsString());
        System.out.println(result.get("created"));
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
