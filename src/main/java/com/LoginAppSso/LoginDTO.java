package com.LoginAppSso;

import java.io.Serializable;

public class LoginDTO implements Serializable {

private Long id;
private String username;
private String password;
private String nonce;
private String state;


public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public void setUsername(String username) {
	this.username = username;
}

public String getNonce() {
	return nonce;
}

public void setNonce(String nonce) {
	this.nonce = nonce;
}

public String getState() {
	return state;
}

public void setState(String state) {
	this.state = state;
}

public String getUsername() {
	return username;
}

public void setPassword(String password) {
	this.password = password;
}

public String getPassword() {
	return password;
}

}
