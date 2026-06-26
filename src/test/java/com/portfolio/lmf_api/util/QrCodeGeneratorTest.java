package com.portfolio.lmf_api.util;


import com.portfolio.lmf_api.exception.InvalidRequestFieldException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QrCodeGeneratorTest {

    @Test
    void whenGeneratingAQRCodeURL_thenReturnAStringRepresentationOfURL() {
        String result = QrCodeGenerator.generateQrCodeUrl("https://portfolio-lopezenzoa.vercel.app/");
        String expected = "https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=https://portfolio-lopezenzoa.vercel.app/";

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(expected, result);
    }

    @Test
    void whenGeneratingAnEmptyQRCodeURL_thenThrowAnInvalidRequestFieldException() {
        assertThrows(
                InvalidRequestFieldException.class,
                () -> QrCodeGenerator.generateQrCodeUrl("")
        );
    }

    @Test
    void whenGeneratingANullQRCodeURL_thenThrowAnInvalidRequestFieldException() {
        assertThrows(
                InvalidRequestFieldException.class,
                () -> QrCodeGenerator.generateQrCodeUrl(null)
        );
    }
}