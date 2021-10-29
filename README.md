# Spring Boot - Warm Me Up

Belonging StackOverflow question: https://stackoverflow.com/q/69760176/324531

## Thing to Demonstrate

Small example to demonstrate that the first request hitting a Spring Boot app is slow.

To experience the problem, following these steps:

- build & run the app (see below)
- execute REST request twice (see below)
- notice that 1st request was much slower than sub-sequent requests
- stop app
- enable pre-loading in source code (see below)
- build & run the app
- execute REST request twice
- notice that 1st request is now roughly as fast as sub-sequent requests

## Possible Cause

It seems that on 1st request, Jackson is initializing "something" needed for parsing request and response messages. On
every sub-sequent request, this instance is re-used. This seems to happen in Spring's
[AbstractMessageConverterMethodArgumentResolver.readWithMessageConverters](https://github.com/spring-projects/spring-framework/blob/5.3.x/spring-webmvc/src/main/java/org/springframework/web/servlet/mvc/method/annotation/AbstractMessageConverterMethodArgumentResolver.java#L146)
calling
into [AbstractJackson2HttpMessageConverter](https://github.com/spring-projects/spring-framework/blob/5.3.x/spring-web/src/main/java/org/springframework/http/converter/json/AbstractJackson2HttpMessageConverter.java)

## Question

How to initialize Jackson in `PreloadComponent` without doing an actual REST request???

## Answer

By adding the JVM option

    -verbose:class

I can see what classes get loaded on each request. I noticed that a lot of Jackson validation classes are in the output
when doing the first request.

To have a fast 1st request, one must do:

- create a distinct warm-up endpoint
- this endpoint must do input validation (`@Valid`) on request body and input parameters
- it can use an own independent DTO
- this DTO should contain all Java validation annotations also used in the real DTOs
- query this warm-up endpoint during start up

When enabling this preloading, 1st request is only 2-3 times slower than sub-sequent requests. Without preloading, 1st
request is about 20-40 times slower than sub-sequent requests.

## Enable Preloading in Source Code

Edit `PreloadComponent.java` and uncomment the following line:

    //        sendWarmUpRestRequest();

`PreloadComponent` is executed when Spring was fully initialized. It sends a REST request to _/warmup_ endpoint. This
endpoint is in a different controller then the "production" endpoint and has its own independent DTO, etc.

Don't forget to build & run the app after changing the source.

## Build & Run

Use Maven to build the application:

    mvn clean package

Afterwards, you can run the app via command line:

    java -jar target/warm-me-up*.jar

To get output when classes get loaded by the JVM, use:

    java -verbose:class -jar target/warm-me-up*.jar

## REST Request

Use `curl` to run REST requests. Use `time` to measure execution time.

    time curl --location --request POST 'http://localhost:8080/api' \
    --header 'Content-Type: application/json' \
    --data-raw '{
    "inputMessage": "abc",
    "someNumber": 123.4,
    "patternString": "this is a fixed string",
    "selectOne": "TWO"
    }'

The response is printed on standard out and will look similar to:

    {"message":"abc","uuid":"892c5f35-10ac-4439-8ba4-088d5ea1ae05"}curl --location --request POST 
    'http://localhost:8080/' --header  --data-raw   0,01s user 0,00s system 3% cpu 0,221 total

The number at the end is the runtime in milli-seconds, 221ms in the given example.
