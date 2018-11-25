package com.wayne.autoread;

import java.util.HashMap;
import java.util.Map;

public class ReceiveMsg {
    public static final String MSG_TYPE_CMD = "cmd";
    private String msgType;
    private Map<String, Object> msg = new HashMap<String, Object>();
}
