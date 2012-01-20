package net.frontlinesms.plugins.learn.ui;

import thinlet.Thinlet;
import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.plugins.learn.data.domain.Topic;
import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

public class TopicEditDialogHandler implements ThinletUiEventHandler {
	private static final String TF_NAME = "tfName";
	private static final String BT_SAVE = "btSave";

	private static final String LAYOUT_FILE = "/ui/plugins/learn/topic/edit.xml";

	private final TopicDao dao;
	private final Topic t;
	private final FrontlineUI ui;
	private final Object dialog;
	
	public TopicEditDialogHandler(FrontlineUI ui, TopicDao dao) {
		this(ui, dao, null);
	}

	public TopicEditDialogHandler(FrontlineUI ui, TopicDao dao, Topic t) {
		this.dao = dao;
		
		String dialogTitle;
		if(t == null) {
			this.t = new Topic();
			dialogTitle = "plugins.learn.topic.new";
		} else {
			this.t = t;
			dialogTitle = "plugins.learn.topic.edit";
		}
		
		this.ui = ui;
		dialog = ui.loadComponentFromFile(LAYOUT_FILE, this);
		ui.setText(dialog, InternationalisationUtils.getI18nString(dialogTitle));
		
		validate();
	}
	
	public Object getDialog() {
		return dialog;
	}
	
//> UI EVENT METHODS
	public void close() {
		ui.remove(dialog);
	}
	
	public void save() throws DuplicateKeyException {
		t.setName(ui.getText(find(TF_NAME)));
		dao.save(t);
		close();
	}
	
	public void validate() {
		boolean valid = !ui.getText(find(TF_NAME)).isEmpty();
		ui.setEnabled(find(BT_SAVE), valid);
	}
	
//> UI HELPER METHODS
	private Object find(String componentName) {
		return Thinlet.find(dialog, componentName);
	}
}
