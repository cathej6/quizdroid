package edu.washington.cathej.quizdroid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by catherinejohnson on 2/9/17.
 */

public class TopicRepository {
    private List<Topic> topics = new ArrayList<Topic>();

    public List<Topic> getAllTopics() {
        return topics;
    }

}
