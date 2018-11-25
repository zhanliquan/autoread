package com.wayne.autoread.mina;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.wayne.autoread.MyApplication;
import com.wayne.autoread.pojo.ApkInfo;
import com.wayne.autoread.work.Worker;
import com.wayne.autoread.work.WorkerFactory;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.util.HashMap;

public class ConnectionManager {

    private ConnectionConfig mConfig;
    private WeakReference<Context> mContext;
    private NioSocketConnector mConnection;
    private IoSession mSession;
    private InetSocketAddress mAddress;

    public ConnectionManager(ConnectionConfig config) {
        this.mConfig = config;
        this.mContext = new WeakReference<Context>(config.getContext());
        init();
    }

    private void init() {
        mAddress = new InetSocketAddress(mConfig.getIp(), mConfig.getPort());
        mConnection = new NioSocketConnector();
        mConnection.getSessionConfig().setReceiveBufferSize(mConfig.getReadBufferSize());
        mConnection.getFilterChain().addLast("logging", new LoggingFilter());
        mConnection.getFilterChain().addLast("codec", new ProtocolCodecFilter(
                new ObjectSerializationCodecFactory()));
        mConnection.setHandler(new DefaultHandler(mConfig.getContext()));

    }

    public boolean connect() {
        try {
            ConnectFuture future = mConnection.connect(mAddress);
            future.awaitUninterruptibly();
            mSession = future.getSession();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("wayne", "连接失败", e);
            return false;
        }
        return mSession != null;
    }

    public void disConnection() {
        mConnection.dispose();
        mConnection = null;
        mSession = null;
        mAddress = null;
        mContext = null;
    }


    private static class DefaultHandler extends IoHandlerAdapter {
        private Context mContext;
        DefaultHandler(Context context) {
            this.mContext = context;

        }

        @Override
        public void sessionOpened(IoSession session) throws Exception {
            SessionManager.getInstance().setSession(session);
        }

        @Override
        public void messageReceived(IoSession session, Object message) throws Exception {
            if (mContext != null) {
                Log.d("wayne", message.toString());
                HashMap msg = JSON.parseObject(message.toString(), HashMap.class);
                Object msgType = msg.get("msgType");
                if ("task".equals(msgType)) {
                    Worker worker = WorkerFactory.createFromMsg(msg);
                    MyApplication.autoReadThread.addWorker(worker);
                }
            }
        }

        @Override
        public void sessionClosed(IoSession session) throws Exception {
            super.sessionClosed(session);
            Log.d("wayne", "sessionClosed");
        }
    }
}
