package org.tinygame.herostory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.LoggerFactory;



/**
 * 主方法入口
 * @author zqs
 * @date 2021-11-17 20:18
 */
public class ServerMain {
    static private final Logger log= LoggerFactory.getLogger(ServerMain.class);

    static private final int SERVER_PORT=12345;
    static public void main(String[] args){
        PropertyConfigurator.configure(ServerMain.class.getClassLoader().getResourceAsStream("log4j.properties"));
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workerGroup=new NioEventLoopGroup();
        try{
            ServerBootstrap b=new ServerBootstrap();
            b.group(bossGroup,workerGroup);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(
                            new HttpServerCodec(),
                            new HttpObjectAggregator(65535),
                            new WebSocketServerProtocolHandler("/websocket"),
                            new GameMsgDecoder(),
                            new GameMsgEncoder(),
                            new GameMsgHandler()
                    );
                }
            });
            //b.option(ChannelOption.SO_BACKLOG,128);
            //b.childOption(ChannelOption.SO_KEEPALIVE,true);
           ChannelFuture f= b.bind(SERVER_PORT).sync();
           if (f.isSuccess()){
               log.info("游戏服务器启动成功！");
           }
           f.channel().closeFuture().sync();
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }




























}
