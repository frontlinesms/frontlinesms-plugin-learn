package net.frontlinesms.plugins.learn.ui;

import java.util.Collections;

import thinlet.Thinlet;
import net.frontlinesms.junit.BaseTestCase;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.ThinletUiEventHandler;

public abstract class ThinletEventHandlerTest<E extends ThinletUiEventHandler> extends BaseTestCase  {
	protected FrontlineUI ui;
	/** event handler instance under test */
	protected E h;
	
	@SuppressWarnings("serial")
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Thinlet.DEFAULT_ENGLISH_BUNDLE = Collections.emptyMap();
		ui = new FrontlineUI() {
			@Override
			protected void handleException(Throwable t) {
				throw new RuntimeException("Unhandled exception/throwable in UI.", t);
			}
		};
		h = createHandler();
	}

	protected abstract E createHandler();
	protected abstract Object getRootComponent();
	
//> UI INTERACTION METHODS/CLASSES
	protected ThinletComponent $(String componentName) {
		return create(getRootComponent(), componentName);
	}

	protected class RealThinletComponent implements ThinletComponent {
		private final Object component;
		
		private RealThinletComponent(Object c) {
			if(c instanceof ThinletComponent) throw new IllegalArgumentException("Don't try to wrap multiple " + getClass().getName());
			this.component = c;
		}
		
		public void click() {
			ui.invokeAction(component);
		}
		
		public void exists() {}
	}
	
	private ThinletComponent create(Object parent, String componentName) {
		Object component = Thinlet.find(parent, componentName);
		if(component == null) return new MissingThinletComponent(componentName);
		else return new RealThinletComponent(component);
	}
}

class MissingThinletComponent implements ThinletComponent {
	private final String id;
	
	MissingThinletComponent(String id) {
		this.id = id;
	}
	
	public void exists() {
		fail();
	}
	
	public void click() {
		fail();
	}
	
	private void fail() {
		BaseTestCase.fail("Component missing: " + id);
	}
}

interface ThinletComponent {
	public void exists();
	public void click();
}