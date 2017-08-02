package com.prosper.want.common.exception;

public class ResourceExistException extends RuntimeException {

	public ResourceExistException() {
		super();
	}

	public ResourceExistException(String s) {
		super(s);
	}

	public ResourceExistException(Throwable t) {
		super(t);
	}

}
