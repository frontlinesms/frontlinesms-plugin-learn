package net.frontlinesms.plugins.learn.ui;

import net.frontlinesms.plugins.learn.data.domain.Reinforcement;
import net.frontlinesms.plugins.learn.data.domain.Topic;
import net.frontlinesms.plugins.learn.data.repository.ReinforcementDao;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.ThinletUiEventHandler;

public class EditReinforcementDialogHandler implements ThinletUiEventHandler {
	private static final String LAYOUT_FILE = "/ui/plugins/learn/reinforcement/edit.xml";
	
	private FrontlineUI ui;
	private ReinforcementDao dao;
	private Object dialog;
	
//> INIT METHODS
	public EditReinforcementDialogHandler(FrontlineUI ui, ReinforcementDao dao, Reinforcement r) {
		this.dao = dao;
		this.ui = ui;
		dialog = ui.loadComponentFromFile(LAYOUT_FILE, this);
		
		validate();
	}
	
//> ACCESSORS
	/** Set the topic that the reinforcement refers to. */
	public void setTopic(Topic topic) {
		// TODO
	}

	public Object getDialog() {
		return dialog;
	}
	
//> UI EVENT METHODS
	public void save() {
		
	}
	
	public void close() {
		ui.remove(dialog);
	}
	
	public void validate() {}
}
