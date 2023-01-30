package com.zuoye.k8s;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MyKubernetesClientTest {

    MyKubernetesClient client;
    @Before
    public void before() {
        this.client = MyKubernetesClient.getInstance();
    }


    @Test
    public void createNamespaceTest() {
        this.client.createNamespace();
    }


    @Test
    public void createNginxPodTest() {
        this.client.createNginxPod();
    }

    @After
    public void after() {
        this.client.close();
    }

}