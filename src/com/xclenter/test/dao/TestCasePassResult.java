package com.xclenter.test.dao;

public class TestCasePassResult {
	int caseCount;
	int passedCaseCount;
	int timeoutCaseCount;
	String message;

	public TestCasePassResult(int caseCount, int passedCaseCount,int timeoutCaseCount,String message) {
		super();
		this.caseCount = caseCount;
		this.passedCaseCount = passedCaseCount;
		this.timeoutCaseCount = timeoutCaseCount;
		this.message = message;
	}

	public int getCaseCount() {
		return caseCount;
	}

	public int getPassedCaseCount() {
		return passedCaseCount;
	}

	public int getTimeoutCaseCount() {
		return timeoutCaseCount;
	}

	public String getMessage() {
		return message;
	}
	
}
