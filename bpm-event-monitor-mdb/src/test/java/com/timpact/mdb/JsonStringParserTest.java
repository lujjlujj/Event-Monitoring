package com.timpact.mdb;

import com.timpact.mdb.Event.IBMBPM857JSONEventConverter;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.junit.Test;

import java.io.File;

public class JsonStringParserTest {


    @Test
    public void parseProcessStartedJSONString() throws Exception {

        JSONObject root = new JSONObject(FileUtils.readFileToString(new File(getClass().getClassLoader().getResource("IBMBPMEvent/process_started").getFile()),
                "UTF-8"));
        IBMBPM857JSONEventConverter converter = new IBMBPM857JSONEventConverter("8.5.7", "bpmCell01");
        System.out.println(converter.convert(root).toString());
    }


    @Test
    public void parseProcessCompletedJSONString() throws Exception {
        JSONObject root = new JSONObject(FileUtils.readFileToString(new File(getClass().getClassLoader().getResource("IBMBPMEvent/process_completed").getFile()),
                "UTF-8"));
        IBMBPM857JSONEventConverter converter = new IBMBPM857JSONEventConverter("8.5.7", "bpmCell01");
        System.out.println(converter.convert(root).toString());
    }

    @Test
    public void parseActivityReadyJSONString() throws Exception {
        JSONObject root = new JSONObject(FileUtils.readFileToString(new File(getClass().getClassLoader().getResource("IBMBPMEvent/activity_ready").getFile()),
                "UTF-8"));
        IBMBPM857JSONEventConverter converter = new IBMBPM857JSONEventConverter("8.5.7", "bpmCell01");
        System.out.println(converter.convert(root).toString());
    }

    @Test
    public void parseActivityActiveJSONString() throws Exception {
        JSONObject root = new JSONObject(FileUtils.readFileToString(new File(getClass().getClassLoader().getResource("IBMBPMEvent/activity_active").getFile()),
                "UTF-8"));
        IBMBPM857JSONEventConverter converter = new IBMBPM857JSONEventConverter("8.5.7", "bpmCell01");
        System.out.println(converter.convert(root).toString());
    }


    @Test
    public void parseActivityCompletedJSONString() throws Exception {
        JSONObject root = new JSONObject(FileUtils.readFileToString(new File(getClass().getClassLoader().getResource("IBMBPMEvent/activity_completed").getFile()),
                "UTF-8"));
        IBMBPM857JSONEventConverter converter = new IBMBPM857JSONEventConverter("8.5.7", "bpmCell01");
        System.out.println(converter.convert(root).toString());
    }
}
