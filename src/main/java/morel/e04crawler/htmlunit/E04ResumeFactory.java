package morel.e04crawler.htmlunit;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import morel.e04crawler.Resume;

import org.apache.commons.lang3.StringUtils;

public class E04ResumeFactory {

	public Resume create(String name, String gender, String birthday,
			String email, String homeNumber, String contactNumber,
			String address, String salaryYear, String desiredPos,
			String desiredArea, String desiredFields) {
		Resume resume = new Resume();
		resume.setName(name);
		resume.setGender(gender);
		resume.setBirthday(formatBirthday(birthday));
		resume.setEmail(email);
		resume.setTel(homeNumber);
		resume.setCellphone(contactNumber);
		resume.setContactAddress(address);
		resume.setPreviousYearSalary(formatInt(salaryYear));
		resume.setExpectJobCategory(formatList(desiredPos));
		resume.setExpectJobArea(formatList(desiredArea));
		resume.setExpectIndustrySectors(formatList(desiredFields));
		return resume;
	}

	private int formatInt(String salaryYear) {
		if (StringUtils.isNotBlank(salaryYear)) {
			return Integer.parseInt(salaryYear);
		}
		return 0;
	}

	private List<String> formatList(String listValue) {
		listValue = listValue.replaceAll("\t", "").trim();
		String[] split = listValue.split("、");
		for (int i = 0; i < split.length; i++) {
			split[i] = StringUtils.trim(split[i]);
		}
		return Arrays.asList(split);
	}
	
	private static Date formatBirthday(String birthday) {
		Calendar c = Calendar.getInstance();
		String[] split = birthday.split("[年|月|日]");
		c.set(Integer.parseInt(split[0]), Integer.parseInt(split[1]) - 1, Integer.parseInt(split[2]), 0, 0, 0);
		return c.getTime();
	}

}
