package com.github.nk.klusterfuck.admin.model;

import javax.persistence.*;

/**
 * Created by nk on 11/6/17.
 */
@Entity
@Table(name = "kf_connectors")
public class Connector {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String displayName;
	private String image;
	private boolean exposed;
	private int port = 0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isExposed() {
		return exposed;
	}

	public void setExposed(boolean exposed) {
		this.exposed = exposed;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
