package com.zuoye.k8s;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceBuilder;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

/**
 * @author ZhangXueJun
 * @Date 2023年01月29日
 */
public class KubernetesClientTest {

    public static void main(String[] args) {
        System.setProperty("kubeconfig", "/Users/zhangxuejun/.kube/config");
        KubernetesClient client = new DefaultKubernetesClient();
        Namespace namespace = new NamespaceBuilder()
                .withNewMetadata()
                .withName("pkslow1")
                .addToLabels("reason", "pkslow-sample")
                .endMetadata()
                .build();
        client.namespaces().createOrReplace(namespace);
        System.out.println("");
    }
}
