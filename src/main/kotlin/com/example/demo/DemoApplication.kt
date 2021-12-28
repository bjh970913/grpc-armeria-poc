package com.example.demo

import bjh970913.service.v1.EchoServiceGrpc
import bjh970913.service.v1.EchoServiceOuterClass
import com.linecorp.armeria.common.grpc.GrpcSerializationFormats
import com.linecorp.armeria.server.Server
import com.linecorp.armeria.server.grpc.GrpcService
import io.grpc.BindableService
import io.grpc.stub.StreamObserver
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import io.grpc.protobuf.services.ProtoReflectionService;


@SpringBootApplication
class DemoApplication

@Component
@kotlin.annotation.Target(AnnotationTarget.CLASS)
annotation class RGrpcService

@RGrpcService
class EchoService : EchoServiceGrpc.EchoServiceImplBase() {
    override fun echo(
        request: EchoServiceOuterClass.StringMessage,
        responseObserver: StreamObserver<EchoServiceOuterClass.StringMessage>
    ) {
        responseObserver.onNext(EchoServiceOuterClass.StringMessage.newBuilder().setValue(request.value).build())
        responseObserver.onCompleted()
    }
}

fun main(args: Array<String>) {
    val ctx = runApplication<DemoApplication>(*args)


    val grpcService = GrpcService.builder()
        .let { builder ->
            val svcs = ctx.getBeansWithAnnotation(RGrpcService::class.java)
                .values.filterIsInstance(BindableService::class.java)
            svcs.fold(builder) { acc, svc -> acc.addService(svc) }
        }
        .addService(ProtoReflectionService.newInstance())
        .supportedSerializationFormats(GrpcSerializationFormats.values())
        .enableHttpJsonTranscoding(true)
        .enableUnframedRequests(true)
        .build()

    val server = Server.builder()
        .http(8087)
        .service(grpcService)
        .build()

    Runtime.getRuntime().addShutdownHook(object : Thread() {
        override fun run() {
            server.stop()
        }
    })
    server.start().get()
}
