package net.frontlinesms.plugins.learn.ui;

import net.frontlinesms.plugins.learn.data.domain.Reinforcement;
import net.frontlinesms.plugins.learn.data.repository.ReinforcementDao;
import net.frontlinesms.ui.FrontlineUI;

public class NewReinforcementDialogHandler extends EditReinforcementDialogHandler {
	public NewReinforcementDialogHandler(FrontlineUI ui, ReinforcementDao dao) {
		super(ui, dao, new Reinforcement());
		ui.setText(getDialog(), "i18n.plugins.learn.reinforcement.new");
	}
}
