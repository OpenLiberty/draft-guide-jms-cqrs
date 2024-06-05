// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2024 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
// end::copyright[]
package io.openliberty.guides.system;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;

import jakarta.annotation.Resource;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;

import io.openliberty.guides.models.SystemLoad;
import jakarta.inject.Inject;

@Singleton
public class SystemService {

    private static final OperatingSystemMXBean OS_MEAN =
            ManagementFactory.getOperatingSystemMXBean();
    private static String hostname = null;

    private static Logger logger = Logger.getLogger(SystemService.class.getName());
    
    @Inject
    @JMSConnectionFactory("InventoryQueueConnectionFactory")
    private JMSContext context;

    @Resource(lookup = "jms/InventoryQueue")
    private Queue queue;

    private static String getHostname() {
        if (hostname == null) {
            try {
                return InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                return System.getenv("HOSTNAME");
            }
        }
        return hostname;
    }
    // tag::schedule[]
    @Schedule(second = "*/15", minute = "*", hour = "*", persistent = false)
    // end::schedule[]
    // tag::sendSystemLoad[]
    public void sendSystemLoad() {
      SystemLoad systemLoad = new SystemLoad(getHostname(), Double.valueOf(OS_MEAN.getSystemLoadAverage()));
      context.createProducer().send(queue, systemLoad.toString());
      logger.info(systemLoad.toString());   
     }
    // end::sendSystemLoad[]
}