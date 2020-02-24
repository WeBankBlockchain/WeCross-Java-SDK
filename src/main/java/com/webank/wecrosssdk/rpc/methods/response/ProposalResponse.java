package com.webank.wecrosssdk.rpc.methods.response;

import com.webank.wecrosssdk.rpc.methods.Response;
import java.util.Arrays;
import java.util.Objects;

public class ProposalResponse extends Response<ProposalResponse.Proposal> {

    public static class Proposal {
        private int errorCode;
        private String errorMessage;
        private int seq;
        private byte[] proposalToSign;
        private String cryptoSuite;
        private String type;

        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public int getSeq() {
            return seq;
        }

        public void setSeq(int seq) {
            this.seq = seq;
        }

        public byte[] getProposalToSign() {
            return proposalToSign;
        }

        public void setProposalToSign(byte[] proposalToSign) {
            this.proposalToSign = proposalToSign;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Proposal)) return false;
            Proposal that = (Proposal) o;
            return getErrorCode() == that.getErrorCode()
                    && getSeq() == that.getSeq()
                    && Objects.equals(getErrorMessage(), that.getErrorMessage())
                    && Arrays.equals(getProposalToSign(), that.getProposalToSign());
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(getErrorCode(), getErrorMessage(), getSeq());
            result = 31 * result + Arrays.hashCode(getProposalToSign());
            return result;
        }

        @Override
        public String toString() {
            return "Proposal{"
                    + "errorCode="
                    + errorCode
                    + ", errorMessage='"
                    + errorMessage
                    + '\''
                    + ", seq="
                    + seq
                    + ", proposalToSign="
                    + Arrays.toString(proposalToSign)
                    + '}';
        }

        public String getCryptoSuite() {
            return cryptoSuite;
        }

        public void setCryptoSuite(String cryptoSuite) {
            this.cryptoSuite = cryptoSuite;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
