package com.wei.common.user.service.impl;

import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.*;

class BambooUserServiceImplTest {

    @Test
    public void testFileName() throws UnsupportedEncodingException {
        String fileName = "dev (1).csv";
        String encodeVal = URLEncoder.encode(fileName, "UTF-8");
        System.out.println(encodeVal);
        String decodeVal = URLDecoder.decode(encodeVal, "UTF-8");
        System.out.println(decodeVal);

    }

}