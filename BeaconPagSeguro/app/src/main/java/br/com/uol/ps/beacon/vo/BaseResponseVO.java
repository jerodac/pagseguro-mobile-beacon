package br.com.uol.ps.beacon.vo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BaseResponseVO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * Mensagem relacionada ao retorno do serviço.
     */
    @SerializedName("message")
    private String mMessage;

    /**
     * Código de erro (não existe no caso de sucesso).
     */
    @SerializedName("errorCode")
    private String mErrorCode;

    public String getMessage() {
        return mMessage;
    }

    public String getErrorCode() {
        return mErrorCode;
    }
}
