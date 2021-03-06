/*
 * Copyright 2017 HugeGraph Authors
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.baidu.hugegraph.dist;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.baidu.hugegraph.HugeException;
import com.baidu.hugegraph.backend.serializer.SerializerFactory;
import com.baidu.hugegraph.backend.store.BackendProviderFactory;
import com.baidu.hugegraph.config.CoreOptions;
import com.baidu.hugegraph.config.HugeConfig;
import com.baidu.hugegraph.config.OptionSpace;
import com.baidu.hugegraph.config.ServerOptions;
import com.baidu.hugegraph.util.E;

public class RegisterUtil {

    static {
        OptionSpace.register("core", CoreOptions.instance());
        OptionSpace.register("dist", DistOptions.instance());
    }

    public static void registerBackends() {
        String confFile = "/backend.properties";
        InputStream input = RegisterUtil.class.getClass()
                                        .getResourceAsStream(confFile);
        E.checkState(input != null, "Can't read file '%s' as stream", confFile);

        PropertiesConfiguration props = new PropertiesConfiguration();
        props.setDelimiterParsingDisabled(true);
        try {
            props.load(input);
        } catch (ConfigurationException e) {
            throw new HugeException("Can't load config file: %s", e, confFile);
        }

        HugeConfig config = new HugeConfig(props);
        List<String> backends = config.get(DistOptions.BACKENDS);
        for (String backend : backends) {
            registerBackend(backend);
        }
    }

    private static void registerBackend(String backend) {
        switch (backend) {
            case "cassandra":
                registerCassandra();
                break;
            case "scylladb":
                registerScyllaDB();
                break;
            case "hbase":
                registerHBase();
                break;
            case "rocksdb":
                registerRocksDB();
                break;
            case "mysql":
                registerMysql();
                break;
            case "palo":
                registerPalo();
                break;
            default:
                throw new HugeException("Unsupported backend type '%s'", backend);
        }
    }

    public static void registerCassandra() {
        // Register config
        OptionSpace.register("cassandra",
                "com.baidu.hugegraph.backend.store.cassandra.CassandraOptions");
        // Register serializer
        SerializerFactory.register("cassandra",
                "com.baidu.hugegraph.backend.store.cassandra.CassandraSerializer");
        // Register backend
        BackendProviderFactory.register("cassandra",
                "com.baidu.hugegraph.backend.store.cassandra.CassandraStoreProvider");
    }

    public static void registerScyllaDB() {
        // Register config
        OptionSpace.register("scylladb",
                "com.baidu.hugegraph.backend.store.cassandra.CassandraOptions");
        // Register serializer
        SerializerFactory.register("scylladb",
                "com.baidu.hugegraph.backend.store.cassandra.CassandraSerializer");
        // Register backend
        BackendProviderFactory.register("scylladb",
                "com.baidu.hugegraph.backend.store.scylladb.ScyllaDBStoreProvider");
    }

    public static void registerHBase() {
    }

    public static void registerRocksDB() {
        // Register config
        OptionSpace.register("rocksdb",
                "com.baidu.hugegraph.backend.store.rocksdb.RocksDBOptions");
        // Register backend
        BackendProviderFactory.register("rocksdb",
                "com.baidu.hugegraph.backend.store.rocksdb.RocksDBStoreProvider");
        BackendProviderFactory.register("rocksdbsst",
                "com.baidu.hugegraph.backend.store.rocksdbsst.RocksDBSstStoreProvider");
    }

    public static void registerMysql() {
        // Register config
        OptionSpace.register("mysql",
                "com.baidu.hugegraph.backend.store.mysql.MysqlOptions");
        // Register serializer
        SerializerFactory.register("mysql",
                "com.baidu.hugegraph.backend.store.mysql.MysqlSerializer");
        // Register backend
        BackendProviderFactory.register("mysql",
                "com.baidu.hugegraph.backend.store.mysql.MysqlStoreProvider");
    }

    public static void registerPalo() {
        // Register config
        OptionSpace.register("palo",
                "com.baidu.hugegraph.backend.store.palo.PaloOptions");
        // Register serializer
        SerializerFactory.register("palo",
                "com.baidu.hugegraph.backend.store.palo.PaloSerializer");
        // Register backend
        BackendProviderFactory.register("palo",
                "com.baidu.hugegraph.backend.store.palo.PaloStoreProvider");
    }

    public static void registerServer() {
        OptionSpace.register("server", ServerOptions.instance());
    }
}
