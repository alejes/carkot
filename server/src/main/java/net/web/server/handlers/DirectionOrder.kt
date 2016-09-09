package net.web.server.handlers

import CodedInputStream
import CodedOutputStream
import DirectionRequest
import GenericResponse
import Result
import net.Handler
import net.web.server.Server

class DirectionOrder : Handler {

    override fun execute(bytesFromClient: ByteArray): ByteArray {
        if (Server.serverMode != Server.ServerMode.MANUAL_MODE) {
            println("Can't execute move order when not in manual mode")
            val protoResponse = GenericResponse.BuilderGenericResponse(Result.BuilderResult(1).build()).build()
            return protoBufToBytes(protoResponse)
        }

        val ins = CodedInputStream(bytesFromClient)
        val order = DirectionRequest.BuilderDirectionRequest(DirectionRequest.Command.FORWARD, 0, false).parseFrom(ins)
        if (order.stop) {
            net.web.server.Server.changeMode(Server.ServerMode.IDLE)
            val protoResponse = GenericResponse.BuilderGenericResponse(Result.BuilderResult(1).build()).build()
            return protoBufToBytes(protoResponse)
        }

        return ByteArray(0)
    }

    private fun protoBufToBytes(protoMessage: GenericResponse): ByteArray {
        val result = ByteArray(protoMessage.getSizeNoTag())
        protoMessage.writeTo(CodedOutputStream(result))
        return result
    }
}