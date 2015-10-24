package com.michir.projects.arxiving.api;

import java.util.Collection;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class Arxiv {

	private String id;
	
	private String path;
	
	private String name;
	
	private Collection<String> tags;
	
	private Date date;

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the tags
	 */
	public Collection<String> getTags() {
		return tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(Collection<String> tags) {
		this.tags = tags;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}
