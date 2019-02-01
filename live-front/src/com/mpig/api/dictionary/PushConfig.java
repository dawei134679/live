package com.mpig.api.dictionary;

public class PushConfig {
	private String certificateFile;
	private String certificatePassword;
	private String production;
	private int threadCount;
	private String iosTokenReids;
	private int batchCount;


    public PushConfig initWith(String certificateFile, String certificatePassword,
                              String production, int threadCount,
                              String iosTokenReids, int batchCount) {

        this.setCertificateFile(certificateFile);
        this.setCertificatePassword(certificatePassword);
        this.setProduction(production);
        this.setThreadCount(threadCount);
        this.setIosTokenReids(iosTokenReids);
        this.setBatchCount(batchCount);
        return this;
    }


	public String getCertificateFile() {
		return certificateFile;
	}


	public void setCertificateFile(String certificateFile) {
		this.certificateFile = certificateFile;
	}


	public String getCertificatePassword() {
		return certificatePassword;
	}


	public void setCertificatePassword(String certificatePassword) {
		this.certificatePassword = certificatePassword;
	}


	public String getProduction() {
		return production;
	}


	public void setProduction(String production) {
		this.production = production;
	}


	public int getThreadCount() {
		return threadCount;
	}


	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}


	public String getIosTokenReids() {
		return iosTokenReids;
	}


	public void setIosTokenReids(String iosTokenReids) {
		this.iosTokenReids = iosTokenReids;
	}


	public int getBatchCount() {
		return batchCount;
	}


	public void setBatchCount(int batchCount) {
		this.batchCount = batchCount;
	}
}
