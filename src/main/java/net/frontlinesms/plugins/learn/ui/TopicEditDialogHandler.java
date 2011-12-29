package net.frontlinesms.plugins.learn.ui;

import net.frontlinesms.plugins.learn.data.domain.Topic;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.ThinletUiEventHandler;

public class TopicEditDialogHandler implements ThinletUiEventHandler {
	private static final String LAYOUT_FILE = "/ui/plugins/learn/topic/edit.xml";
	
	private final Topic t;
	private final FrontlineUI ui;
	private final Object panel;
	
	public TopicEditDialogHandler(FrontlineUI ui) {
		this(ui, new Topic());
	}

	public TopicEditDialogHandler(FrontlineUI ui, Topic t) {
		assert(t != null);
		this.t = t;
		this.ui = ui;
		panel = ui.loadComponentFromFile(LAYOUT_FILE, this);
	}

	public Object getNewDialog() {
		Object dialog = ui.createDialog("i18n.plugins.learn.topic.edit");
		ui.setName(dialog, "dgNewTopic");
		ui.add(dialog, panel);
		return dialog;
	}

	public Object getEditDialog() {
		Object dialog = ui.createDialog("i18n.plugins.learn.topic.edit");
		ui.setName(dialog, "dgEditTopic");
		ui.add(dialog, panel);
		return dialog;
	}

}
