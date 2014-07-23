package morel.e04crawler;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Resume {
	/**
	 * 姓名
	 */
	private String name;
	private String gender;
	private Date birthday;
	private String email;
	private String tel;
	private String cellphone;
	private String contactAddress;
	private List<CodeValue> expectJobCategory;
	private List<CodeValue> expectJobArea;
	private List<CodeValue> expectIndustrySectors;
	private Set<RoleType> expectRoleTypes = new HashSet<RoleType>();
	private int previousYearSalary;

	/**
	 * 取得 gender
	 * 
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * 設定 gender
	 * 
	 * @param gender
	 *            the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * 取得 birthday
	 * 
	 * @return the birthday
	 */
	public Date getBirthday() {
		return birthday;
	}

	/**
	 * 設定 birthday
	 * 
	 * @param birthday
	 *            the birthday to set
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	/**
	 * 取得 email
	 * 
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 設定 email
	 * 
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 取得 tel
	 * 
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}

	/**
	 * 設定 tel
	 * 
	 * @param tel
	 *            the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}

	/**
	 * 取得 cellphone
	 * 
	 * @return the cellphone
	 */
	public String getCellphone() {
		return cellphone;
	}

	/**
	 * 設定 cellphone
	 * 
	 * @param cellphone
	 *            the cellphone to set
	 */
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	/**
	 * 取得 contactAddress
	 * 
	 * @return the contactAddress
	 */
	public String getContactAddress() {
		return contactAddress;
	}

	/**
	 * 設定 contactAddress
	 * 
	 * @param contactAddress
	 *            the contactAddress to set
	 */
	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}

	/**
	 * 取得 希望職務類別
	 * 
	 * @return the expectJobCategory
	 */
	public List<CodeValue> getExpectJobCategory() {
		return expectJobCategory;
	}

	/**
	 * 設定 希望職務類別
	 * 
	 * @param expectJobCategory
	 *            the expectJobCategory to set
	 */
	public void setExpectJobCategory(List<CodeValue> expectJobCategory) {
		this.expectJobCategory = expectJobCategory;
	}

	/**
	 * 取得 希望工作地點
	 * 
	 * @return the expectJobArea
	 */
	public List<CodeValue> getExpectJobArea() {
		return expectJobArea;
	}

	/**
	 * 設定 希望工作地點
	 * 
	 * @param expectJobArea
	 *            the expectJobArea to set
	 */
	public void setExpectJobArea(List<CodeValue> expectJobArea) {
		this.expectJobArea = expectJobArea;
	}

	/**
	 * 取得 希望產業別
	 * 
	 * @return the expectIndustrySectors
	 */
	public List<CodeValue> getExpectIndustrySectors() {
		return expectIndustrySectors;
	}

	/**
	 * 設定 希望產業別
	 * 
	 * @param expectIndustrySectors
	 *            the expectIndustrySectors to set
	 */
	public void setExpectIndustrySectors(List<CodeValue> expectIndustrySectors) {
		this.expectIndustrySectors = expectIndustrySectors;
	}

	/**
	 * 取得 name
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 設定 name
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 取得前份工作年薪
	 * 
	 * @return
	 */
	public int getPreviousYearSalary() {
		return previousYearSalary;
	}

	/**
	 * 設定前份工作年薪
	 * 
	 * @param previousYearSalary
	 */
	public void setPreviousYearSalary(int previousYearSalary) {
		this.previousYearSalary = previousYearSalary;
	}

	/**
	 * 取得期望工作性質
	 * 
	 * @return
	 */
	public Set<RoleType> getExpectRoleTypes() {
		return expectRoleTypes;
	}

	/**
	 * 設定期望工作性質
	 * 
	 * @param expectRoleTypes
	 */
	public void setExpectRoleTypes(Set<RoleType> expectRoleTypes) {
		this.expectRoleTypes = expectRoleTypes;
	}

}
