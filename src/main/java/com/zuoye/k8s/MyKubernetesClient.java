package com.zuoye.k8s;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceBuilder;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;

/**
 * @author ZhangXueJun
 * @Date 2023年01月29日
 */
public class MyKubernetesClient {

    private io.fabric8.kubernetes.client.KubernetesClient client;

    public MyKubernetesClient() {
        System.setProperty("kubeconfig", "/Users/zhangxuejun/.kube/config");
        client = new DefaultKubernetesClient();
    }

    public static MyKubernetesClient getInstance() {
        return new MyKubernetesClient();
    }

    public void close() {
        this.client.close();
    }

    public void createNamespace() {
        Namespace namespace = new NamespaceBuilder()
                .withNewMetadata()
                .withName("pkslow1")
                .addToLabels("reason", "pkslow-sample")
                .endMetadata()
                .build();
        client.namespaces().createOrReplace(namespace);

    }

    public void createNginxPod() {

        Pod pod = new PodBuilder()
                .withNewMetadata()
                .withName("nginx")
                .addToLabels("app", "nginx")
                .endMetadata()
                .withNewSpec()
                .addNewContainer()
                .withName("nginx")
                .withImage("nginx:1.19.5")
                .endContainer()
                .endSpec()
                .build();
        client.pods().inNamespace("pkslow").createOrReplace(pod);
    }
}
