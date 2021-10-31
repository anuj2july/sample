package uk.gov.dwp.cmg.utils;

import java.util.ArrayList;
import java.util.List;

public class SubscribeConsumer {

    private List<String> topics = new ArrayList<>();


    public SubscribeConsumer(String arrVal) {
        topics.add(arrVal);
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

}
