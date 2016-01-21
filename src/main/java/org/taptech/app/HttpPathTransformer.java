package org.taptech.app;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;

/**
 * Created by tap on 10/3/15.
 */
//@Component("httpPathTransformer")
public class HttpPathTransformer extends AbstractMessageTransformer {

    private final static  Logger logger = LoggerFactory.getLogger(HttpPathTransformer.class);


    @Override
    public Object transformMessage(MuleMessage muleMessage, String s) throws TransformerException {
        //String temp = muleMessage.getInboundProperty("http.request.path");
        String temp = muleMessage.getInboundProperty("http.request.uri");
        String rPath = null;
        try {
            rPath = URLDecoder.decode(temp.substring(1, temp.length()), "UTF-8");
            logger.info("RPath {}",rPath);
            muleMessage.setInvocationProperty("rPath",rPath);
        } catch (UnsupportedEncodingException e) {
            logger.error("Error decoding URL {}",rPath,e);
            throw new RuntimeException(e);
        }
        return muleMessage;
    }

    static {
        try {
            String filePath = HttpPathTransformer.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            System.setProperty("classes.dir",filePath);
            System.out.println("-------------- Classes dir "+filePath);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
