package morel.e04crawler;

public class CodeValue {

	private String code;
	private String value;

	public CodeValue(String cateCode, String cateName) {
		code = cateCode;
		value = cateName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "CodeValue [code=" + code + ", value=" + value + "]";
	}
	
}
