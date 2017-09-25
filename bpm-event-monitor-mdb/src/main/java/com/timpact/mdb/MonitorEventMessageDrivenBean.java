package com.timpact.mdb;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * Created by Terry on 17-9-21.
 */
@MessageDriven(mappedName = "TDB", activationConfig = {
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class MonitorEventMessageDrivenBean implements MessageListener {

    private Log log = LogFactory.getLog(MonitorEventMessageDrivenBean.class);


    public void onMessage(Message message) {
        log.info("Received message:" + message.toString());
    }
}
