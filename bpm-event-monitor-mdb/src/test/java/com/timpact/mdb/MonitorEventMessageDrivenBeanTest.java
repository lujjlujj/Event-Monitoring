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

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.io.File;
import java.util.Enumeration;

/**
 * Created by Terry on 17-9-21.
 */
public class MonitorEventMessageDrivenBeanTest {

    @Test
    public void testActivityEvent() throws Throwable {
        MonitorEventMessageDrivenBean bean = new MonitorEventMessageDrivenBean();
        TextMessageImpl message = new TextMessageImpl();
        message.setText(FileUtils.readFileToString(new File(getClass().getClassLoader().getResource("IBMBPMEvent/activity_active").getFile()), "UTF-8"));
        bean.onMessage(message);
        Thread.sleep(1000);
        message.setText(FileUtils.readFileToString(new File(getClass().getClassLoader().getResource("IBMBPMEvent/activity_completed").getFile()), "UTF-8"));
        bean.onMessage(message);
    }

    @Test
    public void testProcessEvent() throws Throwable {
        MonitorEventMessageDrivenBean bean = new MonitorEventMessageDrivenBean();
        TextMessageImpl message = new TextMessageImpl();
        message.setText(FileUtils.readFileToString(new File(getClass().getClassLoader().getResource("IBMBPMEvent/process_started").getFile()), "UTF-8"));
        bean.onMessage(message);
       // Thread.sleep(1000);
       // message.setText(FileUtils.readFileToString(new File(getClass().getClassLoader().getResource("IBMBPMEvent/process_completed").getFile()), "UTF-8"));
       // bean.onMessage(message);
    }

    class TextMessageImpl implements TextMessage {

        private String text;

        public void setText(String string) throws JMSException {
            text = string;
        }

        public String getText() throws JMSException {
            return text;
        }

        public String getJMSMessageID() throws JMSException {
            return "1234435";
        }

        public void setJMSMessageID(String id) throws JMSException {

        }

        public long getJMSTimestamp() throws JMSException {
            return 0;
        }

        public void setJMSTimestamp(long timestamp) throws JMSException {

        }

        public byte[] getJMSCorrelationIDAsBytes() throws JMSException {
            return new byte[0];
        }

        public void setJMSCorrelationIDAsBytes(byte[] correlationID) throws JMSException {

        }

        public void setJMSCorrelationID(String correlationID) throws JMSException {

        }

        public String getJMSCorrelationID() throws JMSException {
            return null;
        }

        public Destination getJMSReplyTo() throws JMSException {
            return null;
        }

        public void setJMSReplyTo(Destination replyTo) throws JMSException {

        }

        public Destination getJMSDestination() throws JMSException {
            return null;
        }

        public void setJMSDestination(Destination destination) throws JMSException {

        }

        public int getJMSDeliveryMode() throws JMSException {
            return 0;
        }

        public void setJMSDeliveryMode(int deliveryMode) throws JMSException {

        }

        public boolean getJMSRedelivered() throws JMSException {
            return false;
        }

        public void setJMSRedelivered(boolean redelivered) throws JMSException {

        }

        public String getJMSType() throws JMSException {
            return null;
        }

        public void setJMSType(String type) throws JMSException {

        }

        public long getJMSExpiration() throws JMSException {
            return 0;
        }

        public void setJMSExpiration(long expiration) throws JMSException {

        }

        public int getJMSPriority() throws JMSException {
            return 0;
        }

        public void setJMSPriority(int priority) throws JMSException {

        }

        public void clearProperties() throws JMSException {

        }

        public boolean propertyExists(String name) throws JMSException {
            return false;
        }

        public boolean getBooleanProperty(String name) throws JMSException {
            return false;
        }

        public byte getByteProperty(String name) throws JMSException {
            return 0;
        }

        public short getShortProperty(String name) throws JMSException {
            return 0;
        }

        public int getIntProperty(String name) throws JMSException {
            return 0;
        }

        public long getLongProperty(String name) throws JMSException {
            return 0;
        }

        public float getFloatProperty(String name) throws JMSException {
            return 0;
        }

        public double getDoubleProperty(String name) throws JMSException {
            return 0;
        }

        public String getStringProperty(String name) throws JMSException {
            return null;
        }

        public Object getObjectProperty(String name) throws JMSException {
            return null;
        }

        public Enumeration getPropertyNames() throws JMSException {
            return null;
        }

        public void setBooleanProperty(String name, boolean value) throws JMSException {

        }

        public void setByteProperty(String name, byte value) throws JMSException {

        }

        public void setShortProperty(String name, short value) throws JMSException {

        }

        public void setIntProperty(String name, int value) throws JMSException {

        }

        public void setLongProperty(String name, long value) throws JMSException {

        }

        public void setFloatProperty(String name, float value) throws JMSException {

        }

        public void setDoubleProperty(String name, double value) throws JMSException {

        }

        public void setStringProperty(String name, String value) throws JMSException {

        }

        public void setObjectProperty(String name, Object value) throws JMSException {

        }

        public void acknowledge() throws JMSException {

        }

        public void clearBody() throws JMSException {

        }
    }
}
