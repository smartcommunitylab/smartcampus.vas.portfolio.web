package eu.trentorise.smartcampus.portfolio.data;

import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

public class PmBasicProfile extends BasicProfile {

	public PmBasicProfile() {
	}

	public PmBasicProfile(BasicProfile basicProfile) {
		super();
		fillBasicProfile(basicProfile);
	}

	private boolean student = false;

	public void setStudent(boolean student) {
		this.student = student;
	}

	public boolean isStudent() {
		return this.student;
	}

	public void fillBasicProfile(BasicProfile basicProfile) {
		setName(basicProfile.getName());
		setSocialId(basicProfile.getSocialId());
		setSurname(basicProfile.getSurname());
		setUserId(basicProfile.getUserId());
	}
}
