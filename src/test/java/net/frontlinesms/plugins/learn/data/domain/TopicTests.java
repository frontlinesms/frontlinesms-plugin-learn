package net.frontlinesms.plugins.learn.data.domain;

import java.util.List;

public class TopicTests extends net.frontlinesms.junit.BaseTestCase {
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

	public void testItemsInitiallyEmpty() {
		// when
		List<TopicItem> items = t.getItems();

		// then
		assertEquals(0, items.size());
	}

	public void testAddItemAppends() {
		// given
		TopicItem item1 = new FakeTopicItem();
		TopicItem item2 = new FakeTopicItem();

		// when
		t.addItem(item1);

		// then
		assertEquals(1, t.getItems().size());
		assertEquals(item1, t.getItems().get(0));

		// when
		t.addItem(item2);

		// then
		assertEquals(2, t.getItems().size());
		assertEquals(item1, t.getItems().get(0));
		assertEquals(item2, t.getItems().get(1));
	}

	public void testGetItemsIsUnmodifiable() {
		// TODO find out what kind of exception this should throw and make this more specific.
		try {
			t.getItems().add(new FakeTopicItem());
			fail("topic.items should be unmodifiable outside of Topic class");
		} catch(UnsupportedOperationException ex) {
			// expected
		}
	}
}

class FakeTopicItem implements TopicItem {
}
