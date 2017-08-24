package com.hazelcast.swarm.driver;


import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Slf4jFactory;
import com.hazelcast.spi.discovery.AddressLocator;
import com.hazelcast.swarm.SwarmNetworkInspector;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class App
{

    private static HazelcastInstance instance;

    public static void main( String[] args ) throws FileNotFoundException, InterruptedException {
        new App().run();
    }

    void run()  throws FileNotFoundException, InterruptedException{

        ClassLoader classLoader = App.class.getClassLoader();
        InputStream stream = classLoader.getResourceAsStream("hazelcast.xml");

        Config config = new XmlConfigBuilder(stream).build();
        config.getNetworkConfig().getJoin().getDiscoveryConfig().setAddressLocator(createAddressLocator(new Slf4jFactory().getLogger("app")));

        instance = Hazelcast.newHazelcastInstance(config);

        while(true){
            TimeUnit.SECONDS.sleep(1);
        }
    }

    private AddressLocator createAddressLocator(ILogger logger) {
        return new SwarmNetworkInspector(createDockerClient(), logger);
    }

    private DockerClient createDockerClient() {
        return DockerClientBuilder.getInstance(config())
                .build();
    }

    private DefaultDockerClientConfig config() {
        DefaultDockerClientConfig.Builder builder = DefaultDockerClientConfig.createDefaultConfigBuilder();
        return builder.build();
    }
}
