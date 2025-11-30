package com.infoworks.domain.jwt;

import com.infoworks.entity.Entity;

public class JWTHeader extends Entity {

    private String typ;
    private String alg;
    private String kid;

    public JWTHeader() {}

    public String getTyp() {
        return typ;
    }

    public JWTHeader setTyp(String typ) {
        this.typ = typ;
        return this;
    }

    public String getAlg() {
        return alg;
    }

    public JWTHeader setAlg(String alg) {
        this.alg = alg;
        return this;
    }

    public String getKid() {
        return kid;
    }

    public JWTHeader setKid(String kid) {
        this.kid = kid;
        return this;
    }
}
