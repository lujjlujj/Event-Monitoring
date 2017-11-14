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

package com.timpact.mdb.config;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Terry on 17-9-26.
 */
public class MDBConfiguration {

    @JsonProperty
    private boolean generateLogRequred = false;
    @JsonProperty
    private String bpmCellName;
    @JsonProperty
    private boolean convertXml2JsonRequired;
    @JsonProperty
    private ESConfiguration esConfiguration;
    @JsonProperty
    private String jsonVersion = "8.5";

    public String getJsonVersion() {
        return jsonVersion;
    }

    public void setJsonVersion(String jsonVersion) {
        this.jsonVersion = jsonVersion;
    }


    public boolean isGenerateLogRequred() {
        return generateLogRequred;
    }

    public void setGenerateLogRequred(boolean generateLogRequred) {
        this.generateLogRequred = generateLogRequred;
    }

    public ESConfiguration getEsConfiguration() {
        return esConfiguration;
    }

    public void setEsConfiguration(ESConfiguration esConfiguration) {
        this.esConfiguration = esConfiguration;
    }

    public String getBpmCellName() {
        return bpmCellName;
    }

    public void setBpmCellName(String bpmCellName) {
        this.bpmCellName = bpmCellName;
    }

    public boolean isConvertXml2JsonRequired() {
        return convertXml2JsonRequired;
    }

    public void setConvertXml2JsonRequired(boolean convertXml2JsonRequired) {
        this.convertXml2JsonRequired = convertXml2JsonRequired;
    }
}
