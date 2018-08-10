# Audit Service
Just a very simple auditing set of interceptors for Java EE

## Build and testing status
TODO I'm still pulling this out of another existing project of mine so it can be more easily reused

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