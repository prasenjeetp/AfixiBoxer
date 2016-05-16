package com.boxer.util;

public class UrlList {
	int _id;
	String url;
	String tittle;
	String status;
	String position;

	public UrlList(String url, String tittle, String status, String position) {
		this.url = url;
		this.tittle = tittle;
		this.status = status;
		this.position = position;
	}
	public UrlList(int id ,String url, String tittle, String status, String position) {
		_id=id;
		this.url = url;
		this.tittle = tittle;
		this.status = status;
		this.position = position;
	}

	public UrlList() {

	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTittle() {
		return tittle;
	}

	public void setTittle(String tittle) {
		this.tittle = tittle;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

}
