package net.frontlinesms.plugins.learn.ui;

public class LearnPluginTabHandlerTests extends ThinletEventHandlerTest<LearnPluginTabHandler> {
//> SETUP METHODS
	@Override
	protected LearnPluginTabHandler createHandler() {
		return new LearnPluginTabHandler(ui);
	}
	
	@Override
	protected Object getRootComponent() {
		return h.getTab();
	}

//> TEST METHODS
	public void testManageTopicsLoaded() {
		$("tbManageTopics").exists();
	}
}
