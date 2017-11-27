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

import org.junit.Test;

/**
 * Created by Terry on 17-9-26.
 */
public class Xml2JsonConverterTest {

    @Test
    public void testConvert() throws Exception {
        String xmlStr = "<AccountBean><id>1</id><name>zhaojd</name><email>mr_zhaojd</email><address>Guangzhou</address><birthday>1992-08</birthday></AccountBean>";
        Xml2JsonConverter converter = new Xml2JsonConverter();
        Object jsonStr = converter.convert(xmlStr);
        System.out.println(jsonStr);
    }
}
