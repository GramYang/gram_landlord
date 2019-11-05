package com.gram.gram_landlord.sdk.assistant;

import com.google.protobuf.ByteString;
import com.gram.gram_landlord.sdk.Action;
import com.gram.gram_landlord.sdk.assistant.entity.Request;
import com.gram.gram_landlord.sdk.assistant.entity.Response;
import com.gram.gram_landlord.sdk.assistant.proto.AssistantProto;
import com.orhanobut.logger.Logger;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

@ChannelHandler.Sharable
public class AssistClientHandler extends ChannelInboundHandlerAdapter {
    private Request request;

    public void setRequest(Request request) {
        this.request = request;
    }

    private void doSend(ChannelHandlerContext ctx, Request request) {
        if(ctx != null && request != null) {
            AssistantProto.Request.Builder builder = AssistantProto.Request.newBuilder();
            builder.setKey(request.getKey());
            builder.putAllData(request.getAll());
            if(request.getPicture() != null) builder.setPicture(ByteString.copyFrom(request.getPicture()));
            AssistantProto.Request sendRequest = builder.build();
            ctx.writeAndFlush(sendRequest).addListener(future -> {
                if(future.isSuccess()) {
                    Action action = new Action(Action.ACTION_SEND_SUCCESSFUL);
                    action.putData("request", request);
                    AssistClient.getClient().sendAction(action);
                } else {
                    Action action = new Action(Action.ACTION_SEND_FAILED);
                    action.putData("request", request);
                    AssistClient.getClient().sendAction(action);
                }
            });
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Logger.i("连接服务器成功：" + ctx.channel().localAddress() + " NID:"
                + ctx.channel().id().asShortText());
        Action action = new Action(Action.ACTION_CONNECTION_SUCCESSFUL);
        action.putData("ctx", ctx);
        AssistClient.getClient().sendAction(action);
        doSend(ctx, request);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Logger.i("与服务器断开连接:" + ctx.channel().localAddress() + " NID:"
                + ctx.channel().id().asShortText());
        Action action = new Action(Action.ACTION_CONNECTION_CLOSED);
        AssistClient.getClient().sendAction(action);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object o) throws Exception {
        if(o instanceof AssistantProto.Response) {
            Response response = new Response();
            AssistantProto.Response builder = (AssistantProto.Response) o;
            response.setKey(builder.getKey());
            response.setCode((int)builder.getCode());
            response.setMessage(builder.getMessage());
            response.putAll(builder.getDataMap());
            if(builder.getPicture() != null) response.setPiture(builder.getPicture().toByteArray());
            Action action = new Action(Action.ACTION_RESPONSE_RECEIVED);
            action.putData(Response.class.getName(), response);
            AssistClient.getClient().sendAction(action);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent && ((IdleStateEvent) evt).state().equals(IdleState.READER_IDLE)) {
            Logger.d("读超时" + IdleState.READER_IDLE + ":" + ctx.channel().localAddress() + " NID:"
                    + ctx.channel().id().asShortText());
            AssistClient.getClient().destroy();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Logger.e("客户端信息接收线程出现异常：" + ctx.channel().localAddress() +
                "NID:" + ctx.channel().id().asShortText());
        Action action = new Action(Action.ACTION_CONNECTION_EXCEPTION);
        action.putData("throwable",cause);
        AssistClient.getClient().sendAction(action);
    }
}
