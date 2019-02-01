package com.mpig.api.modelcomet;

/**
 * Created by YX on 2016/4/17.
 */
public class UpdateIdentityCMod extends BaseCMod {


    private int identity;

    public UpdateIdentityCMod() {
        this.setCometProtocol(CModProtocol.UPDATE_IDENTITY);
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }
}
