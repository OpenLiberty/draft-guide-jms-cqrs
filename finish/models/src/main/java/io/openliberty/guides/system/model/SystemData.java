// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2023 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
// end::copyright[]
package io.openliberty.guides.system.model;

import java.io.Serializable;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.json.JsonObject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Schema(name = "SystemData",
        description = "POJO that represents a single inventory entry.")
@Entity
@Table(name = "SystemData")
@NamedQuery(name = "SystemData.findAll", query = "SELECT e FROM SystemData e")
@NamedQuery(name = "SystemData.findSystem",
            query = "SELECT e FROM SystemData e WHERE e.hostname = :hostname")
public class SystemData implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Jsonb jsonb = JsonbBuilder.create();

    @SequenceGenerator(name = "SEQ",
                       sequenceName = "systemData_id_seq",
                       allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ")
    @Id
    @Column(name = "id")
    private int id;

    @Schema(required = true)
    @Column(name = "hostname")
    private String hostname;

    @Column(name = "osName")
    private String osName;
    @Column(name = "javaVersion")
    private String javaVersion;
    @Column(name = "heapSize")
    private Long heapSize;

    public SystemData() {
    }

    public SystemData(JsonObject jObj) {
        this.id = jObj.getJsonNumber("id").intValue();
        this.hostname = jObj.getString("hostname");
        this.osName = jObj.getString("osName");
        this.javaVersion = jObj.getString("javaVersion");
        this.heapSize = jObj.getJsonNumber("heapSize").longValue();
    }

    public SystemData(String hostname, String osName, String javaVer, Long heapSize) {
        this.hostname = hostname;
        this.osName = osName;
        this.javaVersion = javaVer;
        this.heapSize = heapSize;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    public Long getHeapSize() {
        return heapSize;
    }

    public void setHeapSize(Long heapSize) {
        this.heapSize = heapSize;
    }

    @Override
    public int hashCode() {
        return hostname.hashCode();
    }

    @Override
    public boolean equals(Object system) {
        if (system instanceof SystemData) {
            SystemData sd = (SystemData) system;
            return hostname.equals(sd.getHostname());
        }
        return false;
    }

    public String toString() {
        return jsonb.toJson(this);
    }
}
