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

import org.json.JSONObject;

/**
 * Created by Terry on 17-9-26.
 */
public interface JSONEventConverter {
    /**
     * Converts the original <code>JSONObject</code> to expected <code>JSONObject</code>. Basically, it will
     * reduce the number of level on the original <code>JSONObject</code> which will simply the json structure.
     *
     * @param jsonObject original <code>JSONObject</code>
     * @return target <code>JSONObject</code>
     * @throws Exception if any error occurs.
     */
    public JSONObject convert(JSONObject jsonObject) throws Exception;
}
