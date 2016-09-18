package com.xclenter.test.dao;

public class TestCasePassResult {
	int caseCount;
	int passedCaseCount;

	public TestCasePassResult(int caseCount, int passedCaseCount) {
		super();
		this.caseCount = caseCount;
		this.passedCaseCount = passedCaseCount;
	}

	public int getCaseCount() {
		return caseCount;
	}

	public int getPassedCaseCount() {
		return passedCaseCount;
	}

}
