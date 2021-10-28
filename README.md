# Spring Boot - Warm Me Up

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

## Enable Preloading in Source Code

Edit `PreloadComponent.java` and uncomment the following line:

    //        sendRestRequest();

`PreloadComponent` is executed when Spring was fully initialized. It sends a REST request to the _/warmup_ endpoint.
This is basically the same as manually sending a first REST request.

Don't forget to build & run the app after changing the source.

## Build & Run

Use Maven to build the application:

    mvn clean package

Afterwards, you can run the app via command line:

    java -jar target/warm-me-up*.jar

## REST Request

Use `curl` to run REST requests. Use `time` to measure execution time.

    time curl --location --request POST 'http://localhost:8080/' \
    --header 'Content-Type: application/json' \
    --data-raw '{
        "inputMessage": "abc"
    }'

The response is printed on standard out and will look similar to:

    {"message":"abc","uuid":"892c5f35-10ac-4439-8ba4-088d5ea1ae05"}curl --location --request POST 
    'http://localhost:8080/' --header  --data-raw   0,01s user 0,00s system 3% cpu 0,221 total

The number at the end is the runtime in milli-seconds.
