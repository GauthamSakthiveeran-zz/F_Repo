package com.ooyala.playback.utils;

import java.util.List;

/**
 * Created by akash on 25/08/17.
 */
public class StorageCredentials {
    public List<S3Host> s3Host;
    public List<ElementalServerCluster> elementalServerClusters;
    public List<AzureMediaHost> azureMediaHosts;
    public List<AzureHost> azureHosts;

    public StorageCredentials() {
    }

    public static class S3Host {
        public String bucket;
        public Integer hostId;
        public String accessKeyId;
        public String secretAccessKey;

        public S3Host() {
        }
    }

    public static class ElementalServerCluster {
        public ElementalServerCluster() {
        }
    }

    public static class AzureMediaHost {
        public AzureMediaHost() {
        }
    }

    public static class AzureHost {
        public AzureHost() {
        }
    }
}
