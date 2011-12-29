package net.frontlinesms.plugins.learn.ui;

public class TopicTabHandlerTests extends ThinletEventHandlerTest<TopicTabHandler> {
//> SETUP METHODS
	@Override
	protected TopicTabHandler createHandler() {
		return new TopicTabHandler(ui);
	}
	
	@Override
	protected Object getRootComponent() {
		return h.getTab();
	}
	
//> TEST METHODS
	public void testTopicsListVisisble() {
		$("trTopics").exists();
	}
	
	public void testNewTopicButton() {
		// given
		ThinletComponent button = $("btNewTopic");
		
		// when
		button.click();
		
		// then
		$("dgNewTopic").exists();
	}
}