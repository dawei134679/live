package com.mpig.api.modelcomet;

/**
 * Created by YX on 2016/4/17.
 */
public class UpdateAccountStatusCMod extends BaseCMod {


    private int status;

    public UpdateAccountStatusCMod() {
        this.setCometProtocol(CModProtocol.UPDATE_ACCOUNT_STATUS);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
