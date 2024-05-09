package servicesf;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.61.0)",
    comments = "Source: sf/ServiceContractSF.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ServiceSFGrpc {

  private ServiceSFGrpc() {}

  public static final java.lang.String SERVICE_NAME = "sfservice.ServiceSF";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<servicesf.ImageSubmissionRequest,
      servicesf.ImageSubmissionResponse> getSubmitImageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "submitImage",
      requestType = servicesf.ImageSubmissionRequest.class,
      responseType = servicesf.ImageSubmissionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<servicesf.ImageSubmissionRequest,
      servicesf.ImageSubmissionResponse> getSubmitImageMethod() {
    io.grpc.MethodDescriptor<servicesf.ImageSubmissionRequest, servicesf.ImageSubmissionResponse> getSubmitImageMethod;
    if ((getSubmitImageMethod = ServiceSFGrpc.getSubmitImageMethod) == null) {
      synchronized (ServiceSFGrpc.class) {
        if ((getSubmitImageMethod = ServiceSFGrpc.getSubmitImageMethod) == null) {
          ServiceSFGrpc.getSubmitImageMethod = getSubmitImageMethod =
              io.grpc.MethodDescriptor.<servicesf.ImageSubmissionRequest, servicesf.ImageSubmissionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "submitImage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  servicesf.ImageSubmissionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  servicesf.ImageSubmissionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceSFMethodDescriptorSupplier("submitImage"))
              .build();
        }
      }
    }
    return getSubmitImageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<servicesf.ImageDetailsRequest,
      servicesf.ImageDetailsResponse> getGetImageDetailsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getImageDetails",
      requestType = servicesf.ImageDetailsRequest.class,
      responseType = servicesf.ImageDetailsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<servicesf.ImageDetailsRequest,
      servicesf.ImageDetailsResponse> getGetImageDetailsMethod() {
    io.grpc.MethodDescriptor<servicesf.ImageDetailsRequest, servicesf.ImageDetailsResponse> getGetImageDetailsMethod;
    if ((getGetImageDetailsMethod = ServiceSFGrpc.getGetImageDetailsMethod) == null) {
      synchronized (ServiceSFGrpc.class) {
        if ((getGetImageDetailsMethod = ServiceSFGrpc.getGetImageDetailsMethod) == null) {
          ServiceSFGrpc.getGetImageDetailsMethod = getGetImageDetailsMethod =
              io.grpc.MethodDescriptor.<servicesf.ImageDetailsRequest, servicesf.ImageDetailsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getImageDetails"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  servicesf.ImageDetailsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  servicesf.ImageDetailsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceSFMethodDescriptorSupplier("getImageDetails"))
              .build();
        }
      }
    }
    return getGetImageDetailsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<servicesf.AllFilesWithRequest,
      servicesf.AllFilesWithResponse> getGetAllFilesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getAllFiles",
      requestType = servicesf.AllFilesWithRequest.class,
      responseType = servicesf.AllFilesWithResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<servicesf.AllFilesWithRequest,
      servicesf.AllFilesWithResponse> getGetAllFilesMethod() {
    io.grpc.MethodDescriptor<servicesf.AllFilesWithRequest, servicesf.AllFilesWithResponse> getGetAllFilesMethod;
    if ((getGetAllFilesMethod = ServiceSFGrpc.getGetAllFilesMethod) == null) {
      synchronized (ServiceSFGrpc.class) {
        if ((getGetAllFilesMethod = ServiceSFGrpc.getGetAllFilesMethod) == null) {
          ServiceSFGrpc.getGetAllFilesMethod = getGetAllFilesMethod =
              io.grpc.MethodDescriptor.<servicesf.AllFilesWithRequest, servicesf.AllFilesWithResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getAllFiles"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  servicesf.AllFilesWithRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  servicesf.AllFilesWithResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ServiceSFMethodDescriptorSupplier("getAllFiles"))
              .build();
        }
      }
    }
    return getGetAllFilesMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ServiceSFStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ServiceSFStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ServiceSFStub>() {
        @java.lang.Override
        public ServiceSFStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ServiceSFStub(channel, callOptions);
        }
      };
    return ServiceSFStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ServiceSFBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ServiceSFBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ServiceSFBlockingStub>() {
        @java.lang.Override
        public ServiceSFBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ServiceSFBlockingStub(channel, callOptions);
        }
      };
    return ServiceSFBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ServiceSFFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ServiceSFFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ServiceSFFutureStub>() {
        @java.lang.Override
        public ServiceSFFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ServiceSFFutureStub(channel, callOptions);
        }
      };
    return ServiceSFFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     * <pre>
     * Submit an image
     * </pre>
     */
    default void submitImage(servicesf.ImageSubmissionRequest request,
        io.grpc.stub.StreamObserver<servicesf.ImageSubmissionResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSubmitImageMethod(), responseObserver);
    }

    /**
     * <pre>
     * Get details and translations of image
     * </pre>
     */
    default void getImageDetails(servicesf.ImageDetailsRequest request,
        io.grpc.stub.StreamObserver<servicesf.ImageDetailsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetImageDetailsMethod(), responseObserver);
    }

    /**
     * <pre>
     * Get all files with certain dates and characteristic
     * </pre>
     */
    default void getAllFiles(servicesf.AllFilesWithRequest request,
        io.grpc.stub.StreamObserver<servicesf.AllFilesWithResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetAllFilesMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service ServiceSF.
   */
  public static abstract class ServiceSFImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ServiceSFGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service ServiceSF.
   */
  public static final class ServiceSFStub
      extends io.grpc.stub.AbstractAsyncStub<ServiceSFStub> {
    private ServiceSFStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ServiceSFStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ServiceSFStub(channel, callOptions);
    }

    /**
     * <pre>
     * Submit an image
     * </pre>
     */
    public void submitImage(servicesf.ImageSubmissionRequest request,
        io.grpc.stub.StreamObserver<servicesf.ImageSubmissionResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSubmitImageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Get details and translations of image
     * </pre>
     */
    public void getImageDetails(servicesf.ImageDetailsRequest request,
        io.grpc.stub.StreamObserver<servicesf.ImageDetailsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetImageDetailsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Get all files with certain dates and characteristic
     * </pre>
     */
    public void getAllFiles(servicesf.AllFilesWithRequest request,
        io.grpc.stub.StreamObserver<servicesf.AllFilesWithResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetAllFilesMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service ServiceSF.
   */
  public static final class ServiceSFBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ServiceSFBlockingStub> {
    private ServiceSFBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ServiceSFBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ServiceSFBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Submit an image
     * </pre>
     */
    public servicesf.ImageSubmissionResponse submitImage(servicesf.ImageSubmissionRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSubmitImageMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Get details and translations of image
     * </pre>
     */
    public servicesf.ImageDetailsResponse getImageDetails(servicesf.ImageDetailsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetImageDetailsMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Get all files with certain dates and characteristic
     * </pre>
     */
    public servicesf.AllFilesWithResponse getAllFiles(servicesf.AllFilesWithRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetAllFilesMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service ServiceSF.
   */
  public static final class ServiceSFFutureStub
      extends io.grpc.stub.AbstractFutureStub<ServiceSFFutureStub> {
    private ServiceSFFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ServiceSFFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ServiceSFFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Submit an image
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<servicesf.ImageSubmissionResponse> submitImage(
        servicesf.ImageSubmissionRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSubmitImageMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Get details and translations of image
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<servicesf.ImageDetailsResponse> getImageDetails(
        servicesf.ImageDetailsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetImageDetailsMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Get all files with certain dates and characteristic
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<servicesf.AllFilesWithResponse> getAllFiles(
        servicesf.AllFilesWithRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetAllFilesMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SUBMIT_IMAGE = 0;
  private static final int METHODID_GET_IMAGE_DETAILS = 1;
  private static final int METHODID_GET_ALL_FILES = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SUBMIT_IMAGE:
          serviceImpl.submitImage((servicesf.ImageSubmissionRequest) request,
              (io.grpc.stub.StreamObserver<servicesf.ImageSubmissionResponse>) responseObserver);
          break;
        case METHODID_GET_IMAGE_DETAILS:
          serviceImpl.getImageDetails((servicesf.ImageDetailsRequest) request,
              (io.grpc.stub.StreamObserver<servicesf.ImageDetailsResponse>) responseObserver);
          break;
        case METHODID_GET_ALL_FILES:
          serviceImpl.getAllFiles((servicesf.AllFilesWithRequest) request,
              (io.grpc.stub.StreamObserver<servicesf.AllFilesWithResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getSubmitImageMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              servicesf.ImageSubmissionRequest,
              servicesf.ImageSubmissionResponse>(
                service, METHODID_SUBMIT_IMAGE)))
        .addMethod(
          getGetImageDetailsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              servicesf.ImageDetailsRequest,
              servicesf.ImageDetailsResponse>(
                service, METHODID_GET_IMAGE_DETAILS)))
        .addMethod(
          getGetAllFilesMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              servicesf.AllFilesWithRequest,
              servicesf.AllFilesWithResponse>(
                service, METHODID_GET_ALL_FILES)))
        .build();
  }

  private static abstract class ServiceSFBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ServiceSFBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return servicesf.ServiceContractSF.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ServiceSF");
    }
  }

  private static final class ServiceSFFileDescriptorSupplier
      extends ServiceSFBaseDescriptorSupplier {
    ServiceSFFileDescriptorSupplier() {}
  }

  private static final class ServiceSFMethodDescriptorSupplier
      extends ServiceSFBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    ServiceSFMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ServiceSFGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ServiceSFFileDescriptorSupplier())
              .addMethod(getSubmitImageMethod())
              .addMethod(getGetImageDetailsMethod())
              .addMethod(getGetAllFilesMethod())
              .build();
        }
      }
    }
    return result;
  }
}
