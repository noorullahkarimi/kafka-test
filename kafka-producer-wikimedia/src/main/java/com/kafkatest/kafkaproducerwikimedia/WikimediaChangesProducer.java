package com.kafkatest.kafkaproducerwikimedia;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.concurrent.TimeUnit;
@Service
public class WikimediaChangesProducer {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(WikimediaChangesProducer.class);
    private KafkaTemplate<String, String> KafkaTemplate;

    public WikimediaChangesProducer(
            KafkaTemplate<String, String> kafkaTemplate
    ){
        this.KafkaTemplate = kafkaTemplate;
    }
    public void sendMessage()throws InterruptedException{
        String topic = "wikimedia-recentchange";
        EventHandler eventHandler = new WikimediaChangeHandler(KafkaTemplate, topic);
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";
        EventSource.Builder builder = new EventSource.Builder(eventHandler, URI.create(url));
        EventSource eventSource = builder.build();
        eventSource.start();
        TimeUnit.MINUTES.sleep(10);
    }

}
