package net.frontlinesms.plugins.learn.data.domain;

public class TopicTest extends net.frontlinesms.junit.BaseTestCase {
	/** Instance of Topic under test */
	Topic t;

	public void setUp() { t = new Topic(); }

	public void testIdInitiallyZero() {
		// when
		long id = t.getId();

		// then
		assertEquals(0, id);
	}

	public void testNameInitiallyNull() {
		// when
		String name = t.getName();

		// then
		assertNull(name);
	}

	public void testNameSetGet() {
		// given
		assertNull(t.getName());

		// when
		t.setName("Maths + English");

		// then
		assertEquals("Maths + English", t.getName());
	}
}

class FakeTopicItem extends TopicItem {}
