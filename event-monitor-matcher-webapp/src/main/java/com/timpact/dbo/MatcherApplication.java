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

import java.security.Principal;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <b><code>WXApplication</code></b>
 *
 * <b>Creation Time:</b> 2017年1月7日 上午11:20:14
 */
@SpringBootApplication
@RestController
public class MatcherApplication {
    /**
     * Constructs <code>WXApplication</code>
     */
    public MatcherApplication() {
    }

    @RequestMapping({"/user"})
    public Principal user(Principal user) {
        return user;
    }

    public static void main(String[] args) {
        SpringApplication.run(MatcherApplication.class, args);
    }
}
