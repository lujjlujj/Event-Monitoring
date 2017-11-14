package com.timpact.mdb.Event;

import org.json.JSONObject;

public interface JSONEventConverter {

    public JSONObject convert(JSONObject jsonObject) throws Exception;
}
