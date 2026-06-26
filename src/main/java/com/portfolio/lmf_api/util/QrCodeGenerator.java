package com.portfolio.lmf_api.util;

import com.portfolio.lmf_api.exception.InvalidRequestFieldException;

public class QrCodeGenerator {
    private static final String QR_API_URL = "https://api.qrserver.com/v1/create-qr-code/";

    public static String generateQrCodeUrl(String data) throws InvalidRequestFieldException {
        if (data == null)
            throw new InvalidRequestFieldException("DATA CAN'T BE NULL");

        if (data.trim().isEmpty())
            throw new InvalidRequestFieldException("DATA CAN'T BE BLANK");

        return QR_API_URL + "?size=200x200&data=" + data;
    }
}
