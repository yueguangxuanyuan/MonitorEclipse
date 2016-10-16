package com.xclenter.test.dao;

public class TestCasePassResult {
	int caseCount;
	int passedCaseCount;
	int timeoutCaseCount;

	public TestCasePassResult(int caseCount, int passedCaseCount,int timeoutCaseCount) {
		super();
		this.caseCount = caseCount;
		this.passedCaseCount = passedCaseCount;
		this.timeoutCaseCount = timeoutCaseCount;
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
	
}
