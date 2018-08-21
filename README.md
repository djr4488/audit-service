# Audit Service
Just a very simple auditing set of interceptors for Java EE

## Build and testing status
* Build: [![Build Status](https://travis-ci.org/djr4488/audit-service.svg?branch=master)](https://travis-ci.org/djr4488/audit-service)
* Coverage: [![Coverage Status](https://coveralls.io/repos/github/djr4488/audit-service/badge.svg?branch=master)](https://coveralls.io/github/djr4488/audit-service?branch=master) [![codecov](https://codecov.io/gh/djr4488/audit-service/branch/master/graph/badge.svg)](https://codecov.io/gh/djr4488/audit-service)

## Maven pom.xml
```
<groupId>io.github.djr4488</groupId>
<artifactId>audit-service</artifactId>
<version>1.0.0</version>
```

## How to use this
There are two different audit types that I have
1. Plain and simple Slf4j audit logger
2. Database store based audit logger

To use the Slf4J audit logger, in your class you just do
```
@Interceptors({ AuditLoggerInterceptor.class })
public class Foo {
  @AuditLogger
  public Response bar(BarRequest request) {
    ...
  }
}
```

To use the database class, you'll likely want to setup a database schema using jdbc/AuditLog, if you don't this will probably an available schema whether you want audit logs in it or not
Then in your class
```
@Interceptors( { AuditDatabaseInterceptor.class })
public class Foo {
  @AuditDatabase
  public Response bar(BarReqeust request) {
    ...
  }
}
```
