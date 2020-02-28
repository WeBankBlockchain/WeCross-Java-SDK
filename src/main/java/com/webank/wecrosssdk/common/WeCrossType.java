package com.webank.wecrosssdk.common;

public class WeCrossType {

    // crypto suite of proposal bytes to sign
    public static final String CRYPTO_SUITE_BCOS_SHA3_256_SECP256K1 = "BCOS_SHA3_256_SECP256K1";
    public static final String CRYPTO_SUITE_BCOS_SM2_SM3 = "BCOS_SM2_SM3";
    public static final String CRYPTO_SUITE_FABRIC_BC_SECP256R1 = "FABRIC_BC_SECP256R1";

    // proposal type
    public static final String PROPOSAL_TYPE_PEER_PAYLODAD = "QUERY_PEER_PAYLODAD";
    public static final String PROPOSAL_TYPE_ENDORSER_PAYLODAD = "ENDORSER_PAYLODAD";
    public static final String PROPOSAL_TYPE_ORDERER_PAYLOAD = "ORDERER_PAYLOAD"; // for fabric
}
