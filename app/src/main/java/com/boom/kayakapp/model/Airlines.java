package com.boom.kayakapp.model;

public class Airlines {
	private String name, logoURL, phone, site;
	private int code;

	public Airlines() {
	}

	public Airlines
			(String name, String logoURL, int code, String phone, String site) {
		this.name = name;
		this.logoURL = logoURL;
		this.code = code;
		this.phone = phone;
		this.site = site;

	}

	public String getName() {return name;}
	public void setName(String name) {this.name = name;}

	public String getLogoURL() {return logoURL;}
	public void setLogoURL(String logoURL) {this.logoURL = logoURL;}

	public int getCode() {return code;}
	public void setCode(Integer code) {this.code = code;}

	public String getPhone() {return phone;}
	public void setPhone(String phone) {this.phone = phone;}

	public String getSite() {return site;}
	public void setSite(String site) {this.site = site;}

}
