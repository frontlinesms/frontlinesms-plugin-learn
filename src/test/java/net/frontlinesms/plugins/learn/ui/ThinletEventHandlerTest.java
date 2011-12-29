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
	/** root UI component that this handler is controlling */
	private Object rootComponent;
	
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
		h = initHandler();
		rootComponent = getRootComponent();
	}

	protected abstract E initHandler();
	protected abstract Object getRootComponent();
	
//> UI INTERACTION METHODS/CLASSES
	protected ThinletComponent $(String componentName) {
		return create(rootComponent, componentName);
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
		
		public String getText() {
			return ui.getText(component);
		}
		
		public void setText(String text) {
			ui.setText(component, text);
		}
		
		public void exists() {}
		
		public boolean isEnabled() {
			return ui.isEnabled(component);
		}
	}
	
	private ThinletComponent create(Object parent, String componentName) {
		Object component = Thinlet.find(parent, componentName);
		if(component == null) component = ui.find(componentName);
		if(component == null) return new MissingThinletComponent(componentName);
		else return new RealThinletComponent(component);
	}
}

class MissingThinletComponent implements ThinletComponent {
	private final String id;
	
	MissingThinletComponent(String id) {
		this.id = id;
	}
	
	public void exists() { fail(); }
	public void click() { fail(); }
	public String getText() { return fail(String.class); }
	public void setText(String v) { fail(); }
	public boolean isEnabled() { return fail(boolean.class); }

	private void fail() {
		BaseTestCase.fail("Component missing: " + id);
	}
	
	private <T extends Object> T fail(Class<T> c) {
		BaseTestCase.fail("Component missing: " + id);
		return null;
	}
}

interface ThinletComponent {
	public void exists();
	public void click();
	public String getText();
	public void setText(String text);
	public boolean isEnabled();
}