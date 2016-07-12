package server

import ThisCar
import com.google.protobuf.InvalidProtocolBufferException
import getLocationUrl
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.*
import proto.car.LocationP
import proto.car.RouteP
import setRouteUrl

/**
 * Created by user on 7/6/16.
 */
class Handler : SimpleChannelInboundHandler<Any>() {

    var url: String = ""
    var contentBytes: ByteArray = ByteArray(0);

    override fun channelReadComplete(ctx: ChannelHandlerContext) {

        val car = ThisCar.instance
        var success = true;

        var dataAnswer: ByteArray = ByteArray(0)
        when (url) {
            getLocationUrl -> {
                dataAnswer = LocationP.Location.newBuilder().setLocationResponse(LocationP.Location.LocationResponse.newBuilder().setAngle(car.angle).setX(car.x).setY(car.y)).build().toByteArray()
            }
            setRouteUrl -> {
                var data: RouteP.Route? = null;
                try {
                    data = RouteP.Route.parseFrom(contentBytes)
                } catch (e: InvalidProtocolBufferException) {
                    success = false;
                }
                if (data != null) {
                    val wayPoints = data.wayPointsList
                    println("try set path")
                    ThisCar.instance.setPath(wayPoints)
                }
            }
            else -> {
                success = false
            }
        }

        val response = DefaultFullHttpResponse(HttpVersion.HTTP_1_1, if (success) HttpResponseStatus.OK else HttpResponseStatus.BAD_REQUEST, Unpooled.copiedBuffer(dataAnswer))
        response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes())
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
        super.channelRead(ctx, msg)
    }

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Any) {
        if (msg is HttpRequest) {
            this.url = msg.uri()
        }

        if (msg is HttpContent) {
            val contentsBytes = msg.content();
            contentBytes = ByteArray(contentsBytes.capacity())
            contentsBytes.readBytes(contentBytes)
        }
    }


    override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
        println("exception")
        cause?.printStackTrace()
        if (ctx != null) {
            ctx.close()
        }
    }
}