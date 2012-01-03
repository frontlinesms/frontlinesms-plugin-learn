package net.frontlinesms.plugins.learn;

import static org.mockito.Matchers.argThat;
import net.frontlinesms.plugins.learn.data.domain.Reinforcement;
import net.frontlinesms.plugins.learn.data.domain.Topic;

import org.mockito.ArgumentMatcher;

public class LearnTestUtils {
//> TEST HELPER METHODS
	public static Topic topicWithName(final String expectedName) {
		return argThat(new ArgumentMatcher<Topic>() {
			@Override
			public boolean matches(Object a) {
				Topic t = (Topic) a;
				return t.getName().equals(expectedName);
			}
		});
	}
	
	public static Reinforcement reinforcementWithTextAndTopic(final String expectedReinforementText, final String expectedTopicName) {
		return argThat(new ArgumentMatcher<Reinforcement>() {
			@Override
			public boolean matches(Object o) {
				Reinforcement r = (Reinforcement) o;
				return r.getName().equals(expectedReinforementText) &&
						r.getTopic().getName().equals(expectedTopicName);
			}
		});
	}
}
