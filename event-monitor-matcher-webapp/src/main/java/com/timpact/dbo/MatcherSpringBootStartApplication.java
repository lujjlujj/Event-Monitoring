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
package com.timpact.dbo;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * <b><code>MatcherSpringBootStartApplication</code></b>
 *
 * <b>Creation Time:</b> 2017年1月7日 上午11:20:14
 */
public class MatcherSpringBootStartApplication extends SpringBootServletInitializer {
    /**
     * Constructs <code>MatcherSpringBootStartApplication</code>
     */
    public MatcherSpringBootStartApplication() {
    }

    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(new Class[]{MatcherApplication.class});
    }
}
