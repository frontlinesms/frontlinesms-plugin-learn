package net.frontlinesms.plugins.learn.ui.assessment;

import net.frontlinesms.plugins.learn.data.domain.Assessment;
import net.frontlinesms.plugins.learn.data.repository.AssessmentDao;
import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.plugins.learn.ui.topic.TopicChoosingDialogHandler;
import net.frontlinesms.ui.FrontlineUI;

public class EditAssessmentDialogHandler extends TopicChoosingDialogHandler<Assessment> {
	private final AssessmentDao assessmentDao;
	
	public EditAssessmentDialogHandler(FrontlineUI ui, AssessmentDao assementDao, TopicDao topicDao, Assessment a) {
		super(ui, topicDao, a);
		this.assessmentDao = assementDao;
	}

	@Override
	public String getLayoutFile() { return "/ui/plugins/learn/assessment/edit.xml"; }

	@Override
	public void save() {
		editItem.setTopic(getSelectedTopic());
		
		assessmentDao.save(editItem);
	}

	@Override
	public boolean doValidate() { return false; }
}
